package thewaypointers.trafficsimulator.simulation.models.graph.helper;

import thewaypointers.trafficsimulator.common.TrafficLightColor;
import thewaypointers.trafficsimulator.simulation.enums.NodeType;

public class TrafficLightNode extends Node {

    private TrafficLightColor color;

    public TrafficLightNode(String nodeName, NodeType nodeType) {
        super(nodeName, nodeType);
        setColor(TrafficLightColor.Green);
    }

    public TrafficLightColor getColor() {
        return color;
    }

    public void setColor(TrafficLightColor color) {
        this.color = color;
    }
}
