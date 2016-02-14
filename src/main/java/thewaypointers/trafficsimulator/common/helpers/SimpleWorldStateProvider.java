package thewaypointers.trafficsimulator.common.helpers;

import thewaypointers.trafficsimulator.common.*;

import java.util.ArrayList;
import java.util.HashMap;

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
        roadMap.addRoad("A", "E1", Direction.Up, ROAD_LENGTH);
        roadMap.addRoad("A", "E2", Direction.Down, ROAD_LENGTH);

        // add traffic lights
        ws.getTrafficLightSystem().addJunction(roadMap.getJunction("A"));

        RoadDTO startRoad = roadMap.getJunction("A").getRoad(Direction.Up);
        LocationDTO loc = new LocationDTO(startRoad, startRoad.getEnd("E1"), 0, Lane.Right);
        VehicleDTO v1 = new VehicleDTO(loc, VehicleType.CarNormal);
        ws.getVehicles().add(v1);

        return ws;
    }

    public WorldStateDTO getNextState(float vehicleMovement) {
        JunctionDTO junction = worldState.getRoadMap().getJunctions().get(0);
        RoadDTO downRoad = junction.getRoad(Direction.Down);
        RoadDTO upRoad = junction.getRoad(Direction.Up);
        VehicleDTO v = worldState.getVehicles().get(0);
        LocationDTO loc = v.location;
        if(vehicleMovement > ROAD_LENGTH)
            throw new IllegalArgumentException("Cannot pass vehicleMovement bigger than the length of the road");

        stateNo++;

        if (loc.getDistanceTravelled() + vehicleMovement < loc.getRoad().getLength()) {
             v.location = new LocationDTO(loc.getRoad(),
                                          loc.getOrigin(),
                                          loc.getDistanceTravelled() + vehicleMovement,
                                          loc.getLane());
        }else{
            // jump to next road
            RoadDTO newRoad = loc.getRoad().equals(upRoad) ? downRoad : upRoad;
            v.location = new LocationDTO(newRoad,
                                         newRoad.getFrom(),
                                         loc.getDistanceTravelled() + vehicleMovement - loc.getRoad().getLength(),
                                         loc.getLane());
        }

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
