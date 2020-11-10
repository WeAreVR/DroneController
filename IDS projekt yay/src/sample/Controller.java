package sample;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Controller {
    public Label textHeight;
    public Label labelHeight;
    public Pane pane;
    public Button BTNHøjre;
    public Button BTNNed;
    public Button BTNStart;
    Image characterPicture;

    {
        try {
            characterPicture = new Image(new FileInputStream("C:/Users/louis/OneDrive - Roskilde Universitet/RUC/Datalogi/Software Development/IDS projekt yay/flotDrone.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    ImageView imageView = new ImageView();

    public void initialize() throws FileNotFoundException {
        // runs when application GUI is ready
        System.out.println("ready!");

        // set image and image size
        imageView.setImage(characterPicture);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);

        // set background to POWDERBLUE
        pane.setBackground(new Background(new BackgroundFill(Color.POWDERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void start() {
        if(pane.getChildren() != null) {
            pane.getChildren().remove(imageView);
            imageView.setTranslateX(0);
            imageView.setTranslateY(0);
        }

        // Set start position for imageView
        System.out.println(imageView.getFitHeight());
        imageView.setX(pane.getWidth() / 2 - (imageView.getFitWidth() / 2));
        imageView.setY(pane.getHeight() / 2 - (imageView.getFitHeight() / 2));
        pane.getChildren().add(imageView);
    }




    public void ned() throws FileNotFoundException {
        labelHeight.setText("TranslateX: " + imageView.getTranslateX() + "\nTranslateY: " + imageView.getTranslateY());
        textHeight.setText("X: " + imageView.getX() + "\nY: " + imageView.getY());

        // If the image hits the bounds of pane, DON'T MOVE
        if (imageView.getTranslateY() > (pane.getHeight() / 2) - imageView.getFitHeight() / 2) {
            System.out.println("hov. EXPLOSION****");
        } else {
            imageView.setTranslateY(imageView.getTranslateY() + 5);

            imageView.setRotate(imageView.getRotate() + 90);
        }
    }

    public void højre() throws FileNotFoundException {
        labelHeight.setText("TranslateX: " + imageView.getTranslateX() + "\nTranslateY: " + imageView.getTranslateY());
        textHeight.setText("X: " + imageView.getX() + "\nY: " + imageView.getY());

        // If the image hits the bounds of pane, DON'T MOVE
        if (imageView.getTranslateX() > (pane.getWidth() / 2) - imageView.getFitWidth() / 2) {
            System.out.println("hov. EXPLOSION****");
        } else {
            imageView.setTranslateX(imageView.getTranslateX() + 10);
        }
    }
}

