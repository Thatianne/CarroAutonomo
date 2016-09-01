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
import java.util.Collections;
import java.util.Comparator;

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
    private String horaInicial;
    private int segundos;
    //private Controller cont

    public ControllerRecebe(Via origem, Via destino, int pref, ArrayList<String> msgCarros, String hInicial, int seg) {
        this.origem = origem;
        this.destino = destino;
        this.preferencia = pref;
        this.msgCarros = msgCarros;
        this.horaInicial = hInicial;
        this.segundos = seg;
    }

    public void recebe() {
        int msgIgual = 0;
        int qtdMsg = 0;
        String msg = null;
        //vai poder parar de esperar mensagens quando receber as mensagens dos outros 3 carros, caso tenha
        //Ver casos que tenha menos de 3 carros, pensar em condição de parada (poderia ser pelo tempo)
        while (qtdMsg < 3 || msgIgual != 3) {

            try {

                MulticastSocket ms = new MulticastSocket(5000);
                //ms.setSoTimeout(200);//tempo de espera de mensagem

                InetAddress grp = InetAddress.getByName(this.grupo);
                ms.joinGroup(grp);

                byte[] rec = new byte[256];
                DatagramPacket dp = new DatagramPacket(rec, rec.length); //******* PODE TIRA DE DENTRO DO WHILE DAQUI PRA CIMA?****** TESTAR DEPOIS
                //Recebe hora, origem, destino, hora de término - "HH:mm:ss Norte Sul HH:mm:ss" até terminar de atravesar a via (msg).
                ms.receive(dp);
                msg = new String(dp.getData());
                //se receber uma mesma mensagem 3 vezes seguidas significa que passou 150 milisegundos e não tem nenhum outro carro 
                msgIgual++;

                //adicionar ao vetor de strings que tem as mensagem dos 3 outros carros
                if (!msgCarros.contains(msg)) {//Se a mensagem ainda não foi adicionada
                    msgCarros.add(msg);
                    qtdMsg++;
                    msgIgual = 0;
                }

                //} catch (SocketTimeoutException e) {//se nenhuma mensagem foi recebida em 0.2 segundos
            } catch (IOException ex) {//MulticastSocket
                System.out.println("Erro ao criar o MulticastSocket");
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }

        }
    }

    public void processa() {
        recebe();
        //depois de receber as mensagens dos outros carros, as decisões já podem ser tomadas
        String seuDatagrama = horaInicial+" "+origem.getVia()+" "+destino.getVia()+" "+ segundos;
        msgCarros.add(seuDatagrama);
        
        Collections.sort(msgCarros, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {//se o1 < o2 retorna -1, se o1 > o2 retorna 1, se o1 = o2 retorna 0
                String horaS1, horaS2, minutoS1, minutoS2, segundoS1, segundoS2;
                int hora1, hora2, minuto1, minuto2, segundo1, segundo2;
                horaS1 = o1.substring(0, 2);
                hora1 = Integer.parseInt(horaS1);
                horaS2 = o2.substring(0, 2);
                hora2 = Integer.parseInt(horaS2);
                if (hora1 < hora2) {//é menor
                    return -1;
                } else {
                    //pode ser maior
                    minutoS1 = o1.substring(3, 5);
                    minuto1 = Integer.parseInt(horaS1);
                    minutoS2 = o2.substring(3, 5);
                    minuto2 = Integer.parseInt(horaS2);
                    if (minuto1 < minuto2) {//é menor
                        return -1;
                    } else {
                        //pode ser maior
                        segundoS1 = o1.substring(6, 8);
                        segundo1 = Integer.parseInt(horaS1);
                        segundoS2 = o2.substring(6, 8);
                        segundo2 = Integer.parseInt(horaS2);
                        if (segundo1 < segundo2) {//é menor
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                }
            }
        });

        //A lista está em ordem de preferência, precisa pegar o tempo que o carro vai ter que 
        //"esperar" para os outros realizarem suas ações
        String carro, tempoCarro;
        int total = 0;
        int index = msgCarros.indexOf(seuDatagrama);
        for(int i=0; i<index; i++){
            carro = msgCarros.get(i);
            tempoCarro = carro.substring(carro.length() - 2, carro.length());
            total = total + Integer.parseInt(tempoCarro);            
        }
        
        /*tendo o tempo de "espera" é possível calcular a aceleração de freio do carro pois se tem 
        a velocidade inicial e distância.
        No Controller tem uma função para isso mas o carro precisa passar pelo cruzamento com 60km/h e 40km/h se for dobrar
        */
    }
}
