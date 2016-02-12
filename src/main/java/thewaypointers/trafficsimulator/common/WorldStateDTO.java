package thewaypointers.trafficsimulator.common;

import java.util.List;
import java.util.Map;

public class WorldStateDTO {
    public MapDTO roadMap;
    public List<VehicleDTO> vehicles;
    public Map<JunctionDTO, Map<Direction, TrafficLightDTO>> trafficLights;
}
