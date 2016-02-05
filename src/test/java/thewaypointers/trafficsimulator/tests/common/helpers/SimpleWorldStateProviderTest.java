package thewaypointers.trafficsimulator.tests.common.helpers;

import static org.fest.assertions.api.Assertions.*;
import org.junit.Test;
import thewaypointers.trafficsimulator.common.*;
import thewaypointers.trafficsimulator.common.enums.VehicleType;
import thewaypointers.trafficsimulator.common.helpers.SimpleWorldStateProvider;

public class SimpleWorldStateProviderTest {
    @Test
    public void Provider_generates_correct_world_state() {
        // arrange
        SimpleWorldStateProvider provider = new SimpleWorldStateProvider();

        // act
        WorldStateDTO worldState = provider.getNextState(15);

        // assert
        assertThat(worldState.vehicles.size()).isEqualTo(1);
        assertThat(worldState.roadMap.junctions.size()).isEqualTo(1);

        JunctionDTO junction = worldState.roadMap.junctions.get(0);
        assertThat(junction.connections.get(Direction.Down)).isNotNull();
        assertThat(junction.connections.get(Direction.Up)).isNotNull();
        assertThat(junction.connections.get(Direction.Left)).isNull();
        assertThat(junction.connections.get(Direction.Right)).isNull();

        RoadDTO downRoad = junction.connections.get(Direction.Down);
        RoadDTO upRoad = junction.connections.get(Direction.Up);
        assertThat(downRoad.start.label).isEqualTo("E1");
        assertThat(downRoad.end.label).isEqualTo("A");
        assertThat(upRoad.start.label).isEqualTo("A");
        assertThat(upRoad.end.label).isEqualTo("E2");

        VehicleDTO v = worldState.vehicles.get(0);
        assertThat(v.type).isEqualTo(VehicleType.CarNormal);

        LocationDTO loc = v.location;
        assertThat(loc.getLane()).isEqualTo(Lane.Right);
        assertThat(loc.getRoad().equals(downRoad));
        assertThat(loc.getOrigin().label.equals("E1"));
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
    public void Vehicle_jumps_from_down_to_up_road()
    {
        // arrange
        SimpleWorldStateProvider provider = new SimpleWorldStateProvider();
        final float moveDistance = SimpleWorldStateProvider.ROAD_LENGTH/2;

        // act
        provider.getNextState(moveDistance);
        WorldStateDTO worldState = provider.getNextState(moveDistance);

        // assert
        LocationDTO loc = worldState.vehicles.get(0).location;
        RoadDTO upRoad = worldState.roadMap.junctions.get(0).connections.get(Direction.Up);
        assertThat(loc.getRoad()).isEqualTo(upRoad);
    }

    @Test
    public void Vehicle_loops_back_to_down_road()
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
        RoadDTO downRoad = worldState.roadMap.junctions.get(0).connections.get(Direction.Down);
        assertThat(loc.getRoad()).isEqualTo(downRoad);
    }
}
