package com.example.arnedo_perezmedrano_practica_android_m08uf1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.Date;

public class MainActivity extends Activity {

    private Button jugar;
    private Button salir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        jugar = (Button) findViewById(R.id.jugar);
        jugar.setOnClickListener(this::jugar);
        salir = (Button) findViewById(R.id.salir);
        salir.setOnClickListener(this::salir);
    }

    private void jugar(View view) {
        Intent intent = new Intent(getApplicationContext(),
                GameActivity.class);
        startActivity(intent);
    }

    private void salir(View view) {
        finish();
        System.exit(0);
    }

}