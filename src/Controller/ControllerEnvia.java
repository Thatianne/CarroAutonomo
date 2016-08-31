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
import java.util.StringTokenizer;

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
    private String horaFinal;
    private String[] msgCarros;
    
    public ControllerEnvia(Via origem, Via destino, int pref, String[] msgCarros) {
        this.origem = origem;
        this.destino = destino;
        this.preferencia = pref;
        this.msgCarros = msgCarros;
    }

    public void envia() {
        // hora de inicio
        hora = geraHoraInicial();
        //hora final, que termina  ação requisitada
        horaFinal = geraHoraFinal(origem, destino, hora);
        
        while (true) {
            byte[] b = null;
            //Precisa mandar para todos da rede a hora que ele começou a andar na via para ser decidido a preferência
            //Mandar hora, origem, destino - "HH:mm:ss Norte Sul" até terminar de atravesar a via.
            
                                   
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

    private String geraHoraInicial() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date hora = Calendar.getInstance().getTime();

        String dataFormatada = sdf.format(hora);
        return dataFormatada;
    }
    
    private String geraHoraFinal(Via origem, Via destino, String horaInicial){
        String hora = null, minString;
        int segundos = 0, min;
        String direcao = rota(origem, destino);
        if(direcao.equals("direto")){
            //Gasta 18,5 segundos
            segundos = 19;
        }else if(direcao.equals("direita") || direcao.equals("esquerda")){
            //Gasta 36 segundos (21.6 + 14.4)
            segundos = 36;            
        }
        //Adicionar esses segundos à hora inicial
        
        String segString = horaInicial.substring(6, 8);
        int segInt = Integer.parseInt(segString);        
        segundos = segundos + segInt;
        if(segundos >= 60){//adicionar 1 minuto
            minString = horaInicial.substring(3, 5);
            min = Integer.parseInt(minString);
            min = min + 1;
            segundos = segundos % 60;
            hora = horaInicial.substring(0, 3)+min+":"+ segundos;
            System.out.println(hora);
        }else{
            hora = horaInicial.substring(0,6)+segundos;
            System.out.println(hora);
        }                  
        
        return hora;
    }
    
    private float calAceleracao(float vFinal, float vInicial, float distancia ){
        //Calcula a aceleração para saber se está um freio suave
        float aceleracao;
        
        aceleracao = (float)(((pow(vFinal, 2)) - (pow(vInicial, 2)))/(2*distancia));                
        return aceleracao;//aceleracao em km/h
    }
    
    private float calTempo(float vFinal, float vInicial, float aceleracao){
        float tempo;
        tempo = (vFinal - vInicial)/aceleracao;//tempo em horas
        tempo = tempo * 360;//tempo em segundos
        return tempo;
    }
    
    private String rota(Via origem, Via destino){
        int i = 0;
        String concatenado = origem.getVia()+ " "+ destino.getVia();
        String direcao;
        
        switch(concatenado){
            //direto
            case "Norte Sul":{
                direcao = "direto";
                break;
            }
            
            case "Sul Norte":{
                direcao = "direto";
                break;
            }
            case "Leste Oeste":{
                direcao = "direto";
                break;
            }
            case "Oeste Leste":{
                direcao = "direto";
                break;
            }
            //direita
            case "Norte Oeste":{
                direcao = "direita";
                break;                
            }
            case "Leste Norte":{
                direcao = "direita";
                break;
            }
            case "Sul Leste":{
                direcao = "direita";
                break;
            }
            case "Oeste Sul":{
                direcao = "direita";
                break;
            }
            //esquerda
            case "Norte Leste":{
                direcao = "esquerda";
                break;
            }
            case "Leste Sul":{
                direcao = "esquerda";
                break;
            }
            case "Sul Oeste":{
                direcao = "esquerda";
                break;
            }
            case "Oeste Norte":{
                direcao = "esquerda";
                break;
            }
            //origem == destino
            default:{
                direcao = "erro";
                break;
            }      
        }
        return direcao;
    }
}
