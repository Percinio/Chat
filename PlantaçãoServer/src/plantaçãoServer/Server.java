/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plantaçãoServer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
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
public class Server extends JFrame {

    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;
    private int PORTA = 8885;

    //construtor
    public Server() {
        super("Chat Server");
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
        add(new JScrollPane(chatWindow));
        setSize(800, 600);
        setVisible(true);
    }

    //criar e executar o server
    public void startRunning(int PORTA) {
        try {
            server = new ServerSocket(PORTA);
            while (true) {
                try {
                    waitForConnection();
                    setupStreams();
                    whileChatting();
                } catch (EOFException eof) {
                    showMessage("\n Connection Closed");
                } finally {
                    closeCrap();
                }
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public void startRunning() {
        try {
            server = new ServerSocket(8885);
            while (true) {
                try {
                    waitForConnection();
                    setupStreams();
                    whileChatting();
                } catch (EOFException eof) {
                    showMessage("\n Connection Closed");
                } finally {
                    closeCrap();
                }

            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    //esperar pela conexão,  então mostra info da conexão
    private void waitForConnection() throws IOException {
        showMessage("Waiting for someone to connect... \n");
        connection = server.accept();
        showMessage("\nNow connected to " + connection.getInetAddress().getHostName());
    }
    //get stream to send and receive data

    private void setupStreams() throws IOException {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n Streams are now setup\n");
    }

    //during the chat conversation
    private void whileChatting() throws IOException {
        String message = "You are now connected!";
        int servico;
        int numDados;
        String messagem;
        int checksum; 
        sendMessage(message);
        ableToType(true);
        do {
            //have a conversation
            try {
                message = (String) input.readObject();                
                showMessage("\n" + message);
            } catch (ClassNotFoundException cnfe) {
                showMessage("\n idk wtf that user sent!");
            }
        } while (!message.equals(connection.getInetAddress().getHostName()+" - END"));
    }

    //close streams and scckets after you are done chatting
    private void closeCrap() {
        showMessage("\n Closing connections\n");
        ableToType(false);
        try {
            output.close();
            input.close();
            connection.close();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    //send a message to client
    private void sendMessage(String message) {
        try {
            output.writeObject("SERVER - " + message);
            output.flush();
            showMessage("\n SERVER - " + message);
        } catch (IOException io) {
            chatWindow.append("\n ERROR: DUDE I CANT SEND THAT MESSAGE");

        }

    }

    //updates chatWindow
    private void showMessage(final String text) {
        SwingUtilities.invokeLater(
                new Runnable() {
            @Override
            public void run() {
                chatWindow.append(text);
            }
        });
    }

    //let the user type stuff into their box
    private void ableToType(final boolean tof) {
        SwingUtilities.invokeLater(
                new Runnable() {
            @Override
            public void run() {
                userText.setEditable(tof);
            }
        });

    }
    
    public static int checkSum(int servico, String dados, ObjectOutputStream output) throws IOException {
        int resultado = 0;
        int count = 0;

        output.writeByte(servico);
        resultado += servico;
        int ndedados = dados.length() * 2;

        output.writeByte(ndedados);
        resultado += ndedados;

        ndedados = 0;

        for (count = 0; count < dados.length(); count++) {
            ndedados += dados.charAt(count);
            output.writeChar(dados.charAt(count));
            System.out.println("Dado" + count + " :" + Integer.toHexString(dados.charAt(count)));
        }


        resultado += ndedados;

        System.out.println(Integer.toHexString(resultado));

        output.writeByte(resultado);
        return resultado;
    }
}
