package thewaypointers.trafficsimulator.utils;

public class SpeedConvert {
    public static float getSpeed(float distance, long milis){
        return (distance/milis)*1000;
    }

    public static long getTime(float speed, float distance){
        return (long)((distance/speed)*1000);
    }

    public static float getDistance(float speed, long milis){
        return (speed*milis)/1000;
    }
}
