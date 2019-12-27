/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photoresistor;

import com.fazecast.jSerialComm.SerialPort;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Roman Pelikh
 */
public class Photoresistor {

    static SerialPort selectedPort;
    //AXYS X will shift by 1 each time 
    static int i = 0;

    static JFrame application;
    static JComboBox<String> portList;
    static XYSeries coords;
    static JButton connectionButton;
    static MQTTClient client;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        buildMainForm();
        populateCOMPorts();
        buildChart();
        connectToDataSource();
    }

    /**
     * CREATES MAIN APPLICATION WINDOW AND CONFIGURES IT
     */
    private static void buildMainForm() {
        application = new JFrame();

        application.setTitle("Photoresistor chart");
        application.setSize(800, 800);
        application.setLayout(new BorderLayout());
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //dropdown and connect button 
        portList = new JComboBox<>();
        connectionButton = new JButton("Connect");

        JPanel topPanel = new JPanel();
        topPanel.add(portList);
        topPanel.add(connectionButton);
        application.add(topPanel, BorderLayout.NORTH);
    }

    /**
     * POPULATES DROPDOWN WITH SERIAL PORTS AVAILABLE ON PC
     */
    private static void populateCOMPorts() {
        //Populate dropdown with ports
        SerialPort[] portName = SerialPort.getCommPorts();

        //Loop though ports on PC and populate combo
        for (int i = 0; i < portName.length; i++) {
            portList.addItem(portName[i].getSystemPortName());
        }
    }

    /**
     * CREATES LINE CHART AND CONFIGURES IT
     */
    private static void buildChart() {
        //create line chart
        coords = new XYSeries("Light Sensor Data");

        XYSeriesCollection dataset = new XYSeriesCollection(coords);
        JFreeChart chart = ChartFactory.createXYLineChart("Light sensor readings", "Time (seconds)", "Photo intensity", dataset);
        application.add(new ChartPanel(chart), BorderLayout.CENTER);
        application.setVisible(true);
    }

    /**
     * STARTS NEW THREAD AND READS DATA COMMING FROM SERIAL PORT
     */
    private static void startReadingData() {

        //INITIATE MQTT CLIENT TO SEND DATA TO BROKER
        MQTTClient client = new MQTTClient();
        client.subscribeClient();

        //Create new thread that listens for incoming data and populates graph
        Thread thread = new Thread() {
            @Override
            public void run() {

                //Get data stream from port
                Scanner scanner = new Scanner(selectedPort.getInputStream());

                //UNCOMMENT FOR WEB SOCKET IMPLEMENTATION
                //DataSource ds = new DataSource();
                // CustomWebSocketServer wss = new CustomWebSocketServer(8000);
                //READ DATA 
                while (scanner.hasNextLine()) {
                    try {
                        //READ DATA
                        String line = scanner.nextLine();

                        //SHOW DATA IN CONSOLE
                        System.out.println(line);

                        //CONVERT DATA FROM SERIAL PORT 
                        int number = Integer.parseInt(line.trim());

                        //SEND DATA TO MQTT BROKER
                        client.sendMessage(line);

                        //UNCOMMENT FOR WEB SOCKET IMPLEMENTATION 
//                         ds.getData().add(String.valueOf(number));
//                        if (wss.sockets.size() > 0) {
//                            for (WebSocket ws : wss.sockets) {
//                                if (ws != null) {
//                                    ws.send(String.valueOf(number));
//                                }
//
//                            }
//                        }
                        coords.add(i++, number);
                        application.repaint();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println("ECXEPTION HAPPENED");
                        System.out.println(e.getMessage());
                    }

                }

                scanner.close();

            }

        };

        //RUN THREAD
        thread.start();
//
    }

    /**
     * CREATES ACTION LISTENER FOR BUTTON "CONNECT" AND SET APPROPRIATE
     * VISIBILITY FOR BUTTON BASED ON THE CONNECTION STATUS
     */
    private static void connectToDataSource() {
        connectionButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (connectionButton.getText().equalsIgnoreCase("connect")) {
                    //Connect to serial port 
                    selectedPort = SerialPort.getCommPort(portList.getSelectedItem().toString());
                    //Keep waiting connection
                    selectedPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
                    if (selectedPort.openPort()) {

                        //Change button text
                        connectionButton.setText("Disconnect");

                        //Disable dropdown
                        portList.setEnabled(false);

                    }

                    
                    startReadingData();

                } else {
                    //Disconnect from serial port
                    selectedPort.closePort();
                    //Change button text
                    connectionButton.setText("Connect");

                    //Disable dropdown
                    portList.setEnabled(true);

                    //Reset coordinates 
                    coords.clear();

                    //Reset seconds 
                    i = 0;

                }

            }
        });
    }

}
