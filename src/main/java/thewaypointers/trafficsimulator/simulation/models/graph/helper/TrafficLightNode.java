package thewaypointers.trafficsimulator.simulation.models.graph.helper;

import thewaypointers.trafficsimulator.common.TrafficLightColor;
import thewaypointers.trafficsimulator.simulation.enums.NodeType;

public class TrafficLightNode extends Node {

    private TrafficLightColor left;
    private TrafficLightColor right;
    private TrafficLightColor up;
    private TrafficLightColor down;
    private TrafficLightColor color;
    private float width;
    private float height;

    public TrafficLightNode(String nodeName, NodeType nodeType, float width, float height) {
        super(nodeName, nodeType);
        setLeft(TrafficLightColor.Green);
        setRight(TrafficLightColor.Green);
        setUp(TrafficLightColor.Red);
        setDown(TrafficLightColor.Red);
        this.width = width;
        this.height = height;
    }

    public void  changeLightColor(){
        if(this.getLeft() == TrafficLightColor.Green){
            this.setLeft(TrafficLightColor.Red);
            this.setRight(TrafficLightColor.Red);
            this.setUp(TrafficLightColor.Green);
            this.setDown(TrafficLightColor.Green);
        }
        else{
            this.setUp(TrafficLightColor.Red);
            this.setDown(TrafficLightColor.Red);
            this.setLeft(TrafficLightColor.Green);
            this.setRight(TrafficLightColor.Green);
        }
    }

    public TrafficLightColor getLeft() {
        return left;
    }

    public void setLeft(TrafficLightColor left) {
        this.left = left;
    }

    public TrafficLightColor getRight() {
        return right;
    }

    public void setRight(TrafficLightColor right) {
        this.right = right;
    }

    public TrafficLightColor getUp() {
        return up;
    }

    public void setUp(TrafficLightColor up) {
        this.up = up;
    }

    public TrafficLightColor getDown() {
        return down;
    }

    public void setDown(TrafficLightColor down) {
        this.down = down;
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
