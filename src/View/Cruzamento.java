package View;

import Controller.Controller;
import Model.Via;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Thatianne e Macaule
 */
public class Cruzamento extends Application {

    private Controller controller;

    private ArrayList<String> carros;
    private Group root;
    private Rectangle carro;
    private final float posNorteX = 228;//+15
    private final float posNorteY = 30;
    private final float posSulX = 290;
    private final float posSulY = 465;//-20
    private final float posLesteX = 470;
    private final float posLesteY = 220;
    private final float posOesteX = 30;
    private final float posOesteY = 280;

    private float tempoEspera = 0;
    private float tempo;

    private int comprimento = 40;
    private int largura = 30;

    /*           |   
             A   |   B
          _______|_______                  Quadrantes do cruzamento
                 |
             C   |   D
                 |   
     */
    public static void main(String[] args) {

        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        //faz a tela sem nenhum carro        
        prepara(stage);

    }

    private void prepara(Stage stage) {

        root = new Group();

        HBox hbLayout = new HBox(15);

        Image imgCruzamento = new Image("cruzamento1.png");
        ImageView ivCruzamento = new ImageView(imgCruzamento);
        ivCruzamento.setFitHeight(500);
        ivCruzamento.setFitWidth(500);

        root.getChildren().add(ivCruzamento);

        Image imgBussula = new Image("Bussula.png");
        ImageView ivBussula = new ImageView(imgBussula);
        ivBussula.setFitHeight(170);
        ivBussula.setFitWidth(170);

        VBox vbLayout = new VBox(10);
        vbLayout.setPadding(new Insets(15, 20, 15, 10));

        hbLayout.getChildren().addAll(vbLayout, new Separator(Orientation.VERTICAL), root);

        ObservableList<String> opcoes
                = FXCollections.observableArrayList(
                        "Norte",
                        "Sul",
                        "Leste",
                        "Oeste"
                );

        Label lbOrigem = new Label("Origem");
        ComboBox cbOrigem = new ComboBox(opcoes);
        cbOrigem.setValue("Escolha a origem");

        Label lbDestino = new Label("Destino");
        ComboBox cbDestino = new ComboBox(opcoes);
        cbDestino.setValue("Escolha o destino");

        Button btStart = new Button("Start");

        btStart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                String origem, destino;

                origem = cbOrigem.getValue().toString();
                destino = cbDestino.getValue().toString();

                criarCarro(stage, root);

                try {
                    controller = new Controller(carro, origem, destino);
                    carros = controller.escutaCarros();
                    //desenhar carros dos quais recebeu mensagens
                    if (carros != null) {
                        desenharCarros(stage, carros);
                    }
                    animar(carro, origem + " " + destino, tempoEspera);
                } catch (IOException ex) {
                    Logger.getLogger(Cruzamento.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        Label msgs = new Label("Mensagens");

        TextArea ta = new TextArea();
        ta.setMaxSize(210, 170);
        ta.setEditable(false);
        vbLayout.getChildren().addAll(lbOrigem, cbOrigem, lbDestino, cbDestino, btStart, ivBussula, msgs, ta);

        Scene cena = new Scene(hbLayout, 800, 500);

        stage.setTitle("Cruzamento");
        stage.setScene(cena);
        stage.setResizable(false);
        stage.show();

    }

    private void desenharCarros(Stage stage, ArrayList<String> carros) {
        String dados[] = new String[6];
        float posX, posY, tempoRestante;
        int j;
        float tempoTotal = 0;

        for (int i = 0; i < carros.size(); i++) {
            //relogioLogico tempoQueVaiDemorar posicaoX posicaoY ViaOrigem ViaDestino
            //      0                 1            2        3        4          5
            StringTokenizer st = new StringTokenizer(carros.get(i));
            j = 0;
            while (st.hasMoreTokens()) {
                dados[j] = st.nextToken();
                j++;
            }

            //criar
            criarCarro(stage, root);
            //colocar na tela

            posX = Float.parseFloat(dados[2]);
            posY = Float.parseFloat(dados[3]);
            tempoRestante = Float.parseFloat(dados[1]);

            addCarros(carro, dados[4] + " " + dados[5], posX, posY, tempoRestante);
            tempoTotal = tempoTotal + tempoRestante;

        }
        this.tempoEspera = tempoTotal;
    }

    private void addCarros(Rectangle carro, String tipo, float posX, float posY, float tempoRestante) {
        
        Path caminho = new Path();
        
        System.out.println("***"+tipo+"***");
        switch (tipo) {
            //reto
            case "Norte Sul": {                

                
                caminho.getElements().add(new MoveTo(posX, posY));
                //animaReto(caminho, carro, posNorteX, posNorteY, posNorteX, posNorteY + 130,
                  //      posNorteX, posSulY, this.tempoEspera, tempo);

                break;
            }

            case "Sul Norte": {                

                caminho.getElements().add(new MoveTo(posX, posY));
                
                //animaReto(caminho, carro, posSulX, posSulY, posSulX, posSulY - 130,
                  //      posSulX, posNorteY, this.tempoEspera, tempo);

                break;
            }
            case "Leste Oeste": {     
                
                caminho.getElements().add(new MoveTo(posX, posY));

                //animaReto(caminho, carro, posLesteX, posLesteY, posLesteX - 130,
                  //      posLesteY, posOesteX, posLesteY, this.tempoEspera, tempo);

                break;
            }
            case "Oeste Leste": {                
                System.out.println("entrooooou");
                caminho.getElements().add(new MoveTo(posX, posY));
                
                //animaReto(caminho, carro, posOesteX, posOesteY, posOesteX + 130,
                  //      posOesteY, posLesteX, posOesteY, this.tempoEspera, tempo);

                break;
            }
            //direita
            case "Norte Oeste": {//Ocupa o quadrante A

                CubicCurveTo cubicTo = new CubicCurveTo();

                cubicTo.setControlX1(posNorteX);
                cubicTo.setControlY1(posOesteY - 60);
                cubicTo.setControlX2(posNorteX);
                cubicTo.setControlY2(posOesteY - 50);
                cubicTo.setX(posOesteX);
                cubicTo.setY(posLesteY);

                animaCurva(caminho, carro, cubicTo, posNorteX, posNorteY, posNorteX, posNorteY + 130,
                        this.tempoEspera, tempo);

                break;
            }
            case "Leste Norte": {//quadrantes B

                CubicCurveTo cubicTo = new CubicCurveTo();

                cubicTo.setControlX1(posSulX);
                cubicTo.setControlY1(posNorteY + 200);
                cubicTo.setControlX2(posSulX);
                cubicTo.setControlY2(posNorteY + 180);
                cubicTo.setX(posSulX);
                cubicTo.setY(posNorteY);

                animaCurva(caminho, carro, cubicTo, posLesteX, posLesteY, posLesteX - 130,
                        posLesteY, this.tempoEspera, tempo);

                break;
            }
            case "Sul Leste": {//quadrante D

                CubicCurveTo cubicTo = new CubicCurveTo();

                cubicTo.setControlX1(posSulX);
                cubicTo.setControlY1(posLesteY + 60);
                cubicTo.setControlX2(posSulX);
                cubicTo.setControlY2(posLesteY + 50);
                cubicTo.setX(posLesteX);
                cubicTo.setY(posOesteY);

                animaCurva(caminho, carro, cubicTo, posSulX, posSulY, posSulX, posSulY - 130, this.tempoEspera, tempo);

                break;
            }
            case "Oeste Sul": {//quadrante C                

                CubicCurveTo cubicTo = new CubicCurveTo();

                cubicTo.setControlX1(posNorteX);
                cubicTo.setControlY1(posOesteY - 10);
                cubicTo.setControlX2(posNorteX);
                cubicTo.setControlY2(posOesteY);
                cubicTo.setX(posNorteX);
                cubicTo.setY(posSulY);

                animaCurva(caminho, carro, cubicTo, posOesteX, posOesteY, posOesteX + 130, posOesteY,
                        this.tempoEspera, tempo);

                break;
            }
            //esquerda
            case "Norte Leste": {//ocupa os quadrantes A, C e D

                CubicCurveTo cubicTo = new CubicCurveTo();

                cubicTo.setControlX1(posNorteX);
                cubicTo.setControlY1(posOesteY + 80);
                cubicTo.setControlX2(posNorteX);
                cubicTo.setControlY2(posOesteY - 20);
                cubicTo.setX(posLesteX);
                cubicTo.setY(posOesteY);

                animaCurva(caminho, carro, cubicTo, posNorteX, posNorteY, posNorteX, posNorteY + 130, this.tempoEspera, tempo);

                break;
            }
            case "Leste Sul": {//ocupa os quadrantes A, B e C

                CubicCurveTo cubicTo = new CubicCurveTo();

                cubicTo.setControlX1(posNorteX - 50);
                cubicTo.setControlY1(posLesteY);
                cubicTo.setControlX2(posNorteX);
                cubicTo.setControlY2(posLesteY);
                cubicTo.setX(posNorteX);
                cubicTo.setY(posSulY);

                animaCurva(caminho, carro, cubicTo, posLesteX, posLesteY, posLesteX - 130, posLesteY, this.tempoEspera, tempo);

                break;
            }
            case "Sul Oeste": {//ocupa os quadrantes A, B e D

                CubicCurveTo cubicTo = new CubicCurveTo();

                cubicTo.setControlX1(posSulX);
                cubicTo.setControlY1(posLesteY - 50);
                cubicTo.setControlX2(posSulX);
                cubicTo.setControlY2(posLesteY);
                cubicTo.setX(posOesteX);
                cubicTo.setY(posLesteY);

                animaCurva(caminho, carro, cubicTo, posSulX, posSulY, posSulX, posSulY - 130, this.tempoEspera, tempo);

                break;
            }
            case "Oeste Norte": {//ocupa os quadrantes B, C e D

                CubicCurveTo cubicTo = new CubicCurveTo();

                cubicTo.setControlX1(posSulX + 50);
                cubicTo.setControlY1(posOesteY + 25);
                cubicTo.setControlX2(posSulX);
                cubicTo.setControlY2(posOesteY + 25);
                cubicTo.setX(posSulX);
                cubicTo.setY(posNorteY);
                
                animaCurva(caminho, carro, cubicTo, posOesteX, posOesteY, posOesteX+130, posOesteY, this.tempoEspera, tempo);

                break;
            }
            //origem == destino
            default: {

                root.getChildren().remove(carro);

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Dado errado");
                alert.setHeaderText("Tentando desenhar outros carro");

                alert.show();                
                break;
            }
        }

    }

    private void criarCarro(Stage stage, Group root) {

        carro = new Rectangle(comprimento, largura, Color.CYAN);

        root.getChildren().add(carro);

    }

    private void animaReto(Path caminho, Rectangle carro, float comecoX, float comecoY, float paradaX, float paradaY,
            float fimX, float fimY, float tempoEsp, float tempo) {

        caminho.getElements().add(new MoveTo(comecoX, comecoY));//começa

        //espera no cruzamento
        caminho.getElements().add(new LineTo(paradaX, paradaY));//pausa

        PathTransition pt = setPath(caminho, carro, tempoEsp);

        caminho.getElements().clear();

        pt.setOnFinished(evento -> {
            caminho.getElements().add(new MoveTo(paradaX, paradaY));//pausa

            caminho.getElements().add(new LineTo(fimX, fimY));//fim

            PathTransition pt1 = setPath(caminho, carro, tempo);
            pt1.setOnFinished(ev -> {
                root.getChildren().remove(carro);
            });

        });

    }

    private void animaCurva(Path caminho, Rectangle carro, CubicCurveTo cct, float comecoX, float comecoY,
            float paradaX, float paradaY, float tempoEsp, float tempo) {

        caminho.getElements().add(new MoveTo(comecoX, comecoY));//começa

        caminho.getElements().add(new LineTo(paradaX, paradaY));

        PathTransition pt = setPath(caminho, carro, tempoEsp);

        caminho.getElements().clear();

        pt.setOnFinished(evento -> {

            caminho.getElements().add(new MoveTo(paradaX, paradaY));

            caminho.getElements().add(cct);

            PathTransition pt1 = setPath(caminho, carro, tempo);
            pt1.setOnFinished(ev -> {
                root.getChildren().remove(carro);
            });
        });

    }

    public void animar(Rectangle carro, String tipo, float tempoEspera) throws IOException {
        Via origem = null, destino = null;

        boolean certo = true;

        int reto = 2000;
        int direita = 4000;
        int esquerda = 6000;

        Path caminho = new Path();

        if (this.tempoEspera == 0) {
            this.tempoEspera = 2000;
        }

        switch (tipo) {
            //reto
            case "Norte Sul": {

                tempo = reto;

                origem = Via.NORTE;
                destino = Via.SUL;

                animaReto(caminho, carro, posNorteX, posNorteY, posNorteX, posNorteY + 130,
                        posNorteX, posSulY, this.tempoEspera, tempo);

                break;
            }

            case "Sul Norte": {

                tempo = reto;

                origem = Via.SUL;
                destino = Via.NORTE;

                animaReto(caminho, carro, posSulX, posSulY, posSulX, posSulY - 130,
                        posSulX, posNorteY, this.tempoEspera, tempo);

                break;
            }
            case "Leste Oeste": {

                tempo = reto;

                origem = Via.LESTE;
                destino = Via.OESTE;

                animaReto(caminho, carro, posLesteX, posLesteY, posLesteX - 130,
                        posLesteY, posOesteX, posLesteY, this.tempoEspera, tempo);

                break;
            }
            case "Oeste Leste": {

                tempo = reto;

                origem = Via.OESTE;
                destino = Via.LESTE;

                animaReto(caminho, carro, posOesteX, posOesteY, posOesteX + 130,
                        posOesteY, posLesteX, posOesteY, this.tempoEspera, tempo);

                break;
            }
            //direita
            case "Norte Oeste": {//Ocupa o quadrante A

                tempo = direita;

                origem = Via.NORTE;
                destino = Via.OESTE;

                CubicCurveTo cubicTo = new CubicCurveTo();

                cubicTo.setControlX1(posNorteX);
                cubicTo.setControlY1(posOesteY - 60);
                cubicTo.setControlX2(posNorteX);
                cubicTo.setControlY2(posOesteY - 50);
                cubicTo.setX(posOesteX);
                cubicTo.setY(posLesteY);

                animaCurva(caminho, carro, cubicTo, posNorteX, posNorteY, posNorteX, posNorteY + 130,
                        this.tempoEspera, tempo);

                break;
            }
            case "Leste Norte": {//quadrantes B

                tempo = direita;

                origem = Via.LESTE;
                destino = Via.NORTE;

                CubicCurveTo cubicTo = new CubicCurveTo();

                cubicTo.setControlX1(posSulX);
                cubicTo.setControlY1(posNorteY + 200);
                cubicTo.setControlX2(posSulX);
                cubicTo.setControlY2(posNorteY + 180);
                cubicTo.setX(posSulX);
                cubicTo.setY(posNorteY);

                animaCurva(caminho, carro, cubicTo, posLesteX, posLesteY, posLesteX - 130,
                        posLesteY, this.tempoEspera, tempo);

                break;
            }
            case "Sul Leste": {//quadrante D

                tempo = direita;

                origem = Via.SUL;
                destino = Via.LESTE;

                CubicCurveTo cubicTo = new CubicCurveTo();

                cubicTo.setControlX1(posSulX);
                cubicTo.setControlY1(posLesteY + 60);
                cubicTo.setControlX2(posSulX);
                cubicTo.setControlY2(posLesteY + 50);
                cubicTo.setX(posLesteX);
                cubicTo.setY(posOesteY);

                animaCurva(caminho, carro, cubicTo, posSulX, posSulY, posSulX, posSulY - 130, this.tempoEspera, tempo);

                break;
            }
            case "Oeste Sul": {//quadrante C

                tempo = direita;

                origem = Via.OESTE;
                destino = Via.SUL;

                CubicCurveTo cubicTo = new CubicCurveTo();

                cubicTo.setControlX1(posNorteX);
                cubicTo.setControlY1(posOesteY - 10);
                cubicTo.setControlX2(posNorteX);
                cubicTo.setControlY2(posOesteY);
                cubicTo.setX(posNorteX);
                cubicTo.setY(posSulY);

                animaCurva(caminho, carro, cubicTo, posOesteX, posOesteY, posOesteX + 130, posOesteY,
                        this.tempoEspera, tempo);

                break;
            }
            //esquerda
            case "Norte Leste": {//ocupa os quadrantes A, C e D

                tempo = esquerda;

                origem = Via.NORTE;
                destino = Via.LESTE;

                CubicCurveTo cubicTo = new CubicCurveTo();

                cubicTo.setControlX1(posNorteX);
                cubicTo.setControlY1(posOesteY + 80);
                cubicTo.setControlX2(posNorteX);
                cubicTo.setControlY2(posOesteY - 20);
                cubicTo.setX(posLesteX);
                cubicTo.setY(posOesteY);

                animaCurva(caminho, carro, cubicTo, posNorteX, posNorteY, posNorteX, posNorteY + 130, this.tempoEspera, tempo);

                break;
            }
            case "Leste Sul": {//ocupa os quadrantes A, B e C

                tempo = esquerda;

                origem = Via.LESTE;
                destino = Via.SUL;

                CubicCurveTo cubicTo = new CubicCurveTo();

                cubicTo.setControlX1(posNorteX - 50);
                cubicTo.setControlY1(posLesteY);
                cubicTo.setControlX2(posNorteX);
                cubicTo.setControlY2(posLesteY);
                cubicTo.setX(posNorteX);
                cubicTo.setY(posSulY);

                animaCurva(caminho, carro, cubicTo, posLesteX, posLesteY, posLesteX - 130, posLesteY, this.tempoEspera, tempo);

                break;
            }
            case "Sul Oeste": {//ocupa os quadrantes A, B e D

                tempo = esquerda;

                origem = Via.SUL;
                destino = Via.OESTE;

                CubicCurveTo cubicTo = new CubicCurveTo();

                cubicTo.setControlX1(posSulX);
                cubicTo.setControlY1(posLesteY - 50);
                cubicTo.setControlX2(posSulX);
                cubicTo.setControlY2(posLesteY);
                cubicTo.setX(posOesteX);
                cubicTo.setY(posLesteY);

                animaCurva(caminho, carro, cubicTo, posSulX, posSulY, posSulX, posSulY - 130, this.tempoEspera, tempo);

                break;
            }
            case "Oeste Norte": {//ocupa os quadrantes B, C e D

                tempo = esquerda;

                origem = Via.OESTE;
                destino = Via.NORTE;

                CubicCurveTo cubicTo = new CubicCurveTo();

                cubicTo.setControlX1(posSulX + 50);
                cubicTo.setControlY1(posOesteY + 25);
                cubicTo.setControlX2(posSulX);
                cubicTo.setControlY2(posOesteY + 25);
                cubicTo.setX(posSulX);
                cubicTo.setY(posNorteY);
                
                animaCurva(caminho, carro, cubicTo, posOesteX, posOesteY, posOesteX+130, posOesteY, this.tempoEspera, tempo);

                break;
            }
            //origem == destino
            default: {

                root.getChildren().remove(carro);

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Dado errado");
                alert.setHeaderText("A origem é igual ao destino");

                alert.show();
                certo = false;
                break;
            }
        }
    }

    private PathTransition setPath(Path p, Rectangle n, double d) {

        PathTransition pt = new PathTransition();
        pt.setDuration(Duration.millis(d));

        pt.setPath(p);

        pt.setNode(n);

        pt.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);

        pt.play();

        return pt;

    }
}
