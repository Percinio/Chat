/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clientegpc;

import visualizacao.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;   
import java.net.Socket;

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
    
    public static int checkSum(int servico, String dados) throws IOException {
       
        int checkSum=0;
        int tamanhoDados;
        int dadosProcessados = 0;
        
        tamanhoDados = dados.length() * 2;
        
        output.writeByte(servico);
        System.out.println(servico);
        output.writeByte(tamanhoDados);
        System.out.println(tamanhoDados);
        for (int count =0 ; count < dados.length(); count++) {
            dadosProcessados += dados.charAt(count);
            output.writeChar(dados.charAt(count));
            System.out.println("Dado" + count + " :" + dados.charAt(count));
            //System.out.println("Dado" + count + " :" + Integer.toHexString(dados.charAt(count)));
        }
        
        checkSum += servico;
        checkSum += tamanhoDados;
        checkSum += dadosProcessados;
        
        output.writeByte(checkSum);
        System.out.println(Integer.toHexString(checkSum));  

        return checkSum;
    }

    
    public static void conecta(){
        try{                
            socketCliente = new Socket(endereco,Integer.parseInt(porta));
            input = new DataInputStream(socketCliente.getInputStream());  
            output = new DataOutputStream(socketCliente.getOutputStream());     
            fechaTelaDeAbertura();
            abreTelaPrincipal();
            servicoOla(apelido);
        }catch(IOException e){  
            System.out.println(e);
            System.out.println("Algum problema ocorreu ao criar ou enviar dados pelo socket.");        
        }
    }
    
    public static void servicoOla(String dadosApelido) {
        try {
            checkSum(1, dadosApelido);
        } catch (IOException ex) {
            System.out.println("Não foi possível mandar ola");
            System.out.println(ex);
        }
    }
    
     public static void servicoTrocaApelido(String dadosApelido)throws IOException {
        try {
            checkSum(2, dadosApelido);
        } catch (IOException ex) {
        }
    }
    
    public static void setApelido(String Apelido) {
        ClienteGPC.apelido = Apelido;
    }

    public static void setEndereco(String Endereco) {
        ClienteGPC.endereco = Endereco;
    }

    public static void setPorta(String Porta) {
        ClienteGPC.porta = Porta;
    }
    
}
