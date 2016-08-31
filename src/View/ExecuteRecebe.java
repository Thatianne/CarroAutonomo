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
    
    public ExecuteRecebe(Via origem, Via destino, int pref){
        this.origem = origem;
        this.destino = destino;
        this.preferencia = pref;
    }
    
    public void executa(){
        
        ControllerRecebe cRecebe = new ControllerRecebe();
        cRecebe.recebe();
        
    }
}

