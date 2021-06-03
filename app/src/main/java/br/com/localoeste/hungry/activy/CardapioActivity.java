 package br.com.localoeste.hungry.activy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.localoeste.hungry.R;
import br.com.localoeste.hungry.adapter.AdapterProduto;
import br.com.localoeste.hungry.helper.ConfiguracaoFirebase;
import br.com.localoeste.hungry.helper.UsuarioFirebase;
import br.com.localoeste.hungry.listener.RecyclerItemClickListener;
import br.com.localoeste.hungry.model.Empresa;
import br.com.localoeste.hungry.model.ItemPedido;
import br.com.localoeste.hungry.model.Pedido;
import br.com.localoeste.hungry.model.Produto;
import br.com.localoeste.hungry.model.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class CardapioActivity extends AppCompatActivity {


    private CircleImageView imagemEmpresaCardapio;
    private TextView nomeEmpresaCadapio;
    private TextView horario;
    private TextView status;
    private TextView categoria;
    private TextView textVerCarrinho;
    private TextView textQuantidade;
    private TextView textValor;
    private RecyclerView recyclerViewEmpresaCardapio;
    private Empresa empresaSelecionada;
    private AdapterProduto adapterProduto;
    private List<Produto> produtos = new ArrayList<>();
    public List<ItemPedido>itensCarrinho = new ArrayList<>();
    private ItemPedido itemPedido;
    private FirebaseFirestore referenciaFirestore;
    private String idEmpresaLogada ;
    private String idUsuarioLogado;
    private StorageReference storageRef;
    private String currentDate;
    private String stringDate;
    private SimpleDateFormat format = new SimpleDateFormat("HH:mm");
    private AlertDialog dialog;
    private Pedido pedidoRecuperado;
    private Pedido pedidoAnterior;
    private Usuario usuario;
    private int qtdItensCarrinho;
    private Double totalCarrinho;


    //Initializing Calender Object
    private Calendar calendar = Calendar.getInstance();



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
        recuperarDadosUsuario();



        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            DecimalFormat df = new DecimalFormat("0.00");
            if (bundle.containsKey("item")){
                itemPedido = (ItemPedido) bundle.getSerializable("item");
                pedidoAnterior = (Pedido)  bundle.getSerializable("pedidoAnterior");
                ItemPedido novoPedido = new ItemPedido();

//                Map<String, Object> usuarioShared = loadMap();

                  if (pedidoAnterior != null){
                      if (pedidoAnterior.getStatus().equals("selecionado")){
                          pedidoAnterior.atualizarPedido(pedidoAnterior.getIdPedido());
                          idEmpresaLogada = pedidoAnterior.getIdEmpresa();
                      }


                  }else{

                      novoPedido.setIdProduto(itemPedido.getIdProduto());
                      idEmpresaLogada = itemPedido.getIdEmpresa();

                      novoPedido.setNomeProduto(itemPedido.getNomeProduto());
                      novoPedido.setDescricaoProduto(itemPedido.getDescricaoProduto());
                      novoPedido.setPrecoProduto(itemPedido.getPrecoProduto());
                      novoPedido.setQuantidadeProduto(itemPedido.getQuantidadeProduto());
                      novoPedido.setObservacao(itemPedido.getObservacao());
                      novoPedido.setIdEmpresa(idEmpresaLogada);
                      itensCarrinho.add(novoPedido);



                      //salvar pedido
                      if (pedidoRecuperado == null){

                              pedidoRecuperado = new Pedido();
                              pedidoRecuperado.setIdEmpresa(itemPedido.getIdEmpresa());
                              pedidoRecuperado.setIdProduto(itemPedido.getIdProduto());
                              pedidoRecuperado.setIdUsuario(idUsuarioLogado);
                              pedidoRecuperado.setEndereco(usuario.getEndereco());
                              pedidoRecuperado.setMetodoPagemento(1);
                              pedidoRecuperado.setTotal(itemPedido.getPrecoProduto());
                              pedidoRecuperado.setItens(itensCarrinho);
                              pedidoRecuperado.setNome(usuario.getNome());
                              pedidoRecuperado.setNomeEmpresa(itemPedido.getNomeEmpresa());
                              pedidoRecuperado.setUrlLogo(itemPedido.getUrlLogo());

                              pedidoRecuperado.salvar();
                          String numeroFormatato = df.format(pedidoRecuperado.getTotal());
                          textValor.setText("R$: "+ numeroFormatato);

                          itensCarrinho = pedidoRecuperado.getItens();

                          for (ItemPedido item : itensCarrinho){
                              int qtde = item.getQuantidadeProduto();
                              Double preco = item.getPrecoProduto();

                              qtdItensCarrinho += qtde;
                          }



                      }else{
                          Log.d("log", "Já existe um pedido deste usuário");
                          // pedidoRecuperado.setTotal(itemPedido.getPrecoProduto());
                          pedidoRecuperado = new Pedido();
                          pedidoRecuperado.setItens(itensCarrinho);
                          pedidoRecuperado.atualizarPedido(pedidoRecuperado.getIdPedido());

                          itensCarrinho = pedidoRecuperado.getItens();

                          for (ItemPedido item : itensCarrinho){
                              int qtde = item.getQuantidadeProduto();
                              Double preco = item.getPrecoProduto();


                              qtdItensCarrinho += qtde;
                          }

                          String numeroFormatato = df.format(pedidoRecuperado.getTotal());
                          textValor.setText("R$: "+ numeroFormatato);


                      }


                  }



                  if (idEmpresaLogada != null) {
                      pesquisarEmpresa(idEmpresaLogada);
                  }else{
                      pesquisarEmpresa(empresaSelecionada.getIdEmpresa());
                  }
            }else{

                empresaSelecionada = (Empresa)bundle.getSerializable("empresa");
                //Verificada se foi criado pela tela de detalhes ou Home
                if (empresaSelecionada.getNomeFantasia() != null){


                    configurarEmpresa(empresaSelecionada);

                }else{

                    pesquisarEmpresa(idEmpresaLogada);
                }


            }


            //Configurações do recyclerView
            recyclerViewEmpresaCardapio.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewEmpresaCardapio.setHasFixedSize(true);
            adapterProduto = new AdapterProduto(produtos,this);
            recyclerViewEmpresaCardapio.setAdapter(adapterProduto);


            // Configura a ação de toque do Recycler
            recyclerViewEmpresaCardapio.addOnItemTouchListener(new RecyclerItemClickListener(
                    this,
                    recyclerViewEmpresaCardapio,
                    new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {

                            Produto produtoSelecionado = produtos.get(position);
                            Intent itDescricao = new Intent(CardapioActivity.this ,DescricaoProdutoActivity.class);
                            itDescricao.putExtra("produto",produtoSelecionado);
                            if (pedidoRecuperado != null){
                                if (pedidoRecuperado.getStatus().equals("selecionado")){
                                    itDescricao.putExtra("pedido", (Serializable) pedidoRecuperado);
                                }

                            }

                            startActivity(itDescricao);
                            finish();
                        }

                        @Override
                        public void onLongItemClick(View view, int position) {

                        }

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        }
                    }
            ));


            //Recupera produtos da empresa
            recuperarProdutos(idEmpresaLogada);






        }



        //Ação do botão verCrrinho
        textVerCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pedidoRecuperado != null) {
                    Intent intCarrrinho = new Intent(CardapioActivity.this, CarrinhoActivity.class);
                    intCarrrinho.putExtra("idEmpresa", idEmpresaLogada);
                    intCarrrinho.putExtra("idUsuario", idUsuarioLogado);
                    intCarrrinho.putExtra("idPedido", pedidoRecuperado.getIdPedido());
                    startActivity(intCarrrinho);
                    totalCarrinho = 0.0;
                    qtdItensCarrinho = 0;

                }
            }
        });


    }

  //Sharead Preferences

//    private void saveMap(Map<String, Object> inputMap) {
//       SharedPreferences preferences = getApplicationContext().getSharedPreferences("dados_usuario", Context.MODE_PRIVATE);
//
//        if (preferences != null) {
//            JSONObject jsonObject = new JSONObject(inputMap);
//            String jsonString = jsonObject.toString();
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.remove("dados_usuario").apply();
//            editor.putString("dados_usuario", jsonString);
//            editor.commit();
//        }
//    }
//
//
//    private Map<String, Object> loadMap() {
//        Map<String, Object> outputMap = new HashMap<>();
//        SharedPreferences preferences = getApplicationContext().getSharedPreferences("dados_usuario", Context.MODE_PRIVATE);
//
//
//
//        try {
//            if (preferences != null) {
//                String jsonString = preferences.getString("dados_usuario", (new JSONObject()).toString());
//                JSONObject jsonObject = new JSONObject(jsonString);
//                Iterator<String> keysItr = jsonObject.keys();
//                while (keysItr.hasNext()) {
//                    String key = keysItr.next();
//                    outputMap.put(key, jsonObject.get(key));
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return outputMap;
//    }

    // COnfigura  o cabeçalho  baseado na empresa passada
    private void configurarEmpresa(Empresa empresaSelecionada) {


        nomeEmpresaCadapio.setText(empresaSelecionada.getNomeFantasia());
        idEmpresaLogada = empresaSelecionada.getIdEmpresa();
        categoria.setText(empresaSelecionada.getCategoria());
        horario.setText(empresaSelecionada.getHorarioAbertura()+" - "+empresaSelecionada.getHorarioFechamento());
        if (empresaSelecionada.getStatus()){

            Date present = null;
            try {

                //Horário atual
                present = format.parse(stringDate);
                calendar.setTime(present);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int min = calendar.get(Calendar.MINUTE);

                //Horário abertura
                Date horaAbertura = format.parse(empresaSelecionada.getHorarioAbertura());
                calendar.setTime(horaAbertura);
                int hourAbertura = calendar.get(Calendar.HOUR);
                int minAbertura = calendar.get(Calendar.MINUTE);

                //Horário fechamento
                Date horaFechamento = format.parse(empresaSelecionada.getHorarioFechamento());
                calendar.setTime(horaFechamento);
                int hourFechamento = calendar.get(Calendar.HOUR_OF_DAY);
                int minFechamento = calendar.get(Calendar.MINUTE);

                //Verifica se está no horário de funcionamento
                if (hour > hourAbertura){

                    if (hour == hourFechamento){

                        if (min == minFechamento){
                            status.setTextColor(Color.GREEN);
                            status.setText("Aberto");

                        }else{
                            status.setTextColor(Color.RED);
                            status.setText("Fechado");
                        }


                    }else if (hour < hourFechamento){
                        status.setTextColor(Color.GREEN);
                        status.setText("Aberto");

                    }else{
                        status.setTextColor(Color.RED);
                        status.setText("Fechado");
                    }



                }else{
                    status.setTextColor(Color.RED);
                    status.setText("Fechado");

                }



            } catch (ParseException e) {
                e.printStackTrace();
            }

        }else{
            status.setTextColor(Color.RED);
            status.setText("Fechado");


        }


        String url = empresaSelecionada.getUrlImagem();
        Picasso.get().load(url).into(imagemEmpresaCardapio);



    }

    private void inicializarComponentes() {

        recyclerViewEmpresaCardapio = findViewById(R.id.recyclerPedidosAtendimento);
        nomeEmpresaCadapio = findViewById(R.id.textNomeEmCardapio);
        imagemEmpresaCardapio =  findViewById(R.id.imageCardapioEmpresa);
        horario = findViewById(R.id.textCardapioHorario);
        status = findViewById(R.id.textCadapioStatus);
        categoria = findViewById(R.id.textCardapioCategoria);
        textQuantidade = findViewById(R.id.textQuantidade);
        textValor = findViewById(R.id.textValor);
        textVerCarrinho = findViewById(R.id.textFinalizar);
        referenciaFirestore = ConfiguracaoFirebase.getReferenciaFirestore();
        storageRef =  ConfiguracaoFirebase.getFirebaseStorage();
        idUsuarioLogado = UsuarioFirebase.getId_Usuario();
        usuario = new Usuario();
        usuario.setIdUsuario(idUsuarioLogado);

        Bundle bundle = getIntent().getExtras();
        empresaSelecionada = (Empresa)bundle.getSerializable("empresa");




        //pega horário atual
        //Initializing the date formatter
        DateFormat Date = DateFormat.getDateInstance();

        //Displaying the actual date
        currentDate = Date.format(calendar.getTime());
        Date date = new Date();
        stringDate = format.format(date);




    }

    //Recuperando Produtos da empresa
    private void recuperarProdutos(String idEmp) {
        referenciaFirestore
                .collection("produtos")
                .document(idEmp)
                .collection("produtos_disponiveis")
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





   //Método par recuperar empresa
    private void pesquisarEmpresa(String id) {
        DocumentReference empresaRef = referenciaFirestore
                .collection("empresas")
                .document(idEmpresaLogada);

        empresaRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Empresa dadosEmpresa = documentSnapshot.toObject(Empresa.class);
                    if (dadosEmpresa != null){
                        if (dadosEmpresa.getNomeFantasia() != null){

                             configurarEmpresa(dadosEmpresa);

                        }

                    }
                }
            }
        });

    }



   private void recuperarPedido(String idEmp){
       qtdItensCarrinho = 0;
       totalCarrinho = 0.0;
       itensCarrinho = new ArrayList<>();
       Task<QuerySnapshot> coletionPedidos =  referenciaFirestore.collection("pedidos")
               .document(idEmp)
               .collection("aguardando")
               .whereEqualTo("idUsuario", idUsuarioLogado).get()

      .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
          @Override
          public void onComplete(@NonNull Task<QuerySnapshot> task) {



              if (task.isSuccessful()) {
                  for (QueryDocumentSnapshot document : task.getResult()) {

                      pedidoRecuperado = document.toObject(Pedido.class);



                      if(pedidoRecuperado != null) {

                          if (pedidoRecuperado.getStatus().equalsIgnoreCase("selecionado")) {


                              itensCarrinho = pedidoRecuperado.getItens();

                              for (ItemPedido item : itensCarrinho) {
                                  int qtde = item.getQuantidadeProduto();
                                  Double preco = item.getPrecoProduto();

                                  totalCarrinho += preco;
                                  qtdItensCarrinho += qtde;
                              }


                              DecimalFormat df = new DecimalFormat("0.00");
                              String numeroFormatato = df.format(totalCarrinho);

                              textQuantidade.setText("quant.: " + String.valueOf(qtdItensCarrinho));
                              textValor.setText("R$: " + numeroFormatato);

                          }

                      }

                      Log.d("log", document.getId() + " => " + document.getData());
                  }
              } else {
                  Log.d("log", "Error getting documents: ", task.getException());
              }
          }
      });

        dialog.dismiss();
   }




    public  void recuperarDadosUsuario(){

        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Carregando dados")
                .setCancelable( false )
                .build();
        dialog.show();

        DocumentReference docRef =  referenciaFirestore.collection("usuarios")
                .document(idUsuarioLogado);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Usuario novoUsuario = documentSnapshot.toObject(Usuario.class);

                    usuario = novoUsuario;

                }
                if (empresaSelecionada != null){
                    recuperarPedido(empresaSelecionada.getIdEmpresa());
                }
                else{
                    recuperarPedido(idEmpresaLogada);
                }

            }
        });




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cardapio, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuPedido:

                if (pedidoRecuperado != null) {
                    Intent intCarrrinho = new Intent(CardapioActivity.this, CarrinhoActivity.class);
                    intCarrrinho.putExtra("idEmpresa", idEmpresaLogada);
                    intCarrrinho.putExtra("idUsuario", idUsuarioLogado);
                    intCarrrinho.putExtra("idPedido", pedidoRecuperado.getIdPedido());
                    startActivity(intCarrrinho);
                    textQuantidade.setText("0");
                    textValor.setText("0.00");
                    totalCarrinho = 0.0;
                    qtdItensCarrinho = 0;


                }
                break;

            case R.id.menuCompras:
                Intent itentCompras = new Intent(CardapioActivity.this, ComprasActivity.class);
                startActivity(itentCompras);
                break;

        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onRestart() {

        super.onRestart();
        recuperarPedido(idEmpresaLogada);
        Log.d("log","Chamou onStarte do Cardápio ");
    }

    @Override
    protected void onResume() {
        Log.d("log","Chamou onResume do Cardápio ");

        super.onResume();

    }

    @Override
    protected void onStop() {
        Log.d("log","Chamou onStop do Cardápio ");

        super.onStop();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }




}