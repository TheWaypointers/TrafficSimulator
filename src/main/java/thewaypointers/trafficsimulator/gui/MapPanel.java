package thewaypointers.trafficsimulator.gui;
import javax.swing.*;

import thewaypointers.trafficsimulator.common.Direction;
import thewaypointers.trafficsimulator.common.Lane;
import thewaypointers.trafficsimulator.common.RoadDTO;
import thewaypointers.trafficsimulator.common.VehicleDTO;
import thewaypointers.trafficsimulator.common.WorldStateDTO;
import thewaypointers.trafficsimulator.common.enums.VehicleType;

import java.awt.*;
import java.awt.geom.Line2D;

public class MapPanel extends JPanel{

    WorldStateDTO worldState = new WorldStateDTO();

    public MapPanel(){
        this.setVisible(true);
        this.setSize(600,600);
        this.setBackground(Color.white);

    }


    public void getWorldState(WorldStateDTO worldStateDTO){

        this.worldState =worldStateDTO;

    }

    //draw worldState
    public void paint(Graphics g){
        super.paint(g);

        //draw road
        if (worldState.roadMap.junctions.get(0).connections.get(Direction.Up)==null) {
            //road is from left to right

        }
        if (worldState.roadMap.junctions.get(0).connections.get(Direction.Left)==null) {
            //road is from up to down
            g.setColor(Color.gray);
            RoadDTO up=worldState.roadMap.junctions.get(0).connections.get(Direction.Up);
            RoadDTO down=worldState.roadMap.junctions.get(0).connections.get(Direction.Down);
            float length=up.length+down.length;
            g.fillRect(250,0,50,(int)length);
            Graphics2D  g2=(Graphics2D)g;
            g2.setColor(Color.white);
            float [] arr={15.0f,10.0f};
            BasicStroke stroke = new BasicStroke(1,BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1.0f,arr,0);
            g2.setStroke(stroke);
            Line2D.Float line = new Line2D.Float(275,10,275,10+(int)length);
            g2.draw(line);
            BasicStroke stroke2=new BasicStroke();
            g2.setStroke(stroke2);
            //draw junction
            g.setColor(Color.black);
            g.drawLine(250,(int)up.length,300,(int)up.length);

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

        }


        //draw cars
        VehicleDTO car1 = worldState.vehicles.get(0);
        if (car1.type==VehicleType.CarNormal) {
            int x=0;
            int y=0;
            g.setColor(Color.white);
            if (car1.location.getOrigin()==worldState.roadMap.junctions.get(0).connections.get(Direction.Up).start) {

                y=(int)car1.location.getDistanceTravelled();

                if (car1.location.getLane()==Lane.Right) {
                    x=283;
                }
                if (car1.location.getLane()==Lane.Left) {
                    x=267;
                }
                g.fillRect(x, y, 8, 15);
            }
            if (car1.location.getOrigin()==worldState.roadMap.junctions.get(0).connections.get(Direction.Up).end) {
                if (car1.location.getRoad()==worldState.roadMap.junctions.get(0).connections.get(Direction.Up)) {
                    y=(int)(worldState.roadMap.junctions.get(0).connections.get(Direction.Up).length-car1.location.getDistanceTravelled());

                    if (car1.location.getLane()==Lane.Right) {
                        x=283;
                    }
                    if (car1.location.getLane()==Lane.Left) {
                        x=267;
                    }
                    g.fillRect(x, y-15, 8, 15);
                }
                if (car1.location.getRoad()==worldState.roadMap.junctions.get(0).connections.get(Direction.Down)) {
                    y=(int)(car1.location.getDistanceTravelled()+worldState.roadMap.junctions.get(0).connections.get(Direction.Up).length);

                    if (car1.location.getLane()==Lane.Right) {
                        x=283;
                    }
                    if (car1.location.getLane()==Lane.Left) {
                        x=267;
                    }
                    g.fillRect(x, y, 8, 15);
                }
            }

            if (car1.location.getOrigin()==worldState.roadMap.junctions.get(0).connections.get(Direction.Down).end) {

                y=(int)(worldState.roadMap.junctions.get(0).connections.get(Direction.Down).length+worldState.roadMap.junctions.get(0).connections.get(Direction.Up).length-car1.location.getDistanceTravelled());

                if (car1.location.getLane()==Lane.Right) {
                    x=283;
                }
                if (car1.location.getLane()==Lane.Left) {
                    x=267;
                }

                g.fillRect(x, y-15, 8, 15);
            }
        }
    }

}
