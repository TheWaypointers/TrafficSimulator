package thewaypointers.trafficsimulator.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.Vector;

public class StatisticsPanel extends JPanel {

    public JLabel title;
    public static JTable table;
    private DefaultTableModel tableModel;

    public StatisticsPanel() {
        title = new JLabel();
        initJLabel(title);
        title.setText("Real-time Situation:");
        title.setLocation(5, 0);

        table = new JTable();
        initJTabel(table);

        tableModel = (DefaultTableModel)table.getModel();
        tableModel.setRowCount(0);

        this.setBackground(Color.white);
        this.setLayout(null);
        this.add(title);

    }

    public void addRow(double cautionCarSpeed, double normalCarSpeed, double recklessCarSpeed) {
        Vector v = new Vector();
        v.add(0, "Average speed");
        v.add(1, cautionCarSpeed);
        v.add(2, normalCarSpeed);
        v.add(3, recklessCarSpeed);
        if(tableModel.getRowCount()!=0) {
            tableModel.removeRow(0);
        }
        tableModel.addRow(v);
        table.invalidate();
    }

    private void initJLabel(JLabel label) {

        label.setSize(200, 20);
        label.setBackground(null);
        label.setVisible(true);
        label.setFont(new Font("Arial", 0, 14));

    }

    private void initJTabel(JTable table) {

        String[] columnNames = {"Vehicle Type", "Caution Car", "Normal Car", "Reckless Car"};
        Object[][] obj = {null, null, null, null};
//                Object[][] obj = new Object[1][4];
//        for (int i = 0; i < 4; i++) {
//            switch (i) {
//                case 0:
//                    obj[0][i] = "Average Speed";
//                    break;
//                case 1:
//                    obj[0][i] = "1";
//                    break;
//                case 2:
//                    obj[0][i] = "1";
//                    break;
//                case 3:
//                    obj[0][i] = "1";
//                    break;
//            }
//        }
        table.setModel(new DefaultTableModel(obj, columnNames) {
            Class[] types = new Class[]{
                    java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean[]{
                    false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        //table = new JTable(obj, columnNames);
//        table.setLocation(5,30);
//        table.setVisible(true);

        TableColumn column = null;
        int colunms = table.getColumnCount();
        for (int i = 0; i < colunms; i++) {
            column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(120);
        }
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(table);
        scroll.setSize(500, 50);
        scroll.setLocation(5, 20);
        this.add(scroll);

    }

}
