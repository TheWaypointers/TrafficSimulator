package thewaypointers.trafficsimulator.simulation;

import thewaypointers.trafficsimulator.common.*;
import org.jgrapht.graph.*;
import thewaypointers.trafficsimulator.simulation.enums.NodeType;
import thewaypointers.trafficsimulator.simulation.factories.GraphFactory;
import thewaypointers.trafficsimulator.simulation.factories.VehicleFactory;
import thewaypointers.trafficsimulator.simulation.models.VehicleMap;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.Node;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.RoadEdge;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.TrafficLightNode;
import thewaypointers.trafficsimulator.simulation.models.interfaces.IVehicle;
import thewaypointers.trafficsimulator.simulation.models.managers.VehicleManager;
import thewaypointers.trafficsimulator.utils.VehicleSpawnRatio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Simulation implements ISimulationInputListener, IStateProvider {

    SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> roadGraph;
    HashMap<Node, ArrayList<RoadEdge>> nodeGraphMap;
    WorldStateDTO worldState;
    List<RoadDTO> dtoRoads;
    VehicleSpawnRatio vehicleSpawnRatio;

    GraphFactory graphFactory;

    final int MAX_VEHICLE_NUMBER = 70;
    int vehicleSpawnCounter = 10;
    final int VEHICLE_SPAWN_STEPS = 10;

    final int TRAFFIC_LIGHT_STEPS = 30;
    int trafficLightCounter = 0;
    int vehicleLabelCounter = 1;

    /**
     * The maximum internal clock time interval at which computation occurs.
     */
    final long RESOLUTION = 300;

    /**
     * The internal time clock.
     */
    long clock = 0;

    public Simulation(WorldStateDTO worldState) {
        this.worldState = worldState;
        this.dtoRoads = worldState.getRoadMap().getRoads();
        initiateSimulation();
    }

    public void initiateSimulation() {
        vehicleSpawnRatio = new VehicleSpawnRatio();
        prepareRoadGraph();
    }

    public void SimulationParameterChanged(String parameterName, String value) {
        switch (parameterName){
            case "junctionLabel" :
                changeJunctionType(value);
                break;
            case "cautionCarPercentage":
                int ratioCautionCar = Integer.parseInt(value);
                vehicleSpawnRatio.setCautionCarsRatio(ratioCautionCar);
                break;
            case "normalCarPercentage":
                int ratioNormalCar = Integer.parseInt(value);
                vehicleSpawnRatio.setNormalCarsRatio(ratioNormalCar);
                break;
            case "recklessCarPercentage":
                int ratioReckCar = Integer.parseInt(value);
                vehicleSpawnRatio.setNormalCarsRatio(ratioReckCar);
                break;
            case "busPercentage":
                int busRatio = Integer.parseInt(value);
                vehicleSpawnRatio.setNormalCarsRatio(busRatio);
                break;
            case "ambulancePercentage":
                int ambulanceRatio = Integer.parseInt(value);
                vehicleSpawnRatio.setNormalCarsRatio(ambulanceRatio);
                break;
            default:
                break;
        }
    }

    private void changeJunctionType(String junction) {
        for(Node node : nodeGraphMap.keySet()){
            if(node.getNodeName().equals(junction)){
                if(node.getNodeType() == NodeType.JunctionNormal){
                    node.setNodeType(NodeType.JunctionTrafficLights);
                }else{
                    node.setNodeType(NodeType.JunctionNormal);
                }
            }
        }
    }

    public WorldStateDTO getNextState(long timeStep) {

        long timeLeft = timeStep;
        while (timeLeft > RESOLUTION) {
            timeLeft -= RESOLUTION;
            computeNextSimulationStep(RESOLUTION);
        }
        computeNextSimulationStep(timeLeft);
        getWorldState().setClock(clock);
        return getWorldState();
    }

    private void computeNextSimulationStep(long timeStep) {
        createVehicles();
        moveVehicles(timeStep);

        //temp
        changeWorldState();
        changeTrafficLightState();

        clock += timeStep;
    }

    private synchronized void changeWorldState() {

        worldState.setVehicleList(null);
        VehicleListDTO dtoVehicleList = new VehicleListDTO();
        int index = 1;

        for (IVehicle vehicle: VehicleManager.getVehicleMap().getAllFromRoads()){
            RoadDTO roadDTO = findEqualRoad(vehicle);
            RoadLocationDTO loc = new RoadLocationDTO(roadDTO, roadDTO.getEnd(vehicle.getVehiclesOriginNode()), vehicle.getVehiclesDistanceTravelled(), Lane.Right);
            dtoVehicleList.addVehicle("" + vehicle.getVehicleLabel(), loc, vehicle.getVehiclesType());
        }
        for (IVehicle vehicle:VehicleManager.getVehicleMap().getAllFromJunctions()){
            dtoVehicleList.addVehicle("" + vehicle.getVehicleLabel(), vehicle.getJunctionLocation(), vehicle.getVehiclesType());
        }
        worldState.setVehicleList(dtoVehicleList);

    }

    private RoadDTO findEqualRoad(IVehicle vehicle) {

        for (RoadDTO road : worldState.getRoadMap().getRoads()) {
            if (road != null) {
                if (road.getLabel().equals(vehicle.getVehiclesOriginNode() + vehicle.getVehiclesDestinationNode())) {
                    return road;
                }
            }
        }

        for (RoadDTO road : worldState.getRoadMap().getRoads()) {
            if (road != null) {
                if (road.getLabel().equals(vehicle.getVehiclesDestinationNode() + vehicle.getVehiclesOriginNode())) {
                    return road;
                }
            }
        }

        return null;
    }

    private synchronized void changeTrafficLightState() {

        trafficLightCounter++;

        if (trafficLightCounter == TRAFFIC_LIGHT_STEPS) {

            JunctionDTO junction;;

            for (Node node : nodeGraphMap.keySet()) {
                if (node.getNodeType() == NodeType.JunctionTrafficLights) {
                    TrafficLightNode tfNode = ((TrafficLightNode) node);
                    tfNode.changeLightColor();

                    if(tfNode.getLeftRoad() != null){
                        junction = worldState.getRoadMap().getJunction(tfNode.getNodeName());
                        worldState.getTrafficLightSystem().setTrafficLightColor(junction.getLabel(), Direction.Left, Lane.Right, tfNode.getLeft());
                    }
                    if(tfNode.getRightRoad() != null){
                        junction = worldState.getRoadMap().getJunction(tfNode.getNodeName());
                        worldState.getTrafficLightSystem().setTrafficLightColor(junction.getLabel(), Direction.Right, Lane.Right, tfNode.getRight());
                    }
                    if(tfNode.getDownRoad() != null){
                        junction = worldState.getRoadMap().getJunction(tfNode.getNodeName());
                        worldState.getTrafficLightSystem().setTrafficLightColor(junction.getLabel(), Direction.Down, Lane.Right, tfNode.getDown());
                    }
                    if(tfNode.getUpRoad() != null){
                        junction = worldState.getRoadMap().getJunction(tfNode.getNodeName());
                        worldState.getTrafficLightSystem().setTrafficLightColor(junction.getLabel(), Direction.Up, Lane.Right, tfNode.getUp());
                    }

                }
            }

            trafficLightCounter = 0;
        }

    }

    //move vehicles
    private boolean moveVehicles(long timeStep) {

        VehicleMap vehicleMap = VehicleManager.getVehicleMap();

        //create list of cars to iterate on
        List<IVehicle> currentVehicleList = vehicleMap.getAll();

        //move each car
        for (IVehicle vehicle : currentVehicleList) {
            vehicle.calculateNextPosition(timeStep, nodeGraphMap);
        }

        return true;
    }

    private void createVehicles() {

        if (vehicleSpawnCounter >= VEHICLE_SPAWN_STEPS) {
            int currentVehicleNumber = VehicleManager.getVehicleMap().count();

            if (currentVehicleNumber < MAX_VEHICLE_NUMBER) {


                spawnVehicle();
                vehicleSpawnCounter = 0;
            }
        } else {
            vehicleSpawnCounter++;
        }
    }

    private void spawnVehicle() {

        VehicleFactory vehicleFactory = new VehicleFactory(nodeGraphMap, vehicleSpawnRatio);
        IVehicle vehicle = vehicleFactory.buildVehicle(roadGraph, nodeGraphMap);
        vehicle.setVehicleLabel(vehicleLabelCounter);
        vehicleLabelCounter++;
        VehicleManager.getVehicleMap().add(vehicle.getCurrentRoadEdge().getRoad(), vehicle);

    }

    private void prepareRoadGraph() {
        graphFactory = new GraphFactory(worldState);

        roadGraph = graphFactory.getRoadGraph();
        VehicleManager.setVehicleMap(graphFactory.getVehicleMap());
        nodeGraphMap = graphFactory.getNodeGraphMap();
    }

    public WorldStateDTO getWorldState() {
        return worldState;
    }
}
