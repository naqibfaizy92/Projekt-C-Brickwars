package projektc.projektc.projektc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.graphics.Bitmap.createScaledBitmap;

/**
 * Created by Annika & Naqib on 23.02.2018.
 */

public class GameView extends View implements View.OnClickListener{
    //Brick
    private Paint paint = new Paint();
    protected List<Brick> bricks = new ArrayList<Brick>();
    private Brick nextBrick;
    int brickAmount= 9;
    FrameLayout container;
    Random rnd = new Random();
    int round = rnd.nextInt(5);
    //Ball
    public static final int[] ballImage = { R.drawable.ball, R.drawable.kugel1, R.drawable.kugel2,
            R.drawable.kugel3, R.drawable.kugel4,
            R.drawable.kugel5, R.drawable.kugel6};
    private float x,y,vx,vy,size;
    private ImageView viewBall;
    float minX = 0;
    float minY = 0;
    float maxX;
    float maxY;
    // Die Bricksbilder werden in einem Array abgespeichert
    private static final int[] brickImage = { R.drawable.black,R.drawable.black,R.drawable.black,
            R.drawable.black,R.drawable.black, R.drawable.brick1, R.drawable.brick2,
            R.drawable.brick3, R.drawable.brick4, R.drawable.brick5, R.drawable.brick6,
            R.drawable.brick7, R.drawable.brick8, R.drawable.brick9, R.drawable.brick10,
            R.drawable.brick11, R.drawable.brick12, R.drawable.brick13, R.drawable.brick14,
            R.drawable.brick15};
    // Framerate Attribute
    private long t;
    private long frames;

    public void createLine(int round, int brickAmount){
        //bricks.clear();
        this.round = round;
        for (int i = 0; i < brickAmount; i++) {
            Random rnd = new Random();
            Brick brick = new Brick(round, rnd.nextInt(brickImage.length));
            bricks.add(brick);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int k=0;k<round;k++){
            for (int i = 0; i < brickAmount; i++) {
                nextBrick = bricks.get((brickAmount*k)+i);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), brickImage[nextBrick.image]);
                Bitmap scaledBitmap = createScaledBitmap(bitmap, getWidth()/(brickAmount+1), getWidth()/(brickAmount+1),true);
                //Brick                 + Abstand zum nächsten Brick
                float left = (float) i * (scaledBitmap.getWidth()+(scaledBitmap.getWidth()/(brickAmount+1)));
                // 1 -> Abstand von oben: Eine Zeile
                float top = (float) ((1+k) * (scaledBitmap.getWidth()+(scaledBitmap.getWidth()/(brickAmount+1))));
                nextBrick.x = left;
                nextBrick.y = top;
                nextBrick.w = scaledBitmap.getWidth();
                nextBrick.h = scaledBitmap.getWidth();
                bricks.set((brickAmount*k)+i, nextBrick);
                canvas.drawBitmap(scaledBitmap, left, top, paint);

                bitmap.recycle();
                // wird erweitert um Seite 147
                if(t==0) {
                    t=System.currentTimeMillis();
                    frames++;
                }
            }
        }

    }

    // Berechnung der fps-Werte
    public int getFps() {
        long delta = System.currentTimeMillis();
        if(delta<1000) {
            return 0;
        }
        return (int) (frames/(delta/1000));
    }

    public GameView(Context c, FrameLayout container, Ball ball, Drawable ballDrawable){
        super(c);

        ball.setStartX(((container.getWidth() / 2) - (ball.ballSize / 2)));
        ball.setStartY((container.getHeight() - ball.ballSize));

        size = ball.ballSize;
        Random rnd = new Random();
        x = ball.getStartX();
        y = ball.getStartY();
        vx = -ball.ballSpeed*50/1000*rnd.nextFloat(); //An die Framerate angepasst
        vy = -ball.ballSpeed*50/1000*rnd.nextFloat();
        viewBall = new ImageView(container.getContext());
        viewBall.setImageDrawable(ballDrawable);
        viewBall.setId(NO_ID);
        //container.findViewById(R.id.viewBall).setOnClickListener(this);
        container.addView(viewBall);
        maxX=container.getWidth();
        maxY=container.getHeight();
        move(ball);
    }

    public void move(Ball ball) {
        x += vx;
        y += vy;
        if (x<minX) {
            vx=-vx;
        }
        if (y<minY) {
            vy=-vy;
        }
        if (x>maxX-ball.ballSize) {
            vx=-vx;
        }
        //Der Ball stirbt.
        if (y>maxY-ball.ballSize) {
            ball.setStartX(x);
            ball.setStartY(y);
            return;
        }
        //Kollision mit Rändern
        for (int i=0; i<bricks.size(); i++){

            Brick nextBrick = bricks.get(i);
            if(nextBrick.isBounceable){
                //rechts
                if((x <= nextBrick.x + nextBrick.w )
                        && (x > nextBrick.x + nextBrick.w - ball.ballSize)
                        && (y - ball.ballSize/2)>(nextBrick.y - ball.ballSize/4)
                        && (y - ball.ballSize/2)<(nextBrick.y + nextBrick.h )){

                    nextBrick.lifePoints -= 1;
                    if(nextBrick.lifePoints == 0){
                        nextBrick.image=0;
                        nextBrick.isBounceable = false;
                        invalidate();
                    }
                    bricks.set(i,nextBrick);
                    vx=-vx;
                }
                //links
                if((x <= nextBrick.x)
                        && (x > nextBrick.x - ball.ballSize)
                        && (y - ball.ballSize/2)>(nextBrick.y - ball.ballSize/4)
                        && (y - ball.ballSize/2)<(nextBrick.y + nextBrick.h )){

                    nextBrick.lifePoints -= 1;
                    if(nextBrick.lifePoints == 0){
                        nextBrick.image=0;
                        nextBrick.isBounceable = false;
                        invalidate();
                    }
                    bricks.set(i,nextBrick);
                    vx=-vx;
                }
                // obere Kante des Bricks
                if(y <=(nextBrick.y + nextBrick.h)
                        && (y > (nextBrick.y + nextBrick.w - ball.ballSize))
                        && (x + ball.ballSize/2)>(nextBrick.x - ball.ballSize/4)
                        && (x + ball.ballSize/2)<(nextBrick.x + nextBrick.w + ball.ballSize/4)){

                    nextBrick.lifePoints -= 1;
                    if(nextBrick.lifePoints == 0){
                        nextBrick.image=0;
                        nextBrick.isBounceable = false;
                        invalidate();
                    }
                    bricks.set(i,nextBrick);
                    vy=-vy;
                }
                // untere Kante
                if(y<=(nextBrick.y)
                        && (y > (nextBrick.y - ball.ballSize))
                        && (x + ball.ballSize/2)>(nextBrick.x - ball.ballSize/4)
                        && (x + ball.ballSize/2)<(nextBrick.x + nextBrick.w + ball.ballSize/4)){

                    nextBrick.lifePoints -= 1;
                    if(nextBrick.lifePoints == 0){
                        nextBrick.image=0;
                        nextBrick.isBounceable = false;
                        invalidate();
                    }
                    bricks.set(i,nextBrick);
                    vy=-vy;

                }


            }

        }

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) viewBall.getLayoutParams();
        params.width = Math.round(size);
        params.height = Math.round(size);
        params.leftMargin = Math.round(x);
        params.topMargin = Math.round(y);
        params.gravity = Gravity.LEFT + Gravity.TOP;
        viewBall.setLayoutParams(params);
    }

    // Prüft das Fingertippen auf dem Screen
    @Override
    public void onClick(View view) {
        //if(view.getId()==R.id.) {

        //}
    }





}
