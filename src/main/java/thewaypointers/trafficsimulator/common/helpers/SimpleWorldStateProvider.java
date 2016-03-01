package thewaypointers.trafficsimulator.common.helpers;

import thewaypointers.trafficsimulator.common.*;

public class SimpleWorldStateProvider {
    public static final float ROAD_LENGTH = 300;
    public static final int CHANGE_LIGHTS_EVERY_N_STATES = 1;

    WorldStateDTO roadNetworkWorldState;
    WorldStateDTO firstVersionWorldState;
    
    public boolean roadNetwork = true;
    
    private int stateNo;

    public SimpleWorldStateProvider(){
        firstVersionWorldState = initializeFirstVersionWorldState();
        roadNetworkWorldState = initializeRoadNetworkWorldState();
    }
    
    private WorldStateDTO initializeFirstVersionWorldState(){
        WorldStateDTO ws = new WorldStateDTO();

        MapDTO roadMap = ws.getRoadMap();
        roadMap.addRoad("E1","A", Direction.Down, ROAD_LENGTH);
        roadMap.addRoad("A", "E2", Direction.Down, ROAD_LENGTH);

        // add traffic lights
        ws.getTrafficLightSystem().addJunction(roadMap.getJunction("A"));

        RoadDTO startRoad = roadMap.getJunction("A").getRoad(Direction.Up);
        LocationDTO loc = new LocationDTO(startRoad, startRoad.getEnd("E1"), 0, Lane.Right);
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

        // add cars
        RoadDTO startRoad = roadMap.getJunction("A").getRoad(Direction.Up);
        LocationDTO loc = new LocationDTO(startRoad, startRoad.getEnd("E1"), 0, Lane.Right);
        RoadDTO startRoad2 = roadMap.getJunction("A").getRoad(Direction.Left);
        LocationDTO loc2 = new LocationDTO(startRoad2, startRoad2.getEnd("E3"), 0, Lane.Right);
        RoadDTO startRoad3 = roadMap.getJunction("B").getRoad(Direction.Right);
        LocationDTO loc3 = new LocationDTO(startRoad3, startRoad3.getEnd("B"), 0, Lane.Right);
        RoadDTO startRoad4 = roadMap.getJunction("B").getRoad(Direction.Down);
        LocationDTO loc4 = new LocationDTO(startRoad4, startRoad4.getEnd("E7"), 0, Lane.Right);
        ws.getVehicleList().addVehicle("V1", loc, VehicleType.CarNormal);
        ws.getVehicleList().addVehicle("V2", loc2, VehicleType.EmergencyService);
        ws.getVehicleList().addVehicle("V3", loc3, VehicleType.Bus);
        ws.getVehicleList().addVehicle("V4", loc4, VehicleType.CarCautious);
        return ws;
    }

    public WorldStateDTO getNextState(float vehicleMovement) {
        
        WorldStateDTO worldState = roadNetwork? roadNetworkWorldState : firstVersionWorldState;
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
