package Controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thatianne e Macaule
 */
public class Start {

    private final String grupo = "239.0.0.1";
    private final int porta = 5000;
    private MulticastSocket socket;
    private InetAddress iaGrupo;
    private Controller controller;

    public Start() {
        try {
            this.socket = new MulticastSocket();
            this.iaGrupo = InetAddress.getByName(this.grupo);
            this.socket.joinGroup(this.iaGrupo);
        } catch (IOException ex) {
            Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void comeca(Controller controller) {

        this.controller = controller;
        new Recebe(controller).run();
        
        //Settar posX, posY, origem e destino no controller

    }
    
    public void Entrar(){
        String msg = "ENTRAR-"+ this.controller.getMeuRelogio() + "-" + this.controller.getPosX()
                                    + "-" + this.controller.getPosY() + "-" + this.controller.getOrigem()
                                    + "-" + this.controller.getDestino();

        DatagramPacket datagrama = new DatagramPacket(msg.getBytes(), msg.length(), this.iaGrupo, this.porta);

        try {

            socket.send(datagrama);

        } catch (IOException ex) {
            Logger.getLogger(Recebe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void Sair(){
        
        String msg = "SAIR-"+ this.controller.getMeuRelogio() + "-" + this.controller.getPosX()
                                    + "-" + this.controller.getPosY() + "-" + this.controller.getOrigem()
                                    + "-" + this.controller.getDestino();
        
        DatagramPacket datagrama = new DatagramPacket(msg.getBytes(), msg.length(), this.iaGrupo, this.porta);

        try {

            socket.send(datagrama);

        } catch (IOException ex) {
            Logger.getLogger(Recebe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
