package thewaypointers.trafficsimulator.common;

public class WorldStateDTO {
    private MapDTO roadMap;
    private VehicleListDTO vehicleList;
    private TrafficLightSystemDTO trafficLightSystem;

    public WorldStateDTO(){
        this.roadMap = new MapDTO();
        this.vehicleList = new VehicleListDTO();
        this.trafficLightSystem = new TrafficLightSystemDTO();
    }

    public WorldStateDTO(MapDTO roadMap, VehicleListDTO vehicles, TrafficLightSystemDTO trafficLightSystem) {
        this.roadMap = roadMap;
        this.vehicleList = vehicles;
        this.trafficLightSystem = trafficLightSystem;
    }

    public MapDTO getRoadMap() {
        return roadMap;
    }

    public void setRoadMap(MapDTO roadMap) {
        this.roadMap = roadMap;
    }

    public VehicleListDTO getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(VehicleListDTO vehicleList) {
        this.vehicleList = vehicleList;
    }

    public TrafficLightSystemDTO getTrafficLightSystem() {
        return trafficLightSystem;
    }

    public void setTrafficLightSystem(TrafficLightSystemDTO trafficLightSystem) {
        this.trafficLightSystem = trafficLightSystem;
    }


}
