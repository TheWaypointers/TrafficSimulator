package thewaypointers.trafficsimulator.common.helpers;

import thewaypointers.trafficsimulator.common.*;

public class SimpleWorldStateProvider {
    public static final float ROAD_LENGTH = 300;
    public static final int CHANGE_LIGHTS_EVERY_N_STATES = 1;

    WorldStateDTO worldState;
    
    private int stateNo;

    public SimpleWorldStateProvider(SimpleWorldState state){
        switch(state){
            case FIRST_VERSION:
                worldState = initializeFirstVersionWorldState();
                break;
            case ROAD_NETWORK:
                worldState = initializeRoadNetworkWorldState();
                break;
            default:
                throw new IllegalArgumentException(
                        "No logic implemented for specified state!");
        }
    }
    
    private WorldStateDTO initializeFirstVersionWorldState(){
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

    private WorldStateDTO initializeRoadNetworkWorldState() {

        WorldStateDTO ws = new WorldStateDTO();

        // add roads
        MapDTO roadMap = ws.getRoadMap();
        roadMap.addRoad("E1","A", Direction.Down, ROAD_LENGTH);
        roadMap.addRoad("E3","A",Direction.Right,ROAD_LENGTH);
        roadMap.addRoad("A","B",Direction.Right,ROAD_LENGTH);
        roadMap.addRoad("A","C",Direction.Down,ROAD_LENGTH);
        roadMap.addRoad("E2","B",Direction.Down,ROAD_LENGTH);
        roadMap.addRoad("B","E4",Direction.Right,ROAD_LENGTH);
        roadMap.addRoad("B","E7",Direction.Down,ROAD_LENGTH);
        roadMap.addRoad("E5","C",Direction.Right,ROAD_LENGTH);
        roadMap.addRoad("C","E6",Direction.Down,ROAD_LENGTH);

        // add traffic lights
        ws.getTrafficLightSystem().addJunction(roadMap.getJunction("A"));
        ws.getTrafficLightSystem().addJunction(roadMap.getJunction("B"));
        ws.getTrafficLightSystem().addJunction(roadMap.getJunction("C"));

        // add cars
        RoadDTO startRoad = roadMap.getJunction("A").getRoad(Direction.Up);
        RoadLocationDTO loc = new RoadLocationDTO(startRoad, startRoad.getEnd("E1"), 0, Lane.Right);
        RoadDTO startRoad2 = roadMap.getJunction("A").getRoad(Direction.Left);
        RoadLocationDTO loc2 = new RoadLocationDTO(startRoad2, startRoad2.getEnd("E3"), 0, Lane.Right);
        RoadDTO startRoad3 = roadMap.getJunction("B").getRoad(Direction.Right);
        RoadLocationDTO loc3 = new RoadLocationDTO(startRoad3, startRoad3.getEnd("B"), 0, Lane.Right);
        RoadDTO startRoad4 = roadMap.getJunction("B").getRoad(Direction.Down);
        RoadLocationDTO loc4 = new RoadLocationDTO(startRoad4, startRoad4.getEnd("E7"), 0, Lane.Right);
        ws.getVehicleList().addVehicle("V1", loc, VehicleType.CarNormal);
        ws.getVehicleList().addVehicle("V2", loc2, VehicleType.EmergencyService);
        ws.getVehicleList().addVehicle("V3", loc3, VehicleType.Bus);
        ws.getVehicleList().addVehicle("V4", loc4, VehicleType.CarCautious);
        return ws;
    }

    public WorldStateDTO getNextState(float vehicleMovement) {

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
