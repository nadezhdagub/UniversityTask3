package obstacles;

import javafx.scene.layout.Pane;
import obstacles.types.Bird;
import obstacles.types.Cactus;

import java.util.Random;

public class Obstacles extends Pane {
    private int count;

    public Obstacles(int score) {
        if (generate(score) == 0) {
            count = 1;
            getChildren().add(new Cactus().getCactusView());
        } else {
            getChildren().add(new Bird().getBirdView());
            count = 3;
        }
    }

    public int getCount() {
        return count;
    }

    private int generate(int score) {
        if (score < 10)
            return 0;
        else {
            return new Random().nextInt(2);
        }
    }
}
