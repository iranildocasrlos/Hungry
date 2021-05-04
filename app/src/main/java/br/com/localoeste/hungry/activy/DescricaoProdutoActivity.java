package br.com.localoeste.hungry.activy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.localoeste.hungry.R;
import br.com.localoeste.hungry.model.ItemPedido;
import br.com.localoeste.hungry.model.Produto;

public class DescricaoProdutoActivity extends AppCompatActivity {

    private Produto produtoSelecionado ;
    private TextView descricaoProduto;
    private TextView  quantidadeProduto;
    private TextView observacao;
    private ImageView imagemProduto;
    private String urlProduto;
    private Button  btAdicionar;
    private ImageButton btnAdd;
    private ImageButton btnRemove;
    private  int quantidade = 1;
    private  Double valor = 0.0;
    private String idEmpresa;


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

            if (produtoSelecionado.getPrecoProduto() != null) {
                btAdicionar.setText("Adicionar  " + produtoSelecionado.getPrecoProduto());


            }


            idEmpresa = produtoSelecionado.getIdEmpresa();


            Picasso.get().load(urlProduto).into(imagemProduto);

        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionarItem();
            }
        });


       btnRemove.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               removerItem();
           }
       });



       //Ação do botao adicionar
        btAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemPedido itemPedido = new ItemPedido();
                itemPedido.setIdProduto(produtoSelecionado.getIdProduto());
                itemPedido.setNomeProduto(produtoSelecionado.getNomeProduto());
                itemPedido.setDescricaoProduto(produtoSelecionado.getDescricaoProduto());
                itemPedido.setIdEmpresa(idEmpresa);
                if (observacao.getText().toString() != null){
                    itemPedido.setObservacao(observacao.getText().toString());
                }else {
                    itemPedido.setObservacao("sem observações");
                }

                if (valor!= 0.0){
                    itemPedido.setPrecoProduto(valor);
                }else{
                    itemPedido.setPrecoProduto(produtoSelecionado.getPrecoProduto());
                }

                itemPedido.setQuantidadeProduto(quantidade);

                Intent intentCardapio = new Intent(DescricaoProdutoActivity.this, CardapioActivity.class);
                intentCardapio.putExtra("item",itemPedido);
                startActivity(intentCardapio);
                finish();
            }
        });





    }

    private void adicionarItem(){
        quantidade++;
        btAdicionar.setText("Adicionar  " + (produtoSelecionado.getPrecoProduto()* quantidade));

        valor =  produtoSelecionado.getPrecoProduto() * quantidade;
        quantidadeProduto.setText(String.valueOf(quantidade));

    }

    private void removerItem(){

        if (quantidade != 1){
            valor =  produtoSelecionado.getPrecoProduto() * quantidade;
            quantidade--;
            btAdicionar.setText("Adicionar  " + ( valor - produtoSelecionado.getPrecoProduto() ));


            quantidadeProduto.setText(String.valueOf(quantidade));
        }



    }
    private void inicializaComponentes() {


        descricaoProduto = findViewById(R.id.textViewDescricaoDet);
        quantidadeProduto = findViewById(R.id.textAdicionaQuantidade);
        observacao = findViewById(R.id.editTextTextMultiDescricao);
        imagemProduto = findViewById(R.id.imageDescricao);
        btAdicionar =  findViewById(R.id.btAdicionarDescricao);
        btnAdd = findViewById(R.id.imageButtonAdd);
        btnRemove = findViewById(R.id.imageButtonRemove);





    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d("logs","chamou de volta a tela cardápio");
        finish();
        return true;
    }

}