package Controller;

import Model.Carro;
import java.util.ArrayList;

/**
 *
 * @author Thatianne e Macaule
 */
public class Controller{

    private int meuRelogio = 0;
    private String origem;
    private String destino;
    private float posX;
    private float posY;
    private ArrayList<Carro> carros;
    private ArrayList<String> permissoes;
    private boolean queroIr;
    private boolean minhaVez;
    

    public void Controller(){
        this.permissoes = new ArrayList<String>();
        this.carros = new ArrayList<>();
        this.queroIr = false;
    }
    
    public ArrayList<Carro> getCarros(){
        return carros;
    }
    
    public void addPermissao(String permissao){
        
        if(queroIr){
            permissoes.add(origem);
        }
        
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
    
    public void atualizarCarro(int id, int relogio, float posX, float posY){
        
        for(int i = 0; i < this.carros.size(); i++){
            Carro carro = this.carros.get(i);
            if(carro.getId() == id){
                carro.setRelogio(relogio);
                carro.setPosX(posX);
                carro.setPosY(posY);
            }
        }   
    }
    
    public boolean possoIr(){
        if(this.permissoes.size() == this.carros.size()){
            this.minhaVez = true;
            return true;
        }
        return false;
    }
    
    public void carroSaiu(int id){
        
        for(int i = 0; i < this.carros.size(); i++){
            Carro carro = this.carros.get(i);
            if(carro.getId() == id){
                this.carros.remove(i);
            }
        } 
    }

    public boolean isQueroIr() {
        return queroIr;
    }

    public void setQueroIr(boolean queroIr) {
        this.queroIr = queroIr;
    }

    public boolean isMinhaVez() {
        return minhaVez;
    }

    public void setMinhaVez(boolean minhaVez) {
        this.minhaVez = minhaVez;
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
