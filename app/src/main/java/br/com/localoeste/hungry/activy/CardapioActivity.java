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
import android.provider.CalendarContract;
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

import java.text.DateFormat;
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
import br.com.localoeste.hungry.model.Produto;
import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class CardapioActivity extends AppCompatActivity {


    private CircleImageView imagemEmpresaCardapio;
    private TextView nomeEmpresaCadapio;
    private TextView horario;
    private TextView status;
    private TextView categoria;
    private RecyclerView recyclerViewEmpresaCardapio;
    private Empresa empresaSelecionada;
    private AdapterProduto adapterProduto;
    private List<Produto> produtos = new ArrayList<>();
    private FirebaseFirestore referenciaFirestore;
    private String idEmpresaLogada ;
    private String idUsuarioLogado;
    private StorageReference storageRef;
    private String currentDate;
    private String stringDate;
    private SimpleDateFormat format = new SimpleDateFormat("HH:mm");
    private AlertDialog dialog;

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

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            empresaSelecionada = (Empresa)bundle.getSerializable("empresa");
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

                            if (hour <= hourFechamento){

                                    status.setTextColor(Color.GREEN);
                                    status.setText("Aberto");
                                    Log.d("hora","Aberto");


                            }else {
                                status.setTextColor(Color.RED);
                                status.setText("Fechado");
                                Log.d("hora","Fechado");
                            }



                    }else{
                        status.setTextColor(Color.RED);
                        status.setText("Fechado");
                        Log.d("hora","Fechado");
                    }



                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }else{
                status.setTextColor(Color.RED);
                status.setText("Fechado");
                Log.d("hora","Fechado");

            }


            String url = empresaSelecionada.getUrlImagem();
            Picasso.get().load(url).into(imagemEmpresaCardapio);

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
                            startActivity(itDescricao);
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
            recuperarProdutos();

            //Recupera DADOS Usuario
            recuperarDadosUsuario();


        }


    }

    private void inicializarComponentes() {

        recyclerViewEmpresaCardapio = findViewById(R.id.recyclerProdutoCardapio);
        nomeEmpresaCadapio = findViewById(R.id.textNomeEmCardapio);
        imagemEmpresaCardapio =  findViewById(R.id.imageCardapioEmpresa);
        horario = findViewById(R.id.textCardapioHorario);
        status = findViewById(R.id.textCadapioStatus);
        categoria = findViewById(R.id.textCardapioCategoria);
        referenciaFirestore = ConfiguracaoFirebase.getReferenciaFirestore();
        storageRef =  ConfiguracaoFirebase.getFirebaseStorage();
        idUsuarioLogado = UsuarioFirebase.getId_Usuario();

        //pega horário atual
        //Initializing the date formatter
        DateFormat Date = DateFormat.getDateInstance();

        //Displaying the actual date
        currentDate = Date.format(calendar.getTime());
        Date date = new Date();
        stringDate = format.format(date);
        Log.d("hora",stringDate);

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



   private void recuperarDadosUsuario(){

        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Carregando dados!")
                .setCancelable(false)
                .build();

        dialog.show();

                DocumentReference docRef =  referenciaFirestore.collection("usuarios")
               .document(idUsuarioLogado);

                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            UsuarioFirebase usuarioFirebase = documentSnapshot.toObject(UsuarioFirebase.class);
                            if (usuarioFirebase != null) {



                            }
                            recuperarPedido();
                        }

                    }
                });


   }






   private void recuperarPedido(){

        dialog.dismiss();
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

                break;

        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onRestart() {

        super.onRestart();
        recuperarProdutos();
        Log.d("logs","chamou onRestart");
    }

    @Override
    protected void onResume() {

        super.onResume();

    }








}