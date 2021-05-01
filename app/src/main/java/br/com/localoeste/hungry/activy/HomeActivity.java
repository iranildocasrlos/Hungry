package br.com.localoeste.hungry.activy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import br.com.localoeste.hungry.R;
import br.com.localoeste.hungry.adapter.AdapterEmpresa;
import br.com.localoeste.hungry.adapter.AdapterProduto;
import br.com.localoeste.hungry.helper.ConfiguracaoFirebase;
import br.com.localoeste.hungry.helper.UsuarioFirebase;
import br.com.localoeste.hungry.listener.RecyclerItemClickListener;
import br.com.localoeste.hungry.model.Empresa;
import br.com.localoeste.hungry.model.Produto;

public class HomeActivity extends AppCompatActivity {


    private FirebaseAuth autenticacao;
    private MaterialSearchView searchView;
    private RecyclerView recyclerViewEmpressas;
    private FirebaseFirestore referenciaFirestore;
    private List<Empresa>empresas = new ArrayList<>();
    private AdapterEmpresa adapterEmpresa;


    public UsuarioFirebase usuario =  new UsuarioFirebase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        inicializarComponentes();

        //Configurações da Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Hungry");

        setSupportActionBar(toolbar);
        //Configurações do recyclerView
        recyclerViewEmpressas.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewEmpressas.setHasFixedSize(true);
        adapterEmpresa = new AdapterEmpresa(empresas);
        recyclerViewEmpressas.setAdapter(adapterEmpresa);

        //Recupera produtos da empresa
        recuperarEmpresas();

        //Configurações da pesquisa
        searchView.setHint("Pesquisar Restaurante");
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                pesquisarEmpresa(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        //Configura o evennto de clique
        recyclerViewEmpressas.addOnItemTouchListener(new RecyclerItemClickListener(
                this,
                recyclerViewEmpressas,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Empresa empresaSelecionada = empresas.get(position);
                        Intent i = new Intent(HomeActivity.this, CardapioActivity.class);

                        i.putExtra("empresa", empresaSelecionada);

                        startActivity(i);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        ));

    }

    private void pesquisarEmpresa(String nome) {
        referenciaFirestore
                .collection("empresas")
                .whereArrayContains("nomeFantasia", nome)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        empresas.clear();
                        if (task.isSuccessful()){

                            for(QueryDocumentSnapshot document : task.getResult()){
                                if (document.getData() != null){
                                    Empresa empresa =  document.toObject(Empresa.class);
                                    if (empresa.getUrlImagem()!= "" && empresa.getUrlImagem() != null){
                                        empresas.add(empresa);
                                    }


                                }

                            }

                        }
                        adapterEmpresa.notifyDataSetChanged();
                    }
                });

    }


    private void inicializarComponentes() {
      autenticacao = ConfiguracaoFirebase.getReferenciaAutenticacao();
      referenciaFirestore = ConfiguracaoFirebase.getReferenciaFirestore();
      searchView =  findViewById(R.id.materialSearchView);
      recyclerViewEmpressas = findViewById(R.id.recyclerEmpresas);
    }


    private void recuperarEmpresas() {

        referenciaFirestore
                .collection("empresas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        empresas.clear();
                        if (task.isSuccessful()){

                            for(QueryDocumentSnapshot document : task.getResult()){
                                if (document.getData() != null){
                                    Empresa empresa=  document.toObject(Empresa.class);
                                    if (empresa.getUrlImagem() != "" && empresa.getUrlImagem() != null){
                                        empresas.add(empresa);
                                    }


                                }

                            }

                        }
                        adapterEmpresa.notifyDataSetChanged();
                    }
                });




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_usuario, menu);

        MenuItem item = menu.findItem(R.id.menuPesquisa);
        searchView.setMenuItem(item);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSair:
                deslogarUsuario();
                break;
            case R.id.menuConfiguracoes:
                abrirConfiguracoes();
                break;
            case R.id.menuNovoProduto:

                break;
        }


        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuario() {
        autenticacao.signOut();
        finish();
    }

    private void abrirConfiguracoes(){

        startActivity(new Intent(HomeActivity.this, ConfiguracoesUsuarioActivity.class));

    }


    @Override
    protected void onRestart() {
        adapterEmpresa.notifyDataSetChanged();
        super.onRestart();
        recuperarEmpresas();
        Log.d("logs","chamou onRestart");
    }


    @Override
    protected void onResume() {
        adapterEmpresa.notifyDataSetChanged();
        Log.d("logs","chamou onResume");
        super.onResume();
    }
}