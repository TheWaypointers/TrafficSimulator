package thewaypointers.trafficsimulator.simulation;

import thewaypointers.trafficsimulator.common.*;
import org.jgrapht.graph.*;
import thewaypointers.trafficsimulator.simulation.enums.NodeType;
import thewaypointers.trafficsimulator.simulation.factories.GraphFactory;
import thewaypointers.trafficsimulator.simulation.factories.VehicleFactory;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.Node;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.RoadEdge;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.TrafficLightNode;
import thewaypointers.trafficsimulator.simulation.models.interfaces.IVehicle;
import thewaypointers.trafficsimulator.simulation.models.vehicles.Car;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Simulation implements ISimulationInputListener {

    boolean run;
    IStateChangeListener stateChangeListener;
    SimpleDirectedWeightedGraph<String, DefaultWeightedEdge>  roadGraph;
    HashMap<Node, ArrayList<RoadEdge>> nodeGraphMap;
    HashMap<DefaultWeightedEdge, ArrayList<IVehicle>> vehicleMap;
    WorldStateDTO worldState;
    List<RoadDTO> dtoRoads;

    GraphFactory graphFactory;

    final int MAX_VEHICLE_NUMBER = 1;
    final int TRAFFIC_LIGHT_STEPS = 10;
    int currentVehicleNumber = 0;
    int trafficLightCounter = 0;

    /**
     * The maximum internal clock time interval at which computation occurs.
     */
    final long RESOLUTION = 300;

    /**
     * The internal time clock.
     */
    long clock = 0;

    public Simulation(){
        initiateSimulation();
    }

    public void initiateSimulation(){
        prepareRoadGraph();
        createFirstWorldState();

        createVehicles();
    }

    public void SimulationParameterChanged(String parameterName, String value) {
        // set new value for the parameter in the simulation
    }

    public WorldStateDTO getNextSimulationStep(long timeStep){

        long timeLeft = timeStep;
        while(timeLeft > RESOLUTION) {
            timeLeft -= RESOLUTION;
            computeNextSimulationStep(RESOLUTION);
        }
        computeNextSimulationStep(timeLeft);
        return getWorldState();
    }

    private void computeNextSimulationStep(long timeStep){
        moveVehicles(timeStep);
        checkForLeavingVehicles();

        //temp
        changeWorldState();
        changeTrafficLightState();

        clock += timeStep;
    }

    private synchronized void changeWorldState() {

        worldState.setVehicleList(null);
        VehicleListDTO dtoVehicleList = new VehicleListDTO();
        int index = 1;

        for(DefaultWeightedEdge road : vehicleMap.keySet()){
            for(IVehicle vehicle : vehicleMap.get(road)){
                RoadDTO roadDTO = findEqualRoad(vehicle.getVehiclesOriginNode() + vehicle.getVehiclesDestinationNode());
                LocationDTO loc = new LocationDTO(roadDTO, roadDTO.getEnd(vehicle.getVehiclesOriginNode()), vehicle.getVehiclesDistanceTravelled(), Lane.Right);
                dtoVehicleList.addVehicle("" + index, loc, VehicleType.CarNormal);
                index++;
            }
        }
        worldState.setVehicleList(dtoVehicleList);

    }

    private RoadDTO findEqualRoad(String label) {

        for(RoadDTO road : worldState.getRoadMap().getRoads()){
            if(road.getLabel().equals(label)){
                return road;
            }
        }

        return null;
    }

    private synchronized void changeTrafficLightState() {

        trafficLightCounter++;

        if(trafficLightCounter == TRAFFIC_LIGHT_STEPS) {

            JunctionDTO junction = worldState.getRoadMap().getJunctions().get(0);
            worldState.getTrafficLightSystem()
                    .changeTrafficLightColor(junction.getLabel(), Direction.Down, Lane.Right);
            worldState.getTrafficLightSystem()
                    .changeTrafficLightColor(junction.getLabel(), Direction.Up, Lane.Right);

            for(Node node : nodeGraphMap.keySet()){
                if(node.getNodeType() == NodeType.JunctionTrafficLights){
                    TrafficLightNode tfNode = ((TrafficLightNode) node);
                    tfNode.changeLightColor();
                }
            }

            trafficLightCounter = 0;
        }

    }

    //leaving vehicles
    private synchronized void checkForLeavingVehicles() {
        for(DefaultWeightedEdge road : vehicleMap.keySet() ){
            for(IVehicle vehicle : vehicleMap.get(road)){
                if(vehicle.isVehicleLeavingRoad()){
                    vehicleMap.get(road).remove(vehicle);
                    currentVehicleNumber--;
                }
            }
        }
    }

    //move vehicles
    private boolean moveVehicles(long timeStep) {
        for(DefaultWeightedEdge road : vehicleMap.keySet() ){
            for(IVehicle vehicle : vehicleMap.get(road)){
                vehicle.calculateNextPosition(timeStep, nodeGraphMap, vehicleMap);
            }
        }
        return true;
    }

    private void createVehicles() {

        if(currentVehicleNumber < MAX_VEHICLE_NUMBER){
            //first version
            spawnVehicle();
            currentVehicleNumber++;
        }

    }

    private void spawnVehicle() {

        VehicleFactory vehicleFactory = new VehicleFactory();
        IVehicle vehicle = vehicleFactory.buildVehicle(roadGraph, nodeGraphMap);
        vehicleMap.get(vehicle.getCurrentRoadEdge()).add(vehicle);

    }

    private void prepareRoadGraph() {
        graphFactory = new GraphFactory();

        roadGraph = graphFactory.getRoadGraph();
        vehicleMap = graphFactory.getVehicleMap();
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
