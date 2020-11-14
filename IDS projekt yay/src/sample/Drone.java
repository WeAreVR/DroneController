package sample;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Drone {

    double xPos;
    double yPos;
    double angle = 0;
    double speed = 10;
    double startDroneSize = 50;
    double droneSize = startDroneSize;
    double height = 0;
    double maxHeight = 100;
    boolean inAir = false;
    boolean isDead = false;
    double distance = 0;
    Image droneImage;
    Image droneImageDead;
    ImageView imageView = new ImageView();


    public Label lblYouCrashed;



    public Drone() {
        try {
            droneImage = new javafx.scene.image.Image(new FileInputStream("C:/Users/louis/OneDrive - Roskilde Universitet/RUC/Datalogi/Software Development/IDS projekt yay/flotDroneNoBG.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        try {
            droneImageDead = new javafx.scene.image.Image(new FileInputStream("C:/Users/louis/OneDrive - Roskilde Universitet/RUC/Datalogi/Software Development/IDS projekt yay/flotDroneDeadNoBG.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }


        imageView.setImage(droneImage);
        imageView.setFitHeight(startDroneSize);
        imageView.setFitWidth(startDroneSize);
    }

    public void setStartPos(double x, double y) {
        imageView.setY(y);
        imageView.setX(x);
    }

    public void restart() {
        setPosition(0, 0);
        height = 0;
        inAir = false;
        distance = 0;
        angle = 0;
        imageView.setRotate(angle);
        resize(startDroneSize);
        droneSize = startDroneSize;
        isDead = false;
        imageView.setImage(droneImage);
    }


    public void setHeight(double height) {
        this.height = height;
    }

    public double getHeight() {
        return height;
    }

    public double getDistance() {
        return distance;
    }

    public void collision(){
        isDead = true;
        imageView.setImage(droneImageDead);
    }

    public boolean getIsDead(){
        return isDead;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public double getyPos() {
        return yPos;
    }

    public double getxPos() {
        return xPos;
    }

    public double getDroneSize() {
        return droneSize;
    }

    public String getPosition() {
        return xPos + ", " + yPos;
    }

    public void setPosition(double xPos, double yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
        imageView.setTranslateX(xPos);
        imageView.setTranslateY(yPos);
    }

    public void moveForward() {
        double angleRadian = Math.toRadians(angle);
        xPos += speed * sin(angleRadian);
        yPos -= speed * cos(angleRadian);

        distance += speed;
        setPosition(xPos, yPos);
    }

    public void moveBackwards() {
        double angleRadian = Math.toRadians(angle);
        xPos -= speed * sin(angleRadian);
        yPos += speed * cos(angleRadian);

        distance += speed;
        setPosition(xPos, yPos);
    }

    public void moveLeft() {
        double angleRadian = Math.toRadians(angle);
        xPos -= speed * cos(angleRadian);
        yPos -= speed * sin(angleRadian);

        distance += speed;
        setPosition(xPos, yPos);
    }

    public void moveRight() {
        double angleRadian = Math.toRadians(angle);
        xPos += speed * cos(angleRadian);
        yPos += speed * sin(angleRadian);

        distance += speed;
        setPosition(xPos, yPos);
    }

    public void rotateCW() throws FileNotFoundException {
        if (angle < 359) {
            angle += 10;
        } else {
            angle = 0;
        }
        imageView.setRotate(angle);
    }

    public void rotateCCW() throws FileNotFoundException {
        if (angle > 0) {
            angle -= 10;
        } else {
            angle = 360;
        }
        imageView.setRotate(angle);
    }

    public void down() {
        if (height > 0) {
            height -= 5;
            droneSize -= 7;
            resize(droneSize);
        }
    }

    public void up() {
        if (height < maxHeight) {
            height += 5;
            droneSize += 7;
            resize(droneSize);
        }
    }

    public void land() {
        if (inAir) {
            while (height > 0) {
                down();
            }
            inAir = false;
        }

    }

    public void takeOff() {
        if (!inAir) {
            up();
            inAir = true;
        }
    }


    public boolean isOutsideBounds(double paneWidth, double paneHeight) {
        boolean res;
        if (xPos > (paneWidth / 2) - droneSize / 2 || yPos > (paneHeight / 2 - droneSize / 2) || xPos < (-paneWidth / 2) + droneSize / 2 || yPos < (-paneHeight / 2) + droneSize / 2) {
            res = true;
            isDead = true;
            collision();
        } else {
            res = false;
        }
        return res;
    }

    public void resize(double size) {
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);
    }
}
