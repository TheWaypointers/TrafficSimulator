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

        MapDTO roadMap = new MapDTO();
        roadMap.addRoad("A", "E1", Direction.Up, ROAD_LENGTH);
        roadMap.addRoad("A", "E2", Direction.Down, ROAD_LENGTH);

        // add traffic lights
        TrafficLightDTO downTrafficLight = new TrafficLightDTO();
        TrafficLightDTO upTrafficLight = new TrafficLightDTO();
        downTrafficLight.color = TrafficLightColor.Red;
        upTrafficLight.color = TrafficLightColor.Red;
        HashMap<Direction, TrafficLightDTO> trafficLights = new HashMap<>();
        trafficLights.put(Direction.Down, downTrafficLight);
        trafficLights.put(Direction.Up, upTrafficLight);

        RoadDTO startRoad = roadMap.getJunction("A").getRoad(Direction.Up);
        LocationDTO loc = new LocationDTO(startRoad, startRoad.getEnd("E1"), 0, Lane.Right);
        VehicleDTO v1 = new VehicleDTO(loc, VehicleType.CarNormal);
        ArrayList<VehicleDTO> vehicles = new ArrayList<>();
        vehicles.add(v1);

        WorldStateDTO ws = new WorldStateDTO();

        ws.roadMap = roadMap;
        ws.vehicles = vehicles;
        ws.trafficLights = new HashMap<>();
        ws.trafficLights.put(roadMap.getJunction("A"), trafficLights);
        return ws;
    }

    public WorldStateDTO getNextState(float vehicleMovement) {
        JunctionDTO junction = worldState.roadMap.getJunctions().get(0);
        RoadDTO downRoad = junction.getRoad(Direction.Down);
        RoadDTO upRoad = junction.getRoad(Direction.Up);
        VehicleDTO v = worldState.vehicles.get(0);
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
            TrafficLightDTO upTrafficLight = worldState.trafficLights.get(junction).get(Direction.Up);
            TrafficLightDTO downTrafficLight = worldState.trafficLights.get(junction).get(Direction.Down);
            changeTrafficLightColor(upTrafficLight);
            changeTrafficLightColor(downTrafficLight);
        }

        return worldState;
    }

    public void changeTrafficLightColor(TrafficLightDTO trafficLight) {
        trafficLight.color = trafficLight.color == TrafficLightColor.Red ?
                                                   TrafficLightColor.Green :
                                                   TrafficLightColor.Red;
    }
}
