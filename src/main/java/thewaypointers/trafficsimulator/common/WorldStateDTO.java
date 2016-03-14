package thewaypointers.trafficsimulator.common;

public class WorldStateDTO {
    private float clock;
    private MapDTO roadMap;
    private VehicleListDTO vehicleList;
    private TrafficLightSystemDTO trafficLightSystem;

    public WorldStateDTO(){
        this.roadMap = new MapDTO();
        this.vehicleList = new VehicleListDTO();
        this.trafficLightSystem = new TrafficLightSystemDTO();
        this.clock = 0;
    }

    public WorldStateDTO(MapDTO roadMap, VehicleListDTO vehicles, TrafficLightSystemDTO trafficLightSystem) {
        this.roadMap = roadMap;
        this.vehicleList = vehicles;
        this.trafficLightSystem = trafficLightSystem;
    }

    public WorldStateDTO(MapDTO roadMap,
                         VehicleListDTO vehicles,
                         TrafficLightSystemDTO trafficLightSystem,
                         float clock){
        this(roadMap, vehicles, trafficLightSystem);
        this.clock = clock;
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

    public float getClock() {
        return clock;
    }

    public void setClock(float clock){
        this.clock = clock;
    }


}
