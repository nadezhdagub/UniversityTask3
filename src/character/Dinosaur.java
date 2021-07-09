package character;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import main.Game;
import obstacles.Obstacles;

public class Dinosaur extends Pane {
    public static int DINO_SPEED;
    private static Image dinoGif = new Image("character.gif");
    private static ImageView dinoView = new ImageView(dinoGif);
    public Point2D velocity;
    private boolean duck;

    public Dinosaur() {
        dinoView.setFitHeight(50);
        dinoView.setFitWidth(50);
        velocity = new Point2D(0, 0);
        setTranslateX(50);
        getChildren().add(dinoView);
    }

    public static int dinoViewHeight() {
        return (int) dinoView.getFitHeight();
    }

    public void moveY(int value) {
        boolean moveUpOrDown = value > 0;
        for (int i = 0; i < Math.abs(value); i++) {
            Obstacles o = Game.obst.get(0);
            if (this.getBoundsInParent().intersects(o.getBoundsInParent()) &&
                    getTranslateY() == o.getTranslateY() - 10) {
                new Game().lose();
                return;
            }
            final int FLOOR_POSITION;
            if (duck)
                FLOOR_POSITION = Game.WINDOW_HEIGHT / 2 + dinoViewHeight();
            else
                FLOOR_POSITION = Game.WINDOW_HEIGHT / 2;
            if (getTranslateY() > FLOOR_POSITION)
                setTranslateY(FLOOR_POSITION);
            if (getTranslateY() < FLOOR_POSITION - 100)
                setTranslateY(FLOOR_POSITION - 100);
            this.setTranslateY((getTranslateY() + (moveUpOrDown ? 0.4 : -0.4)));
        }
    }

    public void moveX(int value) {
        for (int i = 0; i < value; i++) {
            Obstacles o = Game.obst.get(0);
            setTranslateX(getTranslateX() + 1);
            int dinoX = (int) getTranslateX();
            int oX = (int) o.getTranslateX();
            oX -= oX % dinoX;
            if (this.getBoundsInParent().intersects(o.getBoundsInParent()) && dinoX == oX) {
                new Game().lose();
                return;
            }
            if (oX == 0) {
                Game.score += o.getCount();
                return;
            }

        }
    }

    public void jump() {
        if (getTranslateY() >= Game.WINDOW_HEIGHT / 2)
            velocity = new Point2D(DINO_SPEED, -50);

    }

    public void duck() {
        duck = true;
        getChildren().remove(dinoView);
        dinoView.setFitWidth(25);
        dinoView.setFitHeight(25);
        getChildren().add(dinoView);
    }

    public void normal() {
        duck = false;
        getChildren().remove(dinoView);
        dinoView.setFitWidth(50);
        dinoView.setFitHeight(50);
        setTranslateY(Game.WINDOW_HEIGHT / 2);
        getChildren().add(dinoView);
    }
}
