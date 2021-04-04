package br.com.localoeste.hungry.activy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import br.com.localoeste.hungry.R;

public class ConfiguracaoEmpresaActivity extends AppCompatActivity {

    private Spinner spinner;
    private Spinner spinnerInicio;
    private Spinner spinnerFinal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao_empresa);

        //Configurações da Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inicializarComponentes();
    }


    private void inicializarComponentes() {
        spinner = findViewById(R.id.spinnerTeste);
        spinnerInicio = findViewById(R.id.spinnerHorarioInicio);
        spinnerFinal = findViewById(R.id.spinnerHorarioFinal);
        String[] categorias = getResources().getStringArray(R.array.nome_categoria);
        String[] horarios = getResources().getStringArray(R.array.horario);

        ArrayAdapter<String> adapterHorario = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                horarios);
        adapterHorario.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                categorias);

        adapterHorario.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinnerInicio.setAdapter(adapterHorario);
        spinnerFinal.setAdapter(adapterHorario);
    }


    private void validarDadosEmpresa(View view) {

    }



}