package br.com.localoeste.hungry.activy;

import androidx.annotation.NonNull;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import br.com.localoeste.hungry.R;
import br.com.localoeste.hungry.helper.ConfiguracaoFirebase;
import br.com.localoeste.hungry.helper.EmpresaFirebase;
import br.com.localoeste.hungry.model.Empresa;
import de.hdodenhof.circleimageview.CircleImageView;

public class ConfiguracaoEmpresaActivity extends AppCompatActivity {

    private Spinner spinnerCategoria;
    private Spinner spinnerInicio;
    private Spinner spinnerFinal;
    private EditText editEmpresaNome, editEmpresaTaxa, editEmpresaTempo;
    private CircleImageView imagePerfilEmpresa;
    private static final int SELECAO_GALERIA = 200;
    private StorageReference storageReference;
    private String idUsuarioLogado ;
    private String urlImagemSelecionada = "";
    private CheckBox checkBoxAutomatico;
    private Button btSalvar;
    private Bitmap imagemParaSalvar = null;
    private Empresa empresa = new Empresa();
    private FirebaseFirestore referenciaFirestore;
    private ArrayAdapter<String> adapterHorario;
    private ArrayAdapter<String> adapter;



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
      storageReference = ConfiguracaoFirebase.getFirebaseStorage();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Bitmap imagem = null;
            try {

                switch (requestCode){
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
                        imagemParaSalvar = imagem;
                }

            }catch (Exception erro){
                erro.printStackTrace();
            }
        }
    }


    private void inicializarComponentes() {
        editEmpresaNome = findViewById(R.id.editNomeProduto);
        editEmpresaTaxa = findViewById(R.id.editPrecoProduto);
        editEmpresaTempo = findViewById(R.id.editDescricaoProduto);
        checkBoxAutomatico = findViewById(R.id.checkBoxAutomatico);
        btSalvar = findViewById(R.id.btSalvarConfigurcacao);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        spinnerInicio = findViewById(R.id.spinnerHorarioInicio);
        spinnerFinal = findViewById(R.id.spinnerHorarioFinal);
        String[] categorias = getResources().getStringArray(R.array.nome_categoria);
        String[] horarios = getResources().getStringArray(R.array.horario);
        imagePerfilEmpresa = findViewById(R.id.imagem_motoboy);
        idUsuarioLogado = EmpresaFirebase.getId_empresa();
        referenciaFirestore = ConfiguracaoFirebase.getReferenciaFirestore();

        adapterHorario = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                horarios);
        adapterHorario.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                categorias);

        adapterHorario.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCategoria.setAdapter(adapter);
        spinnerInicio.setAdapter(adapterHorario);
        spinnerFinal.setAdapter(adapterHorario);


        imagePerfilEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (i.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(i,SELECAO_GALERIA);
                }

            }
        });



        DocumentReference empresaRef = referenciaFirestore
                .collection("empresas")
                .document(idUsuarioLogado);

        empresaRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Empresa dadosEmpresa = documentSnapshot.toObject(Empresa.class);
                    if (dadosEmpresa != null){
                        if (dadosEmpresa.getNomeFantasia() != null){
                            editEmpresaNome.setText(dadosEmpresa.getNomeFantasia());
                            editEmpresaTaxa.setText(dadosEmpresa.getTaxaEntrega().toString());
                            editEmpresaTempo.setText(dadosEmpresa.getTempoEntrega());
                            if (dadosEmpresa.getInicioAutomatico()){
                                checkBoxAutomatico.setChecked(true);
                            }else{
                                checkBoxAutomatico.setChecked(false);
                            }
                            spinnerCategoria.setSelection(adapter.getPosition(dadosEmpresa.getCategoria()));
                            spinnerInicio.setSelection(adapterHorario.getPosition(dadosEmpresa.getHorarioAbertura()));
                            spinnerFinal.setSelection(adapterHorario.getPosition(dadosEmpresa.getHorarioFechamento()));

                            if (dadosEmpresa.getUrlImagem()!= null){
                                urlImagemSelecionada = dadosEmpresa.getUrlImagem();
                                Picasso.get()
                                        .load(urlImagemSelecionada)
                                        .into(imagePerfilEmpresa);
                            }



                        }

                    }
                }
            }
        });

    }




    public void validarDadosEmpresa(View view) {
        salvarImagem();
        String nome = editEmpresaNome.getText().toString();
        String taxa = editEmpresaTaxa.getText().toString();
        String tempo = editEmpresaTempo.getText().toString();
        String categoria = spinnerCategoria.getSelectedItem().toString();
        String horaInicio = spinnerInicio.getSelectedItem().toString();
        String horaFim = spinnerFinal.getSelectedItem().toString();
        Boolean automatico = checkBoxAutomatico.isChecked();

        if (!nome.isEmpty() ){

            if (!tempo.isEmpty() ){

                if (!taxa.isEmpty() ){

                    empresa.setIdEmpresa(idUsuarioLogado);
                    empresa.setNomeFantasia(nome);
                    empresa.setTempoEntrega(tempo);
                    empresa.setTaxaEntrega(Double.parseDouble(taxa));
                    empresa.setCategoria(categoria);
                    empresa.setHorarioAbertura(horaInicio);
                    empresa.setHorarioFechamento(horaFim);
                    empresa.setInicioAutomatico(automatico);
                    empresa.atualizar();
                    exibirMensagem("Atualizado com sucesso!");
                }else {
                    exibirMensagem("Diigite uma taxa para entrega");
                }

            }else {
                exibirMensagem("Diigite o tempo de entrega ");
            }

        }else {
            exibirMensagem("Diigite um nome para empresa");
        }


    }



    private void salvarImagem(){

        if (imagemParaSalvar != null){

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imagemParaSalvar.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            byte[] dadosImagem = baos.toByteArray();

            final   StorageReference imagemRef = storageReference
                    .child("imagens")
                    .child("empresas")
                    .child(idUsuarioLogado + ".jpeg");

            UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ConfiguracaoEmpresaActivity.this,
                            "Erro ao fazer o upload da imagem",
                            Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            Uri uri = task.getResult();
                            urlImagemSelecionada = uri.toString();
                            if (urlImagemSelecionada != ""){
                                Empresa empresa = new Empresa();
                                empresa.setIdEmpresa(idUsuarioLogado);
                                empresa.setUrlImagem(urlImagemSelecionada);

                                empresa.atualizarLogo();

                                finish();

                            }

                        }
                    });

                }
            });



        }

    }

    private  void exibirMensagem(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }



}