package thewaypointers.trafficsimulator.simulation.factories.xml.models;

import thewaypointers.trafficsimulator.common.Direction;

public class RoadXML {
    private String origin;
    private String destination;
    private String length;
    private String direction;

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public long getLength() {
        String lengTolong = length.trim();

        try {
            long lenghtInLong = Long.parseLong(lengTolong);
            return lenghtInLong;
        } catch (Exception ex) {
            System.out.println("Error with casting length to long");
            System.out.println(ex.getMessage());
        }
        return 0;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public Direction getDirection() {
        switch (direction) {
            case "Up":
                return Direction.Up;
            case "Down":
                return Direction.Down;
            case "Right":
                return Direction.Right;
            case "Left":
                return Direction.Left;
            default:
                break;
        }
        return null;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
