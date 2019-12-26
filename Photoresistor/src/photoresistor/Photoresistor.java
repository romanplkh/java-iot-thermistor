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
 * @author Roman
 */
public class Photoresistor {

    static SerialPort chosenPort;
    //AXYS X will shift by 1 each time 
    static int i = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        JFrame application = new JFrame();

        application.setTitle("Photoresistor chart");
        application.setSize(600, 600);

        application.setLayout(new BorderLayout());

        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //dropdown and connect button 
        JComboBox<String> portList = new JComboBox<>();
        JButton connectionButton = new JButton("Connect");

        JPanel topPanel = new JPanel();
        topPanel.add(portList);
        topPanel.add(connectionButton);
        application.add(topPanel, BorderLayout.NORTH);

        //Populate dropdown with ports
        SerialPort[] portName = SerialPort.getCommPorts();

        //Loop though ports on PC and populate combo
        for (int i = 0; i < portName.length; i++) {
            portList.addItem(portName[i].getSystemPortName());
        }

        //create line chart
        XYSeries coords = new XYSeries("Light Sensor Data");

        XYSeriesCollection dataset = new XYSeriesCollection(coords);

        JFreeChart chart = ChartFactory.createXYLineChart("Light sensor readings", "Time (seconds)", "Photo intensity", dataset);

        application.add(new ChartPanel(chart), BorderLayout.CENTER);

        //show window 
        application.setVisible(true);

        DataSource ds = new DataSource();

        //Connect button logic
        connectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (connectionButton.getText().equalsIgnoreCase("connect")) {
                    //Connect to serial port 
                    chosenPort = SerialPort.getCommPort(portList.getSelectedItem().toString());
                    //Keep waiting connection
                    chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
                    if (chosenPort.openPort()) {

                        //Change button text
                        connectionButton.setText("Disconnect");

                        //Disable dropdown
                        portList.setEnabled(false);

                    }
                    
                     MQTTClient client = new MQTTClient();
                     
                     

                    //Create new thread that listens for incoming data and populates graph
                    Thread thread = new Thread() {
                        @Override
                        public void run() {

                            //Get data from SERIAL PORT AND STORE TO SCANNER
                            Scanner scanner = new Scanner(chosenPort.getInputStream());

                            //MyServer msr = new MyServer(8000);

                            //MQTT CLIENT 
                           

                            //msr.start();

                            while (scanner.hasNextLine()) {

                                try {
                                    String line = scanner.nextLine();

                                    System.out.println(line);

                                    //Get values from photoresistor
                                    int number = Integer.parseInt(line.trim());
                                    
                                    
                                    

                                    client.sendMessage(line);

                                    ds.getData().add(String.valueOf(number));

//                                    if (msr.sockets.size() > 0) {
//                                        for (WebSocket ws : msr.sockets) {
//                                            if (ws != null) {
//                                                ws.send(String.valueOf(number));
//                                            }
//
//                                        }
//                                    }

                                    //                                    double tempK = Math.log(10000.0 * ((1024.0 / number - 1)));
                                    //                                    tempK = 1 / (0.001129148 + (0.000234125 + (0.0000000876741 * tempK * tempK)) * tempK);       //  Temp Kelvin
                                    //                                    double tempC = tempK - 273.15;            // Convert Kelvin to Celcius
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

                } else {
                    //Disconnect from serial port

                    chosenPort.closePort();
                    //Change button text
                    connectionButton.setText("Connect");

                    //Disable dropdown
                    portList.setEnabled(true);

                    //REset coordinates 
                    coords.clear();

                    //Reset seconds 
                    i = 0;

                }

            }

        });

    }

}
