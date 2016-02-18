package thewaypointers.trafficsimulator.simulation;

import thewaypointers.trafficsimulator.common.*;
import org.jgrapht.graph.*;
import thewaypointers.trafficsimulator.gui.MainFrame;
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

    final long PROGRAM_TIME_PAUSE_MULTIPLIER = 2;
    final int MAX_VEHICLE_NUMBER = 1;
    int currentVehicleNumber = 0;


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
        MainFrame mainFrame=new MainFrame(worldState);
    }

    //timesteps
    public void runSimulation(long timeStep){
        run = true;
        while(run){

            NextSimulationStep(timeStep);

            try{
                Thread.sleep(timeStep / PROGRAM_TIME_PAUSE_MULTIPLIER);
            }catch(Exception ex){
                System.out.println(ex.getMessage());
            }

        }
    }

    public void SimulationParameterChanged(String parameterName, String value) {
        // set new value for the parameter in the simulation
    }

    public void NextSimulationStep(long timeStep){

            try{
                moveVehicles(timeStep);
                checkForLeavingVehicles();
            }
            catch (Exception ex){
                System.out.println(ex.getMessage());
            }


        //temp
        changeWorldState();
        MainFrame.mapPanel.NewStateReceived(worldState);
        writeConsoleOutput();
        changeTrafficLightState();

        //WorldStateDTO state = new WorldStateDTO();

        // put the new simulation state into the DTO...


        //null exception
        //stateChangeListener.NewStateReceived(state);
    }

    private synchronized void changeWorldState() {

        VehicleDTO v = worldState.vehicles.get(0);
        LocationDTO loc = v.location;

        IVehicle vehicle = null;

        for(DefaultWeightedEdge road : vehicleMap.keySet()){
            if(vehicleMap.get(road).size() > 0){
                vehicle = vehicleMap.get(road).get(0);
            }
        }

        Car car = ((Car) vehicle);
        RoadDTO newRoad = new RoadDTO();

        if(car.getOriginNode() == "1") {
            newRoad = dtoRoads.get(0);
        }else{
            newRoad = dtoRoads.get(1);
        }


        loc = new LocationDTO(newRoad,newRoad.start, vehicle.getVehiclesDistanceTravelled(), loc.getLane());
        worldState.vehicles.get(0).location = loc;

    }

    private synchronized void changeTrafficLightState() {

        JunctionDTO junction = worldState.roadMap.junctions.get(0);
        TrafficLightDTO upTrafficLight = junction.trafficLights.get(Direction.Up);
        TrafficLightDTO downTrafficLight = junction.trafficLights.get(Direction.Down);


        for(Node node : nodeGraphMap.keySet()){
            if(node.getNodeType() == NodeType.JunctionTrafficLights){
                TrafficLightNode tlNode = ((TrafficLightNode) node);

                if(tlNode.getColor() == TrafficLightColor.Green){
                    tlNode.setColor(TrafficLightColor.Red);
                    upTrafficLight.color = TrafficLightColor.Red;
                    downTrafficLight.color = TrafficLightColor.Red;
                }
                else{
                    tlNode.setColor(TrafficLightColor.Green);
                    upTrafficLight.color = TrafficLightColor.Green;
                    downTrafficLight.color = TrafficLightColor.Green;
                }
            }
        }
    }

    //output
    private void writeConsoleOutput() {
        for(DefaultWeightedEdge road : vehicleMap.keySet() ){
            for(IVehicle vehicle : vehicleMap.get(road)){
                System.out.println(String.format("vehicle speed : %s, vehicle distance travelled: %s", vehicle.getVehiclesCurrentSpeed(), vehicle.getVehiclesDistanceTravelled()));
            }
        }

        for(Node node : nodeGraphMap.keySet()){
            if(node.getNodeType() == NodeType.JunctionTrafficLights){
                TrafficLightNode tlNode = ((TrafficLightNode) node);
                System.out.println(String.format("Traffic light is: %s", tlNode.getColor().toString()));
            }
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

}
