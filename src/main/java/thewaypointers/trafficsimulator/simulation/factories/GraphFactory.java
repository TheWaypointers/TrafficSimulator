package thewaypointers.trafficsimulator.simulation.factories;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import thewaypointers.trafficsimulator.common.*;
import thewaypointers.trafficsimulator.simulation.enums.NodeType;
import thewaypointers.trafficsimulator.simulation.models.VehicleMap;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.Node;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.RoadEdge;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.TrafficLightNode;

import java.util.*;

public class GraphFactory {

    private final float ROAD_WIDTH = 50;

    private SimpleDirectedWeightedGraph<String, DefaultWeightedEdge>  roadGraph;
    private HashMap<Node, ArrayList<RoadEdge>> nodeGraphMap;
    private VehicleMap vehicleMap;
    private List<RoadDTO> dtoRoads;
    private WorldStateDTO worldState;

    public GraphFactory(WorldStateDTO worldState) {
        this.worldState = worldState;
        setRoadGraph(new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class));
        setNodeGraphMap(new HashMap<>());
        setVehicleMap(new VehicleMap());

        prepareRoadGraph();
    }

    public void prepareRoadGraph() {

        for (RoadDTO road : worldState.getRoadMap().getRoads()) {

            if (road != null) {
                getRoadGraph().addVertex(road.getFrom().getLabel());
                Node node1 = createNode(road.getFrom().getLabel());

                if (!checkIfNodeWasCreated(node1)) {
                    getNodeGraphMap().put(node1, new ArrayList<>());
                } else {
                    node1 = getNodeFromNodeGraph(node1);
                }


                getRoadGraph().addVertex(road.getTo().getLabel());
                Node node2 = createNode(road.getTo().getLabel());

                if (!checkIfNodeWasCreated(node2)) {
                    getNodeGraphMap().put(node2, new ArrayList<>());
                } else {
                    node2 = getNodeFromNodeGraph(node2);
                }


                DefaultWeightedEdge edge1 = getRoadGraph().addEdge(road.getFrom().getLabel(), road.getTo().getLabel());
                getRoadGraph().setEdgeWeight(edge1, road.getLength());
                RoadEdge re1 = new RoadEdge(edge1, Direction.fromString(worldState.getRoadMap().getDirection(road.getFrom().getLabel(),road.getTo().getLabel())), 30, road.getLength(), road.getFrom().getLabel(), road.getTo().getLabel());
                getNodeGraphMap().get(node1).add(re1);

                DefaultWeightedEdge edge2 = getRoadGraph().addEdge(road.getTo().getLabel(), road.getFrom().getLabel());
                getRoadGraph().setEdgeWeight(edge2, road.getLength());
                RoadEdge re2 = new RoadEdge(edge2, Direction.fromString(worldState.getRoadMap().getDirection(road.getFrom().getLabel(),road.getTo().getLabel())).opposite(), 30, road.getLength(), road.getTo().getLabel(), road.getFrom().getLabel());
                getNodeGraphMap().get(node2).add(re2);
            }
        }
        setNodeDirectionRoads();
    }

    private void setNodeDirectionRoads() {

        for(Node node : getNodeGraphMap().keySet()){
            for(RoadEdge road : getNodeGraphMap().get(node)){
                if(road.getDirection() == Direction.Left){
                    node.setLeftRoad(road);
                } else if (road.getDirection() == Direction.Right){
                    node.setRightRoad(road);
                } else if (road.getDirection() == Direction.Down){
                    node.setDownRoad(road);
                } else{
                    node.setUpRoad(road);
                }
            }
        }

    }

    private Node getNodeFromNodeGraph(Node nodeTemp) {
        for (Node node : nodeGraphMap.keySet()) {
            if (node.getNodeName().equals(nodeTemp.getNodeName())) {
                return node;
            }
        }
        return null;
    }

    private boolean checkIfNodeWasCreated(Node nodeTemp) {
        for (Node node : nodeGraphMap.keySet()) {
            if (node.getNodeName().equals(nodeTemp.getNodeName())) {
                return true;
            }
        }
        return false;
    }

    private Node createNode(String label) {
        if (worldState.getTrafficLightSystem().getJunction(label) != null) {
            return new TrafficLightNode(label, NodeType.JunctionTrafficLights, ROAD_WIDTH, ROAD_WIDTH);
        } else if (label.toLowerCase().startsWith("e")) {
            return new Node(label, NodeType.ExitNode);
        } else {
            return new Node(label, NodeType.JunctionNormal);
        }
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

    public VehicleMap getVehicleMap() {
        return vehicleMap;
    }

    public void setVehicleMap(VehicleMap vehicleMap) {
        this.vehicleMap = vehicleMap;
    }


    public List<RoadDTO> getDtoRoads() {
        return dtoRoads;
    }

    public void setDtoRoads(List<RoadDTO> dtoRoads) {
        this.dtoRoads = dtoRoads;
    }
}
