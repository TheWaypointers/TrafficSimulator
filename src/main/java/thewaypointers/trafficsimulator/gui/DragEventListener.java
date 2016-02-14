package thewaypointers.trafficsimulator.gui;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;

public class DragEventListener implements MouseInputListener {
    Point origin;
    private JPanel panel;

    public DragEventListener(JPanel panel) {
        this.panel = panel;
        origin = new Point();
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
        int x = p.x + (e.getX() - origin.x);
        int y = p.y + (e.getY() - origin.y);

        if((x>0) || (y>0)){
            panel.setLocation(0,0);
        }

        //if the size of Map is 1000*1000
        // will be set as global variable later
        else if((x<-100) || (y<-100)){
            panel.setLocation(-100,-100);
        }
        else {
            panel.setLocation(
                    p.x + (e.getX() - origin.x),
                    p.y + (e.getY() - origin.y));
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {}

    private boolean move(){

        return true;
    }


}
