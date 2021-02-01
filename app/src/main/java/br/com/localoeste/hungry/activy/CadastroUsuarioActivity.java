package br.com.localoeste.hungry.activy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import br.com.localoeste.hungry.R;

public class CadastroUsuarioActivity extends AppCompatActivity {

    public static final int ACTIVITY_CADASTRO_USUARIO = R.layout.activity_cadastro_usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ACTIVITY_CADASTRO_USUARIO);
    }
}