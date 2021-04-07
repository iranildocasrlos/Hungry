package br.com.localoeste.hungry.activy;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import br.com.localoeste.hungry.R;

public class ConfiguracaoEmpresaActivity extends AppCompatActivity {

    private Spinner spinner;
    private Spinner spinnerInicio;
    private Spinner spinnerFinal;
    private EditText editEmpresaNome, getEditEmpresaTaxa,getGetEditEmpresaCategoria, getEditEmpresaTempo;
    private ImageView imagePerfilEmpresa;
    private static final int SELECAO_GALERIA = 200;


    @RequiresApi(api = Build.VERSION_CODES.M)
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

        imagePerfilEmpresa.setOnContextClickListener(new View.OnContextClickListener() {
            @Override
            public boolean onContextClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                if (i.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(i, SELECAO_GALERIA);
                }
                return false;
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Bitmap imagem = null;
            try {

                switch (resultCode){
                    case SELECAO_GALERIA:
                        Uri localImagem = data.getData();
                        imagem = MediaStore.Images
                                .Media
                                .getBitmap(
                                        getContentResolver(),
                                        localImagem
                                );
                        break;
                }
                if (imagem != null){
                        imagePerfilEmpresa.setImageBitmap(imagem);
                }

            }catch (Exception erro){
                erro.printStackTrace();
            }
        }
    }

    private void inicializarComponentes() {
        spinner = findViewById(R.id.spinnerTeste);
        spinnerInicio = findViewById(R.id.spinnerHorarioInicio);
        spinnerFinal = findViewById(R.id.spinnerHorarioFinal);
        String[] categorias = getResources().getStringArray(R.array.nome_categoria);
        String[] horarios = getResources().getStringArray(R.array.horario);
        imagePerfilEmpresa = findViewById(R.id.image_perfil_empresa);

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