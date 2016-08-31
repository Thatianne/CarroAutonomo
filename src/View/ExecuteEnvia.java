package View;

import Controller.ControllerEnvia;
import Model.Via;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thatianne e Macaule
 */
public class ExecuteEnvia implements Runnable {

    private final Via origem;
    private final Via destino;
    private int preferencia;
    private String hora;

    public ExecuteEnvia(Via origem, Via destino, int pref) {
        this.origem = origem;
        this.destino = destino;
        this.preferencia = pref;
    }

    @Override
    public void run() {
        ControllerEnvia cEnvia = new ControllerEnvia(origem, destino, preferencia);
        cEnvia.envia();
    }    
}
