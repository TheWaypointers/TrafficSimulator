package thewaypointers.trafficsimulator.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JunctionTrafficLightsDTO {

    private Map<Direction, RoadTrafficLightsDTO> roads;

    JunctionTrafficLightsDTO(){
        roads = new HashMap<>();
        for(Direction d:Direction.values()){
            roads.put(d, null);
        }
    }

    JunctionTrafficLightsDTO(Map<Direction, RoadTrafficLightsDTO> roads){
        this();
        this.roads.putAll(roads);
    }

    public RoadTrafficLightsDTO getRoad(Direction direction){
        return roads.get(direction);
    }

    void addRoad(Direction direction, Map<Lane, TrafficLightDTO> trafficLights){
        roads.put(direction, new RoadTrafficLightsDTO(trafficLights));
    }

    void addRoad(Direction direction){
        Map<Lane, TrafficLightDTO> trafficLights = new HashMap<>();
        for(Lane L : Lane.values()){
            trafficLights.put(L, new TrafficLightDTO());
        }
        addRoad(direction, trafficLights);
    }
    void addAllRoads(){
        for(Direction d: Direction.values()){
            addRoad(d);
        }
    }

    public List<Direction> GetTrafficlightDirections(){
        List<Direction> directions=new ArrayList<>();
        directions.addAll(this.roads.keySet());
        for (Direction direction:Direction.values()){
            if (roads.get(direction)==null)
                directions.remove(direction);
        }
        return  directions;
    }


}
