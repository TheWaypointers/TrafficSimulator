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

import java.util.*;

public class GraphFactory {

    private SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> roadGraph;
    private HashMap<Node, ArrayList<RoadEdge>> nodeGraphMap;
    private HashMap<DefaultWeightedEdge, ArrayList<IVehicle>> vehicleMap;
    private List<RoadDTO> dtoRoads;
    private WorldStateDTO worldState;

    public GraphFactory(WorldStateDTO worldState) {
        this.worldState = worldState;
        setRoadGraph(new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class));
        setNodeGraphMap(new HashMap<>());
        setVehicleMap(new HashMap<>());

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
                vehicleMap.put(edge1, new ArrayList<>());
                RoadEdge re1 = new RoadEdge(edge1, choseDirection(worldState.getRoadMap().getDirection(road.getFrom().getLabel(),road.getTo().getLabel())), 30, road.getLength(), road.getFrom().getLabel(), road.getTo().getLabel());
                getNodeGraphMap().get(node1).add(re1);

                DefaultWeightedEdge edge2 = getRoadGraph().addEdge(road.getTo().getLabel(), road.getFrom().getLabel());
                getRoadGraph().setEdgeWeight(edge2, road.getLength());
                vehicleMap.put(edge2, new ArrayList<>());
                RoadEdge re2 = new RoadEdge(edge2, oppositeDirection(choseDirection(worldState.getRoadMap().getDirection(road.getFrom().getLabel(),road.getTo().getLabel()))), 30, road.getLength(), road.getTo().getLabel(), road.getFrom().getLabel());
                getNodeGraphMap().get(node2).add(re2);
            }
        }
        setNodeDirectionRoads();
    }

    private void setNodeDirectionRoads() {

        for(Node node : getNodeGraphMap().keySet()){
            for(RoadEdge road : getNodeGraphMap().get(node)){
                if(road.getDirection() == DirectionFromNode.Left){
                    node.setLeftRoad(road);
                } else if (road.getDirection() == DirectionFromNode.Right){
                    node.setRightRoad(road);
                } else if (road.getDirection() == DirectionFromNode.Down){
                    node.setDownRoad(road);
                } else{
                    node.setUpRoad(road);
                }
            }
        }

    }

    private DirectionFromNode oppositeDirection(DirectionFromNode directionFromNode) {
        if(directionFromNode == DirectionFromNode.Down){
            return  DirectionFromNode.Up;
        }
        else{
            return  DirectionFromNode.Left;
        }
    }

    private DirectionFromNode choseDirection(String direction) {
        switch (direction){
            case "Left" :
                return DirectionFromNode.Left;
            case "Down" :
                return DirectionFromNode.Down;
            case "Up" :
                return DirectionFromNode.Up;
            case "Right" :
                return DirectionFromNode.Right;
            default :
                break;
        }
        return null;
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
            return new TrafficLightNode(label, NodeType.JunctionTrafficLights);
        } else if (label.startsWith("E")) {
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
