/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plantaçãoclient;

import javax.swing.JFrame;

/**
 *
 * @author JoãoVictor
 */
public class ClientThread implements Runnable{

    private String serverIP;
    
    //construtor
    public ClientThread(String serverIP) {
        this.serverIP = serverIP;
    }

    @Override
    public void run() {
        Client client = new Client(serverIP);
        client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.startRunning();
    }
}
