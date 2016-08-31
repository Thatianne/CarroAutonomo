/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 *
 * @author Thatianne e Macaule
 */
public class ControllerRecebe {
    
    private final String grupo = "239.0.0.1";
    private final int porta = 5000;
    
    public void recebe(){
        while(true){            
            String msg = null;
        
        try {
                
                MulticastSocket ms = new MulticastSocket(5000);
                InetAddress grp = InetAddress.getByName(this.grupo);
                ms.joinGroup(grp);
                                
                byte[] rec = new byte[256];
                DatagramPacket dp = new DatagramPacket(rec, rec.length);
                ms.receive(dp);
                msg = new String(dp.getData());
                //Recebe hora, origem, destino - "HH:mm:ss Norte Sul" até terminar de atravesar a via (msg).
                
                
                
                
                System.out.println("Mensagem recebida: "+msg);
                                
            } catch (IOException ex) {//MulticastSocket
                System.out.println("Erro ao criar o MulticastSocket");                                
            }catch(Exception e){
                System.out.println("Erro: "+e.getMessage());
            }
               
            
        }
    }
    
}
