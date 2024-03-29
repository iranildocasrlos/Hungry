package br.com.localoeste.hungry.activy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
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
import dmax.dialog.SpotsDialog;

public class ComprasActivity extends AppCompatActivity {


    private FirebaseAuth autenticacao;
    private RecyclerView recyclerCompras;
    private AdapterCompras adapterPedidos;
    private List<Pedido> pedidos = new ArrayList<>();
    public List<ItemPedido>itensCompras = new ArrayList<>();
    private FirebaseFirestore referenciaFirestore;
    private String idEmpresaLogada ,idUsuario, idPedido;
    private StorageReference storageRef;
    private Pedido pedidoRecuperado;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compras);

        //Configurações da Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Minhas Compras");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inicializarComponentes();


        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            if (bundle.containsKey("status_aguardando")){

                idEmpresaLogada = (String)bundle.getSerializable("status_aguardando");
                idUsuario = (String)bundle.getSerializable("idUsuario");
                idPedido = (String)bundle.getSerializable("idPedido");

            }


        }else {
            idUsuario = UsuarioFirebase.getId_Usuario();
        }



        //Configurações do recyclerView
        recyclerCompras.setLayoutManager(new LinearLayoutManager(this));
        recyclerCompras.setHasFixedSize(true);
        adapterPedidos = new AdapterCompras(pedidos , this);
        recyclerCompras.setAdapter(adapterPedidos);

        recuperaEmpresa();

    }



    private void inicializarComponentes() {

        recyclerCompras = findViewById(R.id.recyclerPedidosAtendimento);
        referenciaFirestore = ConfiguracaoFirebase.getReferenciaFirestore();
        autenticacao = ConfiguracaoFirebase.getReferenciaAutenticacao();
        storageRef =  ConfiguracaoFirebase.getFirebaseStorage();

    }


    private void recuperaEmpresa(){

        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Carregando dados")
                .setCancelable( false )
                .build();
        dialog.show();

        referenciaFirestore.collection("meus_pedidos")
                .document("usuarios")
                .collection(idUsuario)
                .get()

                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            if  (!task.getResult().isEmpty()){
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    Map<String,Object> resultado = document.getData();

                                    if (resultado.get("idEmpresa") != null) {
                                        idEmpresaLogada = resultado.get("idEmpresa").toString();
                                        recuperarPedido();
                                    }else{
                                        dialog.dismiss();
                                    }



                                }
                            }else{
                                dialog.dismiss();
                            }

                        }
                    }

                });


    }



    //Recuperando Produtos da empresa
    private void recuperarPedido() {
        Log.d("log","Chamou recuperarPedido");
        referenciaFirestore.collection("meus_pedidos")
                .document("usuarios")
                .collection(idUsuario)
                .whereLessThan("status",Pedido.STATUS_SELECIONADO).get()

                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        pedidos.clear();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                pedidoRecuperado = document.toObject(Pedido.class);


                                if(pedidoRecuperado != null){

                                    itensCompras = pedidoRecuperado.getItens();

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


        dialog.dismiss();
    }



    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}