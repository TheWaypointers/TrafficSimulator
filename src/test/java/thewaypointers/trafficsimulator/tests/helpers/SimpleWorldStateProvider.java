package thewaypointers.trafficsimulator.tests.helpers;

import thewaypointers.trafficsimulator.common.*;
import thewaypointers.trafficsimulator.common.enums.VehicleType;
import thewaypointers.trafficsimulator.common.simulation.models.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimpleWorldStateProvider {
    IStateChangeListener stateChangeListener;
    WorldStateDTO worldState;

    public SimpleWorldStateProvider(IStateChangeListener stateChangeListener){
        this.stateChangeListener = stateChangeListener;
    }

    public WorldStateDTO initializeWorldState()
    {
        RoadDTO e1_a = new RoadDTO();
        RoadDTO a_e2 = new RoadDTO();
        e1_a.length = 300;
        a_e2.length = 300;

        ExitNodeDTO e1 = new ExitNodeDTO("E1");
        ExitNodeDTO e2 = new ExitNodeDTO("E2");
        JunctionDTO a = new JunctionDTO("A", e1_a, a_e2, null, null);

        // connect the roads
        // TODO make this more straightforward
        e1_a.start = e1;
        e1_a.end = a;
        a_e2.start = a;
        a_e2.end = e2;

        MapDTO roadMap = new MapDTO(a);

        LocationDTO loc = new LocationDTO(e1_a, e1, 0, Lane.Right);
        VehicleDTO v1 = new VehicleDTO(loc, VehicleType.CarNormal);
        ArrayList<VehicleDTO> vehicles = new ArrayList<>();
        vehicles.add(v1);

        WorldStateDTO ws = new WorldStateDTO();

        ws.roadMap = roadMap;
        ws.vehicles = vehicles;

        return ws;
    }
}
