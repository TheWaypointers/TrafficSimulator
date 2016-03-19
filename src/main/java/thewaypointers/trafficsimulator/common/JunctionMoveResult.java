package thewaypointers.trafficsimulator.common;

public class JunctionMoveResult {
    JunctionLocationDTO newLocation;
    float remainder;

    public JunctionMoveResult(JunctionLocationDTO newLocation, float remainder) {
        this.newLocation = newLocation;
        this.remainder = remainder;
    }

    public JunctionLocationDTO getNewLocation() {
        return newLocation;
    }

    public float getRemainder() {
        return remainder;
    }
}