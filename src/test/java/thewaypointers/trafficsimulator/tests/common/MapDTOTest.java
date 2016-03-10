package thewaypointers.trafficsimulator.tests.common;

import org.junit.Test;
import thewaypointers.trafficsimulator.common.*;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class MapDTOTest {
    public static final float ROAD_LENGTH = 300;

    @Test
    public void Add_1_road() {
        // arrange
        MapDTO map = new MapDTO();
        //act
        map.addRoad("A", "B", Direction.Right, ROAD_LENGTH);
        //assert
        assertThat(map.getRoads()).hasSize(1);
        // Error: expected size:<1> but was:<0> in:<[]> not able to pass the test.
        //Have not fixed yet.

    }

    @Test
    public void Add_2_roads() {
        // arrange
        MapDTO map = new MapDTO();
        //act
        map.addRoad("A", "B", Direction.Right, ROAD_LENGTH);
        map.addRoad("A", "C", Direction.Down, ROAD_LENGTH);
        JunctionDTO junctionA = map.getJunction("A");
        //assert
        assertThat(map).isNotNull();
        assertThat(map.getRoads());
        assertThat(map.getRoads()).hasSize(2);//Error: expected size:<2> but was:<3>  Have not fixed yet.
        assertThat(map.getJunctions()).hasSize(1);
        // Junction passed
    }

    @Test
    public void Add_4_roads() {
        // arrange
        MapDTO map = new MapDTO();
        //act
        map.addRoad("A", "B", Direction.Right, ROAD_LENGTH);
        map.addRoad("A", "C", Direction.Down, ROAD_LENGTH);
        map.addRoad("B", "D", Direction.Down, ROAD_LENGTH);
        map.addRoad("D", "C", Direction.Left, ROAD_LENGTH);

        JunctionDTO junctionA = map.getJunction("A");
        JunctionDTO junctionB = map.getJunction("B");
        JunctionDTO junctionC = map.getJunction("C");
        JunctionDTO junctionD = map.getJunction("D");

        //assert

        assertThat(map.getRoads()).hasSize(4);//expected size:<4> but was:<5> same problem
        assertThat(map.getJunctions()).hasSize(4);//passed

        //Select junctionA and test the direction of road AC is down; passed
        RoadDTO downRoad = junctionA.getRoad(Direction.Down);
        assertThat(downRoad.equals("AC"));

//        //Select junctionC and test the direction of road AB is left; passed?  not sure because from A to B
        RoadDTO upRoad = junctionB.getRoad(Direction.Left);
        assertThat(upRoad.equals("AB"));

//        //Select junctionC and test the direction of road CD is Right; passed? not sure because from D to C
        RoadDTO rightRoad = junctionC.getRoad(Direction.Right);
        assertThat(rightRoad.equals("CD"));

//
//        //Select junctionC and test the direction of road DB is Right; passed? not sure because from B to D
        RoadDTO leftRoad = junctionD.getRoad(Direction.Up);
        assertThat(leftRoad.equals("DB"));

//
    }
    @Test
    public void two_Exitnodes_one_road() {
        // arrange
        MapDTO map = new MapDTO();
        //act
        map.addRoad("A", "B", Direction.Right, ROAD_LENGTH);
        //assert
        assertThat(map.getJunctions()).hasSize(0);
        assertThat(map.getNodeCount()).isEqualTo(2);
        // Check the number of exitnode of one road,passed




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
        //Expected :false  Actual   :true need to be fixed



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
        //passed



    }
}
