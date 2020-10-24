package ch.ost.mge.ShootTheApple;

import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class GameActivity extends AppCompatActivity implements Camera.PreviewCallback, Runnable {
    private static final long LIFE_TIME = 2000;
    private static final int[] PICTURES = {R.drawable.apple_iphone_39x54, R.drawable.apple_logo_49x27, R.drawable.apple_logo_rainbow_43x52, R.drawable.apple_macbook_56x39, R.drawable.android_48x56};
    private static final int[][] SIZES = {
            {39, 54},
            {49, 27},
            {43, 52},
            {56, 39},
            {48, 56}
    };
    private int points;
    private int lifes;
    private Random generator = new Random();
    private CameraView camera;
    private ViewGroup playMat;
    private float density;
    private Handler handler = new Handler();
    private TextView pointsView = (TextView) findViewById(R.id.points);
    private TextView lifeView = (TextView) findViewById(R.id.lifes);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        camera = (CameraView) findViewById(R.id.camera);
        playMat = (ViewGroup) findViewById(R.id.play_mat);
        density =getResources().getDisplayMetrics().density;
        points = 0;
        lifes = 3;
        camera.setOneShotPreviewCallback(this);
        playGame();
    }

    private void playGame() {
        refreshScreen();
        handler.postDelayed(this, 1000);
    }

    private void refreshScreen() {
        pointsView.setText(R.string.points + ": " + Integer.toString(points));
        lifeView.setText(R.string.life + ": " + Integer.toString(lifes));
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {

    }

    @Override
    public void run() {
        float randomNumber = generator.nextFloat();
        int multiplier = points / 20000 + 1;
        for(int i = 0; i < randomNumber * multiplier + 1; i++) {
            spawn(generator.nextFloat());
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
}
