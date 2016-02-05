package thewaypointers.trafficsimulator.common;

public class LocationDTO {
    public RoadDTO road;
    public float distanceTravelled;
    public NodeDTO origin;
    public Lane lane;

    public LocationDTO(RoadDTO road, NodeDTO origin, float distanceTravelled, Lane lane) {
        this.road = road;
        this.distanceTravelled = distanceTravelled;
        this.origin = origin;
        this.lane = lane;
    }
}
