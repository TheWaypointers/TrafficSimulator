package thewaypointers.trafficsimulator.common;

public class VehicleDTO {
    public LocationDTO location;

    public VehicleDTO(LocationDTO location, VehicleType type) {
        this.location = location;
        this.type = type;
    }

    public VehicleType type;
}
