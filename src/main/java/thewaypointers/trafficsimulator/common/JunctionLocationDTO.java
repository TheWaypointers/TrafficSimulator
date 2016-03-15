package thewaypointers.trafficsimulator.common;

import thewaypointers.trafficsimulator.utils.Angle;
import thewaypointers.trafficsimulator.utils.FloatPoint;
import thewaypointers.trafficsimulator.utils.Rotation;

/**
 * The representation of location of objects inside junctions. The location is defined in
 * three parameters:
 *     - origin - where the vehicle is coming from
 *     - target - where the vehicle is headed
 *     - progress - percent of completion of the move through the junction, a number between 0.0 and 1.0
 * The class determines what kind of move the object is making and what is the shape of the move.
 * It provides helpful methods using the determined information:
 *     - getJunctionCoordinates() - gives normalized coordinates of object in the junction rectangle
 *     - getAngle() - gives object orientation
 *     - move() - produces a new location resulting from object moving ahead by specified distance
 */
public class JunctionLocationDTO implements ILocation {
    private String junctionLabel;
    private Direction origin;
    private Direction target;
    private float progress;

    private enum JunctionRoute{
        StraightRoute,
        LeftTurn,
        RightTurn
    }

    public JunctionLocationDTO(String junctionLabel, Direction origin, Direction target, float progress) {
        this.junctionLabel = junctionLabel;
        this.origin = origin;
        this.target = target;
        this.progress = progress;
    }

    public JunctionLocationDTO(String junctionLabel,
                               Direction origin,
                               Direction target,
                               float distanceTravelled,
                               float junctionWidth,
                               float junctionHeight){
        this(junctionLabel,
                origin,
                target,
                progressFromDistanceTravelled(distanceTravelled,
                                              junctionWidth,
                                              junctionHeight,
                                              getRouteLength(getJunctionRoute(origin,target))));
    }

    public String getJunctionLabel() {
        return junctionLabel;
    }

    public Direction getOrigin() {
        return origin;
    }

    public Direction getTarget() {
        return target;
    }

    public float getProgress() {
        return progress;
    }

    private static JunctionRoute getJunctionRoute(Direction origin, Direction target){
        if(target.equals(origin.toLeft())){
            return JunctionRoute.LeftTurn;
        }else if(target.equals(origin.toRight())){
            return JunctionRoute.RightTurn;
        }else if(target.equals(origin.opposite())){
            return JunctionRoute.StraightRoute;
        }else{
            throw new AssertionError(String.format(
                    "Invalid target %s for origin %s", target, origin));
        }
    }

    private JunctionRoute getJunctionRoute(){
        return  getJunctionRoute(getOrigin(), getTarget());
    }

    public JunctionLocationDTO(JunctionLocationDTO other){
        this(other.getJunctionLabel(), other.getOrigin(), other.getTarget(), other.getProgress());
    }

    @Override
    public ILocation copy() {
        return new JunctionLocationDTO(this);
    }

    public boolean equals(JunctionLocationDTO other){
        return  getJunctionLabel().equals(other.getJunctionLabel()) &&
                getTarget().equals(other.getTarget()) &&
                getOrigin().equals(other.getOrigin()) &&
                getProgress() == other.getProgress();
    }

    @Override
    public boolean equals(Object o) {
        return o.getClass() == JunctionLocationDTO.class &&
                equals((JunctionLocationDTO)o);
    }

    // default origin - Down
    private FloatPoint getLeftTurnCoordinates(float progress){
        float x,y;
        if (progress <= 1/3f){
            x = 0.75f;
            y = 1 - 0.5f*(progress*3);
        }else if(progress >= 1/3f && progress <= 2/3f){
            double radians = Math.toRadians((progress-1/3f)*3*90);
            x = (float)(0.75-0.25*Math.sin(radians));
            y = (float)(0.5-0.25*Math.sin(radians));
        }else{
            x = 0.5f - 0.5f*(progress-2/3f)*3;
            y = 0.25f;
        }
        return new FloatPoint(x,y);
    }

    // default origin - Down
    private FloatPoint getRightTurnCoordinates(float progress){
        double radians = Math.toRadians(progress*90);
        float x = (float)(0.75f + 0.25*Math.sin(radians));
        float y = (float)(1 - 0.25*Math.sin(radians));
        return new FloatPoint(x,y);
    }

    // default origin - Down
    private FloatPoint getStraightRouteCoordinates(float progress){
        float x = 0.75f;
        float y = 1 - progress;
        return new FloatPoint(x,y);
    }

    /**
     * Retrieves normalized coordinates of object inside junction.
     * Top left is (0.0, 0.0), bottom right is (1.0, 1.0).
     * @return coordinates of object
     */
    public FloatPoint getJunctionCoordinates(){

        // get correct curve
        FloatPoint coords;
        switch(getJunctionRoute()){
            case LeftTurn:
                coords = getLeftTurnCoordinates(getProgress());
                break;
            case RightTurn:
                coords = getRightTurnCoordinates(getProgress());
                break;
            case StraightRoute:
                coords = getStraightRouteCoordinates(getProgress());
                break;
            default:
                throw new AssertionError("Unexpected enum value");
        }

        // rotate the coordinates
        FloatPoint rotated, center = new FloatPoint(0.5f,0.5f);
        switch(getOrigin()){
            case Down:
                rotated = coords;
                break;
            case Right:
                rotated = coords.rotate90(center, Rotation.Clockwise);
                break;
            case Left:
                rotated = coords.rotate90(center, Rotation.Counterclockwise);
                break;
            case Up:
                rotated = coords.mirror(center);
                break;
            default:
                throw new AssertionError(String.format(
                        "Unexpected enum value: %s", getOrigin()));
        }
        return rotated;
    }

    // default orientation - Down
    private double getStraightRouteAngle(){
        return Math.PI/2;
    }

    // default orientation - Down
    private double getRightTurnAngle(float progress){
        return Math.PI/2 - (Math.PI/2)*progress;
    }

    // default orientation - Down
    private double getLeftTurnAngle(float progress){
        if (progress <= 1/3f){
            return Math.PI/2;
        }else if(progress >= 1/3f && progress <= 2/3f){
            float progressNormalized = (getProgress()-1/3f)*3;
            return Math.PI/2 + (Math.PI/2)*progressNormalized;
        }else{
            return Math.PI;
        }
    }

    /**
     * Retrieves orientation of object inside junction. Orientation is returned as
     * angle in radians, default orientation (0 radians) is object facing right.
     * @return Angle of object in radians.
     */
    public double getAngle(){

        // get correct angle
        double angle;
        switch(getJunctionRoute()){
            case LeftTurn:
                angle = getLeftTurnAngle(getProgress());
                break;
            case RightTurn:
                angle = getRightTurnAngle(getProgress());
                break;
            case StraightRoute:
                angle = getStraightRouteAngle();
                break;
            default:
                throw new AssertionError("Unexpected enum value");
        }

        // rotate the coordinates
        double rotated;
        switch(getOrigin()){
            case Down:
                rotated = angle;
                break;
            case Right:
                rotated = Angle.rotate90(angle, Rotation.Counterclockwise);
                break;
            case Left:
                rotated = Angle.rotate90(angle, Rotation.Clockwise);
                break;
            case Up:
                rotated = Angle.rotate90(angle, Rotation.Clockwise);
                rotated = Angle.rotate90(rotated, Rotation.Clockwise);
                break;
            default:
                throw new AssertionError(String.format(
                        "Unexpected enum value: %s", getOrigin()));
        }
        return rotated;
    }

    private static float getRouteLength(JunctionRoute route){
        switch (route){
            case StraightRoute:
                return 1;
            case RightTurn:
                return 0.5f;
            case LeftTurn:
                return 1.5f;
            default:
                throw new AssertionError("Unexpected enum value");
        }
    }

    public float getRouteLength(){
        return getRouteLength(getJunctionRoute());
    }

    private JunctionMoveResult move(float distance){
        if(distance<0){
            throw new IllegalArgumentException("Distance cannot be negative");
        }

        float routeLength = getRouteLength();
        float remainder = 0;
        float newDistance = getProgress()*routeLength + distance;
        if(newDistance > routeLength){
            remainder = newDistance - routeLength;
            newDistance = routeLength;
        }
        float newProgress = newDistance/routeLength;
        return new JunctionMoveResult(
                new JunctionLocationDTO(this.getJunctionLabel(),
                                        this.getOrigin(),
                                        this.getTarget(),
                                        newProgress),
                remainder
        );
    }

    /**
     * Moves the object ahead using specified distance. You should specify real dimensions
     * of the junction for the distance to be calculated correctly. Only square junctions are supported
     * for now. Return value is normally JunctionMoveResult containing your new location. In case the
     * move distance is big enough to leave the junction, JunctionMoveResult remainder will be nonzero
     * and will contain the distance that is left to move outside of junction.
     * @param distance - distance to move
     * @param junctionWidth - junction width
     * @param junctionHeight - junction height
     * @return JunctionMoveResult with new location and optionally remainder of distance to move
     *      outside junction
     */
    public JunctionMoveResult move(float distance, float junctionWidth, float junctionHeight){
        if(junctionWidth!=junctionHeight){
            throw new IllegalArgumentException("Non-square junctions not supported yet...");
        }
        float normalized = distance/junctionWidth;
        JunctionMoveResult result = move(normalized);
        return new JunctionMoveResult(result.getNewLocation(),
                                      result.getRemainder()*junctionWidth);
    }

    private static float progressFromDistanceTravelled(float distanceTravelled, float junctionWidth, float junctionHeight, float routeLength){
        if(junctionWidth!=junctionHeight){
            throw new IllegalArgumentException("Non-square junctions not supported yet...");
        }
        return distanceTravelled/(junctionWidth*routeLength);
    }

    private static float distanceTravelledFromProgress(float progress, float junctionWidth, float junctionHeight, float routeLength){
        if(junctionWidth!=junctionHeight){
            throw new IllegalArgumentException("Non-square junctions not supported yet...");
        }
        return progress*junctionWidth*routeLength;
    }

    public float getDistanceTravelled(float junctionWidth, float junctionHeight){
        return distanceTravelledFromProgress(getProgress(), junctionWidth, junctionHeight, getRouteLength());
    }

}
