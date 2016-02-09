package thewaypointers.trafficsimulator.gui;
import javax.swing.*;

import thewaypointers.trafficsimulator.common.*;
import thewaypointers.trafficsimulator.common.enums.VehicleType;

import java.awt.*;
import java.awt.geom.Line2D;

public class MapPanel extends JPanel{

    public static final int RIGHT_LANE_X = 283;
    public static final int LEFT_LANE_X = 267;

    WorldStateDTO worldState = new WorldStateDTO();

    public MapPanel(){
        this.setVisible(true);
        this.setSize(600,600);
        this.setBackground(Color.white);

    }


    public void getWorldState(WorldStateDTO worldStateDTO){

        this.worldState = worldStateDTO;

    }

    private void drawVehicle(Graphics g, VehicleDTO vehicle, MapDTO map){
        JunctionDTO junction = map.junctions.get(0);
        RoadDTO upRoad = junction.connections.get(Direction.Up);
        RoadDTO downRoad = junction.connections.get(Direction.Down);

        if (vehicle.type==VehicleType.CarNormal) {
            int x = vehicle.location.getLane() == Lane.Right? RIGHT_LANE_X : LEFT_LANE_X;
            int y;
            g.setColor(Color.white);
            if (vehicle.location.getOrigin() == upRoad.start) {
                y = (int)vehicle.location.getDistanceTravelled();
                g.fillRect(x, y, 8, 15);
            }
            if (vehicle.location.getOrigin() == upRoad.end) {
                // car is at junction
                if (vehicle.location.getRoad() == upRoad) {
                    y=(int)(upRoad.length - vehicle.location.getDistanceTravelled());
                    g.fillRect(x, y-15, 8, 15);
                }
                if (vehicle.location.getRoad() == downRoad) {
                    y = (int)(vehicle.location.getDistanceTravelled()+upRoad.length);
                    g.fillRect(x, y, 8, 15);
                }
            }

            if (vehicle.location.getOrigin() == downRoad.end) {

                y = (int)(downRoad.length + upRoad.length - vehicle.location.getDistanceTravelled());
                g.fillRect(x, y-15, 8, 15);
            }
        }
    }

    //draw worldState
    public void paint(Graphics g){
        super.paint(g);

        //draw road
        // road is from up to down
        g.setColor(Color.gray);
        JunctionDTO junction = worldState.roadMap.junctions.get(0);
        RoadDTO upRoad = junction.connections.get(Direction.Up);
        RoadDTO downRoad = junction.connections.get(Direction.Down);
        float totalLength = upRoad.length+downRoad.length;

        // draw road
        g.fillRect(250,0,50,(int)totalLength);
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.white);
        float [] arr={15.0f,10.0f};
        BasicStroke stroke = new BasicStroke(1,BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1.0f,arr,0);
        g2.setStroke(stroke);
        Line2D.Float line = new Line2D.Float(275,10,275,10+(int)totalLength);
        g2.draw(line);
        BasicStroke stroke2=new BasicStroke();
        g2.setStroke(stroke2);
        //draw junction
        g.setColor(Color.black);
        g.drawLine(250,(int)upRoad.length,300,(int)upRoad.length);

        //draw lights
        g.setColor(Color.black);
        g.drawLine(250, 300, 300, 300);
        g.drawRect(255, 297, 12, 6);
        g.drawRect(283, 297, 12, 6);

        g.setColor(Color.red);
        g.drawOval(255,297,6,6);
        g.setColor(Color.green);
        g.fillOval(261, 297, 6, 6);

        g.setColor(Color.green);
        g.fillOval(283,297,6,6);
        g.setColor(Color.red);
        g.drawOval(289, 297, 6, 6);


        //draw cars
        for(VehicleDTO vehicle : worldState.vehicles)
            drawVehicle(g, vehicle, worldState.roadMap);
    }

}
