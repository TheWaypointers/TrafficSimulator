package thewaypointers.trafficsimulator.simulation.models.graph.helper;

import org.jgrapht.graph.DefaultWeightedEdge;

public class RoadEdge {

    private DefaultWeightedEdge road;
    private DirectionFromNode direction;

    public RoadEdge(DefaultWeightedEdge road, DirectionFromNode direction){
        this.road = road;
        this.direction = direction;
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
}
