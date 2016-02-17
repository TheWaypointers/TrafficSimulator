package thewaypointers.trafficsimulator.gui;
import javax.swing.*;

import thewaypointers.trafficsimulator.common.*;

import java.awt.*;
import java.awt.geom.Line2D;

public class MapPanel extends JPanel implements IStateChangeListener{

    // configurable parameters
    public static final int MAP_PANEL_WIDTH = 1000;
    public static final int MAP_PANEL_HEIGHT = 1000;
    public static final int MAP_PANEL_WIDTH = 700;
    public static final int MAP_PANEL_HEIGHT = 700;

    public static final int ROAD_Y1 = 250;
    public static final int ROAD_WIDTH = 50;

    public static final int VEHICLE_WIDTH = 8;
    public static final int VEHICLE_HEIGHT = 15;

    public static final int TRAFFIC_LIGHT_SIZE = 6;

    public static final Color BACKGROUND_COLOR = Color.white;
    public static final Color ROAD_COLOR = Color.gray;
    public static final Color LANE_SEPARATOR_LINE_COLOR = Color.white;
    public static final Color VEHICLE_COLOR = Color.white;

    // computable parameters
    public static final int ROAD_LEFT_LANE_X = ROAD_Y1 + (ROAD_WIDTH/4);
    public static final int ROAD_MIDDLE_LINE = ROAD_Y1 + (ROAD_WIDTH/2);
    public static final int ROAD_RIGHT_LANE_X = ROAD_Y1 + (ROAD_WIDTH*3/4);
    public static final int ROAD_Y2 = ROAD_Y1 + ROAD_WIDTH;

    WorldStateDTO worldState = new WorldStateDTO();

    public MapPanel(){
        this.setVisible(true);
        this.setSize(MAP_PANEL_WIDTH,MAP_PANEL_HEIGHT);
        this.setBackground(BACKGROUND_COLOR);

    }

    public void NewStateReceived(WorldStateDTO worldStateDTO){
        this.worldState = worldStateDTO;
        this.repaint();
    }

    private void drawVehicle(Graphics g, VehicleDTO vehicle, MapDTO map){
        JunctionDTO junction = map.junctions.get(0);
        RoadDTO upRoad = junction.connections.get(Direction.Up);
        RoadDTO downRoad = junction.connections.get(Direction.Down);

        g.setColor(VEHICLE_COLOR);
        int x = vehicle.location.getLane() == Lane.Right? ROAD_RIGHT_LANE_X : ROAD_LEFT_LANE_X;
        int y = 0;

        if (vehicle.location.getOrigin() == upRoad.start) {
            y = (int)vehicle.location.getDistanceTravelled();
        }
        if (vehicle.location.getOrigin() == upRoad.end) {
            // car is coming from junction
            if (vehicle.location.getRoad() == upRoad) {
                y=(int)(upRoad.length - vehicle.location.getDistanceTravelled()) - VEHICLE_HEIGHT;
            }
            if (vehicle.location.getRoad() == downRoad) {
                y = (int)(vehicle.location.getDistanceTravelled()+upRoad.length);
            }
        }
        if (vehicle.location.getOrigin() == downRoad.end) {
            y = (int)(downRoad.length + upRoad.length - vehicle.location.getDistanceTravelled()) - VEHICLE_HEIGHT;
        }

        g.fillRect(x, y, VEHICLE_WIDTH, VEHICLE_HEIGHT);
    }

    private void drawTrafficLight(Graphics g, int x, int y, int size, TrafficLightColor color)
    {
        g.setColor(Color.black);
        g.drawRect(x, y, size*2, size);

        g.setColor(Color.red);
        if (color.equals(TrafficLightColor.Red)){
            g.fillOval(x, y, size, size);
        } else {
            g.drawOval(x, y, size, size);
        }
        g.setColor(Color.green);
        if (color.equals(TrafficLightColor.Green)){
            g.fillOval(x+size, y, size, size);
        } else {
            g.drawOval(x+size, y, size, size);
        }
    }

    //draw worldState
    public void paint(Graphics g){
        super.paint(g);
        JunctionDTO junction = worldState.roadMap.junctions.get(0);
        RoadDTO upRoad = junction.connections.get(Direction.Up);
        RoadDTO downRoad = junction.connections.get(Direction.Down);
        float totalLength = upRoad.length+downRoad.length;

        // draw road
        g.setColor(ROAD_COLOR);
        g.fillRect(ROAD_Y1,0,ROAD_WIDTH,(int)totalLength);

        // draw lane separator line
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(LANE_SEPARATOR_LINE_COLOR);
        float [] arr={15.0f,10.0f};
        BasicStroke stroke = new BasicStroke(1,BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1.0f,arr,0);
        g2.setStroke(stroke);
        Line2D.Float line = new Line2D.Float(ROAD_MIDDLE_LINE,10,ROAD_MIDDLE_LINE,10+(int)totalLength);
        g2.draw(line);
        BasicStroke stroke2=new BasicStroke();
        g2.setStroke(stroke2);

        int junction_y = (int)upRoad.length;

        //draw junction
        g.setColor(Color.black);
        g.drawLine(ROAD_Y1,junction_y,ROAD_Y2,junction_y);

        //draw lights
        TrafficLightDTO upTrafficLight = junction.trafficLights.get(Direction.Up);
        TrafficLightDTO downTrafficLight = junction.trafficLights.get(Direction.Down);
        drawTrafficLight(g, 255, 297, TRAFFIC_LIGHT_SIZE, upTrafficLight.color);
        drawTrafficLight(g, 283, 297, TRAFFIC_LIGHT_SIZE, downTrafficLight.color);

        //draw cars
        for(VehicleDTO vehicle : worldState.vehicles)
            drawVehicle(g, vehicle, worldState.roadMap);
    }

}
