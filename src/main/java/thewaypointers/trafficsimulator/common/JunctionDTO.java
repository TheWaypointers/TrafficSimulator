package thewaypointers.trafficsimulator.common;

import java.util.Map;

public class JunctionDTO extends NodeDTO {
    public Map<Direction, RoadDTO> connections;
    public Map<Direction, TrafficLightDTO> trafficLights;
}
