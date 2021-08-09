package br.com.localoeste.hungry.activy;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.localoeste.hungry.R;
import br.com.localoeste.hungry.adapter.AdapterProduto;
import br.com.localoeste.hungry.helper.ConfiguracaoFirebase;
import br.com.localoeste.hungry.helper.EmpresaFirebase;
import br.com.localoeste.hungry.model.Produto;

public class EmpresaActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private RecyclerView recyclerProdutos;
    private AdapterProduto adapterProduto;
    private List<Produto> produtos = new ArrayList<>();
    private FirebaseFirestore referenciaFirestore;
    private String idEmpresaLogada ;
    private StorageReference storageRef;
   


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);



        //Configurações da Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Hungry - Empresas");

        setSupportActionBar(toolbar);
        inicializarComponentes();

        //Configurações do recyclerView
        recyclerProdutos.setLayoutManager(new LinearLayoutManager(this));
        recyclerProdutos.setHasFixedSize(true);
        adapterProduto = new AdapterProduto(produtos,this);
        recyclerProdutos.setAdapter(adapterProduto);

        //Recupera produtos da empresa
        recuperarProdutos();
        swipe();




    }

    //Recuperando Produtos da empresa
    private void recuperarProdutos() {
        referenciaFirestore
                .collection("produtos")
                .document(idEmpresaLogada)
                .collection("produtos_disponiveis")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        produtos.clear();
                        if (task.isSuccessful()){

                            for(QueryDocumentSnapshot document : task.getResult()){
                                if (document.getData() != null){
                                    Produto produto =  document.toObject(Produto.class);
                                     if (produto.getUrlImagemProduto() != "" && produto.getUrlImagemProduto() != null){
                                         produtos.add(produto);
                                     }


                                }

                            }

                        }
                        adapterProduto.notifyDataSetChanged();
                    }
                });
        adapterProduto.notifyDataSetChanged();
    }

    private void inicializarComponentes() {

        recyclerProdutos = findViewById(R.id.recyclerEmpresas);
        idEmpresaLogada = EmpresaFirebase.getId_empresa();
        referenciaFirestore = ConfiguracaoFirebase.getReferenciaFirestore();
        autenticacao = ConfiguracaoFirebase.getReferenciaAutenticacao();
        storageRef =  ConfiguracaoFirebase.getFirebaseStorage();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_empresa, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSair:
                deslogarUsuario();
                finish();
                break;
            case R.id.menuConfiguracoes:
                abrirConfiguracoes();
                break;

            case R.id.menuMotoboy:
                abrirCadastroMotoboy();
                break;

            case R.id.carrinhoFull:
                Intent it = new Intent(EmpresaActivity.this, PedidosActivity.class);
                startActivity(it);
                break;


            case R.id.menuNovoProduto:
                abrirNovoProduto();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuario() {
        autenticacao.signOut();

    }

    private void abrirConfiguracoes(){

        startActivity(new Intent(EmpresaActivity.this, ConfiguracaoEmpresaActivity.class));

    }

    private void abrirNovoProduto(){
        startActivity(new Intent(EmpresaActivity.this, NovoProdutoEmpresaActivity.class));
    }

    private void abrirCadastroMotoboy(){
        startActivity(new Intent(EmpresaActivity.this, CadastroMotoboyActivity.class));
    }


    //Método responsável pelo evento de arrastar da lista de produtos
    private void swipe(){

        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags,swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                excluirProduto(viewHolder);

            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerProdutos);

    }


    private void excluirProduto(RecyclerView.ViewHolder viewHolder){

        int itemProduto = viewHolder.getAdapterPosition();
        LayoutInflater inflater = getLayoutInflater();
        ImageView imageAlert = new ImageView(this);



        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Excluir Produto");


        ImageView imagemAlertProduto = findViewById(R.id.imagemAlert);
        String urlImagemSelecionada = produtos.get(itemProduto).getUrlImagemProduto();
        Picasso.get()
                .load(urlImagemSelecionada)
                .resize(300,300)
                .into(imageAlert);

        alertDialog.setCancelable(false);
        alertDialog.setView(imageAlert);
        alertDialog.setMessage(Html.fromHtml("Deseja excluir escluir este produto?"+"<b>\n \n "+
                produtos.get(itemProduto).getNomeProduto().toUpperCase()+ "</b>"));



        //Criando a caixa ed alerta para o método excluir
        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (produtos.get(itemProduto).getIdProduto() != null){

                    String id_produto_deletar = produtos.get(itemProduto).getIdProduto();

                    referenciaFirestore.collection("produtos")
                                        .document(id_produto_deletar)
                                        .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            String caminhoFoto = "imagens/produtos/"+idEmpresaLogada+"/"+id_produto_deletar+".jpeg";
                   //Exclui foto do produto do Storage
                            StorageReference foodRef = storageRef.child(caminhoFoto);
                            foodRef.delete()
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            exibirMensagem("Erro ao excluir foto do produto");
                                        }
                                    });


                            exibirMensagem("Produto excluído com sucesso!");
                            produtos.remove(id_produto_deletar);
                            produtos.clear();
                            adapterProduto.notifyDataSetChanged();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            exibirMensagem("Erro ao excluir produto!");
                        }
                    });


                }

            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                exibirMensagem("Cancelado");
                adapterProduto.notifyDataSetChanged();
            }
        });

        alertDialog.create();
        alertDialog.show();

    }


    private  void exibirMensagem(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        adapterProduto.notifyDataSetChanged();
        super.onStart();

    }

    @Override
    protected void onRestart() {
        recuperarProdutos();

        super.onRestart();

    }

}