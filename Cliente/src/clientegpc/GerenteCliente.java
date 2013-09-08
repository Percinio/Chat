
package clientegpc;

public class GerenteCliente implements Runnable {

   public GerenteCliente() { }

   public void run() {      
      while(true) {
         ClienteGPC.servicoClientesConectados();
         try {
            Thread.sleep(60000);
         } catch (InterruptedException ex) {
             System.out.println(ex);
         }
      }

   }


}
