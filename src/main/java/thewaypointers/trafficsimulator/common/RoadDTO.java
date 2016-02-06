package thewaypointers.trafficsimulator.common;

public class RoadDTO {
    public float length;
    public NodeDTO start;
    public NodeDTO end;

    public RoadDTO(){}

    public RoadDTO(NodeDTO start, NodeDTO end, float length) {
        this.length = length;
        this.start = start;
        this.end = end;
    }

    public boolean equals(RoadDTO other) {
        return (start.label.equals(other.start.label) && end.label.equals(other.end.label)) ||
                (start.label.equals(other.end.label) && end.label.equals(other.start.label));
    }
}
