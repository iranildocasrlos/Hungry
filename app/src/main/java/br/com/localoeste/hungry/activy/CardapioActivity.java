package br.com.localoeste.hungry.activy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import br.com.localoeste.hungry.model.Empresa;
import br.com.localoeste.hungry.model.Produto;
import de.hdodenhof.circleimageview.CircleImageView;

public class CardapioActivity extends AppCompatActivity {


    private CircleImageView imagemEmpresaCardapio;
    private TextView nomeEmpresaCadapio;
    private RecyclerView recyclerViewEmpresaCardapio;
    private Empresa empresaSelecionada;
    private AdapterProduto adapterProduto;
    private List<Produto> produtos = new ArrayList<>();
    private FirebaseFirestore referenciaFirestore;
    private String idEmpresaLogada ;
    private StorageReference storageRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardapio);

        //Configurações da Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Cadápio");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inicializarComponentes();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            empresaSelecionada = (Empresa)bundle.getSerializable("empresa");
            nomeEmpresaCadapio.setText(empresaSelecionada.getNomeFantasia());
            idEmpresaLogada = empresaSelecionada.getIdEmpresa();

            String url = empresaSelecionada.getUrlImagem();
            Picasso.get().load(url).into(imagemEmpresaCardapio);

            //Configurações do recyclerView
            recyclerViewEmpresaCardapio.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewEmpresaCardapio.setHasFixedSize(true);
            adapterProduto = new AdapterProduto(produtos,this);
            recyclerViewEmpresaCardapio.setAdapter(adapterProduto);

            //Recupera produtos da empresa
            recuperarProdutos();


        }


    }

    private void inicializarComponentes() {

        recyclerViewEmpresaCardapio = findViewById(R.id.recyclerProdutoCardapio);
        nomeEmpresaCadapio = findViewById(R.id.textNomeEmCardapio);
        imagemEmpresaCardapio =  findViewById(R.id.imageCardapioEmpresa);
        referenciaFirestore = ConfiguracaoFirebase.getReferenciaFirestore();
        storageRef =  ConfiguracaoFirebase.getFirebaseStorage();

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
        adapterProduto.notifyDataSetChanged();
    }







}