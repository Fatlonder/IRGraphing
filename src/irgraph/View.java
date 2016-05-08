/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irgraph;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import gnu.io.*;
import java.awt.event.ActionListener;
import java.util.Enumeration;

/**
 *
 * @author ATQU
 */
public class View extends JFrame {

    /**
     * @param args the command line arguments
     *
     */
    private JFrame window = new JFrame();
    private JComboBox<String> portList = new JComboBox<String>();
    private JButton connectButton = new JButton("Connect");
    private Model theModel = new Model();

    View() {
        JPanel topPanel = new JPanel();
        window.setTitle("IR Discrete Values");
        window.setSize(600, 400);
        window.setLayout(new BorderLayout());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        topPanel.add(portList);
        topPanel.add(connectButton);
        window.add(topPanel, BorderLayout.NORTH);

        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            String currPort = currPortId.getName();
            portList.addItem(currPort);

        }
        //dataset.addSeries(series);
        //model.getDataset().addSeries(model.getSeries());
        //JFreeChart chart = ChartFactory.createXYStepChart("IR Data", "Time (ms)", "Bit", theModel.dataset());
        //window.add(new ChartPanel(chart), BorderLayout.CENTER);

        // configure the connect button 
        // Listener for array size
        //show the window
        window.setVisible(true);

    }

    public JFrame getWindow() {
        return window;
    }

    public JComboBox<String> getPortList() {
        return portList;
    }

    public void connectListener(ActionListener ListenForButton) {
        connectButton.addActionListener(ListenForButton);
    }

    /* public void populatePortList(ActionListener ListenForButton) {
     portList.addActionListener(ListenForButton);
     }*/
    public JButton getConnectButton() {
        return connectButton;
    }


}
