package sample;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.FileNotFoundException;
import java.net.SocketException;

public class Controller {
    public Label lblHeight;
    public Label lblDistance;
    public Pane pane;
    public Button btnStart;
    static Drone drone;
    static double paneHeight;
    static double paneWidth;
    public Label lblYouCrashed;


    public void initialize() throws FileNotFoundException, SocketException {
        // runs when application GUI is ready
        System.out.println("ready!");

        drone = new Drone();

        // set background to POWDERBLUE
        pane.setBackground(new Background(new BackgroundFill(Color.POWDERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void restart() {
        if (pane.getChildren() != null) {
            pane.getChildren().remove(drone.getImageView());
        }
        paneHeight = pane.getHeight();
        paneWidth = pane.getWidth();

        updateLabels();
        lblYouCrashed.setText(" ");

        drone.restart();
        double startX = pane.getWidth() / 2 - (drone.getDroneSize() / 2);
        double startY = pane.getHeight() / 2 - (drone.getDroneSize() / 2);
        drone.setStartPos(startX, startY);
        pane.getChildren().add(drone.getImageView());
    }

    public void move() {
        // If the image hits the bounds of pane, DON'T MOVE
        if (drone.isOutsideBounds(paneWidth, paneHeight)) {
            System.out.println("EXPLOSION**** #soundeffects");
            lblYouCrashed.setText("You crashed! Try again...");
        } else {
            drone.moveForward();
        }
        updateLabels();
    }

    public void up() {
        // If the image hits the bounds of pane, DON'T MOVE
        if (drone.isOutsideBounds(paneWidth, paneHeight)) {
            System.out.println("EXPLOSION**** #soundeffects");
        } else {
            drone.up();
        }
        updateLabels();
    }

    public void down() {
        // If the image hits the bounds of pane, DON'T MOVE
        if (drone.isOutsideBounds(paneWidth, paneHeight)) {
            System.out.println("EXPLOSION**** #soundeffects");
        } else {
            drone.down();
        }
        updateLabels();
    }


    public void cw() throws FileNotFoundException {
        // If the image hits the bounds of pane, DON'T MOVE
        if (drone.isOutsideBounds(paneWidth, paneHeight)) {
            System.out.println("EXPLOSION**** #soundeffects");
        } else {
            drone.rotateCW();
        }
        updateLabels();
    }


    public void ccw() throws FileNotFoundException {
        // If the image hits the bounds of pane, DON'T MOVE
        if (drone.isOutsideBounds(paneWidth, paneHeight)) {
            System.out.println("EXPLOSION**** #soundeffects");
        } else {
            drone.rotateCCW();
        }
        updateLabels();
    }

    public void updateLabels(){
        lblHeight.setText("" + drone.getHeight());
        lblDistance.setText("" + drone.getDistance());
    }


    public static void doStuff(String command) throws FileNotFoundException {
        if (!drone.isOutsideBounds(paneWidth, paneHeight)) {
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
                    default:
                        System.out.println("Invalid command");
                }
            }
            System.out.println("Command: " + command);
            System.out.println("Height: " + drone.getHeight() + " inAir: " + drone.inAir + " PaneWidth: " + paneWidth);
        }
    }
}


