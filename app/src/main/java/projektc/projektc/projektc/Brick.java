package projektc.projektc.projektc;

import android.widget.ImageView;

/**
 * Created by Annika on 13.02.2018.
 */

public class Brick {
    public boolean isBounceable;
    public int lifePoints;
    public int image;
    public float x;
    public float y;
    //Positionen nicht wirkliche Höhe und Preite
    public float w;
    public float h;


     public Brick(int lifePoints, int image) {
         if (image < 5) {
             this.isBounceable = false; //Bei durchsichtigen Bricks keine Kollision möglich
         } else{
             this.isBounceable = true; //Bei farbigen Bricks Kollision möglich
         }
         this.lifePoints = lifePoints;
         if (lifePoints ==0){
             this.image =0;
         } else if (image == 19){
             this.lifePoints = lifePoints*2;
         }
         this.image = image;

     }



}
