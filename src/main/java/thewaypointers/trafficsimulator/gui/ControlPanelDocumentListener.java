package thewaypointers.trafficsimulator.gui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ControlPanelDocumentListener implements DocumentListener {

    private JTextField textField;
    private ControlPanel controlPanel;

    ControlPanelDocumentListener(JTextField t, ControlPanel cp) {
        textField = t;
        controlPanel = cp;
    }


    @Override
    public void insertUpdate(DocumentEvent e) {

        int cautionCar = Integer.parseInt(controlPanel.cautionCarsNumberTextField.getText());
        int normalCar = Integer.parseInt(controlPanel.normalCarsNumberTextField.getText());
        int reckCar = Integer.parseInt(controlPanel.reckCarsNumberTextField.getText());
        //int bus = Integer.parseInt(controlPanel.busNumberTextField.getText());
        //int ambulance = Integer.parseInt(controlPanel.ambulanceTextField.getText());

        //int input = Integer.parseInt(textField.getText());
        int total = cautionCar + normalCar + reckCar;
        //+ bus + ambulance;
        //int diffrence = 100 - total;

        if(total > 100){
            //int output = input + diffrence;
            new JumpOutDialog("The total percentage is greater than 100!");
            //textField.setText(String.valueOf(output));
        }




//        if (this.textField.equals(controlPanel.cautionCarsNumberTextField)) {
//            normalCar = normalCar + diffrence;
//            if (normalCar>=0){
//                controlPanel.normalCarsNumberTextField.setText(String.valueOf(normalCar));
//            }
//            else {
//                cautionCar = cautionCar + normalCar;
//                controlPanel.normalCarsNumberTextField.setText(String.valueOf(0));
//                controlPanel.cautionCarsNumberTextField.setText(String.valueOf(cautionCar));
//            }
//
//        } else if (this.textField.equals(controlPanel.normalCarsNumberTextField)) {
//            reckCar = reckCar + diffrence;
//
//            if(reckCar>=0) {
//                controlPanel.reckCarsNumberTextField.setText(String.valueOf(reckCar));
//            }
//            else {
//                normalCar = normalCar + reckCar;
//                controlPanel.reckCarsNumberTextField.setText(String.valueOf(0));
//                controlPanel.normalCarsNumberTextField.setText(String.valueOf(normalCar));
//            }
//        }
//        else if (this.textField.equals(controlPanel.reckCarsNumberTextField)){
//            bus = bus + diffrence;
//
//            if(bus>=0){
//                controlPanel.busNumberTextField.setText(String.valueOf(bus));
//            }
//            else{
//                reckCar = reckCar + bus;
//                controlPanel.busNumberTextField.setText(String.valueOf(0));
//                controlPanel.reckCarsNumberTextField.setText(String.valueOf(reckCar));
//            }
//        }
//        else if (this.textField.equals(controlPanel.busNumberTextField)){
//
//            ambulance = ambulance + diffrence;
//
//            if(ambulance>=0) {
//                controlPanel.ambulanceTextField.setText(String.valueOf(ambulance));
//            }
//            else {
//                bus = bus + ambulance;
//                controlPanel.ambulanceTextField.setText(String.valueOf(0));
//                controlPanel.busNumberTextField.setText(String.valueOf(bus));
//            }
//        }
//        else if (this.textField.equals(controlPanel.ambulanceTextField)){
//            cautionCar = cautionCar + diffrence;
//
//            if(cautionCar>=0) {
//                controlPanel.cautionCarsNumberTextField.setText(String.valueOf(cautionCar));
//            }
//            else {
//                ambulance = ambulance + cautionCar;
//                controlPanel.cautionCarsNumberTextField.setText(String.valueOf(0));
//                controlPanel.ambulanceTextField.setText(String.valueOf(ambulance));
//            }
//        }

    }

    @Override
    public void removeUpdate(DocumentEvent e) {

    }

    @Override
    public void changedUpdate(DocumentEvent e) {

    }
}
