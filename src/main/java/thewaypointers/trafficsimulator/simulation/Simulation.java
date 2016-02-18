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

    final long SIMULATION_TIME_STEP = 500;
    final int MAX_VEHICLE_NUMBER = 1;
    final int TRAFFIC_LIGHT_STEPS = 10;
    final long SIMULATION_TIME_MULTIPLIER = 2;
    int currentVehicleNumber = 0;
    int trafficLightCounter = 0;


    //TODO: use this constructor in the simulation manager
    public Simulation(IStateChangeListener stateChangeListener) {
        this.stateChangeListener = stateChangeListener;
        initiateSimulation();
    }


    public Simulation(){
        initiateSimulation();
    }

    public void initiateSimulation(){
        prepareRoadGraph();
        createFirstWorldState();

        createVehicles();
    }

    //timesteps
    public void runSimulation(){
        run = true;
        while(run){

            NextSimulationStep();

            try{
                Thread.sleep(SIMULATION_TIME_STEP);
            }catch(Exception ex){
                System.out.println(ex.getMessage());
            }

        }
    }

    public void SimulationParameterChanged(String parameterName, String value) {
        // set new value for the parameter in the simulation
    }

    public void NextSimulationStep(){

            try{
                moveVehicles(SIMULATION_TIME_STEP * SIMULATION_TIME_MULTIPLIER);
                checkForLeavingVehicles();
            }
            catch (Exception ex){
                System.out.println(ex.getMessage());
            }


        //temp
        changeWorldState();
        changeTrafficLightState();
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

                    if(tfNode.getColor() == TrafficLightColor.Green){
                        tfNode.setColor(TrafficLightColor.Red);
                    }
                    else{
                        tfNode.setColor(TrafficLightColor.Green);
                    }
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
