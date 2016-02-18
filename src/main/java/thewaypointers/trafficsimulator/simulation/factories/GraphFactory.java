package thewaypointers.trafficsimulator.simulation.factories;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import thewaypointers.trafficsimulator.common.*;
import thewaypointers.trafficsimulator.simulation.enums.NodeType;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.DirectionFromNode;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.Node;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.RoadEdge;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.TrafficLightNode;
import thewaypointers.trafficsimulator.simulation.models.interfaces.IVehicle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GraphFactory {

    private SimpleDirectedWeightedGraph<String, DefaultWeightedEdge>  roadGraph;
    private HashMap<Node, ArrayList<RoadEdge>> nodeGraphMap;
    private HashMap<DefaultWeightedEdge, ArrayList<IVehicle>> vehicleMap;
    private List<RoadDTO> dtoRoads;

    public GraphFactory(){
        setRoadGraph(new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class));
        setNodeGraphMap(new HashMap<>());
        setVehicleMap(new HashMap<>());

        prepareRoadGraph();
    }

    public void prepareRoadGraph() {

        getRoadGraph().addVertex("1");
        getRoadGraph().addVertex("2");
        getRoadGraph().addVertex("3");

        DefaultWeightedEdge e1 = getRoadGraph().addEdge("1", "2");
        getRoadGraph().setEdgeWeight(e1, 300);
        getVehicleMap().put(e1, new ArrayList<>());

        DefaultWeightedEdge e2 = getRoadGraph().addEdge("2", "3");
        getRoadGraph().setEdgeWeight(e2, 300);
        getVehicleMap().put(e2, new ArrayList<>());

        Node node1 = new Node("1", NodeType.ExitNode);
        RoadEdge re1 = new RoadEdge(e1, DirectionFromNode.Right, 30, ((float) getRoadGraph().getEdgeWeight(e1)));

        Node node2 = new TrafficLightNode("2", NodeType.JunctionTrafficLights);
        RoadEdge re21 = new RoadEdge(e1, DirectionFromNode.Left, 30, ((float) getRoadGraph().getEdgeWeight(e1)));
        RoadEdge re22 = new RoadEdge(e2, DirectionFromNode.Right, 30, ((float) getRoadGraph().getEdgeWeight(e2)));

        Node node3 = new Node("3", NodeType.ExitNode);
        RoadEdge re3 = new RoadEdge(e2, DirectionFromNode.Left, 30, ((float) getRoadGraph().getEdgeWeight(e2)));

        getNodeGraphMap().put(node1, new ArrayList<>());
        getNodeGraphMap().get(node1).add(re1);

        getNodeGraphMap().put(node2, new ArrayList<>());
        getNodeGraphMap().get(node2).add(re21);
        getNodeGraphMap().get(node2).add(re22);

        getNodeGraphMap().put(node3, new ArrayList<>());
        getNodeGraphMap().get(node3).add(re3);

    }

    public WorldStateDTO createFirstWorldState() {
        WorldStateDTO worldState = new WorldStateDTO();

        setDtoRoads(new ArrayList<>());

        RoadDTO e1_a = new RoadDTO();
        RoadDTO a_e2 = new RoadDTO();
        e1_a.length = 300;
        a_e2.length = 300;
        getDtoRoads().add(e1_a);
        getDtoRoads().add(a_e2);


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
        a.trafficLights = new HashMap<>();
        a.trafficLights.put(Direction.Down, downTrafficLight);
        a.trafficLights.put(Direction.Up, upTrafficLight);

        MapDTO roadMap = new MapDTO(a);

        LocationDTO loc = new LocationDTO(e1_a, e1_a.start, 0, Lane.Right);
        VehicleDTO v1 = new VehicleDTO(loc, thewaypointers.trafficsimulator.common.VehicleType.CarNormal);
        ArrayList<VehicleDTO> vehicles = new ArrayList<>();
        vehicles.add(v1);

        worldState = new WorldStateDTO();

        worldState.roadMap = roadMap;
        worldState.vehicles = vehicles;

        return worldState;
    }

    public SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> getRoadGraph() {
        return roadGraph;
    }

    public void setRoadGraph(SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> roadGraph) {
        this.roadGraph = roadGraph;
    }

    public HashMap<Node, ArrayList<RoadEdge>> getNodeGraphMap() {
        return nodeGraphMap;
    }

    public void setNodeGraphMap(HashMap<Node, ArrayList<RoadEdge>> nodeGraphMap) {
        this.nodeGraphMap = nodeGraphMap;
    }

    public HashMap<DefaultWeightedEdge, ArrayList<IVehicle>> getVehicleMap() {
        return vehicleMap;
    }

    public void setVehicleMap(HashMap<DefaultWeightedEdge, ArrayList<IVehicle>> vehicleMap) {
        this.vehicleMap = vehicleMap;
    }


    public List<RoadDTO> getDtoRoads() {
        return dtoRoads;
    }

    public void setDtoRoads(List<RoadDTO> dtoRoads) {
        this.dtoRoads = dtoRoads;
    }
}
