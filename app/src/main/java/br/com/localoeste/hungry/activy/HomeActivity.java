package br.com.localoeste.hungry.activy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import br.com.localoeste.hungry.R;
import br.com.localoeste.hungry.helper.UsuarioFirebase;

public class HomeActivity extends AppCompatActivity {

    public UsuarioFirebase usuario =  new UsuarioFirebase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


    }
}