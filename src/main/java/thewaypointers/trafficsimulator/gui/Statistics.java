package thewaypointers.trafficsimulator.gui;

public class Statistics {

    private float distance=0;
    private int time=0;
    private int start_time=0;
    private String label;
    private boolean flag=false;
    private float predistance=0;

    public float getPredistance() {
        return predistance;
    }

    public void setPredistance(float predistance) {
        this.predistance = predistance;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getStart_time() {
        return start_time;
    }

    public void setStart_time(int start_time) {
        this.start_time = start_time;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
