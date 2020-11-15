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
import java.net.SocketException;

public class Controller {
    public Label textHeight;
    public Label labelHeight;
    public Pane pane;
    public Button BTNHøjre;
    public Button BTNNed;
    public Button BTNStart;
    Image characterPicture;
    static Drone drone;
    static double paneHeight;
    static double paneWidth;


    public void initialize() throws FileNotFoundException, SocketException {
        // runs when application GUI is ready
        System.out.println("ready!");

        drone = new Drone();

        // set background to POWDERBLUE
        pane.setBackground(new Background(new BackgroundFill(Color.POWDERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        paneHeight = pane.getHeight();
        paneWidth = pane.getWidth();
    }

    public void restart() {
        if (pane.getChildren() != null) {
            pane.getChildren().remove(drone.getImageView());
        }

        drone.restart();
        double startX = pane.getWidth() / 2 - (drone.getDroneSize() / 2);
        double startY = pane.getHeight() / 2 - (drone.getDroneSize() / 2);
        drone.setStartPos(startX, startY);
        pane.getChildren().add(drone.getImageView());
    }


    public void ned() throws FileNotFoundException {
        labelHeight.setText("Height: " + drone.getHeight());
        textHeight.setText("");

        /*
        // If the image hits the bounds of pane, DON'T MOVE
        if (imageView1.getTranslateY() > (pane.getHeight() / 2) - imageView1.getFitHeight() / 2) {
            System.out.println("hov. EXPLOSION****");
        } else {
            imageView1.setTranslateY(imageView1.getTranslateY() + 5);

            imageView1.setRotate(imageView1.getRotate() + 5);
        }*/
    }


    public void rotateCW() throws FileNotFoundException {
        drone.rotateCW();

    }

    public void move() {
        // If the image hits the bounds of pane, DON'T MOVE
        if (drone.isOutsideBounds(pane.getWidth(), pane.getHeight())) {
            System.out.println("EXPLOSION**** #soundeffects");
        } else {
            drone.up();
        }
    }

    public static void doStuff(String command) throws FileNotFoundException {
        if (drone.inAir || command.equals("takeOff")) {
            switch (command) {
                case "land":
                    drone.land();
                    break;
                case "takeOff":
                    drone.takeOff();
                    break;
                case "up":
                    drone.up();
                    break;
                case "down":
                    drone.down();
                    break;
                case "forward":
                    drone.moveForward();
                    break;
                case "back":
                    drone.moveBackwards();
                    break;
                case "cw":
                    drone.rotateCW();
                    break;
                case "ccw":
                    drone.rotateCCW();
                    break;
                case "left":
                    drone.moveLeft();
                    break;
                case "right":
                    drone.moveRight();
                    break;
                case " ":
                    System.out.printf("mellemrum");
                default:
                    System.out.println("Invalid command");

            }
            System.out.println("Height: " + drone.getHeight() + " inAir: " + drone.inAir);
        }
    }
}


