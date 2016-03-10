package thewaypointers.trafficsimulator.common;

import thewaypointers.trafficsimulator.common.Direction;
import thewaypointers.trafficsimulator.common.TrafficLightDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrafficLightSystemDTO {

    private Map<String, JunctionTrafficLightsDTO> junctions;

    public TrafficLightSystemDTO(){
        junctions = new HashMap<>();
    }

    public TrafficLightSystemDTO(Map<String, JunctionTrafficLightsDTO> junctions){
        this();
        this.junctions.putAll(junctions);
    }

    public JunctionTrafficLightsDTO getJunction(String label){
        return junctions.get(label);
    }

    public RoadTrafficLightsDTO getRoad(String junctionLabel, Direction direction){
        return getJunction(junctionLabel).getRoad(direction);
    }

    public TrafficLightDTO getTrafficLight(
            String junctionLabel,
            Direction direction,
            Lane lane){
        return getRoad(junctionLabel, direction).getTrafficLight(lane);
    }

    public TrafficLightColor getTrafficLightColor(
            String junctionLabel,
            Direction direction,
            Lane lane){
        return getTrafficLight(junctionLabel, direction, lane).getColor();
    }

    public void addJunction(JunctionDTO junction){
        JunctionTrafficLightsDTO junctionTrafficLights = new JunctionTrafficLightsDTO();
        for(Direction d: junction.getConnectedDirections()){
            junctionTrafficLights.addRoad(d);
        }
        junctions.put(junction.getLabel(), junctionTrafficLights);
    }

    public void removeJunction(String label){
        junctions.remove(label);
    }

    public void setTrafficLightColor(
            String junctionLabel,
            Direction direction,
            Lane lane,
            TrafficLightColor color
    ){
        getTrafficLight(junctionLabel, direction, lane).setColor(color);
    }

    public void changeTrafficLightColor(
            String junctionLabel,
            Direction direction,
            Lane lane
    ){
        getTrafficLight(junctionLabel, direction, lane).changeColor();
    }

    public List<String> GetJunctionLabel(){
        List<String> junctionlabel=new ArrayList<>();
        junctionlabel.addAll(this.junctions.keySet());
        return junctionlabel;
    }


}
