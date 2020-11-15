package sample;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

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

    //method to set the starting position of the drone. Is only used when the drone is (re)started
    public void setStartPos(double x, double y) {
        imageView.setY(y);
        imageView.setX(x);
    }

    //restarts the drone. Dronesize, angles etc. is set to starting values (it is landed)
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

    public double getHeight() {
        return height;
    }

    public double getDistance() {
        return distance;
    }

    //When the drone has collided, the image changes and isDead is true
    public void collision(){
        isDead = true;
        imageView.setImage(droneImageDead);
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

    //the drone will move speed length forward according to the angle, which the drone is turned.
    public void moveForward() {
        double angleRadian = Math.toRadians(angle);
        xPos += speed * sin(angleRadian);
        yPos -= speed * cos(angleRadian);

        //increases the distance
        distance += speed;

        setPosition(xPos, yPos);
    }


    //the drone will move speed length backwards according to the angle, which the drone is turned.
    public void moveBackwards() {
        double angleRadian = Math.toRadians(angle);
        xPos -= speed * sin(angleRadian);
        yPos += speed * cos(angleRadian);

        distance += speed;
        setPosition(xPos, yPos);
    }

    //the drone will move speed length to the left according to the angle, which the drone is turned.
    public void moveLeft() {
        double angleRadian = Math.toRadians(angle);
        xPos -= speed * cos(angleRadian);
        yPos -= speed * sin(angleRadian);

        distance += speed;
        setPosition(xPos, yPos);
    }


    //the drone will move speed length right according to the angle, which the drone is turned.
    public void moveRight() {
        double angleRadian = Math.toRadians(angle);
        xPos += speed * cos(angleRadian);
        yPos += speed * sin(angleRadian);

        distance += speed;
        setPosition(xPos, yPos);
    }

    //rotates the drone clockwise
    public void rotateCW() throws FileNotFoundException {
        //is angle get to 360, the angle will reset to 0 (so it doesn't get fore example 540 degrees,
        // which will not work in the methods for moving
        if (angle < 359) {
            angle += 10;
        } else {
            angle = 0;
        }
        imageView.setRotate(angle);
    }

    //rotates the drone counter clockwise
    public void rotateCCW() throws FileNotFoundException {
        //is angle get to 360, the angle will reset to 0 (so it doesn't get fore example 540 degrees,
        // which will not work in the methods for moving
        if (angle > 0) {
            angle -= 10;
        } else {
            angle = 360;
        }
        imageView.setRotate(angle);
    }


    //moves the drone down towards the "ground"
    public void down() {
        //only if the drone is off ground (more than 0 height) will drone go down.
        //the height decreases and the image of the drone is decreased
        if (height > 0) {
            height -= 5;
            droneSize -= 7;
            resize(droneSize);
        }
    }


    //only if the drone is below maxHeight will drone go up.
    //the height increases and the image of the drone gets bigger
    public void up() {
        if (height < maxHeight) {
            height += 5;
            droneSize += 7;
            resize(droneSize);
        }
    }


    //land the drone
    public void land() throws InterruptedException {
        //only is the drone is not landed, can the drone land
        //the height decreases (down()) until the drone reached the "ground" (height = 0).
        //.sleep will add a small delay, which makes the illusion of an animation, where the drone gets smaller and smaller

        if (inAir) {
            while (height > 0) {
                down();
                Thread.sleep(200);
            }
            inAir = false;
        }

    }

    //drone takes off of the "ground
    public void takeOff() {
        //only if the drone is landed, can it take off.
        //the drone goes slightly up and waits for further instructions
        if (!inAir) {
            up();
            inAir = true;
        }
    }


    //checks if the drone is inside/outside the pane. If the drone reached the bounds of pane, it will collide and isDead
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

    //method for resizing the image of drone
    public void resize(double size) {
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);
    }
}
