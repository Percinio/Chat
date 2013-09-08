/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clientegpc;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author JoãoVictor
 */
public class Checksum {

    public static int checkSum(int servico, String dados, DataOutputStream output) throws IOException {
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
