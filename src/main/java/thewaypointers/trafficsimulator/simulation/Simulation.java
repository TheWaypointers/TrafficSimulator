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
        MainFrame mainFrame=new MainFrame(worldState);
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
        TrafficLightDTO upTrafficLight = worldState.trafficLights.get(junction).get(Direction.Up);
        TrafficLightDTO downTrafficLight = worldState.trafficLights.get(junction).get(Direction.Down);


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
        return null;
    }

    private String calculateDestinationNode(String originNode) {
        //TODO: calculate the destination node of the vehicle
        return "3";
    }

    private String calculateOriginNode() {
        //TODO: calculate the node the vehicle will spawn on
        return "1";
    }

    private Stack<String> calculatePath(String origin, String destination) {
        //TODO: calculate path from graph
        Stack decisions = new Stack<>();
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

        dtoRoads = new ArrayList<>();

        RoadDTO e1_a = new RoadDTO();
        RoadDTO a_e2 = new RoadDTO();
        e1_a.length = 300;
        a_e2.length = 300;
        dtoRoads.add(e1_a);
        dtoRoads.add(a_e2);


        ExitNodeDTO e1 = new ExitNodeDTO("E1");
        ExitNodeDTO e2 = new ExitNodeDTO("E2");
        JunctionDTO a = new JunctionDTO("A", e1_a, a_e2, null, null);

        // connect the roads
        // TODO make this more straightforward
        e1_a.start = e1;
        e1_a.end = a;
        a_e2.start = a;
        a_e2.end = e2;

        // add traffic lights
        TrafficLightDTO downTrafficLight = new TrafficLightDTO();
        TrafficLightDTO upTrafficLight = new TrafficLightDTO();
        downTrafficLight.color = TrafficLightColor.Red;
        upTrafficLight.color = TrafficLightColor.Red;
        HashMap<Direction, TrafficLightDTO> trafficLights = new HashMap<>();
        trafficLights.put(Direction.Down, downTrafficLight);
        trafficLights.put(Direction.Up, upTrafficLight);
        worldState.trafficLights = new HashMap<>();
        worldState.trafficLights.put(a, trafficLights);

        MapDTO roadMap = new MapDTO(a);

        LocationDTO loc = new LocationDTO(e1_a, e1_a.start, 0, Lane.Right);
        VehicleDTO v1 = new VehicleDTO(loc, thewaypointers.trafficsimulator.common.VehicleType.CarNormal);
        ArrayList<VehicleDTO> vehicles = new ArrayList<>();
        vehicles.add(v1);

        worldState = new WorldStateDTO();

        worldState.roadMap = roadMap;
        worldState.vehicles = vehicles;
    }

}
