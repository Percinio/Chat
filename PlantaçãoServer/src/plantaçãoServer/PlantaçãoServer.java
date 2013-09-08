/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plantaçãoServer;

import javax.swing.JFrame;

/**
 *
 * @author JoãoVictor
 */
public class PlantaçãoServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Server sally = new Server();
        sally.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sally.startRunning();
    }
}
