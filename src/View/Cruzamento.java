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
 * @author Thati
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
        //movimentar carro
        //movimentar(stage);

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
                } catch (IOException ex) {
                    Logger.getLogger(Cruzamento.class.getName()).log(Level.SEVERE, null, ex);
                }
                //desenhar carros dos quais recebeu mensagens
                if (carros != null) {
                    desenharCarros(stage, carros);
                }                
                
                try {
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
        vbLayout.getChildren().addAll(lbOrigem, cbOrigem, lbDestino, cbDestino, btStart, ivBussula,msgs, ta);

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

        float tempo = tempoRestante;

        switch (tipo) {
            //reto
            case "Norte Sul": {

                caminho.getElements().add(new MoveTo(posX, posY));//começa
                caminho.getElements().add(new LineTo(posNorteX, posSulY));//para

                if (posY > posNorteY + 130) {//passou da área crítica

                }

                break;
            }

            case "Sul Norte": {

                caminho.getElements().add(new MoveTo(posX, posY));
                caminho.getElements().add(new LineTo(posSulX, posNorteY));

                break;
            }
            case "Leste Oeste": {

                caminho.getElements().add(new MoveTo(posX, posY));
                caminho.getElements().add(new LineTo(posOesteX, posLesteY));

                break;
            }
            case "Oeste Leste": {

                caminho.getElements().add(new MoveTo(posX, posY));
                caminho.getElements().add(new LineTo(posLesteX, posOesteY));

                break;
            }
            //direita
            case "Norte Oeste": {//Ocupa o quadrante A

                caminho.getElements().add(new MoveTo(posX, posY));//começa

                CubicCurveTo cubicTo = new CubicCurveTo();

                cubicTo.setControlX1(posNorteX);
                cubicTo.setControlY1(posOesteY - 60);
                cubicTo.setControlX2(posNorteX);
                cubicTo.setControlY2(posOesteY - 50);
                cubicTo.setX(posOesteX);
                cubicTo.setY(posLesteY);

                caminho.getElements().add(cubicTo);

                break;
            }
            case "Leste Norte": {//quadrantes B

                caminho.getElements().add(new MoveTo(posX, posY));//começa                

                CubicCurveTo cubicTo = new CubicCurveTo();

                cubicTo.setControlX1(posSulX);
                cubicTo.setControlY1(posNorteY + 200);
                cubicTo.setControlX2(posSulX);
                cubicTo.setControlY2(posNorteY + 180);
                cubicTo.setX(posSulX);
                cubicTo.setY(posNorteY);

                break;
            }
            case "Sul Leste": {//quadrante D

                caminho.getElements().add(new MoveTo(posX, posY));//começa

                CubicCurveTo cubicTo = new CubicCurveTo();

                cubicTo.setControlX1(posSulX);
                cubicTo.setControlY1(posLesteY + 60);
                cubicTo.setControlX2(posSulX);
                cubicTo.setControlY2(posLesteY + 50);
                cubicTo.setX(posLesteX);
                cubicTo.setY(posOesteY);

                caminho.getElements().add(cubicTo);
                break;
            }
            case "Oeste Sul": {//quadrante C

                caminho.getElements().add(new MoveTo(posX, posY));

                CubicCurveTo cubicTo = new CubicCurveTo();

                cubicTo.setControlX1(posNorteX);
                cubicTo.setControlY1(posOesteY - 10);
                cubicTo.setControlX2(posNorteX);
                cubicTo.setControlY2(posOesteY);
                cubicTo.setX(posNorteX);
                cubicTo.setY(posSulY);

                caminho.getElements().add(cubicTo);

                break;
            }
            //esquerda
            case "Norte Leste": {//ocupa os quadrantes A, C e D

                caminho.getElements().add(new MoveTo(posX, posY));

                CubicCurveTo cubicTo = new CubicCurveTo();

                cubicTo.setControlX1(posNorteX);
                cubicTo.setControlY1(posOesteY + 110);
                cubicTo.setControlX2(posNorteX);
                cubicTo.setControlY2(posOesteY - 20);
                cubicTo.setX(posLesteX);
                cubicTo.setY(posOesteY);

                caminho.getElements().add(cubicTo);

                break;
            }
            case "Leste Sul": {//ocupa os quadrantes A, B e C

                caminho.getElements().add(new MoveTo(posX, posY));

                CubicCurveTo cubicTo = new CubicCurveTo();

                cubicTo.setControlX1(posNorteX - 90);
                cubicTo.setControlY1(posLesteY);
                cubicTo.setControlX2(posNorteX);
                cubicTo.setControlY2(posLesteY);
                cubicTo.setX(posNorteX);
                cubicTo.setY(posSulY);

                caminho.getElements().add(cubicTo);

                break;
            }
            case "Sul Oeste": {//ocupa os quadrantes A, B e D

                caminho.getElements().add(new MoveTo(posX, posY));

                CubicCurveTo cubicTo = new CubicCurveTo();

                cubicTo.setControlX1(posSulX);
                cubicTo.setControlY1(posLesteY - 90);
                cubicTo.setControlX2(posSulX);
                cubicTo.setControlY2(posLesteY);
                cubicTo.setX(posOesteX);
                cubicTo.setY(posLesteY);

                caminho.getElements().add(cubicTo);

                break;
            }
            case "Oeste Norte": {//ocupa os quadrantes B, C e D

                caminho.getElements().add(new MoveTo(posX, posY));

                CubicCurveTo cubicTo = new CubicCurveTo();

                cubicTo.setControlX1(posSulX + 80);
                cubicTo.setControlY1(posOesteY + 25);
                cubicTo.setControlX2(posSulX);
                cubicTo.setControlY2(posOesteY + 25);
                cubicTo.setX(posSulX);
                cubicTo.setY(posNorteY);

                caminho.getElements().add(cubicTo);

                break;
            }
        }

        setPath(caminho, carro, tempo);

    }

    private void criarCarro(Stage stage, Group root) {

        carro = new Rectangle(comprimento, largura, Color.CYAN);

        root.getChildren().add(carro);

    }

    public void animar(Rectangle carro, String tipo, float tempoEspera) throws IOException {
        Via origem = null, destino = null;

        boolean certo = true;

        int reto = 4000;
        int direita = 6000;        
        int esquerda = 8000;

        Path caminho = new Path();

        if(this.tempoEspera == 0){
            this.tempoEspera = 3000;
        }
        
        switch (tipo) {
            //reto
            case "Norte Sul": {

                tempo = reto;

                origem = Via.NORTE;
                destino = Via.SUL;

                caminho.getElements().add(new MoveTo(posNorteX, posNorteY));//começa
                //espera no cruzamento

                caminho.getElements().add((new LineTo(posNorteX, posNorteY + 130)));

                PathTransition pt = setPath(caminho, carro, this.tempoEspera);

                caminho.getElements().clear();

                pt.setOnFinished(evento -> {
                    caminho.getElements().add(new MoveTo(posNorteX, posNorteY + 130));//começa

                    caminho.getElements().add(new LineTo(posNorteX, posSulY));//para

                    PathTransition pt1 = setPath(caminho, carro, tempo);
                    pt1.setOnFinished(ev -> {
                        root.getChildren().remove(carro);
                    });

                });

                break;
            }

            case "Sul Norte": {

                tempo = reto;

                origem = Via.SUL;
                destino = Via.NORTE;

                caminho.getElements().add(new MoveTo(posSulX, posSulY));
                caminho.getElements().add(new LineTo(posSulX, posSulY - 130));

                PathTransition pt = setPath(caminho, carro, this.tempoEspera);

                caminho.getElements().clear();

                pt.setOnFinished(evento -> {

                    caminho.getElements().add(new MoveTo(posSulX, posSulY - 130));
                    caminho.getElements().add(new LineTo(posSulX, posNorteY));
                    PathTransition pt1 = setPath(caminho, carro, tempo);
                    pt1.setOnFinished(ev -> {
                        root.getChildren().remove(carro);
                    });
                });

                break;
            }
            case "Leste Oeste": {

                origem = Via.LESTE;
                destino = Via.OESTE;

                caminho.getElements().add(new MoveTo(posLesteX, posLesteY));
                caminho.getElements().add(new LineTo(posLesteX - 130, posLesteY));
                tempo = reto;

                PathTransition pt = setPath(caminho, carro, this.tempoEspera);

                caminho.getElements().clear();

                pt.setOnFinished(evento -> {
                    caminho.getElements().add(new MoveTo(posLesteX - 130, posLesteY));
                    caminho.getElements().add(new LineTo(posOesteX, posLesteY));
                    PathTransition pt1 = setPath(caminho, carro, tempo);

                    pt1.setOnFinished(ev -> {
                        root.getChildren().remove(carro);
                    });
                });

                break;
            }
            case "Oeste Leste": {

                tempo = reto;

                origem = Via.OESTE;
                destino = Via.LESTE;

                caminho.getElements().add(new MoveTo(posOesteX, posOesteY));
                caminho.getElements().add(new MoveTo(posOesteX + 130, posOesteY));

                PathTransition pt = setPath(caminho, carro, this.tempoEspera);
                caminho.getElements().clear();

                pt.setOnFinished(evento -> {
                    caminho.getElements().add(new MoveTo(posOesteX + 130, posOesteY));
                    caminho.getElements().add(new LineTo(posLesteX, posOesteY));
                    PathTransition pt1 = setPath(caminho, carro, tempo);

                    pt1.setOnFinished(ev -> {
                        root.getChildren().remove(carro);
                    });
                });

                break;
            }
            //direita
            case "Norte Oeste": {//Ocupa o quadrante A

                tempo = direita;

                origem = Via.NORTE;
                destino = Via.OESTE;

                caminho.getElements().add(new MoveTo(posNorteX, posNorteY));//começa

                caminho.getElements().add((new LineTo(posNorteX, posNorteY + 130)));

                PathTransition pt = setPath(caminho, carro, this.tempoEspera);

                caminho.getElements().clear();

                pt.setOnFinished(evento -> {

                    caminho.getElements().add(new MoveTo(posNorteX, posNorteY + 130));

                    CubicCurveTo cubicTo = new CubicCurveTo();

                    cubicTo.setControlX1(posNorteX);
                    cubicTo.setControlY1(posOesteY - 60);
                    cubicTo.setControlX2(posNorteX);
                    cubicTo.setControlY2(posOesteY - 50);
                    cubicTo.setX(posOesteX);
                    cubicTo.setY(posLesteY);

                    caminho.getElements().add(cubicTo);

                    PathTransition pt1 = setPath(caminho, carro, tempo);
                    pt1.setOnFinished(ev -> {
                        root.getChildren().remove(carro);
                    });
                });

                break;
            }
            case "Leste Norte": {//quadrantes B

                caminho.getElements().add(new MoveTo(posLesteX, posLesteY));//começa                

                CubicCurveTo cubicTo = new CubicCurveTo();

                cubicTo.setControlX1(posSulX);
                cubicTo.setControlY1(posNorteY + 200);
                cubicTo.setControlX2(posSulX);
                cubicTo.setControlY2(posNorteY + 180);
                cubicTo.setX(posSulX);
                cubicTo.setY(posNorteY);

                caminho.getElements().add(cubicTo);

                tempo = direita;

                origem = Via.LESTE;
                destino = Via.NORTE;

                break;
            }
            case "Sul Leste": {//quadrante D

                caminho.getElements().add(new MoveTo(posSulX, posSulY));//começa

                CubicCurveTo cubicTo = new CubicCurveTo();

                cubicTo.setControlX1(posSulX);
                cubicTo.setControlY1(posLesteY + 60);
                cubicTo.setControlX2(posSulX);
                cubicTo.setControlY2(posLesteY + 50);
                cubicTo.setX(posLesteX);
                cubicTo.setY(posOesteY);

                caminho.getElements().add(cubicTo);

                tempo = direita;

                origem = Via.SUL;
                destino = Via.LESTE;

                break;
            }
            case "Oeste Sul": {//quadrante C

                caminho.getElements().add(new MoveTo(posOesteX, posOesteY));

                CubicCurveTo cubicTo = new CubicCurveTo();

                cubicTo.setControlX1(posNorteX);
                cubicTo.setControlY1(posOesteY - 10);
                cubicTo.setControlX2(posNorteX);
                cubicTo.setControlY2(posOesteY);
                cubicTo.setX(posNorteX);
                cubicTo.setY(posSulY);

                caminho.getElements().add(cubicTo);

                tempo = direita;

                origem = Via.OESTE;
                destino = Via.SUL;

                break;
            }
            //esquerda
            case "Norte Leste": {//ocupa os quadrantes A, C e D

                caminho.getElements().add(new MoveTo(posNorteX, posNorteY));

                CubicCurveTo cubicTo = new CubicCurveTo();

                cubicTo.setControlX1(posNorteX);
                cubicTo.setControlY1(posOesteY + 110);
                cubicTo.setControlX2(posNorteX);
                cubicTo.setControlY2(posOesteY - 20);
                cubicTo.setX(posLesteX);
                cubicTo.setY(posOesteY);

                caminho.getElements().add(cubicTo);

                tempo = esquerda;

                origem = Via.NORTE;
                destino = Via.LESTE;

                break;
            }
            case "Leste Sul": {//ocupa os quadrantes A, B e C

                caminho.getElements().add(new MoveTo(posLesteX, posLesteY));

                CubicCurveTo cubicTo = new CubicCurveTo();

                cubicTo.setControlX1(posNorteX - 90);
                cubicTo.setControlY1(posLesteY);
                cubicTo.setControlX2(posNorteX);
                cubicTo.setControlY2(posLesteY);
                cubicTo.setX(posNorteX);
                cubicTo.setY(posSulY);

                caminho.getElements().add(cubicTo);

                tempo = esquerda;

                origem = Via.LESTE;
                destino = Via.SUL;

                break;
            }
            case "Sul Oeste": {//ocupa os quadrantes A, B e D

                caminho.getElements().add(new MoveTo(posSulX, posSulY));

                CubicCurveTo cubicTo = new CubicCurveTo();

                cubicTo.setControlX1(posSulX);
                cubicTo.setControlY1(posLesteY - 90);
                cubicTo.setControlX2(posSulX);
                cubicTo.setControlY2(posLesteY);
                cubicTo.setX(posOesteX);
                cubicTo.setY(posLesteY);

                caminho.getElements().add(cubicTo);

                tempo = esquerda;

                origem = Via.SUL;
                destino = Via.OESTE;

                break;
            }
            case "Oeste Norte": {//ocupa os quadrantes B, C e D

                caminho.getElements().add(new MoveTo(posOesteX, posOesteY));

                CubicCurveTo cubicTo = new CubicCurveTo();

                cubicTo.setControlX1(posSulX + 80);
                cubicTo.setControlY1(posOesteY + 25);
                cubicTo.setControlX2(posSulX);
                cubicTo.setControlY2(posOesteY + 25);
                cubicTo.setX(posSulX);
                cubicTo.setY(posNorteY);

                caminho.getElements().add(cubicTo);

                tempo = esquerda;

                origem = Via.OESTE;
                destino = Via.NORTE;

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
