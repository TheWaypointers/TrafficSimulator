package thewaypointers.trafficsimulator.simulation.models.graph.helper;

import org.jgrapht.graph.DefaultWeightedEdge;

public class RoadEdge {

    private DefaultWeightedEdge road;
    private DirectionFromNode direction;
    private long speedLimit;
    private float roadLength;

    private String origin;
    private String destination;

    public RoadEdge(DefaultWeightedEdge road, DirectionFromNode direction, long speedLimit, float roadLength, String origin, String destination){
        this.road = road;
        this.direction = direction;
        this.speedLimit = speedLimit;
        this.roadLength = roadLength;
        this.origin = origin;
        this.destination = destination;
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

    public float getRoadLength() {
        return roadLength;
    }

    public void setRoadLength(float roadLength) {
        this.roadLength = roadLength;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
