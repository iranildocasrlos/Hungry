package br.com.localoeste.hungry.activy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
import br.com.localoeste.hungry.model.ItemPedido;
import br.com.localoeste.hungry.model.Pedido;

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




        //Configurações do recyclerView
        recyclerPedidos.setLayoutManager(new LinearLayoutManager(this));
        recyclerPedidos.setHasFixedSize(true);
        adapterPedidos = new AdapterCompras(pedidos , this);
        recyclerPedidos.setAdapter(adapterPedidos);

        recuperarPedido();
        recuperaUsuario();

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






}




    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return true;
    }


}