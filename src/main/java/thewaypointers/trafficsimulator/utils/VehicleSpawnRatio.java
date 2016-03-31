package thewaypointers.trafficsimulator.utils;

import thewaypointers.trafficsimulator.common.helpers.InitialParameters;

public class VehicleSpawnRatio {
    private int cautionCarsRatio;
    private int normalCarsRatio;
    private int reckCarsRatio;
    private int ambulanceRatio;

    public VehicleSpawnRatio(){
        cautionCarsRatio = InitialParameters.getCautionCarsRatio();
        normalCarsRatio = InitialParameters.getNormalCarsRatio();
        reckCarsRatio = InitialParameters.getReckCarsRatio();
        ambulanceRatio = InitialParameters.getAmbulanceRatio();

    }

    public int getCautionCarsRatio() {
        return cautionCarsRatio;
    }

    public void setCautionCarsRatio(int cautionCarsRatio) {
        this.cautionCarsRatio = cautionCarsRatio;
    }

    public int getNormalCarsRatio() {
        return normalCarsRatio;
    }

    public void setNormalCarsRatio(int normalCarsRatio) {
        this.normalCarsRatio = normalCarsRatio;
    }

    public int getReckCarsRatio() {
        return reckCarsRatio;
    }

    public void setReckCarsRatio(int reckCarsRatio) {
        this.reckCarsRatio = reckCarsRatio;
    }

    public int getAmbulanceRatio() {
        return ambulanceRatio;
    }

    public void setAmbulanceRatio(int ambulanceRatio) {
        this.ambulanceRatio = ambulanceRatio;
    }
}
