/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irgraph;

/**
 *
 * @author FA
 */
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller {

    private View theView;
    private static Model theModel;
    private SerialPort serialPort;
    private static final int TIME_OUT = 2000;
    private static final int DATA_RATE = 9600;
    private BufferedReader input;
    private OutputStream output;

    public Controller(Model theModel, View theView) {
        this.theModel = theModel;
        this.theView = theView;
        this.theView.connectListener(new Connect());
        //this.theView.populatePortList(new PortList());
        this.theView.clearListener(new ClearChart());
        updateChart();
    }

    public Model getTheModel() {
        return theModel;
    }

    public void updateChart() {
        theView.getWindow().add(theModel.getChart(), BorderLayout.CENTER);
    }

    class Connect implements ActionListener, SerialPortEventListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (theView.getConnectButton().getText().equals("Connect")) {
                // attempt to connect to serial port
                System.setProperty("gnu.io.rxtx.SerialPorts", theView.getPortList().getSelectedItem().toString());
                CommPortIdentifier portId = null;
                Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
                //First, Find an instance of serial port as set in PORT_NAMES.
                while (portEnum.hasMoreElements()) {
                    CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
                    if (currPortId.getName().equals(theView.getPortList().getSelectedItem().toString())) {
                        portId = currPortId;
                        System.out.println("Connected on port " + portId.getName());
                        break;
                    }
                }
                if (portId == null) {
                    System.out.println("Could not find COM port.");
                    return;
                }
                try {
                    serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);
                    // set port parameters
                    serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

                    // open the streams
                    input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
                    output = serialPort.getOutputStream();
                    // add event listeners
                    serialPort.addEventListener(this);// i have to check this two methods
                    serialPort.notifyOnDataAvailable(true);
                    // if port is opened 
                    theView.getConnectButton().setText("Disconnect");
                    theView.getPortList().setEnabled(false);

                } catch (PortInUseException | UnsupportedCommOperationException | IOException | TooManyListenersException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                // disconnect from serial port
                serialPort.close();
                theView.getConnectButton().setText("Connect");
                theView.getPortList().setEnabled(true);
                theModel.getSeries().clear();
                theModel.setIndex(0);
            }
        }

        @Override
        public synchronized void serialEvent(SerialPortEvent oEvent) {
            if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
                try {
                    String inputLine = input.readLine();
                    System.out.println(inputLine);
                    theModel.parseDiscrete(inputLine);
                    theView.getWindow().repaint();
                } catch (Exception e) {
                    System.err.println(e.toString());
                }
            }

        }
    }

    class ClearChart implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            theModel.getSeries().clear();
        }
    }

}

/* class PortList implements ActionListener {

 @Override
 public void actionPerformed(ActionEvent e) {
 // Use only once portId to save some code...
 // populate the drop-down box
 Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
 while (portEnum.hasMoreElements()) {
 CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
 System.out.println("Open ports " + currPortId.getName());//DEBUG
 theView.getPortList().addItem(currPortId.getName());
 }
 }

 }*/
