/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plantaçãoclient;

import javax.swing.JOptionPane;

/**
 *
 * @author JoãoVictor
 */
public class PlantaçãoClient {

    /**
     * @param args the command line arguments
     */
    
    private static String serverIP;
    
    public static void main(String[] args) {
        serverIP = JOptionPane.showInputDialog("Ip do Servidor");
        Thread tr = new Thread(new ClientThread(serverIP));
        tr.start();
    }
}
