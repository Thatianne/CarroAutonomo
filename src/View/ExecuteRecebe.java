package View;

import Controller.ControllerRecebe;
import Model.Via;

/**
 *
 * @author Thatianne e Macaule
 */
public class ExecuteRecebe {
    
    private final Via origem;
    private final Via destino;
    private int preferencia;
    private String[] msgCarros;
    
    public ExecuteRecebe(Via origem, Via destino, int pref, String[] msgCarros){
        this.origem = origem;
        this.destino = destino;
        this.preferencia = pref;
        this.msgCarros = msgCarros;
    }
    
    public void executa(){
        
        ControllerRecebe cRecebe = new ControllerRecebe(origem, destino, preferencia, msgCarros);
        cRecebe.recebe();
        
    }
}

