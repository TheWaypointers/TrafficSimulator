package thewaypointers.trafficsimulator.common.helpers;

import thewaypointers.trafficsimulator.common.*;

public class FirstVersionProvider implements IStateProvider {
    public static final float ROAD_LENGTH = 300;
    public static final int CHANGE_LIGHTS_EVERY_N_STATES = 1;
    public static final float ROAD_WIDTH = 20;

    WorldStateDTO worldState;

    private int stateNo;

    public FirstVersionProvider(){
        worldState = initialize();
    }

    private WorldStateDTO initialize(){
        WorldStateDTO ws = new WorldStateDTO();

        MapDTO roadMap = ws.getRoadMap();
        roadMap.addRoad("E1","A", Direction.Down, ROAD_LENGTH);
        roadMap.addRoad("A", "E2", Direction.Down, ROAD_LENGTH);

        // add traffic lights
        ws.getTrafficLightSystem().addJunction(roadMap.getJunction("A"));

        RoadDTO startRoad = roadMap.getJunction("A").getRoad(Direction.Up);
        RoadLocationDTO loc = new RoadLocationDTO(startRoad, startRoad.getEnd("E1"), 0, Lane.Right);
        ws.getVehicleList().addVehicle("V1", loc, VehicleType.CarNormal);

        return ws;
    }

    public WorldStateDTO getNextState(float vehicleMovement){
        JunctionDTO junction = worldState.getRoadMap().getJunctions().get(0);
        RoadDTO downRoad = junction.getRoad(Direction.Down);
        RoadDTO upRoad = junction.getRoad(Direction.Up);
        VehicleDTO v = worldState.getVehicleList().getAll().get(0);
        RoadLocationDTO loc = (RoadLocationDTO) v.getLocation();
        if(vehicleMovement > ROAD_LENGTH)
            throw new IllegalArgumentException("Cannot pass vehicleMovement bigger than the length of the road");

        stateNo++;

        RoadLocationDTO newLocation;
        if (loc.getDistanceTravelled() + vehicleMovement < loc.getRoad().getLength()) {
            newLocation = new RoadLocationDTO(loc.getRoad(),
                    loc.getOrigin(),
                    loc.getDistanceTravelled() + vehicleMovement,
                    loc.getLane());
        }else{
            // jump to next road
            RoadDTO newRoad = loc.getRoad().equals(upRoad) ? downRoad : upRoad;
            newLocation = new RoadLocationDTO(newRoad,
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
