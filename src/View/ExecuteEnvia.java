package View;

import Controller.ControllerEnvia;
import Model.Via;

/**
 *
 * @author Thatianne e Macaule
 */
public class ExecuteEnvia implements Runnable {

    private final Via origem;
    private final Via destino;
    private int preferencia;
    private String hora;
    private String[] msgCarros;

    public ExecuteEnvia(Via origem, Via destino, int pref, String[] msgCarros) {
        this.origem = origem;
        this.destino = destino;
        this.preferencia = pref;
        this.msgCarros = msgCarros;
    }

    @Override
    public void run() {
        ControllerEnvia cEnvia = new ControllerEnvia(origem, destino, preferencia, msgCarros);
        cEnvia.envia();
    }    
}
