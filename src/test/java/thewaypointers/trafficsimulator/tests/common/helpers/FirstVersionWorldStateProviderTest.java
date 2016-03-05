package thewaypointers.trafficsimulator.tests.common.helpers;

import static org.fest.assertions.api.Assertions.*;
import org.junit.Test;
import thewaypointers.trafficsimulator.common.*;
import thewaypointers.trafficsimulator.common.helpers.SimpleWorldStateProvider;

public class FirstVersionWorldStateProviderTest {
    @Test
    public void Provider_generates_correct_world_state() {
        // arrange
        SimpleWorldStateProvider provider = new SimpleWorldStateProvider(SimpleWorldState.FIRST_VERSION);

        // act
        WorldStateDTO worldState = provider.getNextState(15);

        // assert
        assertThat(worldState.getVehicleList().getVehicleCount()).isEqualTo(1);
        assertThat(worldState.getRoadMap().getJunctionCount()).isEqualTo(1);

        JunctionDTO junction = worldState.getRoadMap().getJunctions().get(0);
        assertThat(junction.getRoad(Direction.Down)).isNotNull();
        assertThat(junction.getRoad(Direction.Up)).isNotNull();
        assertThat(junction.getRoad(Direction.Left)).isNull();
        assertThat(junction.getRoad(Direction.Right)).isNull();

        RoadDTO downRoad = junction.getRoad(Direction.Down);
        RoadDTO upRoad = junction.getRoad(Direction.Up);
        assertThat(upRoad.getFrom().getLabel()).isEqualTo("E1");
        assertThat(upRoad.getTo().getLabel()).isEqualTo("A");
        assertThat(downRoad.getFrom().getLabel()).isEqualTo("A");
        assertThat(downRoad.getTo().getLabel()).isEqualTo("E2");

        JunctionTrafficLightsDTO trafficLights = worldState.getTrafficLightSystem().getJunction(junction.getLabel());
        assertThat(trafficLights.getRoad(Direction.Down)).isNotNull();
        assertThat(trafficLights.getRoad(Direction.Up)).isNotNull();
        assertThat(trafficLights.getRoad(Direction.Left)).isNull();
        assertThat(trafficLights.getRoad(Direction.Right)).isNull();

        VehicleDTO v = worldState.getVehicleList().getAll().get(0);
        assertThat(v.getType()).isEqualTo(VehicleType.CarNormal);

        RoadLocationDTO loc = (RoadLocationDTO) v.getLocation();
        assertThat(loc.getLane()).isEqualTo(Lane.Right);
        assertThat(loc.getRoad().equals(downRoad));
        assertThat(loc.getOrigin().getLabel().equals("E1"));
        NodeDTO[] roadEndpoints = {loc.getRoad().getFrom(), loc.getRoad().getTo()};
        assertThat(roadEndpoints).contains(loc.getOrigin());
    }

    @Test
    public void Provider_moves_vehicle()
    {
        // arrange
        SimpleWorldStateProvider provider = new SimpleWorldStateProvider(SimpleWorldState.FIRST_VERSION);
        final float moveDistance = SimpleWorldStateProvider.ROAD_LENGTH/30;

        // act
        WorldStateDTO ws1 = provider.getNextState(moveDistance);
        RoadLocationDTO loc1 = (RoadLocationDTO) ws1.getVehicleList().getAll().get(0).getLocation();
        WorldStateDTO ws2 = provider.getNextState(moveDistance);
        RoadLocationDTO loc2 = (RoadLocationDTO) ws2.getVehicleList().getAll().get(0).getLocation();

        // assert
        assertThat(loc1 != loc2).isTrue();
        assertThat(ws1 == ws2).isTrue();
        assertThat(loc2.getDistanceTravelled()).isEqualTo(loc1.getDistanceTravelled() + moveDistance);
    }

    @Test
    public void Vehicle_jumps_from_up_to_down_road()
    {
        // arrange
        SimpleWorldStateProvider provider = new SimpleWorldStateProvider(SimpleWorldState.FIRST_VERSION);
        final float moveDistance = SimpleWorldStateProvider.ROAD_LENGTH/2;

        // act
        provider.getNextState(moveDistance);
        WorldStateDTO worldState = provider.getNextState(moveDistance);

        // assert
        RoadLocationDTO loc = (RoadLocationDTO)worldState.getVehicleList().getAll().get(0).getLocation();
        RoadDTO downRoad = worldState.getRoadMap().getJunctions().get(0).getRoad(Direction.Down);
        assertThat(loc.getRoad()).isEqualTo(downRoad);
    }

    @Test
    public void Vehicle_loops_back_to_up_road()
    {
        // arrange
        SimpleWorldStateProvider provider = new SimpleWorldStateProvider(SimpleWorldState.FIRST_VERSION);
        final float moveDistance = SimpleWorldStateProvider.ROAD_LENGTH/2;

        // act
        provider.getNextState(moveDistance);
        provider.getNextState(moveDistance);
        provider.getNextState(moveDistance);
        WorldStateDTO worldState = provider.getNextState(moveDistance);

        // assert
        RoadLocationDTO loc = (RoadLocationDTO) worldState.getVehicleList().getAll().get(0).getLocation();
        RoadDTO upRoad = worldState.getRoadMap().getJunctions().get(0).getRoad(Direction.Up);
        assertThat(loc.getRoad()).isEqualTo(upRoad);
    }

    @Test
    public void Traffic_lights_change(){
        // arrange
        SimpleWorldStateProvider provider = new SimpleWorldStateProvider(SimpleWorldState.FIRST_VERSION);

        // act
        WorldStateDTO worldState = provider.getNextState(15);
        JunctionDTO junction = worldState.getRoadMap().getJunctions().get(0);
        TrafficLightColor upStartColor = worldState.getTrafficLightSystem()
                .getTrafficLightColor(junction.getLabel(), Direction.Up, Lane.Right);
        TrafficLightColor downStartColor = worldState.getTrafficLightSystem()
                .getTrafficLightColor(junction.getLabel(), Direction.Down, Lane.Right);
        for(int i=0; i<SimpleWorldStateProvider.CHANGE_LIGHTS_EVERY_N_STATES; i++){
            worldState = provider.getNextState(15);
        }

        // assert
        assertThat(upStartColor).isEqualTo(downStartColor);

        assertThat(worldState).isNotNull();
        junction = worldState.getRoadMap().getJunctions().get(0);
        TrafficLightColor upColor = worldState.getTrafficLightSystem()
                .getTrafficLightColor(junction.getLabel(), Direction.Up, Lane.Right);
        TrafficLightColor downColor = worldState.getTrafficLightSystem()
                .getTrafficLightColor(junction.getLabel(), Direction.Down, Lane.Right);
        assertThat(upColor).isEqualTo(downColor);
        assertThat(upColor).isNotEqualTo(upStartColor);
        assertThat(downColor).isNotEqualTo(downStartColor);
    }
}
