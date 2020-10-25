package ch.ost.mge.ShootTheApple;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Button startGame;
    private Spinner themeSpinner;
    private Spinner languageSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startGame = (Button) findViewById(R.id.start_button);
        startGame.setOnClickListener(this);

        setupThemeDropdown();
        setupLanguageDropdown();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            startGame.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 0);
        }
    }



    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivityForResult(intent, 1);
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
        languageSpinner.setOnItemSelectedListener(this);
    }

    private void setupThemeDropdown() {
        themeSpinner = (Spinner)findViewById(R.id.selectionThemeButton);
        ArrayAdapter<CharSequence> themeAdapter = ArrayAdapter.createFromResource(this,
                R.array.themeSelection, android.R.layout.simple_spinner_item);
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        themeSpinner.setAdapter(themeAdapter);
        themeSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){


        switch(pos){
            case 0:
                setTheme(R.style.OstTheme);
                break;
            case 1:
                setTheme(R.style.HsrTheme);
                break;
            case 2:
                setTheme(R.style.DarkMode);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        setTheme(R.style.DarkMode);
    }
}