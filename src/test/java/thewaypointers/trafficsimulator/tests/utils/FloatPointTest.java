package thewaypointers.trafficsimulator.tests.utils;

import org.junit.Test;
import thewaypointers.trafficsimulator.utils.FloatPoint;
import thewaypointers.trafficsimulator.utils.Rotation;

import static org.fest.assertions.api.Assertions.*;

public class FloatPointTest {

    @Test
    public void testGetX() {
        // arrange and act
        float x = 99;
        float y = 88;
        FloatPoint point = new FloatPoint(x,y);

        // assert
        assertThat(point.getX()).isEqualTo(x);
    }

    @Test
    public void testGetY()  {
        // arrange and act
        float x = 99;
        float y = 88;
        FloatPoint point = new FloatPoint(x,y);

        // assert
        assertThat(point.getY()).isEqualTo(y);
    }

    @Test
    public void testEquals(){
        // arrange
        FloatPoint p1 = new FloatPoint(2,3);
        FloatPoint p2 = new FloatPoint(2,3);

        // act and assert
        assertThat(p1.equals(p2)).isTrue();
    }

    @Test
    public void testObjectEquals(){
        // arrange
        FloatPoint p1 = new FloatPoint(2,3);
        FloatPoint p2 = new FloatPoint(2,3);

        // act and assert
        assertThat(p1.equals((Object)p2)).isTrue();
    }

    @Test
    public void testAdd(){
        // arrange
        FloatPoint p1 = new FloatPoint(2.5f,3);
        FloatPoint p2 = new FloatPoint(1,1);

        // act
        FloatPoint result = p1.add(p2);

        // assert
        assertThat(result).isEqualTo(new FloatPoint(3.5f,4));
    }

    @Test
    public void testSubtract(){
        // arrange
        FloatPoint p1 = new FloatPoint(1.5f,1);
        FloatPoint p2 = new FloatPoint(2,3);

        // act
        FloatPoint result = p1.subtract(p2);

        // assert
        assertThat(result).isEqualTo(new FloatPoint(-0.5f,-2));
    }

    @Test
    public void testRotate90_counterclockwise() {
        // arrange
        FloatPoint center = new FloatPoint(1,2);
        FloatPoint point = new FloatPoint(3,3);

        // act
        FloatPoint result = point.rotate90(center, Rotation.Counterclockwise);

        // assert
        assertThat(result).isEqualTo(new FloatPoint(0,4));
    }

    @Test
    public void testRotate90_clockwise() {
        // arrange
        FloatPoint center = new FloatPoint(1,2);
        FloatPoint point = new FloatPoint(3,3);

        // act
        FloatPoint result = point.rotate90(center, Rotation.Clockwise);

        // assert
        assertThat(result).isEqualTo(new FloatPoint(2,0));
    }

    @Test
    public void testMirror(){
        // arrange
        FloatPoint center = new FloatPoint(1,2);
        FloatPoint point = new FloatPoint(3,3);

        // act
        FloatPoint result = point.mirror(center);

        // assert
        assertThat(result).isEqualTo(new FloatPoint(-1,1));
    }

}