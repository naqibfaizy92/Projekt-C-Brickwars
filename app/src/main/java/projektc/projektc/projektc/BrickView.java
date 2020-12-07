package projektc.projektc.projektc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static android.graphics.Bitmap.createScaledBitmap;

/**
 * Created by naqibfaizy on 22.11.17.
 * Diese Klasse repräsentiert die Bricks, die vom Ball in der BallView-Klasse getroffen werden.
 * Die Bricks lösen sich auf, wenn die oft genug vom Ball getroffen werden.
 */
public class BrickView extends View {

    private Paint paint = new Paint();
    protected List<Brick> bricks = new ArrayList<Brick>();
    private Brick nextBrick;

    int brickAmount= 9;

    // Die Bricksbilder werden in einem Array abgespeichert
    private static final int[] images1 = { R.drawable.black,R.drawable.black,R.drawable.black,
            R.drawable.black,R.drawable.black, R.drawable.brick1, R.drawable.brick2,
            R.drawable.brick3, R.drawable.brick4, R.drawable.brick5, R.drawable.brick6,
            R.drawable.brick7, R.drawable.brick8, R.drawable.brick9, R.drawable.brick10,
            R.drawable.brick11, R.drawable.brick12, R.drawable.brick13, R.drawable.brick14,
            R.drawable.brick15};

    // ...
    public BrickView(Context context) {
        super(context);
        paint.setAntiAlias(true);
    }

    public void createLine(int round, int brickAmount){
        //bricks.clear();
        for (int i = 0; i < brickAmount; i++) {
            Random rnd = new Random();

            Brick brick = new Brick(round, rnd.nextInt(images1.length));
            bricks.add(brick);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int k=0;k<1;k++){
            createLine(1,brickAmount);
            for (int i = 0; i < brickAmount; i++) {
                nextBrick = bricks.get((brickAmount*k)+i);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), images1[nextBrick.image]);
                Bitmap scaledBitmap = createScaledBitmap(bitmap, getWidth()/(brickAmount+1), getWidth()/(brickAmount+1),true);
                                         //Brick                 + Abstand zum nächsten Brick
                float left = (float) i * (scaledBitmap.getWidth()+(scaledBitmap.getWidth()/(brickAmount+1)));
                                   // 1 -> Abstand von oben: Eine Zeile
                float top = (float) ((1+k) * (scaledBitmap.getWidth()+(scaledBitmap.getWidth()/(brickAmount+1))));
                nextBrick.x = left;
                nextBrick.y = top;
                nextBrick.w = left+scaledBitmap.getWidth();
                nextBrick.h = top-scaledBitmap.getWidth();
                bricks.set((brickAmount*k)+i, nextBrick);
                canvas.drawBitmap(scaledBitmap, left, top, paint);
                bitmap.recycle();
            }
        }

    }



}