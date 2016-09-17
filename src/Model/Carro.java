package Model;

/**
 *
 * @author Thati
 */
public class Carro {
   
    private int relogio;
    private int id;
    private float posX;
    private float posY;
    private String origem;
    private String destino;
    
    public Carro(int relogio, int id, float posX, float posY, String origem, String destino){
        this.relogio = relogio;
        this.id = id;
        this.posX = posX;
        this.posY = posY;
        this.origem = origem;
        this.destino = destino;
    }
    
    public Carro(){
        
    }

    public int getRelogio() {
        return relogio;
    }

    public void setRelogio(int relogio) {
        this.relogio = relogio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }
    
    
    
    
}
