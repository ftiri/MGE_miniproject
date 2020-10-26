package ch.ost.mge.ShootTheApple;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener  {
    private Button startGame;
    private Button ranking;

    private Spinner themeSpinner;
    private Spinner languageSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences preferences = getSharedPreferences("ShootTheApple", 0);
        int theme = preferences.getInt("Theme", R.style.OstTheme);
        setTheme(theme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startGame = (Button) findViewById(R.id.start_button);
        startGame.setOnClickListener(this);

        ranking = (Button)findViewById(R.id.ranking_button);
        ranking.setOnClickListener(this);

        setupThemeDropdown();
        setupLanguageDropdown();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            startGame.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 0);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;

        if(view.getId() == R.id.start_button){
            intent = new Intent(this, GameActivity.class);
            startActivityForResult(intent, 1);
        }else{
            intent = new Intent(this, RankingActivity.class);
            startActivityForResult(intent, 0);
        }
    }
 

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startGame.setEnabled(true);
        }
    }

    public void setupLanguageDropdown(){
        languageSpinner = (Spinner)findViewById(R.id.selectionLanguage);
        ArrayAdapter<CharSequence> languageAdapter = ArrayAdapter.createFromResource(this,
                R.array.languageSelection, android.R.layout.simple_spinner_item);
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(languageAdapter);
        languageSpinner.setSelection(0,false);
        languageSpinner.setOnItemSelectedListener(this);
    }

    private void setupThemeDropdown() {
        themeSpinner = (Spinner)findViewById(R.id.selectionThemeButton);
        ArrayAdapter<CharSequence> themeAdapter = ArrayAdapter.createFromResource(this,
                R.array.themeSelection, android.R.layout.simple_spinner_item);
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        themeSpinner.setAdapter(themeAdapter);
        themeSpinner.setSelection(0,false);
        themeSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected (AdapterView<?> parent, View view, int pos, long id){
        if(parent.getId() == R.id.selectionThemeButton) {
            SharedPreferences preferences = getSharedPreferences("ShootTheApple", 0);
            SharedPreferences.Editor editor = preferences.edit();
            switch (pos) {
                case 1:
                    editor.putInt("Theme", R.style.OstTheme);
                    break;
                case 2:
                    editor.putInt("Theme", R.style.HsrTheme);
                    break;
                case 3:
                    editor.putInt("Theme", R.style.DarkMode);
                    break;
            }
            editor.apply();

        } else{
            Locale locale;
            Configuration configuration = new Configuration();
            switch(pos) {
                case 1:
                    locale = new Locale("");
                    Locale.setDefault(locale);

                    configuration.locale = locale;

                    getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
                    break;
                case 2:
                    locale = new Locale("en");
                    Locale.setDefault(locale);

                    configuration.locale = locale;

                    getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
                    break;
                case 3:
                    locale = new Locale("srb");
                    Locale.setDefault(locale);

                    configuration.locale = locale;

                    getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
                    break;
                case 4:
                    locale = new Locale("ro");
                    Locale.setDefault(locale);

                    configuration.locale = locale;

                    getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
                    break;
            }
        }
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            if(resultCode >= 0){
               //TODO
            }
        }
    }


}