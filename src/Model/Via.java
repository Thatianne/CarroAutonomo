package Model;

/**
 *
 * @author Thatianne e Macaule
 */
public enum Via {
    
    NORTE("Norte"), SUL("Sul"), LESTE("Leste"), OESTE("Oeste");
    
    private String nome;
    
    Via(String n){
        this.nome = n;
    }
    
    public String getVia(){
        return this.nome;
    }    
}
