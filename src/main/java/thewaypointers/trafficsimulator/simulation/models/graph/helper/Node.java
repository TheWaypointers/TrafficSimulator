package thewaypointers.trafficsimulator.simulation.models.graph.helper;


import thewaypointers.trafficsimulator.common.Direction;
import thewaypointers.trafficsimulator.simulation.enums.NodeType;

import java.util.HashMap;
import java.util.Map;

public class Node {

    private String nodeName;
    private boolean carEnteringFlag;

    private Map<Direction, RoadEdge> roads;

    private NodeType nodeType;

    public Node(String nodeName, NodeType nodeType){
        roads = new HashMap<>();
        for(Direction d : Direction.values()){
            roads.put(d, null);
        }
        this.setNodeName(nodeName);
        this.setNodeType(nodeType);
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public RoadEdge getLeftRoad() {
        return roads.get(Direction.Left);
    }

    public void setLeftRoad(RoadEdge leftRoad) {
        roads.put(Direction.Left, leftRoad);
    }

    public RoadEdge getUpRoad() {
        return roads.get(Direction.Up);
    }

    public void setUpRoad(RoadEdge upRoad) {
        roads.put(Direction.Up, upRoad);
    }

    public RoadEdge getRightRoad() {
        return roads.get(Direction.Right);
    }

    public void setRightRoad(RoadEdge rightRoad) {
        roads.put(Direction.Right, rightRoad);
    }

    public RoadEdge getDownRoad() {
        return roads.get(Direction.Down);
    }

    public void setDownRoad(RoadEdge downRoad) {
        roads.put(Direction.Down, downRoad);
    }

    public Direction getDirectionOfRoad(RoadEdge road){
        return roads.entrySet().stream()
                .filter(x->x.getValue()==road)
                .map(Map.Entry::getKey)
                .findFirst()
                .get();
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public boolean isCarEnteringFlag() {
        return carEnteringFlag;
    }

    public void setCarEnteringFlag(boolean carEnteringFlag) {
        this.carEnteringFlag = carEnteringFlag;
    }
}
