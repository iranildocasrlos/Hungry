package br.com.localoeste.hungry.activy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import br.com.localoeste.hungry.R;

public class ActivitySplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                 abrirAutenticacao();
            }
        }, 3000);

    }


    private void abrirAutenticacao(){
        Intent i = new Intent(ActivitySplash.this, AutenticacaoActivity.class );
        startActivity(i);
        finish();
    }


}