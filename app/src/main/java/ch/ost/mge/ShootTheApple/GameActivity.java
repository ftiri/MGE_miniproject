package ch.ost.mge.ShootTheApple;

import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements Camera.PreviewCallback, Runnable, View.OnClickListener {
    private static final int[] PICTURES = {R.drawable.apple_iphone_39x54, R.drawable.apple_logo_49x27, R.drawable.apple_logo_rainbow_43x52, R.drawable.apple_macbook_56x39, R.drawable.android_48x56};
    private static final int[][] SIZES = {
            {39, 54},
            {49, 27},
            {43, 52},
            {56, 39},
            {48, 56}
    };
    private long lifeTime;
    private int points;
    private int lifes;
    private Random generator = new Random();
    private CameraView camera;
    private ViewGroup playMat;
    private float density;
    private Handler handler = new Handler();
    private TextView pointsView;
    private TextView lifeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);
        camera = (CameraView) findViewById(R.id.camera);
        playMat = (ViewGroup) findViewById(R.id.play_mat);
        density =getResources().getDisplayMetrics().density;
        pointsView = (TextView) findViewById(R.id.points);
        lifeView = (TextView) findViewById(R.id.lifes);
        points = 0;
        lifes = 3;
        lifeTime = 10000;
        camera.setOneShotPreviewCallback(this);
        playGame();
    }

    private void playGame() {
        refreshScreen();
        handler.postDelayed(this, 1000);
    }

    private void refreshScreen() {
        pointsView.setText(getString(R.string.points) + ": " + Integer.toString(points));
        lifeView.setText(getString(R.string.life) + ": " + Integer.toString(lifes));
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
    }

    @Override
    public void run() {
        float randomNumber = generator.nextFloat();
        int multiplier = points / 20000 + 1;
        for(int i = 0; i < randomNumber * multiplier; i++) {
            spawn(generator.nextFloat());
        }
        removeSymbols();
        //moveSymbols();
        refreshScreen();
        if(!lost()) {
            handler.postDelayed(this, 5000);
            camera.setOneShotPreviewCallback(this);
        }
    }

    private boolean lost() {
        if(lifes > 0) {
            return false;
        }
        gameOver();
        return true;
    }

    private void gameOver() {
        setResult(points);
        finish();
    }

    //private void moveSymbols() {
    //}

    private void removeSymbols() {
        for(int i = 0; i < playMat.getChildCount(); i++) {
            ImageView symbol = (ImageView) playMat.getChildAt(i);
            long age = new Date().getTime() - ((Date) symbol.getTag(R.id.creationTime)).getTime();
            if(age > lifeTime) {
                if(!(boolean) symbol.getTag(R.bool.isAndroid)) {
                    lifes--;
                }
                playMat.removeView(symbol);
                i--;
            }
        }
    }

    private void spawn(float number) {
        int index = generateIndex(number);
        int width = playMat.getWidth();
        int height = playMat.getHeight();

        int symbolWidth = Math.round(density * SIZES[index][0]);
        int symbolHeight = Math.round(density * SIZES[index][1]);

        int left = generator.nextInt(width - symbolWidth);
        int top = generator.nextInt(height - symbolHeight);

        ImageView symbol = new ImageView(this);
        symbol.setImageResource(PICTURES[index]);
        symbol.setOnClickListener(this);

        FrameLayout.LayoutParams parameters = new FrameLayout.LayoutParams(symbolWidth, symbolHeight);
        parameters.leftMargin = left;
        parameters.topMargin = top;
        parameters.gravity = Gravity.TOP + Gravity.LEFT;

        playMat.addView(symbol, parameters);
        symbol.setTag(R.id.creationTime, new Date());
        if(index == 4) {
            symbol.setTag(R.bool.isAndroid, true);
        } else {
            symbol.setTag(R.bool.isAndroid, false);
        }
    }

    private int generateIndex(float number) {
        if(number < 0.2) {
            return 0;
        }
        if(number < 0.4) {
            return 1;
        }
        if(number < 0.6) {
            return 2;
        }
        if(number < 0.8) {
            return 3;
        }
        return 4;
    }

    @Override
    public void onClick(View view) {
        if(!(boolean) view.getTag(R.bool.isAndroid)) {
            points += 1000;
        } else {
            lifes--;
        }
        playMat.removeView(view);
        refreshScreen();
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(this);
    }
}
