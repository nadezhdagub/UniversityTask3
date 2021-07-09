package obstacles.types;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Cactus {
    private final static Image cactusImg = new Image("cactus.gif");
    private ImageView cactusView;

    public Cactus() {
        cactusView = new ImageView(cactusImg);
    }

    public ImageView getCactusView() {
        return cactusView;
    }


}
