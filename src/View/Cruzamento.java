package View;

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

    private Group root;
    private final float posNorteX = 228;
    private final float posNorteY = 30;
    private final float posSulX = 290;
    private final float posSulY = 465;
    private final float posLesteX = 470;
    private final float posLesteY = 220;
    private final float posOesteX = 30;
    private final float posOesteY = 280;

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
        ivBussula.setFitHeight(200);
        ivBussula.setFitWidth(200);

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
            public void handle(ActionEvent t) {
                String origem, destino;
                origem = cbOrigem.getValue().toString();
                destino = cbDestino.getValue().toString();
                criarCarro(stage, root, origem, destino);
            }
        });

        vbLayout.getChildren().addAll(lbOrigem, cbOrigem, lbDestino, cbDestino, btStart, ivBussula);

        Scene cena = new Scene(hbLayout, 800, 500);

        stage.setTitle("Cruzamento");
        stage.setScene(cena);
        stage.setResizable(false);
        stage.show();

    }

    private void criarCarro(Stage stage, Group root, String origem, String destino) {

        Rectangle carro = new Rectangle(40, 30, Color.CYAN);

        root.getChildren().add(carro);

        animar(carro, origem + " " + destino);

    }

    public void animar(Rectangle carro, String tipo) {

        Path caminho = new Path();
               
        switch (tipo) {
            //reto
            case "Norte Sul": {

                caminho.getElements().add(new MoveTo(posNorteX, posNorteY));//começa
                caminho.getElements().add(new LineTo(posNorteX, posSulY));//para
                setPath(caminho, carro, 4000);

                break;
            }

            case "Sul Norte": {

                caminho.getElements().add(new MoveTo(posSulX, posSulY));
                caminho.getElements().add(new LineTo(posSulX, posNorteY));
                setPath(caminho, carro, 4000);

                break;
            }
            case "Leste Oeste": {

                caminho.getElements().add(new MoveTo(posLesteX, posLesteY));
                caminho.getElements().add(new LineTo(posOesteX, posLesteY));
                setPath(caminho, carro, 4000);

                break;
            }
            case "Oeste Leste": {

                caminho.getElements().add(new MoveTo(posOesteX, posOesteY));
                caminho.getElements().add(new LineTo(posLesteX, posOesteY));
                setPath(caminho, carro, 4000);

                break;
            }
            //direita
            case "Norte Oeste": {//Ocupa o quadrante A

                caminho.getElements().add(new MoveTo(posNorteX, posNorteY));//começa

                CubicCurveTo cubicTo = new CubicCurveTo();

                cubicTo.setControlX1(posNorteX);
                cubicTo.setControlY1(posOesteY - 60);
                cubicTo.setControlX2(posNorteX);
                cubicTo.setControlY2(posOesteY - 50);
                cubicTo.setX(posOesteX);
                cubicTo.setY(posLesteY);

                caminho.getElements().add(cubicTo);

                setPath(caminho, carro, 4000);

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

                setPath(caminho, carro, 4000);

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

                setPath(caminho, carro, 4000);

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

                setPath(caminho, carro, 4000);

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

                setPath(caminho, carro, 4000);

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

                setPath(caminho, carro, 4000);

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

                setPath(caminho, carro, 4000);

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

                setPath(caminho, carro, 4000);

                break;
            }
            //origem == destino
            default: {
                
                root.getChildren().remove(carro);
                
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Dado errado");                
                alert.setHeaderText("A origem é igual ao destino");

                alert.show();
                break;
            }
        }

    }

    private void setPath(Path p, Rectangle n, double d) {

        PathTransition pt = new PathTransition();
        pt.setDuration(Duration.millis(4000));

        pt.setPath(p);

        pt.setNode(n);

        pt.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);

        pt.play();

        pt.setOnFinished(evento -> {
            root.getChildren().remove(n);
        });

    }
}
