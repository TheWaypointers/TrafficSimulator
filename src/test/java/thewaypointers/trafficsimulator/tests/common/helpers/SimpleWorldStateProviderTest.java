package thewaypointers.trafficsimulator.tests.common.helpers;

import static org.fest.assertions.api.Assertions.*;
import org.junit.Test;
import thewaypointers.trafficsimulator.common.*;
import thewaypointers.trafficsimulator.common.VehicleType;
import thewaypointers.trafficsimulator.common.helpers.SimpleWorldStateProvider;

import java.util.Map;

public class SimpleWorldStateProviderTest {
    @Test
    public void Provider_generates_correct_world_state() {
        // arrange
        SimpleWorldStateProvider provider = new SimpleWorldStateProvider();

        // act
        WorldStateDTO worldState = provider.getNextState(15);

        // assert
        assertThat(worldState.vehicles.size()).isEqualTo(1);
        assertThat(worldState.roadMap.getJunctionCount()).isEqualTo(1);

        JunctionDTO junction = worldState.roadMap.getJunctions().get(0);
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

        Map<Direction, TrafficLightDTO> trafficLights = worldState.trafficLights.get(junction);
        assertThat(trafficLights.get(Direction.Down)).isNotNull();
        assertThat(trafficLights.get(Direction.Up)).isNotNull();
        assertThat(trafficLights.get(Direction.Left)).isNull();
        assertThat(trafficLights.get(Direction.Right)).isNull();

        VehicleDTO v = worldState.vehicles.get(0);
        assertThat(v.type).isEqualTo(VehicleType.CarNormal);

        LocationDTO loc = v.location;
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
        SimpleWorldStateProvider provider = new SimpleWorldStateProvider();
        final float moveDistance = SimpleWorldStateProvider.ROAD_LENGTH/30;

        // act
        WorldStateDTO ws1 = provider.getNextState(moveDistance);
        LocationDTO loc1 = ws1.vehicles.get(0).location;
        WorldStateDTO ws2 = provider.getNextState(moveDistance);
        LocationDTO loc2 = ws2.vehicles.get(0).location;

        // assert
        assertThat(loc1 != loc2).isTrue();
        assertThat(ws1 == ws2).isTrue();
        assertThat(loc2.getDistanceTravelled()).isEqualTo(loc1.getDistanceTravelled() + moveDistance);
    }

    @Test
    public void Vehicle_jumps_from_up_to_down_road()
    {
        // arrange
        SimpleWorldStateProvider provider = new SimpleWorldStateProvider();
        final float moveDistance = SimpleWorldStateProvider.ROAD_LENGTH/2;

        // act
        provider.getNextState(moveDistance);
        WorldStateDTO worldState = provider.getNextState(moveDistance);

        // assert
        LocationDTO loc = worldState.vehicles.get(0).location;
        RoadDTO downRoad = worldState.roadMap.getJunctions().get(0).getRoad(Direction.Down);
        assertThat(loc.getRoad()).isEqualTo(downRoad);
    }

    @Test
    public void Vehicle_loops_back_to_up_road()
    {
        // arrange
        SimpleWorldStateProvider provider = new SimpleWorldStateProvider();
        final float moveDistance = SimpleWorldStateProvider.ROAD_LENGTH/2;

        // act
        provider.getNextState(moveDistance);
        provider.getNextState(moveDistance);
        provider.getNextState(moveDistance);
        WorldStateDTO worldState = provider.getNextState(moveDistance);

        // assert
        LocationDTO loc = worldState.vehicles.get(0).location;
        RoadDTO upRoad = worldState.roadMap.getJunctions().get(0).getRoad(Direction.Up);
        assertThat(loc.getRoad()).isEqualTo(upRoad);
    }

    @Test
    public void Traffic_lights_change(){
        // arrange
        SimpleWorldStateProvider provider = new SimpleWorldStateProvider();

        // act
        WorldStateDTO worldState = provider.getNextState(15);
        JunctionDTO junction = worldState.roadMap.getJunctions().get(0);
        TrafficLightColor upStartColor = worldState.trafficLights.get(junction).get(Direction.Up).color;
        TrafficLightColor downStartColor = worldState.trafficLights.get(junction).get(Direction.Down).color;
        for(int i=0; i<SimpleWorldStateProvider.CHANGE_LIGHTS_EVERY_N_STATES; i++){
            worldState = provider.getNextState(15);
        }

        // assert
        assertThat(upStartColor).isEqualTo(downStartColor);

        assertThat(worldState).isNotNull();
        junction = worldState.roadMap.getJunctions().get(0);
        TrafficLightDTO upTrafficLight = worldState.trafficLights.get(junction).get(Direction.Up);
        TrafficLightDTO downTrafficLight = worldState.trafficLights.get(junction).get(Direction.Down);
        assertThat(upTrafficLight.color).isEqualTo(downTrafficLight.color);
        assertThat(upTrafficLight.color).isNotEqualTo(upStartColor);
        assertThat(downTrafficLight.color).isNotEqualTo(downStartColor);
    }
}
