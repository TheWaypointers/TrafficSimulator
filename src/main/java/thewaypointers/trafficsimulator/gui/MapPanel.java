package thewaypointers.trafficsimulator.gui;
import javax.swing.*;

import thewaypointers.trafficsimulator.common.*;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.Map;

public class MapPanel extends JPanel implements IStateChangeListener{

    // configurable parameters
    public static final int MAP_PANEL_WIDTH = 1010;
    public static final int MAP_PANEL_HEIGHT = 1010;

    public static final int ROAD_Y1 = 250;
    public static final int ROAD_WIDTH = 50;

    public static final int VEHICLE_WIDTH = 8;
    public static final int VEHICLE_HEIGHT = 16;
    public static final int LANE_DISTANCE =8;
    public static final int TRAFFIC_LIGHT_SIZE =6;

    public static final int JUNCTION_STARTPOINT_X = 310;
    public static final int JUNCTION_STARTPOINT_Y = 310;

    public static final Color BACKGROUND_COLOR = Color.white;
    public static final Color ROAD_COLOR = Color.gray;
    public static final Color LANE_SEPARATOR_LINE_COLOR = Color.white;

    // set color of different kinds of vehicles
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
    private boolean junctionLocationsProcessed = false;

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
        if(!junctionLocationsProcessed){
            processjunctionlocation(worldStateDTO);
            junctionLocationsProcessed = true;
        }
        this.worldState = worldStateDTO;
        this.repaint();
    }

    private void draw_Vehicle(Graphics g, VehicleDTO vehicle,MapDTO map){
        Color color=Color.white;
        int wide,length;
        VehicleType type=vehicle.getType();
        Direction road_direction=this.Getcar_roadedirection(map,vehicle.getLocation());
        Point point=this.compute_xy(map,vehicle.getLocation());
        switch (type) {
            case CarNormal:
                color=VEHICLE_CarNormal_COLOR;
                break;
            case CarCautious:
                color=VEHICLE_CarCautious_COLOR;
                break;
            case CarReckless:
                color=VEHICLE_CarReckless_COLOR;
                break;
            case Bus:
                color=VEHICLE_Bus_COLOR;
                break;
            case EmergencyService:
                color=VEHICLE_EmergencyService_COLOR;
                break;
        }
        if (road_direction==Direction.Up || road_direction==Direction.Down) {
            wide = VEHICLE_WIDTH;
            length = VEHICLE_HEIGHT;
        }
        else{
            wide = VEHICLE_HEIGHT;
            length = VEHICLE_WIDTH;
        }
        g.setColor(color);
        g.fillRect((int) point.getX(), (int) point.getY(), wide, length);
    }

    private void drawVehicles(Graphics g){
        for (VehicleDTO vehicleDTO:worldState.getVehicleList().getAll()){
            draw_Vehicle(g,vehicleDTO,worldState.getRoadMap());
        }
    }

    private void drawTrafficLight(Graphics g, int x, int y, int size, TrafficLightColor color,Direction direction)
    {
        int edgewide,edgeheight,lightx,lighty;
        if (direction==Direction.Up||direction==Direction.Down){
            edgewide=size*2;
            edgeheight=size;
            lightx=x+size;
            lighty=y;
        }
        else {
            edgewide=size;
            edgeheight=size*2;
            lightx=x;
            lighty=y+size;
        }
        g.setColor(Color.black);
        g.drawRect(x, y, edgewide, edgeheight);

        g.setColor(Color.red);
        if (color.equals(TrafficLightColor.Red)){
            g.fillOval(x, y, size, size);
        } else {
            g.drawOval(x, y, size, size);
        }

        g.setColor(Color.green);
        if (color.equals(TrafficLightColor.Green)){
            g.fillOval(lightx, lighty, size, size);
        } else {
            g.drawOval(lightx, lighty, size, size);
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

    //compute junction location(x,y)
    public void processjunctionlocation(WorldStateDTO worldStateDTO){
        int junx=0,juny=0;
        MapDTO mapDTO=worldStateDTO.getRoadMap();
        String junction= mapDTO.getJunctions().get(0).getLabel();
        Point point =new Point(JUNCTION_STARTPOINT_X,JUNCTION_STARTPOINT_Y);
        junctionlocation.put(junction,point);
        for (int i = 0;i < mapDTO.getJunctionCount();i++){
            JunctionDTO junctionDTO = mapDTO.getJunctions().get(i);
            Point projunction;
            if (junctionlocation.containsKey(junctionDTO.getLabel())) {
                projunction=junctionlocation.get(junctionDTO.getLabel());}
            else
                continue;
            for (Direction direction : junctionDTO.getConnectedDirections()){
                RoadDTO roadDTO=junctionDTO.getRoad(direction);
                Point junctionpoint=new Point();
                if (mapDTO.getJunctions().contains(roadDTO.getFrom())&&mapDTO.getJunctions().contains(roadDTO.getTo())) {
                    if (!junctionlocation.containsKey(roadDTO.getTo().getLabel()) || !junctionlocation.containsKey(roadDTO.getFrom().getLabel())) {
                        String jun;
                        if (junctionlocation.containsKey(roadDTO.getTo().getLabel())){
                            jun = roadDTO.getFrom().getLabel();
                        }else{
                            jun = roadDTO.getTo().getLabel();
                        }
                        if (direction == Direction.Left) {
                            junx = (int) (projunction.getX() - roadDTO.getLength() - ROAD_WIDTH);
                            juny = (int) projunction.getY();
                        }
                        else if (direction == Direction.Down){
                            junx = (int) projunction.getX();
                            juny = (int) (projunction.getY() + roadDTO.getLength() + ROAD_WIDTH);
                        }
                        else if (direction == Direction.Right){
                            junx = (int) (projunction.getX() + roadDTO.getLength() + ROAD_WIDTH);
                            juny = (int) projunction.getY();
                        }
                        else{
                            junx = (int) projunction.getX();
                            juny = (int) (projunction.getY() - roadDTO.getLength() - ROAD_WIDTH);
                        }
                        junctionpoint.setLocation(junx,juny);
                        junctionlocation.put(jun, junctionpoint);
                    }
                }
            }
        }

        // at the condition that the first junction we get is not at the top left
        for(JunctionDTO junctionDTO : mapDTO.getJunctions()){
            double minx = JUNCTION_STARTPOINT_X;
            double miny = JUNCTION_STARTPOINT_Y;
            String jun = junctionDTO.getLabel();
            if (junctionlocation.get(jun).getX() < minx)
                minx = junctionlocation.get(jun).getX();
            if (junctionlocation.get(jun).getY() < miny)
                miny = junctionlocation.get(jun).getY();
            if (minx < JUNCTION_STARTPOINT_X || miny < JUNCTION_STARTPOINT_Y){
                double addx = JUNCTION_STARTPOINT_X - minx;
                double addy = JUNCTION_STARTPOINT_Y - miny;
                for(JunctionDTO junctionDTOx : mapDTO.getJunctions()){
                    int x = (int)junctionlocation.get(junctionDTOx.getLabel()).getX();
                    int y = (int)junctionlocation.get(junctionDTOx.getLabel()).getY();
                    junctionlocation.get(junctionDTOx.getLabel()).setLocation(x+addx,y+addy);
                }
            }
        }
    }

    // computer vehicle x&y base on location
    public  Point compute_xy(MapDTO roadmap,LocationDTO locationDTO){
        Point point = new Point();
        double px = 0, py = 0;
        RoadDTO roadDTO = locationDTO.getRoad();
        NodeDTO origin = locationDTO.getOrigin();
        Direction road_direction = Direction.Up;
        Point origin_point = new Point();
        float distance = locationDTO.getDistanceTravelled();
        Lane lane = locationDTO.getLane();
        String junction_label;
        if (junctionlocation.containsKey(roadDTO.getFrom().getLabel())&&junctionlocation.containsKey(roadDTO.getTo().getLabel()))
            junction_label = origin.getLabel();
        else if (junctionlocation.containsKey(roadDTO.getFrom().getLabel())){
            junction_label = roadDTO.getFrom().getLabel();
        }else {
            junction_label = roadDTO.getTo().getLabel();}

        JunctionDTO junction = roadmap.getJunction(junction_label);

        for (Direction direction : junction.getConnectedDirections()){
            if (junction.getRoad(direction).equals(roadDTO)){
                road_direction = direction;
            }
        }

        if (origin.getLabel().equals(junction_label)){
            switch (road_direction){
                case Up:
                    origin_point.setLocation(junctionlocation.get(junction_label).getX()+0.5*ROAD_WIDTH,junctionlocation.get(junction_label).getY());
                    py=origin_point.getY()-distance-0.5*VEHICLE_HEIGHT;
                    if(lane==Lane.Right){
                        px=origin_point.getX()+ LANE_DISTANCE;
                    }
                    else{
                        px=origin_point.getX()- LANE_DISTANCE -VEHICLE_WIDTH;
                    }
                    break;

                case Down:
                    origin_point.setLocation(junctionlocation.get(junction_label).getX()+0.5*ROAD_WIDTH,junctionlocation.get(junction_label).getY()+ROAD_WIDTH);
                    py=origin_point.getY()+distance-0.5*VEHICLE_HEIGHT;
                    if(lane==Lane.Right){
                        px=origin_point.getX()- LANE_DISTANCE -VEHICLE_WIDTH;
                    }
                    else{
                        px=origin_point.getX()+ LANE_DISTANCE;
                    }
                    break;
                case Left:
                    origin_point.setLocation(junctionlocation.get(junction_label).getX(),junctionlocation.get(junction_label).getY()+0.5*ROAD_WIDTH);
                    px=origin_point.getX()-distance-0.5*VEHICLE_HEIGHT;
                    if(lane==Lane.Right){
                        py=origin_point.getY()- LANE_DISTANCE -VEHICLE_WIDTH;
                    }
                    else{
                        py=origin_point.getY()+ LANE_DISTANCE;
                    }
                    break;
                case Right:
                    origin_point.setLocation(junctionlocation.get(junction_label).getX()+ROAD_WIDTH,junctionlocation.get(junction_label).getY()+0.5*ROAD_WIDTH);
                    px=origin_point.getX()+distance-0.5*VEHICLE_HEIGHT;
                    if(lane==Lane.Right){
                        py=origin_point.getY()+ LANE_DISTANCE;
                    }
                    else{
                        py=origin_point.getY()- LANE_DISTANCE - VEHICLE_WIDTH;
                    }
                    break;

            }
        }else{
            switch (road_direction){
                case Down:
                    origin_point.setLocation(junctionlocation.get(junction_label).getX()+0.5*ROAD_WIDTH,junctionlocation.get(junction_label).getY()+roadDTO.getLength()+ROAD_WIDTH);
                    py=origin_point.getY()-distance-0.5*VEHICLE_HEIGHT;
                    if(lane==Lane.Right){
                        px=origin_point.getX()+ LANE_DISTANCE;
                    }
                    else{
                        px=origin_point.getX()- LANE_DISTANCE -VEHICLE_WIDTH;
                    }
                    break;
                case Up:
                    origin_point.setLocation(junctionlocation.get(junction_label).getX()+0.5*ROAD_WIDTH,junctionlocation.get(junction_label).getY()-roadDTO.getLength());
                    py=origin_point.getY()+distance-0.5*VEHICLE_HEIGHT;
                    if(lane==Lane.Right){
                        px=origin_point.getX()- LANE_DISTANCE -VEHICLE_WIDTH;
                    }
                    else{
                        px=origin_point.getX()+ LANE_DISTANCE;
                    }
                    break;
                case Left:
                    origin_point.setLocation(junctionlocation.get(junction_label).getX()-roadDTO.getLength(),junctionlocation.get(junction_label).getY()+0.5*ROAD_WIDTH);
                    px=origin_point.getX()+distance-0.5*VEHICLE_HEIGHT;
                    if(lane==Lane.Right){
                        py=origin_point.getY()+ LANE_DISTANCE;
                    }

                    else{
                        py=origin_point.getY()- LANE_DISTANCE -VEHICLE_WIDTH;
                    }
                    break;
                case Right:
                    origin_point.setLocation(junctionlocation.get(junction_label).getX()+roadDTO.getLength()+ROAD_WIDTH,junctionlocation.get(junction_label).getY()+0.5*ROAD_WIDTH);
                    px=origin_point.getX()-distance-0.5*VEHICLE_HEIGHT;
                    if(lane==Lane.Right){
                        py=origin_point.getY()- LANE_DISTANCE -VEHICLE_WIDTH;
                    }

                    else{
                        py=origin_point.getY()+ LANE_DISTANCE;
                    }
                    break;
            }
        }
        point.setLocation(px,py);

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
