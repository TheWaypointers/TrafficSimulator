package thewaypointers.trafficsimulator.common.helpers;

import thewaypointers.trafficsimulator.common.*;

public class JunctionTestProvider implements IStateProvider {
    public static final float ROAD_LENGTH = 300;
    public static final float ROAD_WIDTH = 20;

    WorldStateDTO worldState;

    public JunctionTestProvider(){
        worldState = initialize();
    }

    private WorldStateDTO initialize(){
        WorldStateDTO ws = new WorldStateDTO();

        MapDTO roadMap = ws.getRoadMap();
        roadMap.addRoad("A","B", Direction.Down, ROAD_LENGTH);
        roadMap.addRoad("B","C", Direction.Right, ROAD_LENGTH);

        JunctionLocationDTO loc = new JunctionLocationDTO(
                "B",
                Direction.Right,
                Direction.Up,
                0f);
        ws.getVehicleList().addVehicle("V1", loc, VehicleType.CarNormal);
        return ws;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    public WorldStateDTO getNextState(float vehicleMovement) {
        JunctionLocationDTO loc = (JunctionLocationDTO) worldState.getVehicleList().getVehicle("V1").getLocation();
        JunctionMoveResult result = loc.move(vehicleMovement, ROAD_WIDTH, ROAD_WIDTH);
        loc = result.getRemainder() > 0 ?
                new JunctionLocationDTO(loc.getJunctionLabel(),
                        loc.getOrigin(),
                        loc.getTarget(),
                        0f) :
                result.getNewLocation();

        worldState.getVehicleList().setVehicleLocation("V1", loc);
        return worldState;
    }
}