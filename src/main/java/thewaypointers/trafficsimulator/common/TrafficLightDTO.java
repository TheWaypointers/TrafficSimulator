package thewaypointers.trafficsimulator.common;

public class TrafficLightDTO {
    private TrafficLightColor color;

    TrafficLightDTO(){
        this.color = TrafficLightColor.Green;
    }

    TrafficLightDTO(TrafficLightColor color) {
        this.color = color;
    }

    public TrafficLightColor getColor() {
        return color;
    }

    void setColor(TrafficLightColor color) {
        this.color = color;
    }

    void changeColor(){
        TrafficLightColor newColor = getColor() == TrafficLightColor.Green?
                                     TrafficLightColor.Red :
                                     TrafficLightColor.Green;
        setColor(newColor);
    }
}
