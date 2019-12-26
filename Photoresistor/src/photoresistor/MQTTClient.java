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
    String clientId = "";
    MemoryPersistence persistence = new MemoryPersistence();

    public void sendMessage(String msg) {

        String content = msg;
        try {
            MqttClient client = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setAutomaticReconnect(true);
            connOpts.setKeepAliveInterval(15);
            connOpts.setConnectionTimeout(30);

            clientId = client.generateClientId();

            //System.out.println("Connecting to broker: " + broker);
            //CONNECT TO BROKER
            client.connect(connOpts);
            // System.out.println("Connected");
            //System.out.println("Publishing message: " + content);

            //SUBSCRIBE TO BROKER
            client.subscribe(topic);

            //PREPARE MESSAGE
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);

            if (client.isConnected()) {
                //SEND MESSAGE
                client.publish(topic, message);
            }

            // System.out.println("Message published");
            //DISCONNECT 
            // client.disconnect();
            //System.out.println("Disconnected");
            //System.exit(0);
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
