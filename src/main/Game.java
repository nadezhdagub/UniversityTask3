package main;

import character.Dinosaur;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import obstacles.Obstacles;

import java.util.ArrayList;

public class Game extends Application {
    public static int WINDOW_HEIGHT;
    public static int WINDOW_WIDTH;
    public static int score = 0;
    public static ArrayList<Obstacles> obst = new ArrayList<>();
    private static int counter;
    private static AnimationTimer timer;
    private static Pane gameRoot = new Pane();
    private static Pane appRoot = new Pane();
    private static Stage gameStage = new Stage();
    private Dinosaur dino = new Dinosaur();
    private Pane floor = new Pane();
    private Label scoreLabel = new Label("Score:" + score);

    public static void main(String[] args) {
        launch(args);
    }

    public void lose() {
        Stage loseStage = new Stage();
        loseStage.setResizable(false);
        HBox loseContent = new HBox();
        loseContent.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        loseContent.setAlignment(Pos.CENTER);
        Label loseLabel = new Label("You lose!\nScore: " + score + "\nPress ENTER to restart!");
        loseContent.getChildren().add(loseLabel);
        Scene loseScene = new Scene(loseContent);
        loseScene.setOnKeyPressed(key -> {
            if (key.getCode().equals(KeyCode.ENTER)) {
                score = 0;
                gameRoot.getChildren().removeAll(floor, dino);
                appRoot.getChildren().removeAll(gameRoot, scoreLabel);
                gameRoot = new Pane();
                appRoot = new Pane();
                dino = new Dinosaur();
                loseStage.close();
                game();
            }
        });
        loseStage.setScene(loseScene);
        loseStage.setTitle("Chrome dinosaur-like game");
        timer.stop();
        gameStage.close();
        loseStage.show();
    }

    private Parent createContent() {
        floor.getChildren().add(
                new Rectangle(WINDOW_WIDTH, 10, Color.rgb(192, 192, 192, 0.4)));
        floor.setLayoutX(0);
        floor.setLayoutY(WINDOW_HEIGHT / 2 + Dinosaur.dinoViewHeight());
        addObstacle();
        scoreLabel.setTextFill(Color.web("#ffffff"));
        gameRoot.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        gameRoot.getChildren().addAll(floor, dino);
        appRoot.getChildren().addAll(gameRoot, scoreLabel);
        appRoot.setBackground(
                new Background(
                        new BackgroundImage(
                                new Image("background.png",
                                        WINDOW_WIDTH, WINDOW_HEIGHT, false, true),
                                BackgroundRepeat.REPEAT,
                                BackgroundRepeat.NO_REPEAT,
                                BackgroundPosition.DEFAULT,
                                BackgroundSize.DEFAULT)));
        return appRoot;
    }

    private void update() {
        if (dino.velocity.getY() < 40)//gravitation
            dino.velocity = dino.velocity.add(0, 3.5);
        dino.moveX((int) dino.velocity.getX());
        dino.moveY((int) dino.velocity.getY());
        scoreLabel.setText("Score:" + score);
        dino.translateXProperty().addListener((obs, old, newValue) -> {
            gameRoot.setLayoutX(-dino.getTranslateX() + 50);
            floor.setLayoutX(dino.getTranslateX() - 50);
        });
        int dinoX = (int) dino.getTranslateX();
        int oX = (int) obst.get(0).getTranslateX();
        oX -= oX % dinoX;
        if (oX == 0) {
            addObstacle();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        VBox fields = new VBox();
        root.setPrefSize(300, 200);
        Label labelRes = new Label("Choose resolution");
        Label labelSpeed = new Label("Choose speed");
        TextField widthTF = new TextField();
        widthTF.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
        widthTF.setPromptText("Width");
        TextField heigthTF = new TextField();
        heigthTF.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
        heigthTF.setPromptText("Height");
        ChoiceBox speedBox = new ChoiceBox();
        for (int i = 5; i < 10; i++) {
            switch (i) {
                case 5:
                    speedBox.getItems().add(Integer.toString(i) + " Easy");
                    break;
                case 7:
                    speedBox.getItems().add(Integer.toString(i) + " Normal");
                    break;
                case 9:
                    speedBox.getItems().add(Integer.toString(i) + " Hard");
                    break;
                default:
                    speedBox.getItems().add(Integer.toString(i));
            }
        }
        speedBox.setValue(speedBox.getItems().get(0));
        fields.setMaxWidth(200);
        fields.getChildren().addAll(widthTF, heigthTF, labelSpeed, speedBox);
        Button btn = new Button("Done");
        root.getChildren().addAll(labelRes, fields, btn);
        root.setAlignment(Pos.CENTER);
        btn.setOnAction(e -> {
            ArrayList values = new ArrayList();
            values.add(widthTF.getText());
            values.add(heigthTF.getText());
            values.add(speedBox.getSelectionModel().getSelectedItem().toString());
            if ((!values.get(0).equals("")
                    && !values.get(1).equals("")) &&
                    Integer.parseInt(values.get(0).toString()) >= 300
                            && Integer.parseInt(values.get(1).toString()) >= 200) {
                WINDOW_WIDTH = Integer.parseInt(values.get(0).toString());
                WINDOW_HEIGHT = Integer.parseInt(values.get(1).toString());
                Dinosaur.DINO_SPEED = Integer.parseInt(values.get(2).toString().substring(0, 1));
                primaryStage.close();
                game();
            }
        });
        Scene scene = new Scene(root);
        primaryStage.setTitle("Chrome dinosaur-like game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void game() {
        dino.setTranslateY(Game.WINDOW_HEIGHT - 60);
        counter = 0;
        Scene scene = new Scene(createContent());
        scene.setOnKeyPressed(key -> {
            if (key.getCode().equals(KeyCode.DOWN) ||
                    key.getCode().equals(KeyCode.S)) {
                dino.duck();
            }
        });
        scene.setOnKeyReleased(key -> {
            if (key.getCode().equals(KeyCode.SPACE) ||
                    key.getCode().equals(KeyCode.UP) ||
                    key.getCode().equals(KeyCode.W)) {
                dino.jump();
            }
            if (key.getCode().equals(KeyCode.DOWN) ||
                    key.getCode().equals(KeyCode.S)) {
                dino.normal();
            }
        });
        gameStage.setResizable(false);
        gameStage.setTitle("Chrome dinosaur-like game");
        gameStage.setScene(scene);
        gameStage.show();
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();
        dino.velocity = new Point2D(Dinosaur.DINO_SPEED, 0);
    }

    private void addObstacle() {
        Obstacles obstacle = new Obstacles(counter);
        counter++;
        obst.add(obstacle);
        obstacle.setTranslateX(counter * WINDOW_WIDTH + (Math.random() * 50 + 250));
        obstacle.setTranslateY(WINDOW_HEIGHT / 2);
        gameRoot.getChildren().add(obstacle);
        if (obst.size() == 2) {
            obst.remove(0);
        }
    }
}
