package thewaypointers.trafficsimulator.common;

public class RoadLocationDTO implements ILocation{
    private RoadDTO road;
    private float distanceTravelled;
    private NodeDTO origin;
    private Lane lane;

    public RoadDTO getRoad() {
        return road;
    }

    public float getDistanceTravelled() {
        return distanceTravelled;
    }

    public NodeDTO getOrigin() {
        return origin;
    }

    public Lane getLane() {
        return lane;
    }

    public RoadLocationDTO(RoadDTO road, NodeDTO origin, float distanceTravelled, Lane lane) {
        this.road = road;
        this.distanceTravelled = distanceTravelled;
        this.origin = origin;
        this.lane = lane;
    }

    public RoadLocationDTO(RoadLocationDTO other){
        this(other.road, other.origin, other.distanceTravelled, other.lane);
    }

    @Override
    public ILocation copy() {
        return new RoadLocationDTO(this);
    }
}
