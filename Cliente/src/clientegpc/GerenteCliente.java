
package clientegpc;

import java.util.ArrayList;

public class GerenteCliente implements Runnable {

   public GerenteCliente() { }
   
   private static int[] listaDeIds;
   
   public void run() {      
      while(true) {
         ClienteGPC.servicoClientesConectados();
         for(int count=0; count < listaDeIds.length; count++){
             ClienteGPC.servicoRequisitarApelido(count);
         }
         try {
            Thread.sleep(60000);
         } catch (InterruptedException ex) {
             System.out.println(ex);
         }
      }

   }


}
