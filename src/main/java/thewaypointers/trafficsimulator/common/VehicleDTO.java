package thewaypointers.trafficsimulator.common;

import thewaypointers.trafficsimulator.simulation.enums.VehicleType;

public class VehicleDTO {
    public LocationDTO location;

    public VehicleDTO(LocationDTO location, VehicleType type) {
        this.location = location;
        this.type = type;
    }

    public VehicleType type;
}
