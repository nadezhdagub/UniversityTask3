package obstacles.types;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Bird {
    private final static Image birdImg = new Image("bird.gif");
    private ImageView birdView;

    public Bird() {
        birdView = new ImageView(birdImg);
    }

    public ImageView getBirdView() {
        return birdView;
    }
}
