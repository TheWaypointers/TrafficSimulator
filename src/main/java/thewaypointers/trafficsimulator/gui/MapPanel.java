package thewaypointers.trafficsimulator.gui;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

public class MapPanel extends JPanel{

   // WorldStateDTO worldState = new WorldState();

    public MapPanel(){
        this.setVisible(true);
        this.setSize(600,600);
        this.setBackground(Color.white);

    }


//    private void getWorldState(){
//
//        worldState = new IStateChangeListener.NewStateReceived;
//
//    }

    //draw worldState
    public void paint(Graphics g){
        super.paint(g);

        //draw road
        g.setColor(Color.gray);

        g.fillRect(250,0,50,600);
        Graphics2D  g2=(Graphics2D)g;
        g2.setColor(Color.white);
        float [] arr={15.0f,10.0f};
        BasicStroke stroke = new BasicStroke(1,BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1.0f,arr,0);
        g2.setStroke(stroke);
        Line2D.Float line = new Line2D.Float(275,0,275,600);
        g2.draw(line);
        BasicStroke stroke2=new BasicStroke();
        g2.setStroke(stroke2);
    }



    //1.draw vehicles
//    public void drawVehicles(int x, int type, Graphics g){
//        //what type
//        switch(type){
//            case 0:
//                //bus
//                g.setColor(Color.red);
//                g.fillRect(10,10,2,1);
//                break;
//            case 1:
//                //car
//                g.getColor(Color.black);
//                g.fillRect(10,20,2,1);
//        }
//    }






}
