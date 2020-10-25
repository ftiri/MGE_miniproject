package ch.ost.mge.ShootTheApple;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RankingActivity extends AppCompatActivity implements View.OnClickListener{

    private Button backToMainButton;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        backToMainButton = (Button)findViewById(R.id.backToMainButton);
        backToMainButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivityForResult(intent, 1);

    }
}
