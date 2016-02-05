package thewaypointers.trafficsimulator.common.helpers;

import thewaypointers.trafficsimulator.common.*;
import thewaypointers.trafficsimulator.common.enums.VehicleType;
import thewaypointers.trafficsimulator.common.simulation.models.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimpleWorldStateProvider {
    public static final float ROAD_LENGTH = 300;

    IStateChangeListener stateChangeListener;
    WorldStateDTO worldState;

    public SimpleWorldStateProvider(IStateChangeListener stateChangeListener){
        this.stateChangeListener = stateChangeListener;
        worldState = initializeWorldState();
    }

    private WorldStateDTO initializeWorldState() {
        RoadDTO e1_a = new RoadDTO();
        RoadDTO a_e2 = new RoadDTO();
        e1_a.length = ROAD_LENGTH;
        a_e2.length = ROAD_LENGTH;

        ExitNodeDTO e1 = new ExitNodeDTO("E1");
        ExitNodeDTO e2 = new ExitNodeDTO("E2");
        JunctionDTO a = new JunctionDTO("A", a_e2, e1_a, null, null);

        // connect the roads
        // TODO make this more straightforward
        e1_a.start = e1;
        e1_a.end = a;
        a_e2.start = a;
        a_e2.end = e2;

        MapDTO roadMap = new MapDTO(a);

        LocationDTO loc = new LocationDTO(e1_a, e1_a.start, 0, Lane.Right);
        VehicleDTO v1 = new VehicleDTO(loc, VehicleType.CarNormal);
        ArrayList<VehicleDTO> vehicles = new ArrayList<>();
        vehicles.add(v1);

        WorldStateDTO ws = new WorldStateDTO();

        ws.roadMap = roadMap;
        ws.vehicles = vehicles;

        return ws;
    }

    public void getNextState(float vehicleMovement) {
        RoadDTO downRoad = worldState.roadMap.junctions.get(0).connections.get(Direction.Down);
        RoadDTO upRoad = worldState.roadMap.junctions.get(0).connections.get(Direction.Up);
        LocationDTO loc = worldState.vehicles.get(0).location;
        if(vehicleMovement > ROAD_LENGTH)
            throw new IllegalArgumentException("Cannot pass vehicleMovement bigger than the length of the road");

        if (loc.distanceTravelled + vehicleMovement < loc.road.length) {
            loc.distanceTravelled += vehicleMovement;
        }else{
            // jump to next road
            loc.distanceTravelled = loc.distanceTravelled + vehicleMovement - loc.road.length;
            loc.road = loc.road.equals(upRoad) ? downRoad : upRoad;
            loc.origin = loc.road.start;
        }
    }
}
