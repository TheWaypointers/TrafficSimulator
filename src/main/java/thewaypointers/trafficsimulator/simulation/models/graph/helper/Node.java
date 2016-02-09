package thewaypointers.trafficsimulator.simulation.models.graph.helper;


import thewaypointers.trafficsimulator.simulation.enums.NodeType;

public class Node {

    private String nodeName;

    private RoadEdge LeftRoad;
    private RoadEdge UpRoad;
    private RoadEdge RightRoad;
    private RoadEdge DownRoad;

    private NodeType nodeType;

    public Node(String nodeName, NodeType nodeType){
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
        return LeftRoad;
    }

    public void setLeftRoad(RoadEdge leftRoad) {
        LeftRoad = leftRoad;
    }

    public RoadEdge getUpRoad() {
        return UpRoad;
    }

    public void setUpRoad(RoadEdge upRoad) {
        UpRoad = upRoad;
    }

    public RoadEdge getRightRoad() {
        return RightRoad;
    }

    public void setRightRoad(RoadEdge rightRoad) {
        RightRoad = rightRoad;
    }

    public RoadEdge getDownRoad() {
        return DownRoad;
    }

    public void setDownRoad(RoadEdge downRoad) {
        DownRoad = downRoad;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }
}
