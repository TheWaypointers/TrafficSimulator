package thewaypointers.trafficsimulator.simulation;

import thewaypointers.trafficsimulator.common.*;
import org.jgrapht.graph.*;
import thewaypointers.trafficsimulator.gui.MainFrame;
import thewaypointers.trafficsimulator.simulation.enums.NodeType;
import thewaypointers.trafficsimulator.simulation.enums.VehicleType;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.DirectionFromNode;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.Node;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.RoadEdge;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.TrafficLightNode;
import thewaypointers.trafficsimulator.simulation.models.interfaces.IVehicle;
import thewaypointers.trafficsimulator.simulation.models.vehicles.Car;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;


public class Simulation implements ISimulationInputListener {

    boolean run;
    IStateChangeListener stateChangeListener;
    SimpleDirectedWeightedGraph<String, DefaultWeightedEdge>  roadGraph;
    HashMap<Node, ArrayList<RoadEdge>> nodeGraphMap = new HashMap<>();
    HashMap<DefaultWeightedEdge, ArrayList<IVehicle>> vehicleMap = new HashMap<>();
    WorldStateDTO worldState;
    List<RoadDTO> dtoRoads;

    final long PROGRAM_TIME_PAUSE_MULTIPLIER = 2;
    final int MAX_VEHICLE_NUMBER = 2;
    int currentVehicleNumber = 0;


    //TODO: use this constructor in the simulation manager
    public Simulation(IStateChangeListener stateChangeListener) {
        this.stateChangeListener = stateChangeListener;
        setGraph();
        CreateVehicles();
    }


    public Simulation(){
        setGraph();
        CreateVehicles();
        createWorldState();
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
        MainFrame.mapContainerPanel.mapPanel.NewStateReceived(worldState);
        writeConsoleOutput();
        changeTrafficLightState();

        //WorldStateDTO state = new WorldStateDTO();

        // put the new simulation state into the DTO...


        //null exception
        //stateChangeListener.NewStateReceived(state);
    }

    private synchronized void changeWorldState() {

        VehicleDTO v = worldState.getVehicleList().getAll().get(0);
        LocationDTO loc = v.getLocation();

        IVehicle vehicle = null;

        for(DefaultWeightedEdge road : vehicleMap.keySet()){
            if(vehicleMap.get(road).size() > 0){
                vehicle = vehicleMap.get(road).get(0);
            }
        }

        if(vehicle == null){
            throw new AssertionError("Vehicle not found!");
        }

        Car car = ((Car) vehicle);
        RoadDTO newRoad;

        if(car.getOriginNode().equals("1")) {
            newRoad = dtoRoads.get(0);
        }else{
            newRoad = dtoRoads.get(1);
        }


        loc = new LocationDTO(newRoad,newRoad.getFrom(), vehicle.getVehiclesDistanceTravelled(), loc.getLane());
        worldState.getVehicleList().setVehicleLocation(v.getLabel(), loc);

    }

    private synchronized void changeTrafficLightState() {

        JunctionDTO junction = worldState.getRoadMap().getJunctions().get(0);
        for(Node node : nodeGraphMap.keySet()){
            if(node.getNodeType() == NodeType.JunctionTrafficLights){
                TrafficLightNode tlNode = ((TrafficLightNode) node);

                if(tlNode.getColor() == TrafficLightColor.Green){
                    tlNode.setColor(TrafficLightColor.Red);
                    worldState.getTrafficLightSystem()
                        .setTrafficLightColor(
                                junction.getLabel(),
                                Direction.Up,
                                Lane.Right,
                                TrafficLightColor.Red);
                    worldState.getTrafficLightSystem()
                            .setTrafficLightColor(
                                    junction.getLabel(),
                                    Direction.Down,
                                    Lane.Right,
                                    TrafficLightColor.Red);
                }
                else{
                    tlNode.setColor(TrafficLightColor.Green);
                    worldState.getTrafficLightSystem()
                            .setTrafficLightColor(
                                    junction.getLabel(),
                                    Direction.Up,
                                    Lane.Right,
                                    TrafficLightColor.Green);
                    worldState.getTrafficLightSystem()
                            .setTrafficLightColor(
                                    junction.getLabel(),
                                    Direction.Down,
                                    Lane.Right,
                                    TrafficLightColor.Green);
                }
            }
        }
    }

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

    private synchronized void checkForLeavingVehicles() {
        for(DefaultWeightedEdge road : vehicleMap.keySet() ){
            for(IVehicle vehicle : vehicleMap.get(road)){
                if(vehicle.isVehicleLeavingRoad()){
                    vehicleMap.get(road).remove(vehicle);
                }
            }
        }
    }

    private boolean moveVehicles(long timeStep) {
        for(DefaultWeightedEdge road : vehicleMap.keySet() ){
            for(IVehicle vehicle : vehicleMap.get(road)){
                vehicle.calculateNextPosition(timeStep, nodeGraphMap, vehicleMap);
            }
        }
        return true;
    }

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

    private void CreateVehicles() {

        if(currentVehicleNumber < MAX_VEHICLE_NUMBER){
            //first version
            spawnVehicle();
            currentVehicleNumber++;
        }

    }

    private void spawnVehicle() {

        String originNode = calculateOriginNode();
        String destinationNode = calculateDestinationNode(originNode);
        RoadEdge firstRoad = getTheFirstRoad(originNode);
        Stack<String> decisions = calculatePath(originNode, destinationNode);
        IVehicle car = new Car(VehicleType.CarNormal, firstRoad.getSpeedLimit(), decisions, firstRoad.getRoad(), ((float) roadGraph.getEdgeWeight(firstRoad.getRoad())), originNode, Lane.Left);
        vehicleMap.get(firstRoad.getRoad()).add(car);

    }

    private RoadEdge getTheFirstRoad(String originNode) {
        for(Node node : nodeGraphMap.keySet()){
            if(node.getNodeName().equals(originNode)){
                return nodeGraphMap.get(node).get(0);
            }
        }
        throw new AssertionError("Road not found!");
    }

    private String calculateDestinationNode(String originNode) {
        //TODO: calculate the destination node of the vehicle
        return "3";
    }

    private String calculateOriginNode() {
        //TODO: calculate the node the vehicle will spawn on
        return "1";
    }

    @SuppressWarnings("unchecked")
    private Stack<String> calculatePath(String origin, String destination) {
        //TODO: calculate path from graph
        Stack<String> decisions = new Stack<>();
        decisions.push("3");
        decisions.push("2");
        return decisions;
    }

    private void setGraph() {
        roadGraph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        roadGraph.addVertex("1");
        roadGraph.addVertex("2");
        roadGraph.addVertex("3");

        DefaultWeightedEdge e1 = roadGraph.addEdge("1", "2");
        roadGraph.setEdgeWeight(e1, 300);
        vehicleMap.put(e1, new ArrayList<>());

        DefaultWeightedEdge e2 = roadGraph.addEdge("2", "3");
        roadGraph.setEdgeWeight(e2, 300);
        vehicleMap.put(e2, new ArrayList<>());

        Node node1 = new Node("1", NodeType.ExitNode);
        RoadEdge re1 = new RoadEdge(e1, DirectionFromNode.Right, 30, ((float) roadGraph.getEdgeWeight(e1)));

        Node node2 = new TrafficLightNode("2", NodeType.JunctionTrafficLights);
        RoadEdge re21 = new RoadEdge(e1, DirectionFromNode.Left, 30, ((float) roadGraph.getEdgeWeight(e1)));
        RoadEdge re22 = new RoadEdge(e2, DirectionFromNode.Right, 30, ((float) roadGraph.getEdgeWeight(e2)));

        Node node3 = new Node("3", NodeType.ExitNode);
        RoadEdge re3 = new RoadEdge(e2, DirectionFromNode.Left, 30, ((float) roadGraph.getEdgeWeight(e2)));

        nodeGraphMap.put(node1, new ArrayList<>());
        nodeGraphMap.get(node1).add(re1);

        nodeGraphMap.put(node2, new ArrayList<>());
        nodeGraphMap.get(node2).add(re21);
        nodeGraphMap.get(node2).add(re22);

        nodeGraphMap.put(node3, new ArrayList<>());
        nodeGraphMap.get(node3).add(re3);

    }

    private void createWorldState() {
        worldState = new WorldStateDTO();
        MapDTO roadMap = worldState.getRoadMap();
        roadMap.addRoad("E1", "A", Direction.Down, 300);
        roadMap.addRoad("A", "E2", Direction.Down, 300);

        // add traffic lights
        worldState.getTrafficLightSystem().addJunction(roadMap.getJunction("A"));

        RoadDTO startRoad = roadMap.getJunction("A").getRoad(Direction.Up);
        LocationDTO loc = new LocationDTO(startRoad, startRoad.getEnd("E1"), 0, Lane.Right);
        VehicleDTO v1 = new VehicleDTO("V1", loc, thewaypointers.trafficsimulator.common.VehicleType.CarNormal);
        worldState.getVehicleList().addVehicle(v1);

        dtoRoads = roadMap.getRoads();
    }

}
