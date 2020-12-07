package projektc.projektc.projektc;

import projektc.projektc.projektc.R;

/**
 * Created by Annika on 09.02.2018.
 */

public class Ball{
    public float ballSize;
    public float ballSpeed;
    public int image;

    private float startX;
    private float startY;

    public Ball(float ballSize, float ballSpeed, int image){
        this.ballSize = ballSize;
        this.ballSpeed = ballSpeed;
        this.image = image;
    }

    public void setStartX(float startX){
        this.startX = startX;
    }

    public float getStartX() {
        return startX;
    }

    public void setStartY(float startY) {
        this.startY = startY;
    }

    public float getStartY() {
        return startY;
    }

    @Override
    public String toString(){
        return "Der Ball mit der Groesse " + ballSize +
                " ist an der Position: " + getStartX() + " und " + getStartY()+".";
    }
}
