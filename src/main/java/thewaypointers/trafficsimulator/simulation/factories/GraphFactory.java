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

        DefaultWeightedEdge e3 = getRoadGraph().addEdge("2", "1");
        getRoadGraph().setEdgeWeight(e3, 300);
        getVehicleMap().put(e3, new ArrayList<>());

        DefaultWeightedEdge e4 = getRoadGraph().addEdge("3", "2");
        getRoadGraph().setEdgeWeight(e4, 300);
        getVehicleMap().put(e4, new ArrayList<>());

        Node node1 = new Node("1", NodeType.ExitNode);
        RoadEdge re1 = new RoadEdge(e1, DirectionFromNode.Down, 30, ((float) getRoadGraph().getEdgeWeight(e1)));

        Node node2 = new TrafficLightNode("2", NodeType.JunctionTrafficLights);
        RoadEdge re21 = new RoadEdge(e1, DirectionFromNode.Up, 30, ((float) getRoadGraph().getEdgeWeight(e1)));
        RoadEdge re22 = new RoadEdge(e2, DirectionFromNode.Down, 30, ((float) getRoadGraph().getEdgeWeight(e2)));

        Node node3 = new Node("3", NodeType.ExitNode);
        RoadEdge re3 = new RoadEdge(e2, DirectionFromNode.Up, 30, ((float) getRoadGraph().getEdgeWeight(e2)));

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

        MapDTO roadMap = worldState.getRoadMap();
        roadMap.addRoad("1","2", Direction.Down, 300);
        roadMap.addRoad("2", "3", Direction.Down, 300);


        // add traffic lights
        worldState.getTrafficLightSystem().addJunction(roadMap.getJunction("2"));

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
