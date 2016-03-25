package thewaypointers.trafficsimulator.tests.common;

import org.fest.assertions.data.Offset;
import org.junit.Test;
import thewaypointers.trafficsimulator.common.Direction;
import thewaypointers.trafficsimulator.common.JunctionLocationDTO;
import thewaypointers.trafficsimulator.common.JunctionMoveResult;
import thewaypointers.trafficsimulator.utils.FloatPoint;

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
    public void testGetProgress() {
        // arrange
        JunctionLocationDTO loc = new JunctionLocationDTO(
                "A",
                Direction.Down,
                Direction.Up,
                0.5f);

        // act and assert
        assertThat(loc.getProgress()).isEqualTo(0.5f);
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

    @Test
    public void testGetAngle_straightRoute(){
        // arrange
        JunctionLocationDTO loc = new JunctionLocationDTO(
                "A",
                Direction.Down,
                Direction.Up,
                0.75f);

        // act and assert
        assertThat(loc.getAngle()).isEqualTo(Math.PI/2, Offset.offset(0.0001));
    }

    @Test
    public void testGetAngle_rightTurn_start(){
        // arrange
        JunctionLocationDTO loc = new JunctionLocationDTO(
                "A",
                Direction.Down,
                Direction.Right,
                0f);

        // act and assert
        assertThat(loc.getAngle()).isEqualTo(Math.PI/2, Offset.offset(0.0001));
    }

    @Test
    public void testGetAngle_rightTurn_end(){
        // arrange
        JunctionLocationDTO loc = new JunctionLocationDTO(
                "A",
                Direction.Down,
                Direction.Right,
                1f);

        // act and assert
        assertThat(loc.getAngle()).isEqualTo(0, Offset.offset(0.0001));
    }

    @Test
    public void testGetAngle_rightTurn_middle(){
        // arrange
        JunctionLocationDTO loc = new JunctionLocationDTO(
                "A",
                Direction.Down,
                Direction.Right,
                0.5f);

        // act and assert
        assertThat(loc.getAngle()).isEqualTo(Math.PI/4, Offset.offset(0.0001));
    }

    @Test
    public void testGetAngle_leftTurn_firstSection(){
        // arrange
        JunctionLocationDTO loc = new JunctionLocationDTO(
                "A",
                Direction.Down,
                Direction.Left,
                0.25f);

        // act and assert
        assertThat(loc.getAngle()).isEqualTo(Math.PI/2, Offset.offset(0.0001));
    }

    @Test
    public void testGetAngle_leftTurn_middle(){
        // arrange
        JunctionLocationDTO loc = new JunctionLocationDTO(
                "A",
                Direction.Down,
                Direction.Left,
                0.5f);

        // act and assert
        assertThat(loc.getAngle()).isEqualTo((3/4f)*Math.PI, Offset.offset(0.0001));
    }

    @Test
    public void testGetAngle_leftTurn_lastSection(){
        // arrange
        JunctionLocationDTO loc = new JunctionLocationDTO(
                "A",
                Direction.Down,
                Direction.Left,
                0.75f);

        // act and assert
        assertThat(loc.getAngle()).isEqualTo(Math.PI, Offset.offset(0.0001));
    }

    @Test
    public void testGetAngle_originLeft(){
        // arrange
        JunctionLocationDTO loc = new JunctionLocationDTO(
                "A",
                Direction.Left,
                Direction.Right,
                0.25f);

        // act and assert
        assertThat(loc.getAngle()).isEqualTo(0, Offset.offset(0.0001));
    }

    @Test
    public void testGetAngle_originUp(){
        // arrange
        JunctionLocationDTO loc = new JunctionLocationDTO(
                "A",
                Direction.Up,
                Direction.Down,
                0.25f);

        // act and assert
        assertThat(loc.getAngle()).isEqualTo((3/2f)*Math.PI, Offset.offset(0.0001));
    }

    @Test
    public void testGetAngle_originRight(){
        // arrange
        JunctionLocationDTO loc = new JunctionLocationDTO(
                "A",
                Direction.Right,
                Direction.Left,
                0.25f);

        // act and assert
        assertThat(loc.getAngle()).isEqualTo(Math.PI, Offset.offset(0.0001));
    }

    @Test
    public void testMove_straightRoute(){
        // arrange
        JunctionLocationDTO loc = new JunctionLocationDTO(
                "A",
                Direction.Down,
                Direction.Up,
                0.25f);

        // act
        JunctionMoveResult result = loc.move(0.5f, 1, 1);

        // assert
        assertThat(result.getNewLocation().getProgress())
                .isEqualTo(0.75f, Offset.offset(0.0001f));
    }

    @Test
    public void testMove_RightTurn(){
        // arrange
        JunctionLocationDTO loc = new JunctionLocationDTO(
                "A",
                Direction.Down,
                Direction.Right,
                0.25f);

        // act
        JunctionMoveResult result = loc.move(0.25f, 1, 1);

        // assert
        assertThat(result.getNewLocation().getProgress())
                .isEqualTo(0.75f, Offset.offset(0.0001f));
    }

    public void testMove_LeftTurn(){
        // arrange
        JunctionLocationDTO loc = new JunctionLocationDTO(
                "A",
                Direction.Down,
                Direction.Left,
                0.25f);

        // act
        JunctionMoveResult result = loc.move(0.3f, 1, 1);

        // assert
        assertThat(result.getNewLocation().getProgress())
                .isEqualTo(0.45f, Offset.offset(0.0001f));
    }

    @Test
    public void testMove_remainder(){
        // arrange
        JunctionLocationDTO loc = new JunctionLocationDTO(
                "A",
                Direction.Down,
                Direction.Up,
                0.75f);

        // act
        JunctionMoveResult result = loc.move(0.5f, 1, 1);

        // assert
        assertThat(result.getNewLocation().getProgress())
                .isEqualTo(1f, Offset.offset(0.0001f));
        assertThat(result.getRemainder()).isEqualTo(0.25f, Offset.offset(0.0001f));
    }

    @Test
    public void testMove_scaleJunction(){
        // arrange
        JunctionLocationDTO loc = new JunctionLocationDTO(
                "A",
                Direction.Down,
                Direction.Up,
                0.5f);

        // act
        JunctionMoveResult result = loc.move(2f, 2, 2);

        // assert
        assertThat(result.getNewLocation().getProgress())
                .isEqualTo(1f, Offset.offset(0.0001f));
        assertThat(result.getRemainder()).isEqualTo(1f, Offset.offset(0.0001f));
    }
}