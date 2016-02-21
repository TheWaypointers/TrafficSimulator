package thewaypointers.trafficsimulator.gui;
import javax.swing.*;
import thewaypointers.trafficsimulator.common.*;
import thewaypointers.trafficsimulator.simulation.models.graph.helper.Node;
import thewaypointers.trafficsimulator.utils.Pair;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapPanel extends JPanel implements IStateChangeListener{

    // configurable parameters
    public static final int MAP_PANEL_WIDTH = 1000;
    public static final int MAP_PANEL_HEIGHT = 1000;

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

    WorldStateDTO worldState = new WorldStateDTO(null, null, null);


    public MapPanel(){
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

        g.setColor(VEHICLE_COLOR);
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
        draw_road_network(g);

//        JunctionDTO junction_1 = worldState.getRoadMap().getJunctions().get(0);
//        JunctionDTO junction_2 = worldState.getRoadMap().getJunctions().get(1);
//        JunctionDTO junction_3 = worldState.getRoadMap().getJunctions().get(2);
//        g.setColor(ROAD_COLOR);
//        g.fillRect(300,300,50,50);
//        this.draw_road(junction_1, Direction.Up, g, 300, 300);
//        this.draw_road(junction_1, Direction.Down, g, 300, 300);
//        this.draw_road(junction_1, Direction.Left, g, 300, 300);
//        this.draw_road(junction_1, Direction.Right, g, 300, 300);
//        g.setColor(ROAD_COLOR);
//        g.fillRect(650,300,50,50);
//        this.draw_road(junction_2, Direction.Up, g, 650, 300);
//        this.draw_road(junction_2, Direction.Down, g, 650, 300);
//        this.draw_road(junction_2, Direction.Right, g, 650, 300);
//        g.setColor(ROAD_COLOR);
//        g.fillRect(300,650,50,50);
//        this.draw_road(junction_3, Direction.Down, g, 300, 650);
//        this.draw_road(junction_3, Direction.Left, g, 300, 650);


/*
//        RoadDTO upRoad = junction.getRoad(Direction.Up);
//        RoadDTO downRoad = junction.getRoad(Direction.Down);
//        float totalLength = upRoad.getLength()+downRoad.getLength();

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

        int junction_y = (int)upRoad.getLength();

        //draw junction
        g.setColor(Color.black);
        g.drawLine(ROAD_Y1,junction_y,ROAD_Y2,junction_y);

        //draw lights
        TrafficLightDTO upTrafficLight = worldState.getTrafficLightSystem()
                .getTrafficLight(junction.getLabel(), Direction.Up, Lane.Right);
        TrafficLightDTO downTrafficLight = worldState.getTrafficLightSystem()
                .getTrafficLight(junction.getLabel(), Direction.Down, Lane.Right);
        drawTrafficLight(g, 255, 297, TRAFFIC_LIGHT_SIZE, upTrafficLight.getColor());
        drawTrafficLight(g, 283, 297, TRAFFIC_LIGHT_SIZE, downTrafficLight.getColor());

        //draw cars
        //for(VehicleDTO vehicle : worldState.getVehicleList().getAll())
           // drawVehicle(g, vehicle, worldState.getRoadMap());
     */
    }

    /*
    This method is for drawing road_network
    It gets information from worldState
    It will be used by paint
     */
    public void draw_road_network(Graphics g) {

        int location[][]={
                {300,300}, {300,650}, {650,300}
        };

        MapDTO mapDTO = worldState.getRoadMap();
        List<JunctionDTO> junctionList = mapDTO.getJunctions();
        //List<RoadDTO> roadList = mapDTO.getRoads();

        for (int i = 0; i < junctionList.size(); i++) {
            JunctionDTO junctionDTO = junctionList.get(i);
            List<Direction> directionList = junctionDTO.getConnectedDirections();
            List<Pair<RoadDTO,Direction>> roads = junctionDTO.getRoadsAndDirections();
            //System.out.println("Juction "+i+": " +roads.size());
            System.out.println("Directions of junction" + i + ": ");
            for(int ii=0;ii<directionList.size();ii++){
                System.out.println(directionList.get(ii));

                RoadDTO roadDTO = junctionDTO.getRoad(directionList.get(ii));
                draw_road(junctionDTO, roadDTO, directionList.get(ii), g,location[i][0],location[i][1]);
            }


//            for(int j=0; j<roads.size(); j++){
//                Pair<RoadDTO, Direction> road = roads.get(i);
//
//                RoadDTO roadDTO = road.getItem1();
//                Direction direction = road.getItem2();
//                System.out.println("Juction" +i+ ": Road" +j+ "- direction" +direction + " - location: "+location[i][0]+","+location[i][1]);
//                draw_road(junctionDTO, roadDTO, direction, g, location[i][0], location[i][1]);
//            }
        }
    }

    public void draw_road(JunctionDTO junctionDTO, RoadDTO road, Direction direction, Graphics g, int x, int y){
        g.setColor(ROAD_COLOR);
        //RoadDTO road=junctionDTO.getRoad(direction);
        switch(direction){
            case Up:
                int upx=x;
                int upy=(int )(y-road.getLength());
                g.fillRect(upx,upy,ROAD_WIDTH,(int)road.getLength());
                draw_line(upx + 25, upy, upx + 25, upy + (int) road.getLength(), g);
                break;
            case Down:
                int down_x=x;
                int down_y=y+ROAD_WIDTH;
                g.fillRect(down_x,down_y,ROAD_WIDTH,(int)road.getLength());
                draw_line(down_x + 25, down_y, down_x + 25, down_y + (int) road.getLength(), g);
                break;
            case Right:
                int right_x=x+ROAD_WIDTH;
                int right_y=y;
                g.fillRect(right_x,right_y,(int)road.getLength(),ROAD_WIDTH);
                draw_line(right_x, right_y + 25, right_x + (int) road.getLength(), right_y + 25, g);
                break;
            case Left:
                int left_x=(int )(x-road.getLength());
                int left_y=y;
                g.fillRect(left_x,left_y,(int)road.getLength(),ROAD_WIDTH);
                draw_line(left_x, left_y + 25, left_x + (int) road.getLength(), left_y + 25, g);
                break;

        }
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

}
