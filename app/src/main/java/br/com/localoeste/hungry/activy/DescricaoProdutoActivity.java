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
    private Spinner  spinnerQuantidadeProduto;
    private ImageView imagemProduto;
    private String urlProduto,quantidade;
    private Button  btAdicionar;
    private ArrayAdapter<String> adapter;

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

            quantidade = spinnerQuantidadeProduto.getSelectedItem().toString();

            Picasso.get().load(urlProduto).into(imagemProduto);



        }



    }

    private void inicializaComponentes() {


        descricaoProduto = findViewById(R.id.textViewDescricaoDet);
        spinnerQuantidadeProduto = findViewById(R.id.spinnerQuantDescricao);
        imagemProduto = findViewById(R.id.imageDescricao);
        btAdicionar =  findViewById(R.id.btAdicionarDescricao);
        String[] quantidades = getResources().getStringArray(R.array.quantidade_item);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                quantidades);
        spinnerQuantidadeProduto.setAdapter(adapter);

    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d("logs","chamou de volta a tela cardápio");
        finish();
        return true;
    }

}