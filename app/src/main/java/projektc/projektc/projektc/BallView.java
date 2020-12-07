package projektc.projektc.projektc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static android.graphics.Bitmap.createScaledBitmap;

/**
 * Created by naqibfaizy on 13.12.17.
 * Diese Klasse repräsentiert den Ball.
 * Der Ball fliegt nach oben und soll von den Wänden abprallen.
 * Der Ball zerstört zudem die Bricks.
 */

public class BallView extends View implements View.OnClickListener{

    private static final int[] ballImage = { R.drawable.ball, R.drawable.kugel1, R.drawable.kugel2,
            R.drawable.kugel3, R.drawable.kugel4,
            R.drawable.kugel5, R.drawable.kugel6};
    private float x,y,vx,vy,size;
    private ImageView view;
    float minX = 0;
    float minY = 0;
    float maxX;
    float maxY;
    BrickView brickview;

    private static final int[] images1 = { R.drawable.black,R.drawable.black,R.drawable.black,
            R.drawable.black,R.drawable.black, R.drawable.brick1, R.drawable.brick2,
            R.drawable.brick3, R.drawable.brick4, R.drawable.brick5, R.drawable.brick6,
            R.drawable.brick7, R.drawable.brick8, R.drawable.brick9, R.drawable.brick10,
            R.drawable.brick11, R.drawable.brick12, R.drawable.brick13, R.drawable.brick14,
            R.drawable.brick15};

    public BallView(Context c, FrameLayout container, Ball ball, Drawable ballDrawable){
        super(c);
        ball.setStartX(((container.getWidth()/2)-(ball.ballSize/2)));
        ball.setStartY((container.getHeight()-ball.ballSize));
        size = ball.ballSize;
        x = ball.getStartX();
        y = ball.getStartY();
        vx = -ball.ballSpeed*50/1000; //An die Framerate angepasst
        vy = -ball.ballSpeed*50/1000;
        view = new ImageView(container.getContext());
        view.setImageDrawable(ballDrawable);
        container.addView(view);
        maxX=container.getWidth();
        maxY=container.getHeight();
        move(ball);
    }

    public void move(Ball ball) {
        x += vx;
        y += vy;
        //Kollision mit Rändern
        if (x<minX) {
            vx=-vx; // linke Wand
        }
        if (y<minY) {
            vy=-vy; // obere Wand
        }
        if (x>maxX-ball.ballSize) {
            vx=-vx; // rechte Wand
        }
        if (y>maxY-ball.ballSize) {
            vy=-vy; // untere Wand
        }
        //Kollision mit Bricks
        if(x>images1[0]) {
            vx=-vx; // links oben
        }
        if(y==images1[0]) {
            vy=-vy;
        }
        if(maxX-ball.ballSize==images1[0]) { //max macht es schneller
            vx=-vx;
        }
        if(maxY-ball.ballSize==images1[0]) { // vllt random.nextBrick
            vy=-vy;
        }

        /*for (int i=0; i<brickview.bricks.size(); i++){
            Brick nextBrick = brickview.bricks.get(i);
            if(nextBrick.isBounceable){
                // links            rechts           oben              unten
                if(x>nextBrick.x && x<nextBrick.y && y>=nextBrick.h && y <=nextBrick.w){
                    vx=-vx;
                }
            }

*/
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.width = Math.round(size);
        params.height = Math.round(size);
        params.leftMargin = Math.round(x);
        params.topMargin = Math.round(y);
        params.gravity = Gravity.LEFT + Gravity.TOP;
        view.setLayoutParams(params);
    }

    // Prüft das Fingertippen auf dem Screen
    @Override
    public void onClick(View view) {
        MainActivity m = new MainActivity();
        //m.getMoveRunnable();
    }

}
