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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
   


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);

        autenticacao = ConfiguracaoFirebase.getReferenciaAutenticacao();

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
                .whereEqualTo("idEmpresa", idEmpresaLogada)
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

    }

    private void inicializarComponentes() {

        recyclerProdutos = findViewById(R.id.recyclerProdutos);
        idEmpresaLogada = EmpresaFirebase.getId_empresa();
        referenciaFirestore = ConfiguracaoFirebase.getReferenciaFirestore();

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


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Excluir Produto");
        alertDialog.setMessage(Html.fromHtml("Deseja excluir escluir este produto?"+"<b>\n \n "+
                               produtos.get(itemProduto).getNomeProduto().toUpperCase()+ "</b>"));

        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

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


}