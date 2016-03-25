package thewaypointers.trafficsimulator.simulation.models.graph.helper;

import thewaypointers.trafficsimulator.common.TrafficLightColor;
import thewaypointers.trafficsimulator.simulation.enums.NodeType;

public class TrafficLightNode extends Node {

    private TrafficLightColor color;
    private float width;
    private float height;

    public TrafficLightNode(String nodeName, NodeType nodeType, float width, float height) {
        super(nodeName, nodeType);
        this.width = width;
        this.height = height;
        setColor(TrafficLightColor.Green);
    }

    public void  changeLightColor(){
        if(this.getColor() == TrafficLightColor.Green){
            this.setColor(TrafficLightColor.Red);
        }
        else{
            this.setColor(TrafficLightColor.Green);
        }
    }

    public TrafficLightColor getColor() {
        return color;
    }

    public void setColor(TrafficLightColor color) {
        this.color = color;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
