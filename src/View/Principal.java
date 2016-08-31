package View;

import Model.Via;
import java.util.Date;

/**
 *
 * @author Thatianne e Macaule
 */
public class Principal {
    
    private final Via origem;// ******** VER DEPOIS SE VAI DAR ERRO **********
    private final Via destino;
    private int preferencia = 0;
    
    
    public static void main(String[] args) {
        Principal p = new Principal(Via.NORTE, Via.LESTE);
        p.start();
    }
    
    public Principal(Via origem, Via destino){
        this.origem = origem;
        this.destino = destino;
    }
    
    public Date start(){
        Date tempo = null;
        //cria a thread para mandar mensagem
        ExecuteEnvia ee = new ExecuteEnvia(origem, destino, preferencia);        
        Thread t = new Thread(ee);
        t.start();
        
        //thread para receber
        ExecuteRecebe er = new ExecuteRecebe(origem, destino, preferencia);
        er.executa();
        
        //retorna o tempo que leva pra o carro ter preferência, poderia retornar o tempo que demora para ele atravesar
        return tempo;
    }
    
}
