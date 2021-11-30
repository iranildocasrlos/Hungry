package br.com.localoeste.hungry.activy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.localoeste.hungry.R;
import br.com.localoeste.hungry.adapter.AdapterCompras;
import br.com.localoeste.hungry.helper.ConfiguracaoFirebase;
import br.com.localoeste.hungry.helper.UsuarioFirebase;
import br.com.localoeste.hungry.listener.RecyclerItemClickListener;
import br.com.localoeste.hungry.model.Destino;
import br.com.localoeste.hungry.model.ItemPedido;
import br.com.localoeste.hungry.model.Pedido;
import br.com.localoeste.hungry.model.Requisicao;
import br.com.localoeste.hungry.model.Usuario;
import dmax.dialog.SpotsDialog;

public class PedidosActivity extends AppCompatActivity {


    private FirebaseAuth autenticacao;
    private RecyclerView recyclerPedidos;
    private AdapterCompras adapterPedidos;
    private List<Pedido> pedidos = new ArrayList<>();
    public List<ItemPedido>itensPedidos = new ArrayList<>();
    private FirebaseFirestore referenciaFirestore;
    private String idEmpresa ,idUsuario, idPedido;
    private StorageReference storageRef;
    private Pedido pedidoRecuperado;
    private LatLng meuLocal;
    private EditText endereco;
    private Destino destino;
    private Usuario usuario = new Usuario();
    private Destino addressDestino;
    private String nome, enderecoD,idUsuarioD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        //Configurações da Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Pedidos");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inicializarComponentes();



            idEmpresa = UsuarioFirebase.getId_Usuario();

        Intent intent = getIntent();
        Bundle parametros =intent.getExtras();
        if(parametros != null){
            String nomeU = parametros.getString("nomeUsuario");
            String enderecoU = parametros.getString("nomeRua");
            String idUsuario = parametros.getString("numeroId");

            nome = (String)parametros.getSerializable("nomeUsuario");
            enderecoD = (String)parametros.getSerializable("nomeRua");
            idUsuarioD = (String)parametros.getSerializable("numeroId");

            //Toast.makeText(PedidosActivity.this, "nome", Toast.LENGTH_SHORT).show();
        }






        //Configurações do recyclerView
        recyclerPedidos.setLayoutManager(new LinearLayoutManager(this));
        recyclerPedidos.setHasFixedSize(true);
        adapterPedidos = new AdapterCompras(pedidos , this);
        recyclerPedidos.setAdapter(adapterPedidos);

        recyclerPedidos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerPedidos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                //  ItemPedido itemPedido = itensPedidos.get(position);
                                Pedido pedido = pedidos.get(position);
                                //  startActivity(new Intent(PedidosActivity.this, RequisicoesActivity.class));
                                if( pedido.getStatus().equals(Pedido.STATUS_AGUARDANDO)) {
                                    pedido.setStatus(Pedido.STATUS_PREPARANDO);
                                    pedido.atualizarStatusMeusPedidos();
                                    pedido.atualizarStatus();

                                } else if( pedido.getStatus().equals(Pedido.STATUS_PREPARANDO)){
                                    pedido.setStatus(Pedido.STATUS_A_CAMINHO);
                                    pedido.atualizarStatusAcaminho();
                                    pedido.atualizarStatusMeusPedidosAcaminho();
                                    startActivity(new Intent(PedidosActivity.this, AcompanhamentoPedidoActivity.class));

                                    salvarRequisicao(usuario);
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

        recuperarPedido();
        recuperaUsuario();



    }


    private void salvarRequisicao(Usuario usuario) {

        Requisicao requisicao = new Requisicao();

        requisicao.setCliente(usuario);

        Usuario usuariologado = UsuarioFirebase.getDadosUsuarioLogado();
        requisicao.setCliente(usuariologado);
        //  usuariologado.setLatitude(String.valueOf(meuLocal.latitude));
        //  usuariologado.setLongitude(String.valueOf(meuLocal.longitude));

        //         requisicao.setDestino(destino);
        //         usuario = UsuarioFirebase.getDadosUsuarioLogado();
        //         requisicao.setCliente(usuario);
        //         usuario.setNumero( String.valueOf(addressDestino.getLatitude()) );
        //        usuario.setLongitude( String.valueOf(addressDestino.getLongitude()) );
        requisicao.setBairro(usuario.getBairro());
        requisicao.setUsuario(Usuario.getId_Usuario());
        requisicao.setStatus(Pedido.STATUS_A_CAMINHO);
        requisicao.salvarRequisicao();

    }



    private void inicializarComponentes() {

        recyclerPedidos = findViewById(R.id.recyclerPedidosAtendimento);
        referenciaFirestore = ConfiguracaoFirebase.getReferenciaFirestore();
        autenticacao = ConfiguracaoFirebase.getReferenciaAutenticacao();
        storageRef =  ConfiguracaoFirebase.getFirebaseStorage();

    }


    private void recuperaUsuario(){
        referenciaFirestore.collection("pedidos")
                .whereArrayContains("idEmpresa",idEmpresa)
                .get()

                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Map<String,Object> resultado = document.getData();

                                if (resultado.get("idEmpresa") != null) {
                                    idUsuario = resultado.get("idUsuario").toString();
                                    recuperarPedido();

                                }


                            }
                        }
                    }
                });

    }



    //Recuperando Produtos da empresa
    private void recuperarPedido() {

        referenciaFirestore.collection("pedidos")
                .document(idEmpresa)
                .collection("aguardando")
                .get()

                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        pedidos.clear();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                pedidoRecuperado = document.toObject(Pedido.class);


                                if(pedidoRecuperado != null){

                                    itensPedidos = pedidoRecuperado.getItens();

                                    pedidos.add(pedidoRecuperado);
                                    pedidoRecuperado = null;



                                }



                                Log.d("log", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("log", "Error getting documents: ", task.getException());
                        }
                        adapterPedidos.notifyDataSetChanged();
                    }

                });



        recuperarDadosUsuario();


}



    public  void recuperarDadosUsuario(){


        DocumentReference docRef =  referenciaFirestore.collection("usuarios")
                .document(idUsuario);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Usuario novoUsuario = documentSnapshot.toObject(Usuario.class);

                    usuario = novoUsuario;

                }

            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return true;
    }


}