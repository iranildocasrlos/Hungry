package br.com.localoeste.hungry.activy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
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
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import br.com.localoeste.hungry.helper.ConfiguracaoFirebase;
import br.com.localoeste.hungry.R;
import br.com.localoeste.hungry.helper.EmpresaFirebase;
import br.com.localoeste.hungry.model.Empresa;
import br.com.localoeste.hungry.model.Motoboy;
//import br.com.localoeste.hungry.model.Produto;
import br.com.localoeste.hungry.model.Requisicao;
import br.com.localoeste.hungry.model.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;

public class CadastroMotoboyActivity extends AppCompatActivity {


    private Bitmap imagemParaSalvar = null;
    private CircleImageView imagemMotoboy;
    private String urlImagemSelecionada = "";
    private String urlImagemEmpresa = "";
    private EditText endereco;
    private static final int SELECAO_GALERIA = 200;
    private StorageReference storageReference;
    private FirebaseFirestore referenciaFirestore;
    private EditText editMotoboyNome, editMotoMarca, editMotoPlaca, editMotoCor, editMotoModelo;
    private String idUsuarioLogado;
    private String idProduto = "";
    private Motoboy motoboy = new Motoboy();
    private String nomeRecuperadoEmpresa;

    private Usuario usuario = new Usuario();


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
        imagemMotoboy = findViewById(R.id.imagem_motoboy);

        endereco =findViewById(R.id.enderecoUsuario);
        idUsuarioLogado = EmpresaFirebase.getId_empresa();
        referenciaFirestore = ConfiguracaoFirebase.getReferenciaFirestore();
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();

        pesquisarEmpresa(idUsuarioLogado);

        imagemMotoboy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (i.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(i, SELECAO_GALERIA);
                }

            }
        });

    }

    public void validarDadosMotoboy(View view) {


        //Usuario usuario = new usuario();
        //String enderecoDestino = usuario.getEndereco();
        String enderecoDestino = endereco.getText().toString();



                String enderecoM = endereco.getText().toString();
                String nomeMotoboy = editMotoboyNome.getText().toString();
                String marcaMoto = editMotoMarca.getText().toString();
                String modeloMoto = editMotoModelo.getText().toString();
                String placaMoto = editMotoPlaca.getText().toString();
                String corMoto = editMotoCor.getText().toString();

                Motoboy motoboy = new Motoboy();
                motoboy.setIdMotoboy(idUsuarioLogado);
                motoboy.setIdEmpresa(idUsuarioLogado);
                motoboy.setNomeMotoboy(nomeMotoboy);
                motoboy.setMarcaMoto(marcaMoto);
                motoboy.setModeloMoto(modeloMoto);
                motoboy.setPlacaMoto(placaMoto);
                motoboy.setCorMoto(corMoto);
                motoboy.setUrlImagemMotoboy(urlImagemSelecionada);
                motoboy.setNomeEmpresa(nomeRecuperadoEmpresa);
                motoboy.setUrlImagemEmpresa(urlImagemEmpresa);
                motoboy.salvar();
                salvarImagem();
                exibirMensagem("motoboy salvo com sucesso");
                abrirTelaRequisicoes();
                finish();

       }


    private Address recuperarEndereco(String endereco){

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> listaEnderecos = geocoder.getFromLocationName(endereco, 1);
            if( listaEnderecos != null && listaEnderecos.size() > 0 ){
                Address address = listaEnderecos.get(0);

                return address;

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

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
                    .child(motoboy.getIdMotoboy()+ ".jpeg");

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

                                motoboy.setIdEmpresa(idUsuarioLogado);
                                motoboy.setUrlImagemMotoboy(urlImagemSelecionada);

                                Toast.makeText(CadastroMotoboyActivity.this,
                                        "Sucesso ao salvar imagem",
                                        Toast.LENGTH_SHORT).show();


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
                    imagemMotoboy.setImageBitmap(imagem);
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