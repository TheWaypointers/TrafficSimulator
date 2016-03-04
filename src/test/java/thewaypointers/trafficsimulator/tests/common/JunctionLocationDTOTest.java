package thewaypointers.trafficsimulator.tests.common;

import org.junit.Test;
import thewaypointers.trafficsimulator.common.Direction;
import thewaypointers.trafficsimulator.common.JunctionDTO;
import thewaypointers.trafficsimulator.common.JunctionLocationDTO;

import static org.junit.Assert.*;
import static org.fest.assertions.api.Assertions.*;

public class JunctionLocationDTOTest {

    @Test
    public void testGetJunctionLabel() {
        // arrange
        JunctionLocationDTO loc = new JunctionLocationDTO(
                "A",
                Direction.Down,
                Direction.Up,
                0.5f);

        // act and assert
        assertThat(loc.getJunctionLabel()).isEqualTo("A");
    }

    @Test
    public void testGetOrigin() {
        // arrange
        JunctionLocationDTO loc = new JunctionLocationDTO(
                "A",
                Direction.Down,
                Direction.Up,
                0.5f);

        // act and assert
        assertThat(loc.getOrigin()).isEqualTo(Direction.Down);
    }

    @Test
    public void testGetTarget() {
        // arrange
        JunctionLocationDTO loc = new JunctionLocationDTO(
                "A",
                Direction.Down,
                Direction.Up,
                0.5f);

        // act and assert
        assertThat(loc.getTarget()).isEqualTo(Direction.Up);
    }

    @Test
    public void testGetPercentageTravelled() {
        // arrange
        JunctionLocationDTO loc = new JunctionLocationDTO(
                "A",
                Direction.Down,
                Direction.Up,
                0.5f);

        // act and assert
        assertThat(loc.getPercentageTravelled()).isEqualTo(0.5f);
    }

    @Test
    public void testCopy() throws Exception {
        // arrange
        JunctionLocationDTO loc = new JunctionLocationDTO(
                "A",
                Direction.Down,
                Direction.Up,
                0.5f);

        // act and assert
        assertThat(loc.copy()).isEqualTo(loc);
    }

    @Test
    public void testGetJunctionCoordinates() {

    }
}