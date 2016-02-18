package thewaypointers.trafficsimulator.common;

import java.util.HashMap;
import java.util.Map;

public class RoadTrafficLightsDTO {

    private Map<Lane, TrafficLightDTO> trafficLights;

    RoadTrafficLightsDTO(){
        trafficLights = new HashMap<>();
        for(Lane L : Lane.values()){
            trafficLights.put(L, null);
        }
    }

    RoadTrafficLightsDTO(Map<Lane, TrafficLightDTO> trafficLights){
        this();
        this.trafficLights.putAll(trafficLights);
    }

    public TrafficLightDTO getTrafficLight(Lane lane){
        return trafficLights.get(lane);
    }

    void addTrafficLight(Lane lane, TrafficLightColor color){
        trafficLights.put(lane, new TrafficLightDTO(color));
    }

    void addTrafficLight(Lane lane){
        trafficLights.put(lane, new TrafficLightDTO());
    }
}
