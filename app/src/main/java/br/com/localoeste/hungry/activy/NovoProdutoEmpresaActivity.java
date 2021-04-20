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
import android.widget.ArrayAdapter;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import br.com.localoeste.hungry.R;
import br.com.localoeste.hungry.helper.ConfiguracaoFirebase;
import br.com.localoeste.hungry.helper.EmpresaFirebase;
import br.com.localoeste.hungry.model.Empresa;
import br.com.localoeste.hungry.model.Produto;
import de.hdodenhof.circleimageview.CircleImageView;

public class NovoProdutoEmpresaActivity extends AppCompatActivity {

    private Bitmap imagemParaSalvar = null;
    private CircleImageView imagemProduto;
    private String urlImagemSelecionada = "";
    private static final int SELECAO_GALERIA = 200;
    private StorageReference storageReference;
    private FirebaseFirestore referenciaFirestore;
    private EditText editProdutoNome, editProdutoDescricao, editProdutoPreco;
    private String idUsuarioLogado ;
    private String idProduto ="";
    private Produto produto = new Produto();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_produto_empresa);


        //Configurações da Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo Produto");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inicializarComponentes();

    }


    private void inicializarComponentes() {
        editProdutoNome = findViewById(R.id.editNomeProduto);
        editProdutoDescricao = findViewById(R.id.editDescricaoProduto);
        editProdutoPreco = findViewById(R.id.editPrecoProduto);
        imagemProduto = findViewById(R.id.imagem_produto);
        idUsuarioLogado = EmpresaFirebase.getId_empresa();
        referenciaFirestore = ConfiguracaoFirebase.getReferenciaFirestore();
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();



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



//        DocumentReference produtoRef = referenciaFirestore
//                .collection("produtos")
//                .document(idUsuarioLogado);
//
//        produtoRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if(documentSnapshot.exists()){
//                    Produto dadosProduto = documentSnapshot.toObject(Produto.class);
//                    if (dadosProduto != null){
//                        if (dadosProduto.getNomeProduto() != null){
//
//
//
//
//                            if (dadosProduto.getUrlImagemProduto()!= null){
//                                urlImagemSelecionada = dadosProduto.getUrlImagemProduto();
//                                Picasso.get()
//                                        .load(urlImagemSelecionada)
//                                        .into(imagemProduto);
//                            }
//
//
//
//                        }
//
//                    }
//                }
//            }
//        });

    }
    public void validarDadosProduto(View view) {
        UUID uuid = UUID.randomUUID();
        idProduto = String.valueOf(uuid);

        String nomeProduto = editProdutoNome.getText().toString();
        String descricaoProduto = editProdutoDescricao.getText().toString();
        String precoProduto = editProdutoPreco.getText().toString();


        if (!nomeProduto.isEmpty() ){

            if (!descricaoProduto.isEmpty() ){

                if (!precoProduto.isEmpty() ){



                    produto.setIdProduto(idProduto);
                    produto.setIdUsuario(idUsuarioLogado);
                    produto.setNomeProduto(nomeProduto);
                    produto.setDescricaoProduto(descricaoProduto);
                    produto.setPrecoProduto(Double.parseDouble(precoProduto));
                    produto.setUrlImagemProduto(urlImagemSelecionada);
                    produto.salvar();
                    salvarImagem();
                    exibirMensagem("Produto salvo com sucesso");

                    finish();

                }else {
                    exibirMensagem("Diigite um preço para o produto");
                }

            }else {
                exibirMensagem("Diigite uma descrição para o produto ");
            }

        }else {
            exibirMensagem("Diigite um nome para o produto");
        }


    }



    private void salvarImagem(){

        if (imagemParaSalvar != null){

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imagemParaSalvar.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            byte[] dadosImagem = baos.toByteArray();

            final StorageReference imagemRef = storageReference
                    .child("imagens")
                    .child("produtos")
                    .child(idUsuarioLogado)
                    .child(idProduto+ ".jpeg");

            UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(NovoProdutoEmpresaActivity.this,
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
                                Produto produto = new Produto();
                                produto.setIdProduto(idProduto);
                                produto.setUrlImagemProduto(urlImagemSelecionada);
                                produto.salvarFoto();



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

    private  void exibirMensagem(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }


}