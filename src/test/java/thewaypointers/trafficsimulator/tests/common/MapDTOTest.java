package thewaypointers.trafficsimulator.tests.common;

import org.junit.Test;
import thewaypointers.trafficsimulator.common.*;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class MapDTOTest {
    public static final float ROAD_LENGTH = 300;

    @Test
    public void Add_one_road() {
        // arrange
        MapDTO map = new MapDTO();
        //act
        WorldStateDTO wsd = new WorldStateDTO();
        map.addRoad("A", "B", Direction.Right, ROAD_LENGTH);
        JunctionDTO junction = wsd.getRoadMap().getJunctions().get(0);
        //assert
        assertThat(junction.getRoad(Direction.Down)).isNull();
        assertThat(junction.getRoad(Direction.Up)).isNull();
        assertThat(junction.getRoad(Direction.Right)).isNotNull();
        assertThat(junction.getRoad(Direction.Left)).isNull();

        RoadDTO RoadAB = junction.getRoad(Direction.Right);
        assertThat(RoadAB.getFrom().getLabel()).isEqualTo("A");
        assertThat(RoadAB.getTo().getLabel()).isEqualTo("B");

    }

    @Test
    public void Add_2_road() {
        // arrange
        MapDTO map = new MapDTO();
        //act
        WorldStateDTO wsd = new WorldStateDTO();
        map.addRoad("A", "B", Direction.Right, ROAD_LENGTH);
        map.addRoad("A", "C", Direction.Down, ROAD_LENGTH);
        JunctionDTO junction = wsd.getRoadMap().getJunctions().get(0);
        //assert
        assertThat(junction.getRoad(Direction.Down)).isNotNull();
        assertThat(junction.getRoad(Direction.Up)).isNull();
        assertThat(junction.getRoad(Direction.Right)).isNotNull();
        assertThat(junction.getRoad(Direction.Left)).isNull();

        RoadDTO RoadAB = junction.getRoad(Direction.Right);
        assertThat(RoadAB.getFrom().getLabel()).isEqualTo("A");
        assertThat(RoadAB.getTo().getLabel()).isEqualTo("B");
        RoadDTO RoadAC = junction.getRoad(Direction.Down);
        assertThat(RoadAB.getFrom().getLabel()).isEqualTo("A");
        assertThat(RoadAB.getTo().getLabel()).isEqualTo("C");
    }

    @Test
    public void Add_2_roads() {
        // arrange
        MapDTO map = new MapDTO();
        //act
        WorldStateDTO wsd = new WorldStateDTO();
        map.addRoad("A", "B", Direction.Right, ROAD_LENGTH);
        map.addRoad("A", "C", Direction.Down, ROAD_LENGTH);
        JunctionDTO junction = wsd.getRoadMap().getJunctions().get(0);
        //assert
        assertThat(junction.getRoad(Direction.Down)).isNotNull();
        assertThat(junction.getRoad(Direction.Up)).isNull();
        assertThat(junction.getRoad(Direction.Right)).isNotNull();
        assertThat(junction.getRoad(Direction.Left)).isNull();

        RoadDTO RoadAB = junction.getRoad(Direction.Right);
        assertThat(RoadAB.getFrom().getLabel()).isEqualTo("A");
        assertThat(RoadAB.getTo().getLabel()).isEqualTo("B");
        RoadDTO RoadAC = junction.getRoad(Direction.Down);
        assertThat(RoadAB.getFrom().getLabel()).isEqualTo("A");
        assertThat(RoadAB.getTo().getLabel()).isEqualTo("C");


    }


    @Test
    public void node_is_different_from_junction() {
        // arrange
        MapDTO map = new MapDTO();
        //act
        int node = map.getNodeCount();
        int junction = map.getJunctionCount();
        //assert
        assertThat(node == junction).isFalse();



    }

    @Test
    public void getjunctioncount_agree_with_getjunction() {
        // arrange
        MapDTO map = new MapDTO();
        //act
        int jc = map.getJunctionCount();
        List<JunctionDTO> j = map.getJunctions();
        //assert

        assertThat(jc == j.size()).isTrue();



    }
}
