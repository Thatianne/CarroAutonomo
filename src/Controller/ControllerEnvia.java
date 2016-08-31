/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Via;
import java.io.IOException;
import static java.lang.Math.pow;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Thatianne e Macaule
 */
public class ControllerEnvia {

    private final String grupo = "239.0.0.1";
    private final int porta = 5000;

    private final Via origem;
    private final Via destino;
    private int preferencia;
    private String hora;
    
    public ControllerEnvia(Via origem, Via destino, int pref) {
        this.origem = origem;
        this.destino = destino;
        this.preferencia = pref;
    }

    public void envia() {
        while (true) {
            byte[] b = null;
            //Precisa mandar para todos da rede a hora que ele começou a andar na via para ser decidido a preferência
            //Mandar hora, origem, destino - "HH:mm:ss Norte Sul" até terminar de atravesar a via.
            hora = geraHora();
            
            String msg = hora + " " + origem.getVia() + " " + destino.getVia();

            try {

                InetAddress addr = InetAddress.getByName(this.grupo);
                DatagramSocket dSocket = new DatagramSocket();
                b = msg.getBytes();

                DatagramPacket dp = new DatagramPacket(b, b.length, addr, this.porta);
                dSocket.send(dp);
                System.out.println(new String(dp.getData()));
                System.out.println("Enviado");

            } catch (UnknownHostException ex) {
                System.out.println("Host desconhecido");//InetAddress                        
            } catch (SocketException ex) {
                System.out.println("Exceção de socket");//DatagramSocket                       
            } catch (IOException ex) {//DatagramSocket.send
                System.out.println("O envio de pacote não funcionou");
            }

            try {
                //de tempo em tempo
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                System.out.println("Exceção de interrupção");
            }

        }
    }

    private String geraHora() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date hora = Calendar.getInstance().getTime();

        String dataFormatada = sdf.format(hora);
        return dataFormatada;
    }
    
    private float calAceleracao(float vFinal, float vInicial, float distancia ){
        //Calcula a aceleração para saber se está um freio suave
        float aceleracao;
        
        aceleracao = (float)(((pow(vFinal, 2)) - (pow(vInicial, 2)))/(2*distancia));                
        return aceleracao;//aceleracao em km/h
    }
    
    private float calTempo(float vFinal, float vInicial, float aceleracao){
        float tempo;
        tempo = (vFinal - vInicial)/aceleracao;
        return tempo;//tempo em horas
    }
    

}
