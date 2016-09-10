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
}
