package thewaypointers.trafficsimulator.common;

import java.util.Map;

public class JunctionDTO extends NodeDTO {
    Map<Direction, RoadDTO> connections;
    Map<Direction, TrafficLightDTO> trafficLights;
}
