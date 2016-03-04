package thewaypointers.trafficsimulator.tests.common;

import org.fest.assertions.data.Offset;
import org.junit.Test;
import thewaypointers.trafficsimulator.common.Direction;
import thewaypointers.trafficsimulator.common.JunctionDTO;
import thewaypointers.trafficsimulator.common.JunctionLocationDTO;
import thewaypointers.trafficsimulator.utils.FloatPoint;

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
    public void testCopy() {
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
    public void testGetJunctionCoordinates_StraightRoute() {
        // arrange
        JunctionLocationDTO loc = new JunctionLocationDTO(
                "A",
                Direction.Down,
                Direction.Up,
                0.75f);

        // act and assert
        assertThat(loc.getJunctionCoordinates())
                .isEqualTo(new FloatPoint(0.75f, 0.25f));
    }

    @Test
    public void testGetJunctionCoordinates_StraightRoute_minprogress() {
        // arrange
        JunctionLocationDTO loc = new JunctionLocationDTO(
                "A",
                Direction.Down,
                Direction.Up,
                0f);

        // act and assert
        assertThat(loc.getJunctionCoordinates())
                .isEqualTo(new FloatPoint(0.75f, 1f));
    }

    @Test
    public void testGetJunctionCoordinates_StraightRoute_maxprogress() {
        // arrange
        JunctionLocationDTO loc = new JunctionLocationDTO(
                "A",
                Direction.Down,
                Direction.Up,
                1f);

        // act and assert
        assertThat(loc.getJunctionCoordinates())
                .isEqualTo(new FloatPoint(0.75f, 0f));
    }

    @Test
    public void testGetJunctionCoordinates_RightTurn_minprogress() {
        // arrange
        JunctionLocationDTO loc = new JunctionLocationDTO(
                "A",
                Direction.Down,
                Direction.Left,
                0f);

        // act and assert
        assertThat(loc.getJunctionCoordinates())
                .isEqualTo(new FloatPoint(0.75f, 1f));
    }

    @Test
    public void testGetJunctionCoordinates_RightTurn_maxprogress() {
        // arrange
        JunctionLocationDTO loc = new JunctionLocationDTO(
                "A",
                Direction.Down,
                Direction.Right,
                1f);

        // act and assert
        assertThat(loc.getJunctionCoordinates())
                .isEqualTo(new FloatPoint(1f, 0.75f));
    }

    @Test
    public void testGetJunctionCoordinates_RightTurn_middle() {
        // arrange
        JunctionLocationDTO loc = new JunctionLocationDTO(
                "A",
                Direction.Down,
                Direction.Right,
                0.5f);

        // act
        FloatPoint coords = loc.getJunctionCoordinates();

        // assert
        assertThat(coords.getX()).isGreaterThan(0.75f);
        assertThat(coords.getX()).isLessThan(1f);
        assertThat(coords.getY()).isGreaterThan(0.75f);
        assertThat(coords.getY()).isLessThan(1f);
    }

    @Test
    public void testGetJunctionCoordinates_LeftTurn_minprogress() {
        // arrange
        JunctionLocationDTO loc = new JunctionLocationDTO(
                "A",
                Direction.Down,
                Direction.Left,
                0f);

        // act and assert
        assertThat(loc.getJunctionCoordinates())
                .isEqualTo(new FloatPoint(0.75f, 1f));
    }

    @Test
    public void testGetJunctionCoordinates_LeftTurn_maxprogress() {
        // arrange
        JunctionLocationDTO loc = new JunctionLocationDTO(
                "A",
                Direction.Down,
                Direction.Left,
                1f);

        // act
        FloatPoint coords = loc.getJunctionCoordinates();

        // assert
        assertThat(coords.getX()).isEqualTo(0, Offset.offset(0.0001f));
        assertThat(coords.getY()).isEqualTo(0.25f, Offset.offset(0.0001f));
    }

    @Test
    public void testGetJunctionCoordinates_LeftTurn_middle() {
        // arrange
        JunctionLocationDTO loc = new JunctionLocationDTO(
                "A",
                Direction.Down,
                Direction.Left,
                0.5f);

        // act
        FloatPoint coords = loc.getJunctionCoordinates();

        // assert
        assertThat(coords.getX()).isGreaterThan(0.5f);
        assertThat(coords.getX()).isLessThan(0.75f);
        assertThat(coords.getY()).isGreaterThan(0.25f);
        assertThat(coords.getY()).isLessThan(0.5f);
    }

    @Test
    public void testGetJunctionCoordinates_LeftTurn_FirstSection() {
        // arrange
        JunctionLocationDTO loc = new JunctionLocationDTO(
                "A",
                Direction.Down,
                Direction.Left,
                0.25f);

        // act and assert
        assertThat(loc.getJunctionCoordinates().getX()).isEqualTo(0.75f);
    }

    @Test
    public void testGetJunctionCoordinates_LeftTurn_LastSection() {
        // arrange
        JunctionLocationDTO loc = new JunctionLocationDTO(
                "A",
                Direction.Down,
                Direction.Left,
                0.75f);

        // act and assert
        assertThat(loc.getJunctionCoordinates().getY()).isEqualTo(0.25f);
    }

    @Test
    public void testGetJunctionCoordinates_originLeft() {
        // arrange
        JunctionLocationDTO loc = new JunctionLocationDTO(
                "A",
                Direction.Left,
                Direction.Right,
                0.75f);

        // act and assert
        assertThat(loc.getJunctionCoordinates())
                .isEqualTo(new FloatPoint(0.75f, 0.75f));
    }

    @Test
    public void testGetJunctionCoordinates_originUp() {
        // arrange
        JunctionLocationDTO loc = new JunctionLocationDTO(
                "A",
                Direction.Up,
                Direction.Down,
                0.75f);

        // act and assert
        assertThat(loc.getJunctionCoordinates())
                .isEqualTo(new FloatPoint(0.25f, 0.75f));
    }

    @Test
    public void testGetJunctionCoordinates_originRight() {
        // arrange
        JunctionLocationDTO loc = new JunctionLocationDTO(
                "A",
                Direction.Right,
                Direction.Left,
                0.75f);

        // act and assert
        assertThat(loc.getJunctionCoordinates())
                .isEqualTo(new FloatPoint(0.25f, 0.25f));
    }

}