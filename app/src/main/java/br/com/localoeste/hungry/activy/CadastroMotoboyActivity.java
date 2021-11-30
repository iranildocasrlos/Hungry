package br.com.localoeste.hungry.activy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
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

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import br.com.localoeste.hungry.helper.ConfiguracaoFirebase;
import br.com.localoeste.hungry.R;
import br.com.localoeste.hungry.helper.EmpresaFirebase;
import br.com.localoeste.hungry.model.Empresa;
import br.com.localoeste.hungry.model.Motoboy;
//import br.com.localoeste.hungry.model.Produto;
import de.hdodenhof.circleimageview.CircleImageView;

public class CadastroMotoboyActivity extends AppCompatActivity {


    private Bitmap imagemParaSalvar = null;
    private CircleImageView imagemProduto;
    private String urlImagemSelecionada = "";
    private String urlImagemEmpresa = "";
    private static final int SELECAO_GALERIA = 200;
    private StorageReference storageReference;
    private FirebaseFirestore referenciaFirestore;
    private EditText editMotoboyNome, editMotoMarca, editMotoPlaca, editMotoCor, editMotoModelo;
    private String idUsuarioLogado ;
    private String idProduto ="";
    //private Produto produto = new Produto();
    private Motoboy motoboy = new Motoboy();
    private String nomeRecuperadoEmpresa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_motoboy);


        //Configurações da Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Cadastro Motoboy");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inicializarComponentes();

    }


    private void inicializarComponentes() {
        editMotoboyNome = findViewById(R.id.editNomeMotoboy);
        editMotoMarca = findViewById(R.id.editMarcaMoto);
        editMotoModelo = findViewById(R.id.editModeloMoto);
        editMotoPlaca = findViewById(R.id.editPlacaMoto);
        editMotoCor = findViewById(R.id.editCorMoto);
        imagemProduto = findViewById(R.id.imagem_produto);
        idUsuarioLogado = EmpresaFirebase.getId_empresa();
        referenciaFirestore = ConfiguracaoFirebase.getReferenciaFirestore();
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();

        pesquisarEmpresa(idUsuarioLogado);

        imagemProduto.setOnClickListener(new View.OnClickListener() {
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





    }
    public void validarDadosProduto(View view) {
        UUID uuid = UUID.randomUUID();
        idProduto = String.valueOf(uuid);

        String nomeMotoboy = editMotoboyNome.getText().toString();
        String marcaMoto = editMotoMarca.getText().toString();
        String modeloMoto = editMotoModelo.getText().toString();
        String placaMoto = editMotoPlaca.getText().toString();
        String corMoto = editMotoCor.getText().toString();


        if (!nomeMotoboy.isEmpty()) {

            if (!marcaMoto.isEmpty()) {

                if (!modeloMoto.isEmpty()) {

                    if (!placaMoto.isEmpty()) {

                        if (!corMoto.isEmpty()) {


                            motoboy.setIdProduto(idProduto);
                            motoboy.setIdEmpresa(idUsuarioLogado);
                            motoboy.setNomeMotoboy(nomeMotoboy);
                            motoboy.setMarcaMoto(marcaMoto);
                            motoboy.setModeloMoto(modeloMoto);
                            motoboy.setPlacaMoto(placaMoto);
                            motoboy.setCorMoto(corMoto);
                            // motoboy.setPrecoUnidade(corMoto);;
                            motoboy.setUrlImagemMotoboy(urlImagemSelecionada);
                            motoboy.setNomeEmpresa(nomeRecuperadoEmpresa);
                            motoboy.setUrlImagemEmpresa(urlImagemEmpresa);
                            motoboy.salvar();
                            salvarImagem();
                            exibirMensagem("motoboy salvo com sucesso");
                            abrirTelaRequisicoes();
                            finish();

                        } else {
                            exibirMensagem("Diigite um preço para o produto");
                        }

                    } else {
                        exibirMensagem("Diigite uma descrição para o produto ");
                    }

                } else {
                    exibirMensagem("Diigite um nome para o produto");
                }


            }
        }
    }
    private void abrirTelaRequisicoes(){
        startActivity(new Intent(CadastroMotoboyActivity.this, RequisicoesActivity.class));
    }
    private void salvarImagem(){

        if (imagemParaSalvar != null){

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imagemParaSalvar.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            byte[] dadosImagem = baos.toByteArray();

            final StorageReference imagemRef = storageReference
                    .child("imagens")
                    .child("motoboys")
                    .child(idUsuarioLogado)
                    .child(idProduto+ ".jpeg");

            UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CadastroMotoboyActivity.this,
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
                                Motoboy motoboy = new Motoboy();
                                motoboy.setIdProduto(idProduto);
                                motoboy.setIdEmpresa(idUsuarioLogado);
                                motoboy.setUrlImagemMotoboy(urlImagemSelecionada);
                                motoboy.salvarFoto();



                            }

                        }
                    });

                }
            });



        }

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
                    imagemProduto.setImageBitmap(imagem);
                    imagemParaSalvar = imagem;
                }

            }catch (Exception erro){
                erro.printStackTrace();
            }
        }
    }


    //Método par recuperar empresa
    private void pesquisarEmpresa(String id) {

        String urlLogo = "";

        referenciaFirestore = ConfiguracaoFirebase.getReferenciaFirestore();
        DocumentReference empresaRef = referenciaFirestore
                .collection("empresas")
                .document(id);

        empresaRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Empresa dadosEmpresa = documentSnapshot.toObject(Empresa.class);
                    if (dadosEmpresa != null){
                        if (dadosEmpresa.getUrlImagem()!= null){
                            urlImagemEmpresa = dadosEmpresa.getUrlImagem();
                            nomeRecuperadoEmpresa = dadosEmpresa.getNomeFantasia();

                        }

                    }
                }
            }
        });

    }

    private  void exibirMensagem(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }


}