package projektc.projektc.projektc;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity implements View.OnClickListener {

    // Instanz-Variablen für Punkte, Runde, ballamount und highscore
    private int coins, ballamount, highscore;
    private int round = 1;
    private Ball ball;
    Random rnd = new Random();

    SeekBar ballSizeBar;

    private Drawable ballDrawable;
    private Set<GameView> balls = new HashSet<GameView>();
    private ViewGroup container;
    // Dichte
    private float density;
    GameView gv;

    protected float scale(float v) {
        return density * v;
    }

    // ScheduledExecutorService wird erstellt, er fungiert als Wecker
    protected ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);

    @Override
    protected void onCreate(Bundle saveStatistiedInstanceState) {
        super.onCreate(saveStatistiedInstanceState);
        setContentView(R.layout.activity_main);
        // TODO größe und geschwindigkeit des balls auf dem handy speichern und ausgeben SeekBarListener
        //final SeekBar ballSizeBar = (SeekBar)findViewById(R.id.ballSize);
        //final SeekBar ballSpeedBar = (SeekBar)findViewById(R.id.ballSpeed);
        //ballSizeBar.setOnSeekBarChangeListener(new seekBarListener());
        //ballSpeedBar.setOnSeekBarChangeListener(new seekBarListener());


        //Log.d("DER Wert von bitmap ", " ist " );

    }

    @Override
    protected void onResume() {
        super.onResume();
        showStartFragment();
    }


    // Fülle die TextViews anhand der id mit Text
    private void fillTextView(int id, String text) {
        TextView tv = (TextView) findViewById(id);
        tv.setText(text);
    }

    // Fülle alle vier TextViews mit den Instanzvariablen
    private void update() {
        highscore = loadStatistic(Integer.toString(highscore));
        coins = loadStatistic(Integer.toString(coins));
        ballamount = loadStatistic(Integer.toString(ballamount));
        //ball.ballSize = loadStatistic(Integer.toString(ball));
        //ball.ballSpeed = loadStatistic(Integer.toString(ball));
        fillTextView(R.id.highscore, Integer.toString(highscore));
        fillTextView(R.id.coins, Integer.toString(coins));
        fillTextView(R.id.round, Integer.toString(round));
        fillTextView(R.id.ballamount, Integer.toString(ballamount));

    }

    //Lädt Highscore und Coins aus dem Speicher. Default 0
    public int loadStatistic(String ort) {
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        return sp.getInt(ort, 0);
    }

    //Speichert Highscore und Coins.
    public void saveStatistic(String ort, int zahl) {
        //ToDo Zahl hier lokal speichern???
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putInt(ort, zahl);
        e.commit();
    }

    //ToDo Bei Gameover einfügen: Zum Speichern des Highscores
    public void saveHighscore(int round, int highscore) {
        if(round > highscore)

        {
            saveStatistic("highscore", round);
        }

    }
    //saveStatistic(coins, coins);

    // Rufe Main_Activity mit fragment_start.xml auf
    private void showStartFragment() {
        container = (ViewGroup)findViewById(R.id.container);
        container.removeAllViews();
        View start = getLayoutInflater().inflate(R.layout.fragment_start, null);
        final View robot = start.findViewById(R.id.startFigur);
        container.addView(start);
        // Animation des Robot
        Animation a = AnimationUtils.loadAnimation(this, R.anim.pulse);
        a.setAnimationListener(new SimpleAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                robot.setVisibility(View.VISIBLE);
            }
        });
        robot.startAnimation(a);
        container.addView(getLayoutInflater().inflate(R.layout.fragment_start, null));
        container.findViewById(R.id.start).setOnClickListener(this);
    }

    // Rufe den GameOverscreen auf
    private void showGameOverFragment() {
        ViewGroup container = (ViewGroup) findViewById(R.id.container);
        container.removeAllViews();
        container.addView(getLayoutInflater().inflate(R.layout.fragment_gameover, null));
        container.findViewById(R.id.play_again).setOnClickListener(this);
    }

    //Rufe Einstellungen auf
    private void showOptionsFragment() {
        ViewGroup container = (ViewGroup)findViewById(R.id.container);
        container.removeAllViews();
        container.addView(getLayoutInflater().inflate(R.layout.fragment_options, null));
        container.findViewById(R.id.options).setOnClickListener(this);
    }

    // Verarbeitet das Fingertippen auf dem Bildschirm
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.start) {
            startGame();
        } else if(view.getId()==R.id.play_again) {
            showStartFragment();
        } else if(view.getId()==R.id.options){
            showOptionsFragment();
        } else if(view.getId()==R.id.back) {
            showStartFragment();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        executor.shutdown();
        balls.clear();
    }

    private Runnable moveRunnable = new Runnable() {
        @Override
        public void run() {
            for(final GameView b : balls) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        b.move(ball);
                    }
                });
            }
        }
    };

    private void startGame() {
        //container = (ViewGroup) findViewById(R.id.container);
        container.removeAllViews();
        density = getResources().getDisplayMetrics().density;
        ball = new Ball(scale(15f), scale(100f), rnd.nextInt(gv.ballImage.length));
        ballDrawable = getResources().getDrawable(gv.ballImage[ball.image]);
        gv = new GameView(this, (FrameLayout) container, ball, ballDrawable);
        for(int j = 0; j < round; j++){
            gv.createLine(j+1, gv.brickAmount);
        }
        container.addView(gv, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        update();
        round +=1;
        balls.clear();
        for(int i=0; i<round; i++) {
            balls.add(gv);
            //String sDensity = Float.toString(density);
            Log.w("Der Wert von Density ", " ist " + density + "");
            Log.e("Die Breite ", "des Containers ist "+container.getHeight());
            Log.i("Der Ball hat die Werte " , ball.toString()+"");
            // Ausgabe des Fps-Werts
            Log.e(getClass().getSimpleName(), Integer.toString(gv.getFps()) + " fps");
        }
        // Animation der Bricks
        Animation a = AnimationUtils.loadAnimation(this, R.anim.brick);
        a.setAnimationListener(new SimpleAnimationListener() {
            @Override
            public synchronized void onAnimationStart(Animation animation) {
                /*try {
                    executor.wait();
                } catch (InterruptedException e) { // Spiel stürzt sonst ab
                    e.printStackTrace();
                }*/
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                Log.e("Blub", "onAnimationEnd");
                //executor.scheduleWithFixedDelay(moveRunnable, 2000, 25, TimeUnit.MILLISECONDS); //prallt sonst nicht abh
            }
        });
        gv.startAnimation(a);
        executor.scheduleWithFixedDelay(moveRunnable, 2000, 25, TimeUnit.MILLISECONDS);
        saveHighscore(round, highscore);
    }

}

