package br.com.localoeste.hungry.activy;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.localoeste.hungry.R;
import br.com.localoeste.hungry.adapter.AdapterCarrinho;
import br.com.localoeste.hungry.helper.ConfiguracaoFirebase;
import br.com.localoeste.hungry.model.Empresa;
import br.com.localoeste.hungry.model.ItemPedido;
import br.com.localoeste.hungry.model.Pagamento;
import br.com.localoeste.hungry.model.Pedido;
import br.com.localoeste.hungry.model.Produto;
import br.com.localoeste.hungry.model.Usuario;

public class CarrinhoActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private RecyclerView recyclerCarrinho;
    private AdapterCarrinho adapterProduto;
    private List<Produto> produtos = new ArrayList<>();
    public List<ItemPedido>itensCarrinho = new ArrayList<>();
    private FirebaseFirestore referenciaFirestore;
    private String idEmpresaLogada ,idUsuario, idPedido;
    private StorageReference storageRef;
    private TextView textQuantidade;
    private TextView textValor;
    private int qtdItensCarrinho;
    private int totalItem = 0;
    private Double totalCarrinho, valorFrete;
    private Double precoDemais = 0.0;
    private Pedido pedidoRecuperado;
    private Double porcentagem = 0.15;
    private int metodoPagamento;
    private Empresa empresa;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            if (bundle.containsKey("idEmpresa")){

                idEmpresaLogada = (String)bundle.getSerializable("idEmpresa");
                idUsuario = (String)bundle.getSerializable("idUsuario");
                idPedido = (String)bundle.getSerializable("idPedido");
            }


        }


        //Configurações da Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Meus pedidos");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inicializarComponentes();

        //Configurações do recyclerView
        recyclerCarrinho.setLayoutManager(new LinearLayoutManager(this));
        recyclerCarrinho.setHasFixedSize(true);
        adapterProduto = new AdapterCarrinho(produtos,this);
        recyclerCarrinho.setAdapter(adapterProduto);


        adapterProduto.OnRecyclerViewClickListener(new AdapterCarrinho.OnRecyclerViewClickListener() {
            @Override
            public void OnItemClick(int position, int quantidadeEscolhida, Double precoProduto) {
                for (ItemPedido pedido: itensCarrinho) {


                    if (pedido.getIdProduto() != itensCarrinho.get(position).getIdProduto()){
                        totalItem += pedido.getQuantidadeProduto();
                        precoDemais += pedido.getPrecoProduto();
                        itensCarrinho.get(position).setQuantidadeProduto(quantidadeEscolhida);
                        itensCarrinho.get(position).setPrecoProduto(precoProduto);

                    }


                }
                String quantidadeTotal = String.valueOf(totalItem + quantidadeEscolhida);
                textQuantidade.setText("quant.: "+quantidadeTotal);

                DecimalFormat df = new DecimalFormat("0.00");

                String numeroFormatato = df.format(precoDemais + precoProduto);
                textValor.setText("R$: "+ numeroFormatato);


                totalItem = 0;
                precoDemais = 0.0;
            }
        });

        //Recupera produtos da empresa
        recuperarPedido();
        swipe();




    }
    private Double recuperarFrete(){
        referenciaFirestore.collection("empresas")
                .document(idEmpresaLogada).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()){

                                empresa = document.toObject(Empresa.class);

                                valorFrete = empresa.getTaxaEntrega();


                            }
                        }
                    }
                });

        return valorFrete;


    }

    //Recuperando Produtos da empresa
    private void recuperarPedido() {
        qtdItensCarrinho = 0;
        totalCarrinho = 0.0;
        itensCarrinho = new ArrayList<>();
         referenciaFirestore.collection("pedidos")
                .document(idEmpresaLogada)
                .collection("aguardando")
                .whereEqualTo("status",Pedido.STATUS_SELECIONADO).get()

                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {



                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                pedidoRecuperado = document.toObject(Pedido.class);

                                if(pedidoRecuperado != null){



                                    itensCarrinho = pedidoRecuperado.getItens();

                                    for (ItemPedido item : itensCarrinho){
                                        int qtde = item.getQuantidadeProduto();
                                        Double preco = item.getPrecoProduto();
                                        String id = item.getIdProduto();
                                        String idEmpresa = item.getIdEmpresa();
                                        int quantidade = item.getQuantidadeProduto();
                                        Double precoAtualizado = item.getPrecoProduto();
                                        recuperarProdutos(idEmpresa, id, quantidade, precoAtualizado);
                                        totalCarrinho += preco;
                                        qtdItensCarrinho += qtde;
                                    }

                                    if (pedidoRecuperado.getItens().isEmpty()){
                                        excluirPedido();
                                    }

                                    DecimalFormat df = new DecimalFormat("0.00");
                                    String numeroFormatato = df.format(totalCarrinho);

                                    textQuantidade.setText("quant.: "+ String.valueOf(qtdItensCarrinho));
                                    textValor.setText("R$: "+ numeroFormatato);

                                }



                                Log.d("log", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("log", "Error getting documents: ", task.getException());
                        }
                    }
                });


    }



    private void inicializarComponentes() {

        recyclerCarrinho = findViewById(R.id.recyclerPedidosAtendimento);
        referenciaFirestore = ConfiguracaoFirebase.getReferenciaFirestore();
        autenticacao = ConfiguracaoFirebase.getReferenciaAutenticacao();
        storageRef =  ConfiguracaoFirebase.getFirebaseStorage();
        textQuantidade = findViewById(R.id.textQuantidadeCarrinho);
        textValor = findViewById(R.id.textValorCarrinho);


    }



    private void recuperarProdutos(String idEmp, String idP, int quantidade, Double preco) {
        referenciaFirestore
                .collection("produtos")
                .document( idEmp)
                .collection("produtos_disponiveis")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       // produtos.clear();
                        if (task.isSuccessful()){

                            for(QueryDocumentSnapshot document : task.getResult()){
                                if (document.getData() != null){
                                    Produto produto =  document.toObject(Produto.class);
                                    if (produto.getUrlImagemProduto() != "" && produto.getUrlImagemProduto() != null){
                                       if (produto.getIdProduto().equalsIgnoreCase(idP)){
                                           produto.setQuantidade(quantidade);
                                           produto.setPrecoProduto(preco);
                                           produtos.add(produto);
                                       }

                                    }


                                }

                            }

                        }
                        adapterProduto.notifyDataSetChanged();
                    }
                });
        adapterProduto.notifyDataSetChanged();
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_carrinho, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSair:
                deslogarUsuario();
                finish();
                break;

            case R.id.menuCompras:
                Intent itentAndamento = new Intent(CarrinhoActivity.this, ComprasActivity.class);
                itentAndamento.putExtra("status_aguardando", pedidoRecuperado.getIdEmpresa());
                itentAndamento.putExtra("idPedido", pedidoRecuperado.getIdPedido());
                itentAndamento.putExtra("idUsuario", pedidoRecuperado.getIdUsuario());

                startActivity(itentAndamento);
                break;

            case R.id.menuComprasHome:
                if (pedidoRecuperado != null) {
                    Intent intCarrrinho = new Intent(this, CarrinhoActivity.class);
                    intCarrrinho.putExtra("idEmpresa", idEmpresaLogada);
                    intCarrrinho.putExtra("idUsuario", idUsuario);
                    intCarrrinho.putExtra("idPedido", pedidoRecuperado.getIdPedido());
                    startActivity(intCarrrinho);
                    textQuantidade.setText("0");
                    textValor.setText("0.00");
                    totalCarrinho = 0.0;
                    qtdItensCarrinho = 0;
                    break;
                }

        }


        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuario() {
        autenticacao.signOut();

    }





    //Método responsável pelo evento de arrastar da lista de produtos
    private void swipe(){

        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags,swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                excluirItem(viewHolder);

            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerCarrinho);

    }


    private void excluirItem(RecyclerView.ViewHolder viewHolder){

        int itemProduto = viewHolder.getAdapterPosition();
        LayoutInflater inflater = getLayoutInflater();
        ImageView imageAlert = new ImageView(this);



        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Excluir Produto");


        ImageView imagemAlertProduto = findViewById(R.id.imagemAlert);
        String urlImagemSelecionada = produtos.get(itemProduto).getUrlImagemProduto();
        Picasso.get()
                .load(urlImagemSelecionada)
                .resize(300,300)
                .into(imageAlert);

        alertDialog.setCancelable(false);
        alertDialog.setView(imageAlert);
        alertDialog.setMessage(Html.fromHtml("Deseja excluir escluir este produto?"+"<b>\n \n "+
                produtos.get(itemProduto).getNomeProduto().toUpperCase()+ "</b>"));



        //Criando a caixa ed alerta para o método excluir
        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (itensCarrinho.get(itemProduto).getIdProduto() != null){

                    Double valorAbater = (pedidoRecuperado.getTotal()  -  itensCarrinho.get(itemProduto).getPrecoProduto());

                    pedidoRecuperado.setTotal(valorAbater);
                    produtos.remove(itemProduto);
                    itensCarrinho.remove(itemProduto);

                    DocumentReference documentRef = referenciaFirestore
                            .collection("pedidos")
                            .document(idEmpresaLogada)
                            .collection("aguardando")
                            .document(idPedido);

                    Map<String, Object> data = new HashMap<>();
                    if (itensCarrinho.isEmpty()){
                        data.put("idProduto","");
                    }
                    data.put("itens",itensCarrinho);
                    data.put("total",valorAbater);
                    documentRef.update(data);

                    DecimalFormat df = new DecimalFormat("0.00");

                    totalCarrinho = valorAbater;
                    String numeroFormatato = df.format(totalCarrinho);

                    qtdItensCarrinho = itensCarrinho.size();
                    textQuantidade.setText("quant.: "+ String.valueOf(qtdItensCarrinho));
                    textValor.setText("R$: "+ numeroFormatato);

                    if (pedidoRecuperado.getItens().isEmpty()){
                        excluirPedido();
                    }


                }
                adapterProduto.notifyDataSetChanged();
            }

        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                exibirMensagem("Cancelado");
                adapterProduto.notifyDataSetChanged();
            }
        });

        alertDialog.create();
        alertDialog.show();
        adapterProduto.notifyDataSetChanged();
    }



    private  void excluirPedido(){
        referenciaFirestore
                .collection("pedidos")
                .document(idEmpresaLogada)
                .collection("aguardando")
                .document(idPedido)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        exibirMensagem("Pedido excluído");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        exibirMensagem("Sem itens no pedido");
                    }
                });
    }




    public void confirmarPedido(View view) {
        DecimalFormat df = new DecimalFormat("00.00");
        recuperarFrete();

        String valorExtraido = textValor.getText().toString().replaceAll(",",".");
            Log.d("Log",String.valueOf(valorExtraido.length()));
        if (valorExtraido.length() > 6 ){

            Double valorAtualizado;
            if (valorExtraido.length() > 6 && valorExtraido.length() < 9){
                 valorAtualizado = Double.parseDouble( valorExtraido.substring(3,8).trim());
            }else{
                valorAtualizado = Double.parseDouble( valorExtraido.substring(3,9).trim());
            }

                Usuario usuarioLogado = new Usuario();
                usuarioLogado.setIdUsuario(idUsuario);
                usuarioLogado.recuperarDadosUsuario(usuarioLogado.getIdUsuario());

            if (pedidoRecuperado.getTotal() != valorAtualizado){
                pedidoRecuperado.setTotal(valorAtualizado);
                pedidoRecuperado.setNome(usuarioLogado.getNome());
                pedidoRecuperado.atualizarPedido(idPedido);
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Selecione um método de pagamento");


            CharSequence[] itens = new CharSequence[]{
                    "Pagar pelo App" , "Máquina cartão", "Dinheiro"
            };
            builder.setSingleChoiceItems(itens, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    metodoPagamento = which;
                }
            });

            final EditText editObservacao = new EditText(this);
            editObservacao.setHint("Digite uma observação");
            builder.setView( editObservacao );

            builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Map<String, Object> data = new HashMap<>();
                    String observacao = editObservacao.getText().toString();

                    data.put("observacaoEmpresa", observacao);
                    data.put("status", Pedido.STATUS_AGUARDANDO);
                    data.put("metodoPagamento", metodoPagamento);
                    data.put("frete",valorFrete);
                    data.put("urlLogo",pedidoRecuperado.getUrlLogo());
                    data.put("nome", usuarioLogado.getNome());
                    data.put("endereco", usuarioLogado.getEndereco());
                    data.put("telefone",usuarioLogado.getTelefone());
                    data.put("nomeEmpresa",pedidoRecuperado.getNomeEmpresa());
                    //adicionar data

                    //Cria o nó meus pedidos
                    pedidoRecuperado.salvarPedidoUsuario();
                    pedidoRecuperado.atualizarStatusPedido(idPedido, data);
                    pedidoRecuperado.atualizarPedidoUsuario(idPedido,data);




                    Pagamento novoPagamento = new Pagamento();

                    //Calculos de porcentagem do valor pago
                    Double porcentagemGahnos = (pedidoRecuperado.getTotal()*porcentagem);
                    Double totalReceber = (pedidoRecuperado.getTotal() - porcentagemGahnos);
                    String valorLiquidoFormatado = new DecimalFormat("##,00")
                            .format(totalReceber)
                            .replaceAll(",",".");
                   //         Fim da formatação dos valores


                    novoPagamento.setIdEmpresa(pedidoRecuperado.getIdEmpresa());
                    novoPagamento.setMetodoPagamento(metodoPagamento);
                    novoPagamento.setIdUsuario(pedidoRecuperado.getIdUsuario());
                    novoPagamento.setIdPedido(pedidoRecuperado.getIdPedido());
                    novoPagamento.setNomeEmpreea(pedidoRecuperado.getNomeEmpresa());
                    novoPagamento.setValor((pedidoRecuperado.getTotal()+valorFrete));
                    novoPagamento.setValorLiquido(Double.parseDouble(valorLiquidoFormatado));
                    novoPagamento.setFrete(valorFrete);
                    novoPagamento.salvarPagamento();


                    if (metodoPagamento == 0){
                        Intent itPagamento = new Intent(CarrinhoActivity.this, PaymentActivity.class);
                        itPagamento.putExtra("pagamento", (pedidoRecuperado.getTotal()+valorFrete));
                        itPagamento.putExtra("frete", valorFrete);
                        itPagamento.putExtra("idPedido",pedidoRecuperado.getIdPedido());
                        itPagamento.putExtra("idEmpresa",pedidoRecuperado.getIdEmpresa());
                        startActivity(itPagamento);
                    }else{

                        Intent itentAndamento = new Intent(CarrinhoActivity.this, ComprasActivity.class);
                        itentAndamento.putExtra("status_aguardando", pedidoRecuperado.getIdEmpresa());
                        itentAndamento.putExtra("idUsuario", pedidoRecuperado.getIdUsuario());
                        itentAndamento.putExtra("idPedido", pedidoRecuperado.getIdPedido());

                        startActivity(itentAndamento);
                    }


                    pedidoRecuperado = null;
                    finish();

                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        }




        }






    private  void exibirMensagem(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {

        super.onStart();

    }

    @Override
    protected void onRestart() {
        recuperarPedido();
        adapterProduto.notifyDataSetChanged();
        super.onRestart();

    }


    @Override
    public boolean onSupportNavigateUp() {
        Log.d("logs","chamou de volta a tela cardápio");
        finish();
        return true;
    }


}