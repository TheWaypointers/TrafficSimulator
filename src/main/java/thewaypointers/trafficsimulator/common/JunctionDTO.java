package thewaypointers.trafficsimulator.common;

import java.util.HashMap;
import java.util.Map;

public class JunctionDTO extends NodeDTO {
    public Map<Direction, RoadDTO> connections;

    public JunctionDTO(String label,
                       RoadDTO up,
                       RoadDTO down,
                       RoadDTO left,
                       RoadDTO right) {
        super(label);
        connections = new HashMap<>();
        connections.put(Direction.Up, up);
        connections.put(Direction.Down, down);
        connections.put(Direction.Left, left);
        connections.put(Direction.Right, right);
    }
}
