package thewaypointers.trafficsimulator.common;

public class InitialParameters {

    private static final int cautionCarsRatio = 25;
    private static final int normalCarsRatio = 40;
    private static final int reckCarsRatio = 25;
    private static final int ambulanceRatio = 10;

    public static int getCautionCarsRatio() {
        return cautionCarsRatio;
    }

    public static int getNormalCarsRatio() {
        return normalCarsRatio;
    }

    public static int getReckCarsRatio() {
        return reckCarsRatio;
    }

    public static int getAmbulanceRatio() {
        return ambulanceRatio;
    }
}
