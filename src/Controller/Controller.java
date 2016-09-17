package Controller;

import Model.Carro;
import java.util.ArrayList;

/**
 *
 * @author Thatianne e Macaule
 */
public class Controller{

    private int meuRelogio;
    private String origem;
    private String destino;
    private float posX;
    private float posY;
    private ArrayList<Carro> carros;
    

    public void Controller(){
        this.carros = new ArrayList<>();
    }
    
    public ArrayList<Carro> getCarros(){
        return carros;
    }
    
    public boolean addCarro(Carro carro){
        boolean existe = false;
        for(int i=0; i<carros.size(); i++){
            Carro atual = carros.get(i);
            if(carro.getId() == atual.getId()){
                existe = true;
            }
        }
        
        if(!existe){
            carros.add(carro);
        }
        
        return existe;
    }
    
    public int getMeuRelogio() {
        return meuRelogio;
    }

    public void setMeuRelogio(int meuRelogio) {
        this.meuRelogio = meuRelogio;
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
    
    
}
