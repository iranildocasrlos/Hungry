package br.com.localoeste.hungry.activy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.localoeste.hungry.R;
import br.com.localoeste.hungry.model.Produto;

public class DescricaoProdutoActivity extends AppCompatActivity {

    private Produto produtoSelecionado ;
    private TextView descricaoProduto;
    private TextView  quantidadeProduto;
    private ImageView imagemProduto;
    private String urlProduto;
    private Button  btAdicionar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descricao_produto);

        //Configurações da Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Descrição Produto");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inicializaComponentes();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null){

            produtoSelecionado = (Produto)bundle.getSerializable("produto");

            if (produtoSelecionado.getNomeProduto()!= null){
                toolbar.setTitle(produtoSelecionado.getNomeProduto());
            }
            if (produtoSelecionado.getDescricaoProduto()!= null){
                descricaoProduto.setText(produtoSelecionado.getDescricaoProduto());
            }
            if (produtoSelecionado.getUrlImagemProduto()!= null){
                urlProduto = produtoSelecionado.getUrlImagemProduto();
            }

            if (produtoSelecionado.getPrecoProduto() != null){
                btAdicionar.setText("Adicionar  " + produtoSelecionado.getPrecoProduto());


            }


            Picasso.get().load(urlProduto).into(imagemProduto);



        }



    }

    private void inicializaComponentes() {


        descricaoProduto = findViewById(R.id.textViewDescricaoDet);
        quantidadeProduto = findViewById(R.id.textQuantidadeDetalhes);
        imagemProduto = findViewById(R.id.imageDescricao);
        btAdicionar =  findViewById(R.id.btAdicionarDescricao);




    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d("logs","chamou de volta a tela cardápio");
        finish();
        return true;
    }

}