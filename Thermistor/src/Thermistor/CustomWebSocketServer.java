/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Thermistor;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

/**
 *
 * @author Roman
 */
public class CustomWebSocketServer extends WebSocketServer {

    private DataSource ds;

    public List<WebSocket> sockets = new ArrayList<>();

    public CustomWebSocketServer(int port) {
        super(new InetSocketAddress(port));
        this.ds = new DataSource();

    }

    @Override
    public void onOpen(WebSocket ws, ClientHandshake ch) {

        sockets.add(ws);
        List<String> datas = ds.getData();

        System.out.println(datas.size());

        for (String num : datas) {
            ws.send(num);
        }

        System.out.println("Host String " + ws.getRemoteSocketAddress().getHostString());
        System.out.println("New connection from " + ws.getRemoteSocketAddress().getAddress().getHostAddress());

    }

    @Override
    public void onClose(WebSocket ws, int i, String string, boolean bln) {
        this.sockets.clear();
        System.out.println("Closed connection to " + ws.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onMessage(WebSocket ws, String message) {
        System.out.println("From client: " + message);
    }

    @Override
    public void onError(WebSocket ws, Exception excptn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
