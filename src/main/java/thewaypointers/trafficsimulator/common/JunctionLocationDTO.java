package thewaypointers.trafficsimulator.common;

import thewaypointers.trafficsimulator.utils.FloatPoint;
import thewaypointers.trafficsimulator.utils.Rotation;

public class JunctionLocationDTO implements ILocation {
    private JunctionDTO junction;
    private Direction origin;
    private Direction target;
    private float percentageTravelled;

    public JunctionLocationDTO(JunctionDTO junction, Direction origin, Direction target, float percentageTravelled) {
        this.junction = junction;
        this.origin = origin;
        this.target = target;
        this.percentageTravelled = percentageTravelled;
    }

    public JunctionDTO getJunction() {
        return junction;
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
        this(other.getJunction(), other.getOrigin(), other.getTarget(), other.getPercentageTravelled());
    }

    @Override
    public ILocation copy() {
        return new JunctionLocationDTO(this);
    }

    // default origin - Down
    private FloatPoint getLeftTurnRouteCoordinates(float progress){
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
    private FloatPoint getRightTurnRouteCoordinates(float progress){
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
            coords = getLeftTurnRouteCoordinates(getPercentageTravelled());
        }else if(getTarget().equals(getOrigin().toRight())){
            coords = getRightTurnRouteCoordinates(getPercentageTravelled());
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
                rotated = coords.rotate90(center, Rotation.Counterclockwise);
                break;
            case Left:
                rotated = coords.rotate90(center, Rotation.Clockwise);
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

}
