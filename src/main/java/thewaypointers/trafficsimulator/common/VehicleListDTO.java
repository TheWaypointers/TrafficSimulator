package thewaypointers.trafficsimulator.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VehicleListDTO {

    private Map<String, VehicleDTO> vehicles;

    public VehicleListDTO(){
        vehicles = new HashMap<>();
    }

    public void addVehicle(VehicleDTO vehicle){
        vehicles.put(vehicle.getLabel(), new VehicleDTO(vehicle));
    }

    public void addVehicle(String label, ILocation location, VehicleType type){
        vehicles.put(label, new VehicleDTO(label, location, type));
    }

    public void addVehicle(String label, ILocation location, VehicleType type, float speed){
        vehicles.put(label, new VehicleDTO(label, speed, location, type));
    }

    public VehicleDTO getVehicle(String label){
        return vehicles.get(label);
    }

    public void setVehicleSpeed(String label, float newSpeed){
        vehicles.get(label).setSpeed(newSpeed);
    }

    public void setVehicleLocation(String label, ILocation newLocation){
        vehicles.get(label).setLocation(newLocation);
    }

    public List<VehicleDTO> getAll(){
        return vehicles.values().stream().collect(Collectors.toList());
    }

    public int getVehicleCount(){
        return vehicles.size();
    }
}
