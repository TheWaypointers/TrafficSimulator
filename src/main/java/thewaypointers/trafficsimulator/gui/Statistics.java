package thewaypointers.trafficsimulator.gui;

public class Statistics {

    private float distance=0;
    private float tmp=0;
    private int time=0;
    private int start_time=0;
    private String label;

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

    public float getTmp() {
        return tmp;
    }

    public void setTmp(float tmp) {
        this.tmp = tmp;
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
