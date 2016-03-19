package thewaypointers.trafficsimulator.utils;

public class Angle {

    public static double rotate90(double angle, Rotation rotation){
        return rotation == Rotation.Counterclockwise?
                add(angle, Math.PI/2) :
                subtract(angle, Math.PI/2);

    }

    public static double add(double angle1, double angle2){
        return (angle1+angle2) % (2*Math.PI);
    }

    public static double subtract(double angle1, double angle2){
        angle1 = angle1 % (Math.PI*2);
        angle2 = angle2 % (Math.PI*2);
        double result = angle1 - angle2;
        return result < 0? (Math.PI*2)+result : result;
    }
}
