package thewaypointers.trafficsimulator.simulation.models.graph.helper;

import org.jgrapht.graph.DefaultWeightedEdge;
import thewaypointers.trafficsimulator.common.Direction;

public class RoadEdge {

    private DefaultWeightedEdge road;
    private Direction direction;
    private long speedLimit;
    private float length;

    private String origin;
    private String destination;

    public RoadEdge(DefaultWeightedEdge road, Direction direction, long speedLimit, float length, String origin, String destination){
        this.road = road;
        this.direction = direction;
        this.speedLimit = speedLimit;
        this.length = length;
        this.origin = origin;
        this.destination = destination;
    }

    public DefaultWeightedEdge getRoad() {
        return road;
    }

    public void setRoad(DefaultWeightedEdge road) {
        this.road = road;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
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
