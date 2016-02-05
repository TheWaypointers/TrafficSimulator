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
        assertThat(v.type).isEqualsToByComparingFields(VehicleType.CarNormal);

        LocationDTO loc = v.location;
        assertThat(loc.lane).isEqualsToByComparingFields(Lane.Right);
        assertThat(loc.road.equals(downRoad));
        assertThat(loc.origin.label.equals("E1"));
    }

}
