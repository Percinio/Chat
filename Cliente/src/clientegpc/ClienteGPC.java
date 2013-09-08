/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clientegpc;

import visualisacao.TelaAbertura;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;  
import java.io.OutputStream;
import java.io.PrintStream;  
import java.net.Socket;
import visualisacao.TelaPrincipal;

/**
 *
 * @author Gabriel
 */
public class ClienteGPC {
    private static TelaAbertura telaAbertura = new TelaAbertura();
    private static TelaPrincipal telaPrincipal = new TelaPrincipal();
    private static String endereco;
    private static String porta;
    private static String apelido;
    private static Socket socketCliente;  
    private static DataInputStream input;  
    private static DataOutputStream output; 
    
    
    private static void abreTelaDeAbertura(){     
        telaAbertura.setVisible(true);
    }
    private static void fechaTelaDeAbertura(){     
        telaAbertura.setVisible(false);
    }
    private static void abreTelaPrincipal(){     
        telaPrincipal.setVisible(true);
    }
    private static void fechaTelaPrincipal(){     
        telaPrincipal.setVisible(false);
    }
    
    public static void main(String[] args) {
       abreTelaDeAbertura();
    }
    
    public static void conecta(){
        try{                
            socketCliente = new Socket(endereco,Integer.parseInt(porta));               
            input = new DataInputStream(socketCliente.getInputStream());  
            output = new DataOutputStream(socketCliente.getOutputStream());     
            fechaTelaDeAbertura();
            abreTelaPrincipal();
        }catch(IOException e){  
            System.out.println(e);
            System.out.println("Algum problema ocorreu ao criar ou enviar dados pelo socket.");        
        }
    }
    public static void setApelido(String Apelido) {
        System.out.println(Apelido);
        ClienteGPC.apelido = Apelido;
    }

    public static void setEndereco(String Endereco) {
        System.out.println(Endereco);
        ClienteGPC.endereco = Endereco;
    }

    public static void setPorta(String Porta) {
        System.out.println(Porta);
        ClienteGPC.porta = Porta;
    }
    
}
