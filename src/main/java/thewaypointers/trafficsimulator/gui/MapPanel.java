package thewaypointers.trafficsimulator.gui;
import javax.swing.*;


import thewaypointers.trafficsimulator.common.*;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.Node;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapPanel extends JPanel implements IStateChangeListener{

    // configurable parameters
    public static final int MAP_PANEL_WIDTH = 1010;
    public static final int MAP_PANEL_HEIGHT = 1010;

    public static final int ROAD_Y1 = 250;
    public static final int ROAD_WIDTH = 50;


    public static final int VEHICLE_WIDTH = 8;
    public static final int VEHICLE_HEIGHT = 16;
    public static final  int Lane_distance=8;
    public static final int TRAFFIC_LIGHT_SIZE = 6;

    public static final Color BACKGROUND_COLOR = Color.white;
    public static final Color ROAD_COLOR = Color.gray;
    public static final Color LANE_SEPARATOR_LINE_COLOR = Color.white;

    // set car color
    public static final Color VEHICLE_CarNormal_COLOR = Color.blue;
    public static final Color VEHICLE_CarCautious_COLOR = Color.yellow;
    public static final Color VEHICLE_EmergencyService_COLOR = Color.white;
    public static final Color VEHICLE_Bus_COLOR = Color.cyan;
    public static final Color VEHICLE_CarReckless_COLOR = Color.red;

    // computable parameters
    public static final int ROAD_LEFT_LANE_X = ROAD_Y1 + (ROAD_WIDTH/4);
    public static final int ROAD_MIDDLE_LINE = ROAD_Y1 + (ROAD_WIDTH/2);
    public static final int ROAD_RIGHT_LANE_X = ROAD_Y1 + (ROAD_WIDTH*3/4);
    public static final int ROAD_Y2 = ROAD_Y1 + ROAD_WIDTH;

    WorldStateDTO worldState = new WorldStateDTO(null, null, null);

    public  static Map<String,Point> junctionlocation;


    public MapPanel(){
        junctionlocation=new HashMap<>();
        this.setVisible(true);
        this.setSize(MAP_PANEL_WIDTH,MAP_PANEL_HEIGHT);
        this.setBackground(BACKGROUND_COLOR);

        PanelMouseDragger mouseDragger= new PanelMouseDragger(this);
        this.addMouseListener(mouseDragger);
        this.addMouseMotionListener(mouseDragger);

    }

    public void NewStateReceived(WorldStateDTO worldStateDTO){
        this.worldState = worldStateDTO;
        this.repaint();
    }

    private void drawVehicle(Graphics g, VehicleDTO vehicle, MapDTO map){
        JunctionDTO junction = map.getJunctions().get(0);
        RoadDTO upRoad = junction.getRoad(Direction.Up);
        RoadDTO downRoad = junction.getRoad(Direction.Down);

        g.setColor(VEHICLE_CarNormal_COLOR);
        int x = vehicle.getLocation().getLane() == Lane.Right? ROAD_RIGHT_LANE_X : ROAD_LEFT_LANE_X;
        int y = 0;

        if (vehicle.getLocation().getOrigin().equals(upRoad.getFrom())) {
            y = (int)vehicle.getLocation().getDistanceTravelled();
        }
        if (vehicle.getLocation().getOrigin().equals(upRoad.getTo())) {
            // car is coming from junction
            if (vehicle.getLocation().getRoad().equals(upRoad)) {
                y=(int)(upRoad.getLength() - vehicle.getLocation().getDistanceTravelled()) - VEHICLE_HEIGHT;
            }
            if (vehicle.getLocation().getRoad().equals(downRoad)) {
                y = (int)(vehicle.getLocation().getDistanceTravelled()+upRoad.getLength());
            }
        }
        if (vehicle.getLocation().getOrigin().equals(downRoad.getTo())) {
            y = (int)(downRoad.getLength() + upRoad.getLength() - vehicle.getLocation().getDistanceTravelled()) - VEHICLE_HEIGHT;
        }

        g.fillRect(x, y, VEHICLE_WIDTH, VEHICLE_HEIGHT);
    }

    private void draw_Vehicle(Graphics g, VehicleDTO vehicle,MapDTO map){
        VehicleType type=vehicle.getType();
        Direction road_direction=this.Getcar_roadedirection(map,vehicle.getLocation());
        Point point=this.compute_xy(map,vehicle.getLocation());
        switch (type) {
            case CarNormal:
                g.setColor(VEHICLE_CarNormal_COLOR);
                break;
            case CarCautious:
                g.setColor(VEHICLE_CarCautious_COLOR);
                break;
            case CarReckless:
                g.setColor(VEHICLE_CarReckless_COLOR);
                break;
            case Bus:
                g.setColor(VEHICLE_Bus_COLOR);
                break;
            case EmergencyService:
                g.setColor(VEHICLE_EmergencyService_COLOR);
                break;
        }
        if (road_direction==Direction.Up|| road_direction==Direction.Down)
            g.fillRect((int)point.getX(),(int)point.getY(),VEHICLE_WIDTH,VEHICLE_HEIGHT);
        else
            g.fillRect((int)point.getX(),(int)point.getY(),VEHICLE_HEIGHT,VEHICLE_WIDTH);


    }

    private void drawVehicles(Graphics g){
        for (VehicleDTO vehicleDTO:worldState.getVehicleList().getAll()){
            draw_Vehicle(g,vehicleDTO,worldState.getRoadMap());
        }
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
        this.draw_road_network(g);
        this.drawVehicles(g);

    }


    public void draw_road_network(Graphics g) {
        MapDTO mapDTO = worldState.getRoadMap();
        g.setColor(ROAD_COLOR);

        for (JunctionDTO junctionDTO:mapDTO.getJunctions()){
            g.setColor(ROAD_COLOR);
            String junname=junctionDTO.getLabel();
            int x=(int)junctionlocation.get(junname).getX();
            int y=(int)junctionlocation.get(junname).getY();
            g.fillRect(x,y,ROAD_WIDTH,ROAD_WIDTH);
            for (Direction direction:junctionDTO.getConnectedDirections()){
                this.draw_road(junctionDTO, direction, g, x, y);
            }
        }

    }

    public void draw_road(JunctionDTO junctionDTO, Direction direction, Graphics g, int x, int y){
        g.setColor(ROAD_COLOR);
        RoadDTO road=junctionDTO.getRoad(direction);
        int roadX=0, roadY=0, roadWidth, roadLength, startX, startY, endX, endY;
        switch(direction) {
            case Up:
                roadX = x;
                roadY = (int) (y - road.getLength());
                break;
            case Down:
                roadX = x;
                roadY = y + ROAD_WIDTH;
                break;
            case Right:
                roadX = x + ROAD_WIDTH;
                roadY = y;
                break;
            case Left:
                roadX = (int) (x - road.getLength());
                roadY = y;
                break;
        }
        if (direction.isVertical()){
            roadWidth = ROAD_WIDTH;
            roadLength = (int) road.getLength();
            startX = roadX + 25;
            endX = roadY;
            startY = roadX + 25;
            endY = roadY + (int) road.getLength();
        }else{
            roadWidth = (int) road.getLength();
            roadLength = ROAD_WIDTH;
            startX = roadX;
            endX = roadY + 25;
            startY = roadX + (int) road.getLength();
            endY = roadY + 25;
        }

        g.fillRect(roadX,roadY, roadWidth, roadLength);
        draw_line(startX, endX, startY, endY, g);
    }

    private void draw_line(int star_X, int end_X, int star_Y, int end_Y, Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(LANE_SEPARATOR_LINE_COLOR);
        float [] arr={15.0f,10.0f};
        BasicStroke stroke = new BasicStroke(1,BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1.0f,arr,0);
        g2.setStroke(stroke);
        Line2D.Float line = new Line2D.Float(star_X,end_X,star_Y,end_Y);
        g2.draw(line);
        BasicStroke stroke2=new BasicStroke();
        g2.setStroke(stroke2);
    }

    public void  processjunctionlocation(WorldStateDTO worldStateDTO){

        MapDTO mapDTO=worldStateDTO.getRoadMap();
        String junction= mapDTO.getJunctions().get(0).getLabel();
        // Point point=new Point((int)mapDTO.getRoads().get(0).getLength(),(int)mapDTO.getRoads().get(0).getLength());
        Point point =new Point(310,310);
        junctionlocation.put(junction,point);
        for (int i=0;i<mapDTO.getJunctionCount();i++){
            JunctionDTO junctionDTO=mapDTO.getJunctions().get(i);
            Point projunction;
            if (junctionlocation.containsKey(junctionDTO.getLabel())){
                projunction=junctionlocation.get(junctionDTO.getLabel());}
            else
                continue;
            for (Direction direction:junctionDTO.getConnectedDirections()){
                RoadDTO roadDTO=junctionDTO.getRoad(direction);
                if (mapDTO.getJunctions().contains(roadDTO.getFrom())&&mapDTO.getJunctions().contains(roadDTO.getTo())) {
                    if (!junctionlocation.containsKey(roadDTO.getTo().getLabel()) || !junctionlocation.containsKey(roadDTO.getFrom().getLabel())) {
                        String jun;
                        Point p;
                        if (junctionlocation.containsKey(roadDTO.getTo().getLabel())){
                            jun = roadDTO.getFrom().getLabel();
                        }else{
                            jun = roadDTO.getTo().getLabel();
                        }
                        if (direction == Direction.Left)
                            p = new Point((int) (projunction.getX() - roadDTO.getLength() - ROAD_WIDTH), (int) projunction.getY());

                        else if (direction == Direction.Down)
                            p = new Point((int) projunction.getX(), (int) (projunction.getY() + roadDTO.getLength() + ROAD_WIDTH));
                        else if (direction == Direction.Right)
                            p = new Point((int) (projunction.getX() + roadDTO.getLength() + ROAD_WIDTH), (int) projunction.getY());
                        else
                            p = new Point((int) projunction.getX(), (int) (projunction.getY() - roadDTO.getLength() - ROAD_WIDTH));
                        junctionlocation.put(jun, p);

                    }
                }



            }
        }
        for(JunctionDTO junctionDTO:mapDTO.getJunctions()){
            double minx=310;
            double miny=310;
            String jun=junctionDTO.getLabel();
            if (junctionlocation.get(jun).getX()<minx)
                minx=junctionlocation.get(jun).getX();
            if (junctionlocation.get(jun).getY()<miny)
                miny=junctionlocation.get(jun).getY();

            if (minx<310){
                double add=310-minx;
                for(JunctionDTO junctionDTOx:mapDTO.getJunctions()){
                    int x=(int)junctionlocation.get(junctionDTOx.getLabel()).getX();
                    int y=(int)junctionlocation.get(junctionDTOx.getLabel()).getY();
                    junctionlocation.get(junctionDTOx.getLabel()).setLocation(x+add,y);
                }

            }
            if (miny<310){
                double add=310-miny;
                for(JunctionDTO junctionDTOy:mapDTO.getJunctions()){
                    int x=(int)junctionlocation.get(junctionDTOy.getLabel()).getX();
                    int y=(int)junctionlocation.get(junctionDTOy.getLabel()).getY();
                    junctionlocation.get(junctionDTOy.getLabel()).setLocation(x,y+add);
                }
            }
        }

    }

    // computer Vehicle x&y base on  location
    public  Point compute_xy(MapDTO roadmap,LocationDTO locationDTO){
        Point point=new Point();
        RoadDTO roadDTO=locationDTO.getRoad();
        NodeDTO origin=locationDTO.getOrigin();
        Direction road_direction=Direction.Up;
        Point origin_point=new Point();
        float distance=locationDTO.getDistanceTravelled();
        Lane lane=locationDTO.getLane();
        String junction_label;
        if (junctionlocation.containsKey(roadDTO.getFrom().getLabel())&&junctionlocation.containsKey(roadDTO.getTo().getLabel()))
            junction_label=origin.getLabel();
        else if (junctionlocation.containsKey(roadDTO.getFrom().getLabel())){
            junction_label=roadDTO.getFrom().getLabel();
        }else {
            junction_label=roadDTO.getTo().getLabel();}
        JunctionDTO junction= roadmap.getJunction(junction_label);

        for (Direction direction:junction.getConnectedDirections()){
            if (junction.getRoad(direction).equals(roadDTO)){
                road_direction=direction;
            }
        }

        if (origin.getLabel().equals(junction_label)){

            switch (road_direction){
                case Up:
                    origin_point.setLocation(junctionlocation.get(junction_label).getX()+0.5*ROAD_WIDTH,junctionlocation.get(junction_label).getY());
                    if(lane==Lane.Right)
                        point.setLocation(origin_point.getX()+Lane_distance,origin_point.getY()-distance-0.5*VEHICLE_HEIGHT);
                    else
                        point.setLocation(origin_point.getX()-Lane_distance -VEHICLE_WIDTH,origin_point.getY()-distance-0.5*VEHICLE_HEIGHT);
                    break;

                case Down:
                    origin_point.setLocation(junctionlocation.get(junction_label).getX()+0.5*ROAD_WIDTH,junctionlocation.get(junction_label).getY()+ROAD_WIDTH);
                    if(lane==Lane.Right)
                        point.setLocation(origin_point.getX()-Lane_distance-VEHICLE_WIDTH,origin_point.getY()+distance-0.5*VEHICLE_HEIGHT);
                    else
                        point.setLocation(origin_point.getX()+Lane_distance,origin_point.getY()+distance-0.5*VEHICLE_HEIGHT);
                    break;
                case Left:
                    origin_point.setLocation(junctionlocation.get(junction_label).getX(),junctionlocation.get(junction_label).getY()+0.5*ROAD_WIDTH);
                    if(lane==Lane.Right)
                        point.setLocation(origin_point.getX()-distance-0.5*VEHICLE_HEIGHT,origin_point.getY()-Lane_distance-VEHICLE_WIDTH);
                    else
                        point.setLocation(origin_point.getX()-distance-0.5*VEHICLE_HEIGHT,origin_point.getY()+Lane_distance);
                    break;
                case Right:
                    origin_point.setLocation(junctionlocation.get(junction_label).getX()+ROAD_WIDTH,junctionlocation.get(junction_label).getY()+0.5*ROAD_WIDTH);
                    if(lane==Lane.Right)
                        point.setLocation(origin_point.getX()+distance-0.5*VEHICLE_HEIGHT,origin_point.getY()+Lane_distance);
                    else
                        point.setLocation(origin_point.getX()+distance-0.5*VEHICLE_HEIGHT,origin_point.getY()-Lane_distance - VEHICLE_WIDTH);
                    break;

            }
        }else{
            switch (road_direction){

                case Down:
                    origin_point.setLocation(junctionlocation.get(junction_label).getX()+0.5*ROAD_WIDTH,junctionlocation.get(junction_label).getY()+roadDTO.getLength()+ROAD_WIDTH);
                    if(lane==Lane.Right)
                        point.setLocation(origin_point.getX()+Lane_distance,origin_point.getY()-distance-0.5*VEHICLE_HEIGHT);
                    else
                        point.setLocation(origin_point.getX()-Lane_distance -VEHICLE_WIDTH,origin_point.getY()-distance-0.5*VEHICLE_HEIGHT);

                    break;
                case Up:
                    origin_point.setLocation(junctionlocation.get(junction_label).getX()+0.5*ROAD_WIDTH,junctionlocation.get(junction_label).getY()-roadDTO.getLength());
                    if(lane==Lane.Right)
                        point.setLocation(origin_point.getX()-Lane_distance-VEHICLE_WIDTH,origin_point.getY()+distance-0.5*VEHICLE_HEIGHT);
                    else
                        point.setLocation(origin_point.getX()+Lane_distance,origin_point.getY()+distance-0.5*VEHICLE_HEIGHT);

                    break;
                case Left:
                    origin_point.setLocation(junctionlocation.get(junction_label).getX()-roadDTO.getLength(),junctionlocation.get(junction_label).getY()+0.5*ROAD_WIDTH);
                    if(lane==Lane.Right)
                        point.setLocation(origin_point.getX()+distance-0.5*VEHICLE_HEIGHT,origin_point.getY()+Lane_distance);
                    else
                        point.setLocation(origin_point.getX()+distance-0.5*VEHICLE_HEIGHT,origin_point.getY()-Lane_distance -VEHICLE_WIDTH);
                    break;
                case Right:
                    origin_point.setLocation(junctionlocation.get(junction_label).getX()+roadDTO.getLength()+ROAD_WIDTH,junctionlocation.get(junction_label).getY()+0.5*ROAD_WIDTH);
                    if(lane==Lane.Right)
                        point.setLocation(origin_point.getX()-distance-0.5*VEHICLE_HEIGHT,origin_point.getY()-Lane_distance-VEHICLE_WIDTH);
                    else
                        point.setLocation(origin_point.getX()-distance-0.5*VEHICLE_HEIGHT,origin_point.getY()+Lane_distance);

                    break;
            }
        }


        return  point;
    }

    public Direction Getcar_roadedirection(MapDTO road_map,LocationDTO locationDTO){
        RoadDTO roadDTO=locationDTO.getRoad();
        Direction road_direction=null;
        String junction_label;
        if (junctionlocation.containsKey(roadDTO.getFrom().getLabel())){
            junction_label=roadDTO.getFrom().getLabel();
        }else {
            junction_label=roadDTO.getTo().getLabel();}
        JunctionDTO junction= road_map.getJunction(junction_label);

        for (Direction direction:junction.getConnectedDirections()){
            if (junction.getRoad(direction).equals(roadDTO)){
                road_direction=direction;
            }
        }
        return road_direction;

    }

}
