package thewaypointers.trafficsimulator.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JunctionDTO extends NodeDTO {
    private Map<Direction, RoadDTO> connections;

    JunctionDTO(String label){
        super(label);
        initialize(new HashMap<>());
    }

    private void initialize(Map<Direction, RoadDTO> connectionMap){
        Map<Direction, RoadDTO> defaultMap = new HashMap<>();
        defaultMap.put(Direction.Down, null);
        defaultMap.put(Direction.Up, null);
        defaultMap.put(Direction.Left, null);
        defaultMap.put(Direction.Right, null);

        connections = new HashMap<>();
        connections.putAll(defaultMap);
        connections.putAll(connectionMap);
    }

    public JunctionDTO(String label, Map<Direction, RoadDTO> connections){
        this(label);
        initialize(connections);
    }

    public JunctionDTO(String label,
                       RoadDTO up,
                       RoadDTO down,
                       RoadDTO left,
                       RoadDTO right) {
        this(label);
        connections = new HashMap<>();
        connections.put(Direction.Up, up);
        connections.put(Direction.Down, down);
        connections.put(Direction.Left, left);
        connections.put(Direction.Right, right);
    }

    public JunctionDTO(JunctionDTO o){
        this(o.getLabel());
        initialize(o.connections);
    }

    void setRoad(Direction direction, RoadDTO road){ connections.put(direction, road); }

    public RoadDTO getRoad(Direction direction){
        return connections.get(direction);
    }

    public RoadDTO getRoadLeadingTo(NodeDTO node){
        return connections.entrySet().stream().filter(
                r -> r.getValue().getFrom().equals(node) ||
                     r.getValue().getTo().equals(node)
        ).findFirst().get().getValue();
    }

    public JunctionDTO changeRoad(Direction direction, RoadDTO road){
        JunctionDTO newJunction = new JunctionDTO(this);
        newJunction.connections.put(direction, road);
        return newJunction;
    }

    public int getConnectedRoadsCount(){
        return (int)connections.values().stream().filter(x -> x!=null).count();
    }

    public List<RoadDTO> getRoads(){
        List<RoadDTO> ret = new ArrayList<>();
        ret.addAll(connections.values().stream().filter(x -> x!=null).collect(Collectors.toList()));
        return ret;
    }
}
