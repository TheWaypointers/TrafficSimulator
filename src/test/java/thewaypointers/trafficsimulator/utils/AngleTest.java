package thewaypointers.trafficsimulator.utils;

import org.fest.assertions.data.Offset;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.fest.assertions.api.Assertions.*;

public class AngleTest {

    @Test
    public void testAdd(){
        assertThat(Angle.add(
                (7/4f)*Math.PI, (1/2f)*Math.PI
        )).isEqualTo((1/4f)*Math.PI, Offset.offset(0.0001));
    }

    @Test
    public void testSubtract(){
        assertThat(Angle.subtract(
                (1/4f)*Math.PI, (1/2f)*Math.PI
        )).isEqualTo((7/4f)*Math.PI, Offset.offset(0.0001));
    }

    @Test
    public void testSubtract2(){
        assertThat(Angle.subtract(
                (1/2f)*Math.PI, (1/4f)*Math.PI
        )).isEqualTo((1/4f)*Math.PI, Offset.offset(0.0001));
    }

    @Test
    public void testRotate90_counterclockwise() {
        assertThat(Angle.rotate90(
                (7/4f)*Math.PI, Rotation.Counterclockwise
        )).isEqualTo((1/4f)*Math.PI, Offset.offset(0.0001));
    }

    @Test
    public void testRotate90_clockwise() {
        assertThat(Angle.rotate90(
                (1/4f)*Math.PI, Rotation.Clockwise
        )).isEqualTo((7/4f)*Math.PI, Offset.offset(0.0001));
    }
}