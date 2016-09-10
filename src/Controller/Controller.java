package Controller;

import Model.Via;
import java.io.IOException;
import static java.lang.Math.pow;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Thatianne e Macaule
 */
public class Controller {

    private ControllerEnvia enviarCtrl;
    private Rectangle carro;
    private String origemString;
    private String destinoString;

    private final String grupo = "239.0.0.1";
    private long tempo1 = 2000;
    private String origem;
    private String destino;
    private int preferencia;
    private ArrayList<String> msgCarros;
    private String horaInicial;
    private int segundos;
    private MulticastSocket ms;

    public Controller(Rectangle carro, String origem, String destino) throws IOException {

        this.origem = origem;
        this.destino = destino;
        this.carro = carro;
        this.msgCarros = new ArrayList<String>();

    }

    public ArrayList escutaCarros() throws IOException {

        this.ms = new MulticastSocket(5000);
        ms.setSoTimeout(100);//tempo de espera de mensagem                        
        InetAddress grp = InetAddress.getByName(this.grupo);
        ms.joinGroup(grp);

        long inicio = 0;
        long fim = 0;
        long tempo = 0;

        String msg;

        inicio = System.currentTimeMillis();

        while (tempo <= 300) {
            try {
                //esperar por mensagem durante 300 milisegundos
                
                msg = receberMensagem();                                
                
                if (msgCarros != null) {                    
                    if (msg != null) {
                        String rel = msg.substring(0, msg.indexOf(" "));
                        //ver se o já tem o número do relógio lógico no array
                        if(msgCarros.size()==0){
                            msgCarros.add(msg);
                        }
                        for(int i=0; i<msgCarros.size(); i++){
                            String m = msgCarros.get(i);
                            StringTokenizer st = new StringTokenizer(m);
                            String s = st.nextToken();
                            if(!rel.equals(s)){
                                msgCarros.add(msg);
                            }
                        }
                        
                    }
                }

                fim = System.currentTimeMillis();
                tempo = fim - inicio;

                System.out.println(tempo);

            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        //Depois que recebeu as mensagens ai vai freiar se precisar e verificar se as rotas chocam
        if (msgCarros != null && msgCarros.size()>1) {
            Collections.sort(msgCarros, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    StringTokenizer st1 = new StringTokenizer(o1);                    
                    StringTokenizer st2 = new StringTokenizer(o2);
                    
                    String oS1 = st1.nextToken();
                    String oS2 = st2.nextToken();
                    
                    int log1 = Integer.parseInt(oS1);
                    int log2 = Integer.parseInt(oS2);
                    if (log1 < log2) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });
        }

        //criar thread e mandar mensagens
        if (msgCarros != null && msgCarros.size() != 0) {
            
            String ultimo = msgCarros.get(msgCarros.size() - 1);
            StringTokenizer st = new StringTokenizer(ultimo);
                        
            String n = st.nextToken();
            preferencia = Integer.parseInt(n);
                        
            String temp = st.nextToken();
            tempo1 = Long.parseLong(temp);
        }

        preferencia++;
        long n = setTempo(origem, destino);//tempo necessário
        enviarCtrl = new ControllerEnvia(carro, preferencia, n+tempo1, origem, destino);
        //tomar as decisões com base nos carros ouvidos
        Thread t = new Thread(enviarCtrl);
        t.start();

        return msgCarros;
    }

    private String receberMensagem() throws IOException {
        DatagramPacket dp = null;
        String msg = null;
        try {

            byte[] rec = new byte[256];
            dp = new DatagramPacket(rec, rec.length);
            //relogioLogico tempoQueVaiDemorar posicaoX posicaoY ViaOrigem ViaDestino
            ms.receive(dp);
            
            msg = new String(dp.getData());

        } catch (SocketTimeoutException e) {
            System.out.println("Tempo de espera expirado");
            msg = null;
        } catch (SocketException ex) {
            msg = null;
            System.out.println("Socket exception");
        }
        return msg;
    }

    private long setTempo(String origem, String destino) {
        long tempo = 0;
        String n = origem +" "+destino;
        if(n.equals("Norte Sul") || n.equals("Sul Norte") || n.equals("Leste Oeste") || n.equals("Oeste Leste")){
            tempo = 2000;            
        }else if(n.equals("Norte Oeste") || n.equals("Leste Norte")|| n.equals("Sul Leste") || n.equals("Oeste Sul")){
            tempo = 4000;
        }else{
            tempo = 6000;
        }            
        
        return tempo;
    }
}
