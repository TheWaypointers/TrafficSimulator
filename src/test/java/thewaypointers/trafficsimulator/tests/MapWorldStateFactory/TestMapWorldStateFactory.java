package thewaypointers.trafficsimulator.tests.MapWorldStateFactory;

import org.junit.Test;
import thewaypointers.trafficsimulator.common.Direction;
import thewaypointers.trafficsimulator.common.Lane;
import thewaypointers.trafficsimulator.common.TrafficLightDTO;
import thewaypointers.trafficsimulator.common.WorldStateDTO;
import thewaypointers.trafficsimulator.simulation.factories.MapWorldStateFactory;

import static org.fest.assertions.api.Assertions.assertThat;

public class TestMapWorldStateFactory {
    @Test
    public void test2Roads(){


        //arrange
        WorldStateDTO worldState = null; //from factory
        //act
        final String PATH_TO_MAP = "roadmaps/Add2RoadsTest.xml";
        MapWorldStateFactory factory = new MapWorldStateFactory(PATH_TO_MAP);
        worldState = factory.getWorldState();
       //arrest
        assertThat(worldState.getRoadMap().getJunctions().contains("A"));
        assertThat(worldState.getRoadMap().getJunctionCount()).isEqualTo(1);
        assertThat(worldState.getRoadMap().getRoads().contains("AE1"));
        assertThat(worldState.getRoadMap().getRoads().contains("AE2"));
        assertThat(worldState.getRoadMap().getRoads()).hasSize(2);
        assertThat(worldState.getRoadMap().getNodeCount()).isEqualTo(3);
        assertThat(worldState.getRoadMap().getDirection("E1","A")).isEqualTo("Down");
        assertThat(worldState.getRoadMap().getDirection("E2","A")).isEqualTo("Up");
        assertThat(worldState.getTrafficLightSystem().getTrafficLight("A",Direction.Up, Lane.Right));
        assertThat(worldState.getTrafficLightSystem().getTrafficLight("A",Direction.Down,Lane.Right));

    }

    @Test
    public void test3Roads(){

        //arrange
        WorldStateDTO worldState = null; //from factory
        //act
        final String PATH_TO_MAP = "roadmaps/Add3RoadsTest.xml";
        MapWorldStateFactory factory = new MapWorldStateFactory(PATH_TO_MAP);
        worldState = factory.getWorldState();
        //arrest
        assertThat(worldState.getRoadMap().getJunctions().contains("A"));
        assertThat(worldState.getRoadMap().getJunctions().contains("B"));
        assertThat(worldState.getRoadMap().getJunctionCount()).isEqualTo(2);
        assertThat(worldState.getRoadMap().getRoads().contains("AE1"));
        assertThat(worldState.getRoadMap().getRoads().contains("AB"));
        assertThat(worldState.getRoadMap().getRoads().contains("BE2"));
        assertThat(worldState.getRoadMap().getRoads()).hasSize(3);
        assertThat(worldState.getRoadMap().getNodeCount()).isEqualTo(4);
        assertThat(worldState.getRoadMap().getDirection("A","E1")).isEqualTo("Right");
        assertThat(worldState.getRoadMap().getDirection("A","B")).isEqualTo("Down");
        assertThat(worldState.getRoadMap().getDirection("B","E2")).isEqualTo("Right");

        assertThat(worldState.getTrafficLightSystem().getTrafficLight("A",Direction.Down, Lane.Right)).isNotNull();
        assertThat(worldState.getTrafficLightSystem().getTrafficLight("A",Direction.Right,Lane.Right)).isNotNull();
        assertThat(worldState.getTrafficLightSystem().getTrafficLight("B",Direction.Up,Lane.Right)).isNotNull();
        assertThat(worldState.getTrafficLightSystem().getTrafficLight("B",Direction.Right,Lane.Right)).isNotNull();

        //problem with the traffic light when there isn't any Up/Down junction.
    }
    @Test
    public void test4Roads(){

        //arrange
        WorldStateDTO worldState = null; //from factory
        //act
        final String PATH_TO_MAP = "roadmaps/Add4RoadsTest.xml";
        MapWorldStateFactory factory = new MapWorldStateFactory(PATH_TO_MAP);
        worldState = factory.getWorldState();
        //arrest
        assertThat(worldState.getRoadMap().getJunctions().contains("A"));
        assertThat(worldState.getRoadMap().getJunctions().contains("B"));
        assertThat(worldState.getRoadMap().getJunctions().contains("C"));
        assertThat(worldState.getRoadMap().getJunctionCount()).isEqualTo(3);
        assertThat(worldState.getRoadMap().getRoads().contains("AB"));
        assertThat(worldState.getRoadMap().getRoads().contains("AC"));
        assertThat(worldState.getRoadMap().getRoads().contains("CE1"));
        assertThat(worldState.getRoadMap().getRoads().contains("BE2"));
        assertThat(worldState.getRoadMap().getRoads()).hasSize(4);
        assertThat(worldState.getRoadMap().getNodeCount()).isEqualTo(5);
        assertThat(worldState.getRoadMap().getDirection("A","B")).isEqualTo("Right");
        assertThat(worldState.getRoadMap().getDirection("A","C")).isEqualTo("Down");
        assertThat(worldState.getRoadMap().getDirection("C","E1")).isEqualTo("Left");
        assertThat(worldState.getRoadMap().getDirection("E2","B")).isEqualTo("Up");

        assertThat(worldState.getTrafficLightSystem().getTrafficLight("A",Direction.Right,Lane.Right));
        assertThat(worldState.getTrafficLightSystem().getTrafficLight("A",Direction.Down,Lane.Right));
        assertThat(worldState.getTrafficLightSystem().getTrafficLight("B",Direction.Left,Lane.Right));
        assertThat(worldState.getTrafficLightSystem().getTrafficLight("B",Direction.Down,Lane.Right));
        assertThat(worldState.getTrafficLightSystem().getTrafficLight("C",Direction.Up,Lane.Right));
        assertThat(worldState.getTrafficLightSystem().getTrafficLight("C",Direction.Left,Lane.Right));
        //problem of the traffic lights.
        // If a junction has only down road, it will have up traffic light only.
        //If a junction has only up road, it will have down traffic light only.
        //We don't have left or right traffic lights
    }
    @Test
    public void testComplexRoads(){

        //arrange
        WorldStateDTO worldState = null; //from factory
        //act
        final String PATH_TO_MAP = "roadmaps/complex.xml";
        MapWorldStateFactory factory = new MapWorldStateFactory(PATH_TO_MAP);
        worldState = factory.getWorldState();
        //arrest
        assertThat(worldState.getRoadMap().getJunctions().contains("A"));
        assertThat(worldState.getRoadMap().getJunctions().contains("B"));
        assertThat(worldState.getRoadMap().getJunctions().contains("C"));
        assertThat(worldState.getRoadMap().getJunctionCount()).isEqualTo(3);
        assertThat(worldState.getRoadMap().getRoads().contains("E1A"));
        assertThat(worldState.getRoadMap().getRoads().contains("E3A"));
        assertThat(worldState.getRoadMap().getRoads().contains("AB"));
        assertThat(worldState.getRoadMap().getRoads().contains("AC"));
        assertThat(worldState.getRoadMap().getRoads().contains("E2B"));
        assertThat(worldState.getRoadMap().getRoads().contains("BE7"));
        assertThat(worldState.getRoadMap().getRoads().contains("BE4"));
        assertThat(worldState.getRoadMap().getRoads().contains("E5C"));
        assertThat(worldState.getRoadMap().getRoads().contains("CE6"));
        assertThat(worldState.getRoadMap().getRoads()).hasSize(9);
        assertThat(worldState.getRoadMap().getNodeCount()).isEqualTo(10);
        assertThat(worldState.getRoadMap().getDirection("E1","A")).isEqualTo("Down");
        assertThat(worldState.getRoadMap().getDirection("E3","A")).isEqualTo("Right");
        assertThat(worldState.getRoadMap().getDirection("A","C")).isEqualTo("Down");
        assertThat(worldState.getRoadMap().getDirection("E2","B")).isEqualTo("Down");
        assertThat(worldState.getRoadMap().getDirection("B","E4")).isEqualTo("Right");
        assertThat(worldState.getRoadMap().getDirection("B","E7")).isEqualTo("Down");
        assertThat(worldState.getRoadMap().getDirection("A","C")).isEqualTo("Down");
        assertThat(worldState.getRoadMap().getDirection("E5","C")).isEqualTo("Right");
        assertThat(worldState.getRoadMap().getDirection("C","E6")).isEqualTo("Down");

        assertThat(worldState.getTrafficLightSystem().getTrafficLight("A",Direction.Up,Lane.Right));
        assertThat(worldState.getTrafficLightSystem().getTrafficLight("A",Direction.Left,Lane.Right));
        assertThat(worldState.getTrafficLightSystem().getTrafficLight("B",Direction.Up,Lane.Right));
        assertThat(worldState.getTrafficLightSystem().getTrafficLight("B",Direction.Down,Lane.Right));
        assertThat(worldState.getTrafficLightSystem().getTrafficLight("C",Direction.Up,Lane.Right));
        assertThat(worldState.getTrafficLightSystem().getTrafficLight("C",Direction.Down,Lane.Right));
        //No problem of the traffic lights because all the junctions in this map have up and down road
        //We don't have left or right traffic lights
    }


}