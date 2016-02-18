package thewaypointers.trafficsimulator.gui;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;

public class PanelMouseDragger implements MouseInputListener {
    Point origin;
    private JPanel panel;
     //MapContainerPanel mapContainerPanel;
    private int panel_X;
    private int panel_Y;

    public PanelMouseDragger(JPanel panel) {

        this.panel = panel;

        origin = new Point();
        panel_X = panel.getWidth();
        panel_Y = panel.getHeight();
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    //record the location of mouse
    @Override
    public void mousePressed(MouseEvent e) {
        origin.x = e.getX();
        origin.y = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {}


    @Override
    public void mouseEntered(MouseEvent e) {
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
    }


    @Override
    public void mouseExited(MouseEvent e) {
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    //define new location
    @Override
    public void mouseDragged(MouseEvent e) {
        Point p = panel.getLocation();

       // int range_X = mapContainerPanel.getWidth()- panel_X;
       // int range_Y = mapContainerPanel.getHeight() - panel_Y;

        int range_X = 600 - panel_X;
        int range_Y = 600 - panel_Y;




        int x = p.x + (e.getX() - origin.x);
        int y = p.y + (e.getY() - origin.y);

        if(x<range_X){
            if (y<range_Y){
            panel.setLocation(range_X,range_Y);
        }
            else if((y>=range_Y) && (y<=0)){
            panel.setLocation(range_X,y);
        }
            else if(y>0){
            panel.setLocation(range_X,0);
            }
        }
        else if((x>=range_X)&&(x<=0)){
            if (y<range_Y){
                panel.setLocation(x,range_Y);
            }
            else if((y>=range_Y) && (y<=0)){
                panel.setLocation(x,y);
            }
            else if(y>0){
                panel.setLocation(x,0);
            }
        }
       else{
            if (y<range_Y){
                panel.setLocation(0,range_Y);
            }
            else if((y>=range_Y) && (y<=0)){
                panel.setLocation(0,y);
            }
            else if(y>0){
                panel.setLocation(0,0);
            }
        }


    }

    @Override
    public void mouseMoved(MouseEvent e) {}

    private boolean move(){

        return true;
    }


}
