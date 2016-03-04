package thewaypointers.trafficsimulator.common;

import thewaypointers.trafficsimulator.utils.Angle;
import thewaypointers.trafficsimulator.utils.FloatPoint;
import thewaypointers.trafficsimulator.utils.Rotation;

public class JunctionLocationDTO implements ILocation {
    private String junctionLabel;
    private Direction origin;
    private Direction target;
    private float percentageTravelled;

    public JunctionLocationDTO(String junctionLabel, Direction origin, Direction target, float percentageTravelled) {
        this.junctionLabel = junctionLabel;
        this.origin = origin;
        this.target = target;
        this.percentageTravelled = percentageTravelled;
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

    public float getPercentageTravelled() {
        return percentageTravelled;
    }

    public JunctionLocationDTO(JunctionLocationDTO other){
        this(other.getJunctionLabel(), other.getOrigin(), other.getTarget(), other.getPercentageTravelled());
    }

    @Override
    public ILocation copy() {
        return new JunctionLocationDTO(this);
    }

    public boolean equals(JunctionLocationDTO other){
        return  getJunctionLabel().equals(other.getJunctionLabel()) &&
                getTarget().equals(other.getTarget()) &&
                getOrigin().equals(other.getOrigin()) &&
                getPercentageTravelled() == other.getPercentageTravelled();
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

    public FloatPoint getJunctionCoordinates(){

        // get correct curve
        FloatPoint coords;
        if(getTarget().equals(getOrigin().toLeft())){
            coords = getLeftTurnCoordinates(getPercentageTravelled());
        }else if(getTarget().equals(getOrigin().toRight())){
            coords = getRightTurnCoordinates(getPercentageTravelled());
        }else if(getTarget().equals(getOrigin().opposite())){
            coords = getStraightRouteCoordinates(getPercentageTravelled());
        }else{
            throw new AssertionError(String.format(
                    "Invalid target %s for origin %s", getTarget(), getOrigin()));
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
            float progressNormalized = (getPercentageTravelled()-1/3f)*3;
            return Math.PI/2 + (Math.PI/2)*progressNormalized;
        }else{
            return Math.PI;
        }
    }

    public double getAngle(){

        // get correct angle
        double angle;
        if(getTarget().equals(getOrigin().toLeft())){
            angle = getLeftTurnAngle(getPercentageTravelled());
        }else if(getTarget().equals(getOrigin().toRight())){
            angle = getRightTurnAngle(getPercentageTravelled());
        }else if(getTarget().equals(getOrigin().opposite())){
            angle = getStraightRouteAngle();
        }else{
            throw new AssertionError(String.format(
                    "Invalid target %s for origin %s", getTarget(), getOrigin()));
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
}
