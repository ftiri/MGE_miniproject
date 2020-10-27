package ch.ost.mge.ShootTheApple;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class RankingActivity extends AppCompatActivity implements View.OnClickListener{
    private Button backToMainButton;
    private EditText playerName;
    private LinearLayout newHighscore;
    private TextView ranking;
    private String[] rankingNames;
    private int[] rankingScores = new int[10];
    private int highscore;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        backToMainButton = (Button)findViewById(R.id.backToMainButton);
        backToMainButton.setOnClickListener(this);
        playerName = (EditText) findViewById(R.id.playerName);
        newHighscore = (LinearLayout) findViewById(R.id.newHighscoreWrapper);
        ranking = (TextView) findViewById(R.id.ranking);
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
        readRanking();
        if(getIntent().getExtras() != null) {
            highscore = getIntent().getExtras().getInt("points", 0);
            newHighscore.setVisibility(View.VISIBLE);
        } else {
            newHighscore.setVisibility(View.INVISIBLE);
        }
    }

    private void readRanking() {
        try {
            InputStream inputStream = getBaseContext().openFileInput("rankingNames.txt");

            if (inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                rankingNames = stringBuilder.toString().split(",");
            }
            inputStream = getBaseContext().openFileInput("rankingScores.txt");

            if (inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                String[] temp = stringBuilder.toString().split(",");
                for(int i = 0; i < temp.length; i++) {
                    rankingScores[i] = Integer.parseInt(temp[i]);
                }
            }
        }
        catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        buildRanking();
    }

    private void buildRanking() {
        if(rankingNames != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for(int i = 0; i < rankingNames.length; i++) {
                stringBuilder.append(i + 1).append(". ").append(rankingNames[i]).append(" ").append(rankingScores[i]).append("\n");
            }
            ranking.setText(stringBuilder.toString());
        }
    }

    private void writeRanking() {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getBaseContext().openFileOutput("rankingNames.txt", 0));
            for(int i = 0; i < rankingNames.length; i++) {
                outputStreamWriter.write(rankingNames[i] + ",");
            }
            outputStreamWriter.close();

            outputStreamWriter = new OutputStreamWriter(getBaseContext().openFileOutput("rankingScores.txt", 0));
            for(int i = 0; i < rankingNames.length; i++) {
                outputStreamWriter.write(rankingScores[i] + ",");
            }
            outputStreamWriter.close();

            SharedPreferences preferences = getSharedPreferences("ShootTheApple", 0);
            SharedPreferences.Editor editor = preferences.edit();
            if(rankingScores.length > 9) {
                editor.putInt("minHighscore", rankingScores[rankingScores.length - 1]);
            }
        }
        catch (IOException e) {
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.backToMainButton) {
            finish();
        } else {
            updateRanking();
            writeRanking();
            buildRanking();
        }
    }

    private void updateRanking() {
        String player = playerName.getText().toString();
        player.replace(",", "");
        if(rankingNames == null) {
            rankingNames = new String[] {player};
            rankingScores = new int[] {highscore};
        } else if(rankingNames.length < 10) {
            String[] tempNames = new String[rankingNames.length + 1];
            int[] tempHighscores = new int[rankingNames.length + 1];
            for(int i = 0; i < rankingNames.length; i++) {
                tempNames[i] = rankingNames[i];
                tempHighscores[i] = rankingScores[i];
            }
            tempNames[tempNames.length - 1] = player;
            tempHighscores[tempHighscores.length - 1] = highscore;
            rankingNames = tempNames;
            rankingScores = tempHighscores;
        } else {
            rankingScores[rankingScores.length - 1] = highscore;
            rankingNames[rankingNames.length - 1] = player;
        }
        sortRanking();
        newHighscore.setVisibility(View.INVISIBLE);
    }

    private void sortRanking() {
        for(int i = rankingNames.length - 1; i > 0; i--) {
            if(rankingScores[i] > rankingScores[i - 1]) {
                int tempScore = rankingScores[i];
                String tempName = rankingNames[i];
                rankingScores[i] = rankingScores[i - 1];
                rankingNames[i] = rankingNames[i - 1];
                rankingScores[i - 1] = tempScore;
                rankingNames[i - 1] = tempName;
            }
        }
    }
}
