package br.com.localoeste.hungry.activy;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import br.com.localoeste.hungry.R;
import br.com.localoeste.hungry.adapter.AdapterRequisicoes;
import br.com.localoeste.hungry.helper.ConfiguracaoFirebase;
import br.com.localoeste.hungry.helper.UsuarioFirebase;
import br.com.localoeste.hungry.listener.RecyclerItemClickListener;
import br.com.localoeste.hungry.model.Empresa;
import br.com.localoeste.hungry.model.Pedido;
import br.com.localoeste.hungry.model.Requisicao;

public class RequisicoesActivity extends AppCompatActivity {


    //Componentes
    private TextView nome;
    private TextView distancia;


    private FirebaseAuth autenticacao;
    private RecyclerView recyclerRequisicoes;
    private AdapterRequisicoes adapterRequisicoes;
    private List<Requisicao> requisicoes = new ArrayList<>();
    private FirebaseFirestore referenciaFirestore;
    private String idUsuarioLogado;
    private StorageReference storageRef;




    //firebase
    private FirebaseFirestore firestore;

    private Requisicao requisicao = new Requisicao();

    private Requisicao requisicaoRecuperado, requisicaoParaConsulta;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requisicoes);

        firestore = FirebaseFirestore.getInstance();

        //Configurações da Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Requisições");

        setSupportActionBar(toolbar);
        inicializarComponentes();

        //Configurações do recyclerView
        recyclerRequisicoes.setLayoutManager(new LinearLayoutManager(this));
        recyclerRequisicoes.setHasFixedSize(true);
        adapterRequisicoes = new AdapterRequisicoes(requisicoes,this);
        recyclerRequisicoes.setAdapter(adapterRequisicoes);

        //Recuperar Requisicao
        recuperarRequisicoes();


        recyclerRequisicoes.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerRequisicoes,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Requisicao requisicao = requisicoes.get(position);

                                if( requisicao.getStatus().equals(Requisicao.STATUS_PRONTO)) {
                                    requisicao.setStatus(Requisicao.STATUS_VIAGEM);
                                                                }
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }

                        }
                )
        );



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuSair:
                autenticacao.signOut();
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);

       // switch (item.getItemId()){
         //   case R.id.menuSair:
           //     deslogarUsuario();
             //   finish();
               // break;
    }
    private void recuperarRequisicoes() {
       DocumentReference reference =  referenciaFirestore.collection("requisicoes")
                .document("pronto");

        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                       if(documentSnapshot.getData().get("status").equals("pronto")) {

                           Requisicao requisicaoRetorno =  (Requisicao) documentSnapshot.toObject(Requisicao.class);
                           String usuario = (String) documentSnapshot.getData().get("usuario");
                           String cidade = (String) documentSnapshot.getData().get("cidade");

                                                   Log.d("dyww nome", usuario);
                           Log.d("dyww distancia", cidade);
                       }
                    }
                }

            }

        });
    }


    private void inicializarComponentes() {

        //Configura componentes
        nome = findViewById(R.id.textRequisicaoNome);
        distancia = findViewById(R.id.textRequisicaoDistancia);

        getSupportActionBar().setTitle("Requisições");

        //Configura componentes


        recyclerRequisicoes = findViewById(R.id.recyclerRequisicoes);
        idUsuarioLogado = UsuarioFirebase.getId_Usuario();
        referenciaFirestore = ConfiguracaoFirebase.getReferenciaFirestore();
        autenticacao = ConfiguracaoFirebase.getReferenciaAutenticacao();
        storageRef =  ConfiguracaoFirebase.getFirebaseStorage();

    }

    private  void exibirMensagem(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        adapterRequisicoes.notifyDataSetChanged();
        super.onStart();

    }

    @Override
    protected void onRestart() {

        super.onRestart();

    }

    }
