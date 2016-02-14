package thewaypointers.trafficsimulator.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WorldStateDTO {
    private MapDTO roadMap;
    private List<VehicleDTO> vehicles;
    private TrafficLightSystemDTO trafficLightSystem;

    public WorldStateDTO(){
        this.roadMap = new MapDTO();
        this.vehicles = new ArrayList<>();
        this.trafficLightSystem = new TrafficLightSystemDTO();
    }

    public WorldStateDTO(MapDTO roadMap, List<VehicleDTO> vehicles, TrafficLightSystemDTO trafficLightSystem) {
        this.roadMap = roadMap;
        this.vehicles = vehicles;
        this.trafficLightSystem = trafficLightSystem;
    }

    public MapDTO getRoadMap() {
        return roadMap;
    }

    public void setRoadMap(MapDTO roadMap) {
        this.roadMap = roadMap;
    }

    public List<VehicleDTO> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<VehicleDTO> vehicles) {
        this.vehicles = vehicles;
    }

    public TrafficLightSystemDTO getTrafficLightSystem() {
        return trafficLightSystem;
    }

    public void setTrafficLightSystem(TrafficLightSystemDTO trafficLightSystem) {
        this.trafficLightSystem = trafficLightSystem;
    }


}
