/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Thatianne e Macaule
 */
public class ControllerEnvia implements Runnable {

    private final String grupo = "239.0.0.1";
    private final int porta = 5000;

    private String origem;
    private String destino;
    private int pref; // Relógio lógico
    private String horaInicial;
    private int segundos;
    private ArrayList msgCarros;
    private long tempoRestante;

    private Rectangle carro;

    public ControllerEnvia(Rectangle carro, int pref, long tempoRestante, String origem, String destino) {
        this.carro = carro;
        this.pref = pref;
        this.origem = origem;
        this.destino = destino;        
        this.tempoRestante = tempoRestante;
    }

    @Override
    public void run() {

        int ultPref;
        double x;
        double y;        
        
        long fim, intervalo, tempo;
        long inicio = System.currentTimeMillis();
        
        while (true) {

            x = carro.getLocalToSceneTransform().getTx();
            y = carro.getLocalToSceneTransform().getTy();

            try {
                //mandar: relogioLogico tempoQueVaiDemorar posicaoX posicaoY ViaOrigem ViaDestino
                
                fim = System.currentTimeMillis();
                intervalo = fim - inicio;
                tempo = tempoRestante - intervalo;
                
                String msg = pref+" "+tempo+" "+x+" "+y+" "+origem+" "+destino;
                
                byte[] b = msg.getBytes();
                InetAddress addr = InetAddress.getByName(this.grupo);
                DatagramSocket dSocket = new DatagramSocket();
                DatagramPacket dp = new DatagramPacket(b, b.length, addr, this.porta);
                dSocket.send(dp);
                
                System.out.println(new String(dp.getData()));

                Thread.sleep(50);
            } catch (InterruptedException ex) {
                System.out.println("deu ruim");
            } catch (UnknownHostException ex) {
                Logger.getLogger(ControllerEnvia.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SocketException ex) {
                Logger.getLogger(ControllerEnvia.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ControllerEnvia.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
