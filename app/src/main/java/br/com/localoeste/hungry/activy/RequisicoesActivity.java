package br.com.localoeste.hungry.activy;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.style.TtsSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.Query;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import br.com.localoeste.hungry.R;
import br.com.localoeste.hungry.adapter.AdapterEmpresa;
import br.com.localoeste.hungry.adapter.AdapterRequisicoes;
import br.com.localoeste.hungry.helper.ConfiguracaoFirebase;
import br.com.localoeste.hungry.helper.UsuarioFirebase;
import br.com.localoeste.hungry.listener.RecyclerItemClickListener;
import br.com.localoeste.hungry.model.Empresa;
import br.com.localoeste.hungry.model.Pedido;
import br.com.localoeste.hungry.model.Requisicao;

public class RequisicoesActivity extends AppCompatActivity {


    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth autenticacao;
    private RecyclerView mFirestoreList;
    private FirestoreRecyclerAdapter adapter;


    private AdapterEmpresa adapterEmpresa;

    private List<Empresa> empresas = new ArrayList<>();


    private String idEmpresa ,idUsuario, idPedido;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requisicoes);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mFirestoreList = findViewById(R.id.recyclerRequisicoes);

        idEmpresa = UsuarioFirebase.getId_Usuario();


        //Configurações da Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Requisições");

        setSupportActionBar(toolbar);

        //Configurações do recyclerView
        mFirestoreList.setLayoutManager(new LinearLayoutManager(this));
        mFirestoreList.setHasFixedSize(true);
       // adapterEmpresa = new AdapterEmpresa(empresas,this);
        mFirestoreList.setAdapter(adapterEmpresa);

        mFirestoreList.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        mFirestoreList,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                //Requisicao requisicao = requisicoes.get(position);
                                Empresa empresa = empresas.get(position);

                                  startActivity(new Intent(RequisicoesActivity.this, CorridaActivity.class));


                                //   if( requisicao.getStatus().equals(Requisicao.STATUS_PRONTO)) {
                               //     requisicao.setStatus(Requisicao.STATUS_VIAGEM);
                               // }
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



        //Query
        Query query = firebaseFirestore.collection("empresas");
                //.document(idEmpresa)
                //.collection(idUsuario);
                //.document(idPedido)
                //.get();
        //RecyclerOptions
        FirestoreRecyclerOptions<requisicaoFirebase> options = new FirestoreRecyclerOptions.Builder<requisicaoFirebase>()
                .setQuery(query, requisicaoFirebase.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<requisicaoFirebase, RequisicaoViewHolder>(options) {
            @NonNull
            @Override
            public RequisicaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_requisicao, parent,false);

                return new RequisicaoViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull RequisicaoViewHolder holder, int position, @NonNull requisicaoFirebase model) {
                holder.list_nome.setText(model.getEmail());
                holder.list_endereco.setText(model.getEndereco());
            }
        };
        mFirestoreList.setHasFixedSize(true);
        mFirestoreList.setLayoutManager(new LinearLayoutManager(this));
        mFirestoreList.setAdapter(adapter);
    }


    private class RequisicaoViewHolder extends RecyclerView.ViewHolder{

        private TextView list_nome;
        private TextView list_endereco;

        public RequisicaoViewHolder (@NonNull View itemView){
            super(itemView);

            list_nome = itemView.findViewById(R.id.list_nome);
            list_endereco = itemView.findViewById(R.id.list_endereco);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}