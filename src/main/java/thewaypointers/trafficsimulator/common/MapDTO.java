package thewaypointers.trafficsimulator.common;

import java.util.*;

public class MapDTO {

    private Map<String, JunctionDTO> junctions;

    public MapDTO(){
        initialize(new HashMap<>());
    }

    public void initialize(Map<String, JunctionDTO> junctions)
    {
        this.junctions = new HashMap<>(junctions);
    }

    private List<JunctionDTO> getJunctionsUnprocessed(){
        List<JunctionDTO> junctions = new ArrayList<>();
        junctions.addAll(this.junctions.values());
        return junctions;
    }

    private List<RoadDTO> getRoads(List<JunctionDTO> junctions){
        // change this to set if performance unsatisfactory
        List<RoadDTO> roads = new ArrayList<>();
        for (JunctionDTO junction : junctions){
            for (Direction d : Direction.values()){
                RoadDTO road = junction.getRoad(d);
                //TODO check if contains() works well for road
                if(!roads.contains(road)){
                    roads.add(road);
                }
            }
        }
        return roads;
    }

    /**
     * Changes all Junctions with only one road connected into ExitNodeDTOs.
     */
    private List<JunctionDTO> process(List<JunctionDTO> junctions){
        List<JunctionDTO> newJunctions = new ArrayList<>();
        for(JunctionDTO junction : junctions){
            if(junction.getConnectedRoadsCount() > 1){
                newJunctions.add(junction);
            }else{
                RoadDTO road = junction.getRoads().get(0);
                NodeDTO newNode = new ExitNodeDTO(junction.getLabel());
                road.setEnd(junction, newNode);
            }
        }
        return newJunctions;
    }

    public List<JunctionDTO> getJunctions(){
        return process(getJunctionsUnprocessed());
    }

    public List<RoadDTO> getRoads(){
        return getRoads(getJunctions());
    }

    public RoadDTO getRoad(String label){
        List<RoadDTO> roads = getRoads();
        return roads.stream().filter(x->x.equals(label)).findFirst().get();
    }

    public int getJunctionCount(){
        return getJunctions().size();
    }

    public int getNodeCount(){
        return junctions.size();
    }

    public void addRoad(String from, String to, Direction direction, float length){
        RoadDTO newRoad = new RoadDTO();
        newRoad.setLength(length);
        if(!junctions.containsKey(from)){
            junctions.put(from, new JunctionDTO(from));
        }
        if(!junctions.containsKey(to)){
            junctions.put(to, new JunctionDTO(to));
        }
        JunctionDTO fromJunction = junctions.get(from);
        JunctionDTO toJunction = junctions.get(to);

        fromJunction.setRoad(direction, newRoad);
        toJunction.setRoad(direction.opposite(), newRoad);
        newRoad.setFrom(fromJunction);
        newRoad.setTo(toJunction);
    }

    public JunctionDTO getJunction(String label){
        //TODO rewrite processing so this can use the dictionary again
        return getJunctions().stream().filter(x->x.getLabel().equals(label)).findFirst().get();
    }
}
