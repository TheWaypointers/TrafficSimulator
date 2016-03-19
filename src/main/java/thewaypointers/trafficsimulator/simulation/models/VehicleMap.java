package thewaypointers.trafficsimulator.simulation.models;

import org.jgrapht.graph.DefaultWeightedEdge;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.Node;
import thewaypointers.trafficsimulator.simulation.models.interfaces.IVehicle;
import thewaypointers.trafficsimulator.utils.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class VehicleMap {
    HashMap<DefaultWeightedEdge, ArrayList<IVehicle>> roadMap;
    HashMap<Node, ArrayList<IVehicle>> junctionMap;

    public VehicleMap(){
        roadMap = new HashMap<>();
        junctionMap = new HashMap<>();
    }

    public List<IVehicle> getFromRoad(DefaultWeightedEdge road){
        return roadMap.get(road);
    }

    private <TKey> List<IVehicle> getAll(HashMap<TKey, ArrayList<IVehicle>> map){
        return map.entrySet().stream()
                .map(x->x.getValue())
                .collect(ArrayList<IVehicle>::new, List::addAll, List::addAll);
    }

    public List<IVehicle> getAllFromRoads(){
        return getAll(roadMap);
    }

    public List<IVehicle> getAllFromJunctions(){
        return getAll(junctionMap);
    }

    public List<IVehicle> getAll(){
        List<IVehicle> list1 = getAllFromRoads();
        List<IVehicle> list2 = getAllFromJunctions();
        list1.addAll(list2);
        return list1;
    }

    private ArrayList<IVehicle> find(IVehicle vehicle){
        ArrayList<IVehicle> list;
        for(DefaultWeightedEdge road : roadMap.keySet()){
            list = roadMap.get(road);
            if(list.contains(vehicle)){
                return list;
            }
        }
        for(Node junction : junctionMap.keySet()){
            list = junctionMap.get(junction);
            if(list.contains(vehicle)){
                return list;
            }
        }
        throw new IllegalArgumentException(String.format("Vehicle %s not found!", vehicle));
    }

    public void remove(IVehicle vehicle){
        ArrayList<IVehicle> list = find(vehicle);
        list.remove(vehicle);
    }

    public void add(DefaultWeightedEdge road, IVehicle vehicle){
        if(!roadMap.containsKey(road)){
            roadMap.put(road, new ArrayList<>());
        }
        roadMap.get(road).add(vehicle);
    }

    public void add(Node road, IVehicle vehicle){
        if(!junctionMap.containsKey(road)){
            junctionMap.put(road, new ArrayList<>());
        }
        junctionMap.get(road).add(vehicle);
    }

    public int count(){
        int inJunctions = junctionMap.entrySet().stream()
                .mapToInt(x->x.getValue().size())
                .sum();
        int inRoads = roadMap.entrySet().stream()
                .mapToInt(x->x.getValue().size())
                .sum();
        return inJunctions + inRoads;
    }
}
