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
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private static void abreTelaDeAbertura() {
        telaAbertura.setVisible(true);
    }

    private static void fechaTelaDeAbertura() {
        telaAbertura.setVisible(false);
    }

    private static void abreTelaPrincipal() {
        telaPrincipal.setVisible(true);
    }

    private static void fechaTelaPrincipal() {
        telaPrincipal.setVisible(false);
    }

    public static void main(String[] args) {
        abreTelaDeAbertura();
    }

    public static int checkSum(int servico, String dados) throws IOException {

        int checkSum = 0;
        int tamanhoDados;
        int dadosProcessados = 0;

        tamanhoDados = dados.length() * 2;

        output.writeByte(servico);
        System.out.println(servico);
        output.writeByte(tamanhoDados);
        System.out.println(tamanhoDados);
        for (int count = 0; count < dados.length(); count++) {
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

    public static void checkSumParaEnviarMensagem(int destinatario, String mensagem) {
        int tamanho = mensagem.length() * 2;
        try {
            output.writeByte(5);
            output.writeByte(tamanho);               
            output.writeByte(0);
            output.writeByte(destinatario);
            output.writeByte(5+tamanho);
        } catch (IOException ex) {
            Logger.getLogger(ClienteGPC.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
    
        public static void checkSumParaEnviarMensagem(String mensagem) {
        int tamanho = mensagem.length() * 2;
        try {
            output.writeByte(5);
            output.writeByte(tamanho);               
            output.writeByte(0);
            output.writeByte(0);
            output.writeByte(5+tamanho);
        } catch (IOException ex) {
            Logger.getLogger(ClienteGPC.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    public static int checkSumParaListaDeApelidos(int cliente) throws IOException {

        int checkSum = 0;
        int tamanhoDados;
        int dadosProcessados = 0;

        String strNum = String.valueOf(cliente);
        tamanhoDados = (strNum.length() * 4);

        output.writeByte(4);
        output.writeByte(tamanhoDados);

        for (int count = 0; count < strNum.length(); count++) {
            dadosProcessados += Integer.parseInt(String.valueOf(strNum.charAt(count)));
            output.writeInt(Integer.parseInt(String.valueOf(strNum.charAt(count))));
            //System.out.println("Dado" + count + " :" + Integer.toHexString(dados.charAt(count)));
        }

        checkSum += 4;
        checkSum += tamanhoDados;
        checkSum += dadosProcessados;

        output.writeByte(checkSum);

        return checkSum;
    }

    public static void checkSumParaPedidoDeClientesConectados() throws IOException {
        output.writeByte(3);

        output.writeByte(0);
        output.writeByte(0);

        output.writeByte(0);
        output.writeByte(3);
    }

    public static void checkSumParaDesconectar() throws IOException {
        output.writeByte(160);

        output.writeByte(0);
        output.writeByte(0);

        output.writeByte(0);
        output.writeByte(160);
    }

    public static void conecta() {
        try {
            socketCliente = new Socket(endereco, Integer.parseInt(porta));
            input = new DataInputStream(socketCliente.getInputStream());
            output = new DataOutputStream(socketCliente.getOutputStream());
            fechaTelaDeAbertura();
            abreTelaPrincipal();
            servicoOla(apelido);

            GerenteCliente gerente = new GerenteCliente();
            Thread threadGerente = new Thread(gerente);
            threadGerente.start();
        } catch (IOException e) {
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

    public static void servicoTrocaApelido(String dadosApelido) throws IOException {
        try {
            checkSum(2, dadosApelido);
        } catch (IOException ex) {
        }
    }

    public static void servicoClientesConectados() {
        try {
            checkSumParaPedidoDeClientesConectados();

        } catch (IOException ex) {
            System.out.println("Não foi possível descobrir os clientes conectados");
            System.out.println(ex);
        }
    }

    public static void servicoRequisitarApelido(int idCliente) {
        try {
            checkSumParaListaDeApelidos(idCliente);
        } catch (IOException ex) {
            System.out.println("Não foi possível descobrir os apelidos");
            System.out.println(ex);
        }
    }

    public static void servicoDesconectar() {
        try {
            checkSumParaDesconectar();
        } catch (IOException ex) {
            System.out.println("Não foi possível descobrir os apelidos");
            System.out.println(ex);
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
