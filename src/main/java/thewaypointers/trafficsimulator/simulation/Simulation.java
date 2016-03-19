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
import thewaypointers.trafficsimulator.simulation.models.vehicles.Car;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class Simulation implements ISimulationInputListener {

    SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> roadGraph;
    HashMap<Node, ArrayList<RoadEdge>> nodeGraphMap;
    WorldStateDTO worldState;
    List<RoadDTO> dtoRoads;

    GraphFactory graphFactory;

    final int MAX_VEHICLE_NUMBER = 15;
    int vehicleSpawnCounter = 10;
    final int VEHICLE_SPAWN_STEPS = 10;

    final int TRAFFIC_LIGHT_STEPS = 15;
    int trafficLightCounter = 0;

    /**
     * The maximum internal clock time interval at which computation occurs.
     */
    final long RESOLUTION = 300;

    /**
     * The internal time clock.
     */
    long clock = 0;

    public Simulation() {
        initiateSimulation();
    }

    public void initiateSimulation() {
        prepareRoadGraph();
        createFirstWorldState();
    }

    public void SimulationParameterChanged(String parameterName, String value) {
        // set new value for the parameter in the simulation
    }

    public WorldStateDTO getNextSimulationStep(long timeStep) {

        long timeLeft = timeStep;
        while (timeLeft > RESOLUTION) {
            timeLeft -= RESOLUTION;
            computeNextSimulationStep(RESOLUTION);
        }
        computeNextSimulationStep(timeLeft);
        return getWorldState();
    }

    private void computeNextSimulationStep(long timeStep) {
        createVehicles();
        moveVehicles(timeStep);

        //temp
        changeWorldState();
        changeTrafficLightState();

        // DEBUG
        Car c = (Car)VehicleManager.getVehicleMap().getAll().get(0);
        if(c.getCurrentRoad()!=null){
            System.out.println("Current distance on road: " + c.getDistanceTravelled());
        }else{
            System.out.println("Current distance on junction: " + c.getJunctionLocation().getDistanceTravelled(50,50));
        }
        // DEBUG

        clock += timeStep;
    }

    private synchronized void changeWorldState() {

        worldState.setVehicleList(null);
        VehicleListDTO dtoVehicleList = new VehicleListDTO();
        int index = 1;

        for (IVehicle vehicle: VehicleManager.getVehicleMap().getAllFromRoads()){
            RoadDTO roadDTO = findEqualRoad(vehicle.getVehiclesOriginNode() + vehicle.getVehiclesDestinationNode());
            RoadLocationDTO loc = new RoadLocationDTO(roadDTO, roadDTO.getEnd(vehicle.getVehiclesOriginNode()), vehicle.getVehiclesDistanceTravelled(), Lane.Right);
            dtoVehicleList.addVehicle("" + index, loc, VehicleType.CarNormal);
            index++;
        }
        for (IVehicle vehicle:VehicleManager.getVehicleMap().getAllFromJunctions()){
            dtoVehicleList.addVehicle("" + index, vehicle.getJunctionLocation(), VehicleType.CarNormal);
        }
        worldState.setVehicleList(dtoVehicleList);

    }

    private RoadDTO findEqualRoad(String label) {

        String sortedLabel = sortLabel(label);

        for (RoadDTO road : worldState.getRoadMap().getRoads()) {
            if (road.getLabel().equals(sortedLabel)) {
                return road;
            }
        }
        return null;
    }

    private String sortLabel(String label) {
        char[] chars = label.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }


    private synchronized void changeTrafficLightState() {

        trafficLightCounter++;

        if (trafficLightCounter == TRAFFIC_LIGHT_STEPS) {

            JunctionDTO junction = worldState.getRoadMap().getJunctions().get(0);
            worldState.getTrafficLightSystem()
                    .changeTrafficLightColor(junction.getLabel(), Direction.Down, Lane.Right);
            worldState.getTrafficLightSystem()
                    .changeTrafficLightColor(junction.getLabel(), Direction.Up, Lane.Right);

            for (Node node : nodeGraphMap.keySet()) {
                if (node.getNodeType() == NodeType.JunctionTrafficLights) {
                    TrafficLightNode tfNode = ((TrafficLightNode) node);
                    tfNode.changeLightColor();
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

        VehicleFactory vehicleFactory = new VehicleFactory(nodeGraphMap);
        IVehicle vehicle = vehicleFactory.buildVehicle(roadGraph, nodeGraphMap);
        VehicleManager.getVehicleMap().add(vehicle.getCurrentRoadEdge().getRoad(), vehicle);

    }

    private void prepareRoadGraph() {
        graphFactory = new GraphFactory();

        roadGraph = graphFactory.getRoadGraph();
        VehicleManager.setVehicleMap(graphFactory.getVehicleMap());
        nodeGraphMap = graphFactory.getNodeGraphMap();
    }

    private void createFirstWorldState() {
        worldState = graphFactory.createFirstWorldState();
        dtoRoads = graphFactory.getDtoRoads();
    }

    public WorldStateDTO getWorldState() {
        return worldState;
    }
}
