package thewaypointers.trafficsimulator.common.helpers;

import thewaypointers.trafficsimulator.common.*;

import java.util.HashMap;
import java.util.Map;

public class SimpleWorldStateProvider {
    public static final float ROAD_LENGTH = 300;
    public static final int CHANGE_LIGHTS_EVERY_N_STATES = 1;

    WorldStateDTO worldState;
    private int stateNo;

    public SimpleWorldStateProvider(){
        worldState = initializeWorldState();
    }

    private WorldStateDTO initializeWorldState() {

        WorldStateDTO ws = new WorldStateDTO();


        MapDTO roadMap = ws.getRoadMap();

        ExitNodeDTO a = new ExitNodeDTO("A");
        ExitNodeDTO b = new ExitNodeDTO("B");
        ExitNodeDTO c = new ExitNodeDTO("C");
        ExitNodeDTO exitE1 = new ExitNodeDTO("E1");
        ExitNodeDTO exitE2 = new ExitNodeDTO("E2");
        ExitNodeDTO exitE3 = new ExitNodeDTO("E3");
        ExitNodeDTO exitE4 = new ExitNodeDTO("E4");
        ExitNodeDTO exitE5 = new ExitNodeDTO("E5");
        ExitNodeDTO exitE6 = new ExitNodeDTO("E6");
        ExitNodeDTO exitE7 = new ExitNodeDTO("E7");


        RoadDTO r1 = new RoadDTO(a,exitE3,300);
        RoadDTO r2 = new RoadDTO(a,exitE1,300);
        RoadDTO r3 = new RoadDTO(a,b,300);
        RoadDTO r4 = new RoadDTO(a,c,300);
        RoadDTO r5 = new RoadDTO(b,a,300);
        RoadDTO r6 = new RoadDTO(b,exitE2,300);
        RoadDTO r7 = new RoadDTO(b,exitE4,300);
        RoadDTO r8 = new RoadDTO(b,exitE7,300);
        RoadDTO r9 = new RoadDTO(c,exitE5,300);
        RoadDTO r10 = new RoadDTO(c,a,300);
        RoadDTO r11 = new RoadDTO(c,exitE6,300);

        Map<Direction, RoadDTO> junctionA_map = new HashMap<>();
        junctionA_map.put(Direction.Down, r4);
        junctionA_map.put(Direction.Up, r2);
        junctionA_map.put(Direction.Left, r1);
        junctionA_map.put(Direction.Right, r3);

        Map<Direction, RoadDTO> junctionB_map = new HashMap<>();
        junctionA_map.put(Direction.Down, r8);
        junctionA_map.put(Direction.Up, r6);
        junctionA_map.put(Direction.Left, r5);
        junctionA_map.put(Direction.Right, r7);

        Map<Direction, RoadDTO> junctionC_map = new HashMap<>();
        junctionA_map.put(Direction.Down, r11);
        junctionA_map.put(Direction.Up, r10);
        junctionA_map.put(Direction.Left, r9);

        JunctionDTO junctionA = new JunctionDTO("A",junctionA_map);
        JunctionDTO junctionB = new JunctionDTO("B",junctionB_map);
        JunctionDTO junctionC = new JunctionDTO("C",junctionC_map);

        roadMap.addJunction(junctionA);
        roadMap.addJunction(junctionB);
        roadMap.addJunction(junctionC);

        /*
        roadMap.addRoad("A","E1", Direction.Up, 300);
        roadMap.addRoad("A","E3",Direction.Left,300);
        roadMap.addRoad("A","B",Direction.Right,300);
        roadMap.addRoad("A","C",Direction.Down,300);
        roadMap.addRoad("B","E2",Direction.Up,300);
        roadMap.addRoad("B","E4",Direction.Right,300);
        roadMap.addRoad("B","E7",Direction.Down,300);
        roadMap.addRoad("C","E5",Direction.Left,300);
        roadMap.addRoad("C","E6",Direction.Down,300);

*/



        // add traffic lights
        ws.getTrafficLightSystem().addJunction(roadMap.getJunction("A"));

        RoadDTO startRoad = roadMap.getJunction("A").getRoad(Direction.Up);
        LocationDTO loc = new LocationDTO(startRoad, startRoad.getEnd("E1"), 0, Lane.Right);
        ws.getVehicleList().addVehicle("V1", loc, VehicleType.CarNormal);

        return ws;
    }

    public WorldStateDTO getNextState(float vehicleMovement) {
        JunctionDTO junction = worldState.getRoadMap().getJunctions().get(0);
        RoadDTO downRoad = junction.getRoad(Direction.Down);
        RoadDTO upRoad = junction.getRoad(Direction.Up);
        VehicleDTO v = worldState.getVehicleList().getAll().get(0);
        LocationDTO loc = v.getLocation();
        if(vehicleMovement > ROAD_LENGTH)
            throw new IllegalArgumentException("Cannot pass vehicleMovement bigger than the length of the road");

        stateNo++;

        LocationDTO newLocation;
        if (loc.getDistanceTravelled() + vehicleMovement < loc.getRoad().getLength()) {
             newLocation = new LocationDTO(loc.getRoad(),
                                           loc.getOrigin(),
                                           loc.getDistanceTravelled() + vehicleMovement,
                                           loc.getLane());
        }else{
            // jump to next road
            RoadDTO newRoad = loc.getRoad().equals(upRoad) ? downRoad : upRoad;
            newLocation = new LocationDTO(newRoad,
                                          newRoad.getFrom(),
                                          loc.getDistanceTravelled() + vehicleMovement - loc.getRoad().getLength(),
                                          loc.getLane());
        }
        worldState.getVehicleList().setVehicleLocation(v.getLabel(), newLocation);

        //change traffic lights
        if(stateNo % CHANGE_LIGHTS_EVERY_N_STATES == 0){
            worldState.getTrafficLightSystem()
                    .changeTrafficLightColor(junction.getLabel(), Direction.Down, Lane.Right);
            worldState.getTrafficLightSystem()
                    .changeTrafficLightColor(junction.getLabel(), Direction.Up, Lane.Right);
        }

        return worldState;
    }
}
