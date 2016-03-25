package thewaypointers.trafficsimulator.common.helpers;

import thewaypointers.trafficsimulator.common.*;

public class JunctionTestProvider implements IStateProvider {
    public static final float ROAD_LENGTH = 300;
    public static final float ROAD_WIDTH = 20;

    public static final String STRAIGHT_GOER_NAME = "straight";
    public static final String RIGHT_TURNER_NAME = "rightTurn";
    public static final String LEFT_TURNER_NAME = "leftTurn";

    WorldStateDTO worldState;

    public JunctionTestProvider(){
        worldState = initialize();
    }

    private WorldStateDTO initialize(){
        WorldStateDTO ws = new WorldStateDTO();

        MapDTO roadMap = ws.getRoadMap();
        roadMap.addRoad("A","B", Direction.Down, ROAD_LENGTH);
        roadMap.addRoad("B","C", Direction.Right, ROAD_LENGTH);
        roadMap.addRoad("C","D", Direction.Up, ROAD_LENGTH);
        roadMap.addRoad("D","A", Direction.Left, ROAD_LENGTH);

        JunctionLocationDTO loc = new JunctionLocationDTO(
                "A",
                Direction.Down,
                Direction.Up,
                0f);
        ws.getVehicleList().addVehicle(STRAIGHT_GOER_NAME, loc, VehicleType.CarNormal);
        loc = new JunctionLocationDTO(
                "B",
                Direction.Down,
                Direction.Right,
                0f);
        ws.getVehicleList().addVehicle(RIGHT_TURNER_NAME, loc, VehicleType.CarNormal);
        loc = new JunctionLocationDTO(
                "C",
                Direction.Down,
                Direction.Left,
                0f);
        ws.getVehicleList().addVehicle(LEFT_TURNER_NAME, loc, VehicleType.CarNormal);
        return ws;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    public WorldStateDTO getNextState(long vehicleMovement) {
        for (VehicleDTO v : worldState.getVehicleList().getAll()){
            JunctionLocationDTO loc = (JunctionLocationDTO) v.getLocation();
            JunctionMoveResult result = loc.move(vehicleMovement, ROAD_WIDTH, ROAD_WIDTH);
            loc = result.getRemainder() > 0 ?
                    new JunctionLocationDTO(loc.getJunctionLabel(),
                            loc.getOrigin().toLeft(),
                            loc.getTarget().toLeft(),
                            0f) :
                    result.getNewLocation();

            worldState.getVehicleList().setVehicleLocation(v.getLabel(), loc);
        }
        worldState.setClock(worldState.getClock()+1);
        return worldState;
    }
}