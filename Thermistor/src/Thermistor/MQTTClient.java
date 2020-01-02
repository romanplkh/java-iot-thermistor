/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Thermistor;

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

    String topic;
    int qos;
    String broker;
    MqttClient client;

    /**
     *
     * @param topic - topic to subscribe
     * @param qualityOfService 1 - guaranteed delivery at least once, 2 -
     * guaranteed delivery exactly once, 0 - guaranteed delivery at most once
     * @param brokerAddress - address of broker and port
     */
    public MQTTClient(String topic, int qualityOfService, String brokerAddress) {
        this.topic = topic;
        this.qos = qualityOfService;
        this.broker = brokerAddress;
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

            if (this.client.isConnected()) {
                this.client.subscribe(topic);
            } else {
                System.out.println("Client is not connected....");
            }

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
