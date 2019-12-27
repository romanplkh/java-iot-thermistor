/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photoresistor;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 *
 * @author Roman
 */
public class MQTTClient {

    String topic = "lightdata";
    int qos = 2;
    String broker = "tcp://127.0.0.1:1883";

    MqttClient client;

    
    //!TODO: DYNAMICALLY PSS VALUES TO CONSTRUCTOR
    public MQTTClient() {
        this.configureClient();
    }

    private void configureClient() {

        try {

            MqttConnectOptions connOpts = new MqttConnectOptions();
            MemoryPersistence persistence = new MemoryPersistence();

            connOpts.setCleanSession(true);
            connOpts.setAutomaticReconnect(true);
            connOpts.setKeepAliveInterval(15);
            connOpts.setConnectionTimeout(30);

            this.client = new MqttClient(broker, "javaSensorReader", persistence);

            //CONNECT TO BROKER
            this.client.connect(connOpts);
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }

    }

    public void subscribeClient() {
        try {
            //SUBSCRIBE TO BROKER
            this.client.subscribe(topic);
        } catch (MqttException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void sendMessage(String msg) {

        String content = msg;
        try {

            //PREPARE MESSAGE
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);

            if (this.client.isConnected()) {
                //SEND MESSAGE
                client.publish(topic, message);
            }

            //DISCONNECT 
            // client.disconnect();
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();

        }
    }

}
