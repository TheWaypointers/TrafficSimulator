package thewaypointers.trafficsimulator.simulation.models.graph.helper;

import org.jgrapht.graph.DefaultWeightedEdge;

public class RoadEdge {

    private DefaultWeightedEdge road;
    private DirectionFromNode direction;
    private long speedLimit;
    private float length;

    public RoadEdge(DefaultWeightedEdge road, DirectionFromNode direction, long speedLimit, float roadLength){
        this.road = road;
        this.direction = direction;
        this.speedLimit = speedLimit;
        this.length = roadLength;
    }

    public DefaultWeightedEdge getRoad() {
        return road;
    }

    public void setRoad(DefaultWeightedEdge road) {
        this.road = road;
    }

    public DirectionFromNode getDirection() {
        return direction;
    }

    public void setDirection(DirectionFromNode direction) {
        this.direction = direction;
    }

    public long getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(long speedLimit) {
        this.speedLimit = speedLimit;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

}
