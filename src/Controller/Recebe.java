package Controller;

import Model.Carro;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thatianne e Macaule
 */
public class Recebe implements Runnable {

    private Controller controller;
    private final String grupo = "239.0.0.1";
    private final int porta = 5000;
    private MulticastSocket socket;
    private InetAddress iaGrupo;
    private ArrayList<Carro> carros;
    private String mensagem;

    public Recebe(Controller controller){
        try {
            this.controller = controller;
            this.carros = new ArrayList<Carro>();
            this.socket = new MulticastSocket();
            this.iaGrupo = InetAddress.getByName(this.grupo);
            this.socket.joinGroup(this.iaGrupo);
        } catch (IOException ex) {
            Logger.getLogger(Recebe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        this.conectado();

        byte[] recebeByte;
        DatagramPacket dataPacket;

        InetAddress ia = null;
        try {

            ia = InetAddress.getLocalHost();

        } catch (UnknownHostException ex) {
            Logger.getLogger(Recebe.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Host desconhecido");
        }

        while (true) {
            String ip;
            String id;
            try {

                recebeByte = new byte[1000];
                dataPacket = new DatagramPacket(recebeByte, recebeByte.length);
                socket.receive(dataPacket);

                if (dataPacket.getAddress().equals(ia)) {

                } else {

                    mensagem = new String(dataPacket.getData(), dataPacket.getOffset(), dataPacket.getLength());

                    String[] str = mensagem.split("-");
                    /*  MENSAGEM-RELÓGIO-POSX-POSY-ORIGEM-DESTINO
                            0       1      2    3   4       5 
                    */

                    this.attRelogio(Integer.parseInt(str[1]));

                    switch (str[0]) {
                        case "CONECTADO": {
                            
                            String resp = "BLZ-" + this.controller.getMeuRelogio() + "-" + this.controller.getPosX()
                                    + "-" + this.controller.getPosY() + "-" + this.controller.getOrigem()
                                    + "-" + this.controller.getDestino();
                            DatagramPacket pacote = new DatagramPacket(resp.getBytes(), resp.length(), this.iaGrupo, this.porta);
                            this.socket.send(pacote);
                            break;
                        }
                        case "BLZ": {
                            
                            //Precisa pegar o IP de quem enviou o datagrama
                            ip = dataPacket.getAddress().getHostAddress();
                            id = ip.substring(ip.lastIndexOf("."), ip.length());
                            Carro carro = new Carro(Integer.parseInt(str[1]), Integer.parseInt(id),
                                    Float.parseFloat(str[2]), Float.parseFloat(str[3]), str[4], str[5]);
                            
                            controller.addCarro(carro);
                            break;
                        }
                        case "ENTRAR":{
                            if(this.controller.getMeuRelogio() < Integer.parseInt(str[1])){
                                
                            }
                            break;
                        }
                    }
                }

            } catch (IOException ex) {
                Logger.getLogger(Recebe.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    private void conectado() {

        String msg = "CONECTADO-0-0-0-0-0";

        DatagramPacket datagrama = new DatagramPacket(msg.getBytes(), msg.length(), this.iaGrupo, this.porta);

        try {

            socket.send(datagrama);

        } catch (IOException ex) {
            Logger.getLogger(Recebe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void attRelogio(int parseInt) {

    }

}
