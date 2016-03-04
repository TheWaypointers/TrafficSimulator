package thewaypointers.trafficsimulator.common;

public class VehicleDTO {
    private String label;
    private float speed;
    private ILocation location;
    private VehicleType type;

    public VehicleDTO(String label, float speed, ILocation location, VehicleType type) {
        this(label, location, type);
        this.speed = speed;
    }

    public VehicleDTO(String label, ILocation location, VehicleType type){
        this.label = label;
        this.location = location;
        this.type = type;
    }

    public VehicleDTO(VehicleDTO other){
        this.label = other.getLabel();
        this.location = other.getLocation().copy();
        this.speed = other.getSpeed();
        this.type = other.getType();
    }

    public String getLabel() {
        return label;
    }

    void setLabel(String label) {
        this.label = label;
    }

    public float getSpeed() {
        return speed;
    }

    void setSpeed(float speed) {
        this.speed = speed;
    }

    public ILocation getLocation() {
        return location;
    }

    void setLocation(ILocation location) {
        this.location = location;
    }

    public VehicleType getType() {
        return type;
    }

    void setType(VehicleType type) {
        this.type = type;
    }
}
