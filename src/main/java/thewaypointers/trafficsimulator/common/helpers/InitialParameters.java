package thewaypointers.trafficsimulator.common.helpers;

public class InitialParameters {

    private static final int cautionCarsRatio = 25;
    private static final int normalCarsRatio = 40;
    private static final int reckCarsRatio = 25;
    private static final int ambulanceRatio = 10;
    private static final int trafficLightSteps = 70;
    private static final int maxVehicleNumber = 40;


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

    public static int getTrafficLightSteps() {
        return trafficLightSteps;
    }

    public static int getMaxVehicleNumber() {
        return maxVehicleNumber;
    }
}
