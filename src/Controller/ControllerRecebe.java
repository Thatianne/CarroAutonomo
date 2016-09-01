/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Via;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;

/**
 *
 * @author Thatianne e Macaule
 */
public class ControllerRecebe {
    
    private final String grupo = "239.0.0.1";
    private final int porta = 5000;
    
    private final Via origem;
    private final Via destino;
    private int preferencia;    
    private ArrayList<String> msgCarros;
    
    public ControllerRecebe(Via origem, Via destino, int pref, ArrayList<String> msgCarros){
        this.origem = origem;
        this.destino = destino;
        this.preferencia = pref;
        this.msgCarros = msgCarros;
    }
    
    public void recebe(){
        int qtdMsg = 0;
        String msg = null;
        //vai poder parar de esperar mensagens quando receber as mensagens dos outros 3 carros caso tenha
        //Ver casos que tenha menos de 3 carros, pensar em condição de parada (poderia ser pelo tempo)
        while(qtdMsg < 3){                        
        
        try {
                
                MulticastSocket ms = new MulticastSocket(5000);
                InetAddress grp = InetAddress.getByName(this.grupo);
                ms.joinGroup(grp);
                                
                byte[] rec = new byte[256];
                DatagramPacket dp = new DatagramPacket(rec, rec.length); //******* PODE TIRA DE DENTRO DO WHILE DAQUI PRA CIMA?****** TESTAR DEPOIS
                ms.receive(dp);
                msg = new String(dp.getData());
                //Recebe hora, origem, destino, hora de término - "HH:mm:ss Norte Sul HH:mm:ss" até terminar de atravesar a via (msg).
                
                //adicionar ao vetor de strings que tem as mensagem dos 3 outros carros
                if(!msgCarros.contains(msg)){//Se a mensagem ainda não foi adicionada
                    msgCarros.add(msg);
                    qtdMsg ++;
                }                               
            } catch (IOException ex) {//MulticastSocket
                System.out.println("Erro ao criar o MulticastSocket");                                
            }catch(Exception e){
                System.out.println("Erro: "+e.getMessage());
            }
               
            
        }
    }
    
}
