package thewaypointers.trafficsimulator.utils;

public class VehicleSpawnRatio {
    private int cautionCarsRatio;
    private int normalCarsRatio;
    private int reckCarsRatio;
    private int ambulanceRatio;
    private int busRatio;

    public VehicleSpawnRatio(){
        cautionCarsRatio = 20;
        normalCarsRatio = 20;
        reckCarsRatio = 20;
        ambulanceRatio = 20;
        busRatio = 20;

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

    public int getBusRatio() {
        return busRatio;
    }

    public void setBusRatio(int busRatio) {
        this.busRatio = busRatio;
    }
}
