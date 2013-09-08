/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plantaçãoclient;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author JoãoVictor
 */
public class Client extends JFrame {

    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String serverIP;
    private Socket connection;

    //construtor
    public Client(String host) {
        super("Chat Client");
        serverIP = host;
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage(e.getActionCommand());
                userText.setText("");
            }
        });
        add(userText, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        setSize(800, 600);
        setVisible(true);
    }

    //connect to server
    public void startRunning() {
        try {
            connectToServer();
            setupStreams();
            whileChatting();
        } catch (EOFException eof) {
            showMessage("\n Client terminated connection");
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            closeCrap();
        }
    }

    //connect to server
    private void connectToServer() throws IOException {
        showMessage("Attempting connection...\n");
        connection = new Socket(InetAddress.getByName(serverIP), 8885);
        showMessage("Connected to: " + connection.getInetAddress().getHostName());
    }

    //set up streams to send and receive messages
    private void setupStreams() throws IOException {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n DUde your streams are now good to go!\n ");
    }

    //while chatting with server
    private void whileChatting() throws IOException {
        ableToType(true);
        do {
            try {
                message = (String) input.readObject();
                showMessage("\n" + message);
            } catch (ClassNotFoundException cnfe) {
                showMessage("\n I dont know that object type");
            }
        } while (!message.equals("SERVER - END"));
    }

    //close the streams and scokets
    private void closeCrap() {
        showMessage("\n closing crap down");
        ableToType(false);
        try {
            output.close();
            input.close();
            connection.close();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    //send messages to server
    private void sendMessage(String message) {
        try {
            output.writeObject(connection.getLocalAddress().getHostName()+ " - " + message);
            output.flush();
            showMessage("\n" + connection.getLocalAddress().getHostName() + " - " + message);
        } catch (IOException io) {
            chatWindow.append("\n something messed up sending message hoss!");
        }
    }

    //change/update chatWindow
    private void showMessage(final String message) {
        SwingUtilities.invokeLater(
                new Runnable() {
            @Override
            public void run() {
                chatWindow.append(message);
            }
        });


    }

    //gives user permission to type crap into the text box
    private void ableToType(final boolean tof) {
        SwingUtilities.invokeLater(
                new Runnable() {
            @Override
            public void run() {
                userText.setEditable(tof);
            }
        });

    }
}
