package br.com.localoeste.hungry.activy;

import androidx.annotation.NonNull;
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

import br.com.localoeste.hungry.R;
import br.com.localoeste.hungry.helper.ConfiguracaoFirebase;
import br.com.localoeste.hungry.helper.EmpresaFirebase;
import br.com.localoeste.hungry.model.Empresa;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_produto_empresa);


        //Configurações da Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo Produto");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void inicializarComponentes() {
        editProdutoNome = findViewById(R.id.editNomeProduto);
        editProdutoDescricao = findViewById(R.id.editDescricaoProduto);
        editProdutoPreco = findViewById(R.id.editPrecoProduto);
        imagemProduto = findViewById(R.id.imagem_produto);

        referenciaFirestore = ConfiguracaoFirebase.getReferenciaFirestore();




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



        DocumentReference produtoRef = referenciaFirestore
                .collection("produtos")
                .document(idUsuarioLogado);

//        produtoRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if(documentSnapshot.exists()){
//                    Empresa dadosEmpresa = documentSnapshot.toObject(Empresa.class);
//                    if (dadosEmpresa != null){
//                        if (dadosEmpresa.getNomeFantasia() != null){
//                            editEmpresaNome.setText(dadosEmpresa.getNomeFantasia());
//                            editEmpresaTaxa.setText(dadosEmpresa.getTaxaEntrega().toString());
//                            editEmpresaTempo.setText(dadosEmpresa.getTempoEntrega());
//                            if (dadosEmpresa.getInicioAutomatico()){
//                                checkBoxAutomatico.setChecked(true);
//                            }else{
//                                checkBoxAutomatico.setChecked(false);
//                            }
//                            spinnerCategoria.setSelection(adapter.getPosition(dadosEmpresa.getCategoria()));
//                            spinnerInicio.setSelection(adapterHorario.getPosition(dadosEmpresa.getHorarioAbertura()));
//                            spinnerFinal.setSelection(adapterHorario.getPosition(dadosEmpresa.getHorarioFechamento()));
//
//                            if (dadosEmpresa.getUrlImagem()!= null){
//                                urlImagemSelecionada = dadosEmpresa.getUrlImagem();
//                                Picasso.get()
//                                        .load(urlImagemSelecionada)
//                                        .into(imagePerfilEmpresa);
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
        salvarImagem();
        String nomeProduto = editProdutoNome.getText().toString();
        String descricaoProduto = editProdutoDescricao.getText().toString();
        String precoProduto = editProdutoPreco.getText().toString();


        if (!nomeProduto.isEmpty() ){

            if (!descricaoProduto.isEmpty() ){

                if (!precoProduto.isEmpty() ){


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

            final StorageReference imagemRef = storageReference
                    .child("imagens")
                    .child("empresas")
                    .child(idUsuarioLogado + ".jpeg");

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