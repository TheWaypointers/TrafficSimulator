package thewaypointers.trafficsimulator.gui;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;

public class JumpOutDialog extends JDialog {
    private JPanel panel;
    private JLabel infoLable;
    private JButton confirmButton;
    private String context;

    public JumpOutDialog(String context){
        this.context = context;
        initGUI();
    }

    public JumpOutDialog(JFrame frame, String context) {
        super(frame);
        this.context=context;
        initGUI();
    }

    private void initGUI() {
        //this.setLayout(null);
        this.setVisible(true);
        try {
            {
                panel = new JPanel();
                getContentPane().add(panel, BorderLayout.CENTER);
                panel.setLayout(null);
                {
                    infoLable = new JLabel();
                    panel.add(infoLable);
                    infoLable.setLocation(20, 40);
                    infoLable.setSize(260, 60);
                    infoLable.setText(context);
                    infoLable.setFont(new java.awt.Font("Avial", 0, 16));
                    infoLable.setLocale(new java.util.Locale("zh"));
                }
                {
                    confirmButton = new JButton();
                    panel.add(confirmButton);
                    confirmButton.setVisible(true);
                    confirmButton.setText("OK");
                    confirmButton.setLocation(100,120);
                    confirmButton.setSize(100,30);
                    confirmButton.setFont(new java.awt.Font("Avial", 0, 14));
                    confirmButton.addActionListener(new CloseListener());
                }
            }
            this.setSize(300, 200);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    class CloseListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent arg0) {
            // TODO Auto-generated method stub
            setVisible(false);

        }

    }


}
