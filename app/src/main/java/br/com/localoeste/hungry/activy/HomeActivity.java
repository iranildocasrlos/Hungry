package br.com.localoeste.hungry.activy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.SphericalUtil;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

//Geofire

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.localoeste.hungry.R;
import br.com.localoeste.hungry.adapter.AdapterEmpresa;
import br.com.localoeste.hungry.adapter.AdapterProduto;
import br.com.localoeste.hungry.helper.ConfiguracaoFirebase;
import br.com.localoeste.hungry.helper.Permissoes;
import br.com.localoeste.hungry.helper.UsuarioFirebase;
import br.com.localoeste.hungry.listener.RecyclerItemClickListener;
import br.com.localoeste.hungry.model.Empresa;
import br.com.localoeste.hungry.model.Produto;
import br.com.localoeste.hungry.model.Usuario;

import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {


    private FirebaseAuth autenticacao;
    private MaterialSearchView searchView;
    private RecyclerView recyclerViewEmpressas;
    private FirebaseFirestore referenciaFirestore;
    private List<Empresa> empresas = new ArrayList<>();
    private AdapterEmpresa adapterEmpresa;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationManager locationManager;
    private LocationListener locationListener;
    public LatLng localUsuario;
    private LatLng localEmpresa;

    private ProgressBar progressBar;

    private TextView tv;
    private ImageView img;
    //TextView text;



    private String[] permissoes = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET
    };

    public UsuarioFirebase usuario = new UsuarioFirebase();
    private Permissoes permissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
      //  progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.MULTIPLY);


        inicializarComponentes();
        recuperarLocalizacaoUsuario();

        //Configurações da Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Hungry");
        setSupportActionBar(toolbar);

        //Configurações do recyclerView
        recyclerViewEmpressas.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewEmpressas.setHasFixedSize(true);
        adapterEmpresa = new AdapterEmpresa(empresas);
        recyclerViewEmpressas.setAdapter(adapterEmpresa);


        //Recupera produtos da empresa


        //Configurações da pesquisa
        searchView.setHint("Pesquisar Empresa");
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                //  pesquisarEmpresa(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                pesquisarEmpresa(newText);
                return true;
            }

        });


        //Configura o evennto de clique
        recyclerViewEmpressas.addOnItemTouchListener(new RecyclerItemClickListener(
                this,
                recyclerViewEmpressas,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Empresa empresaSelecionada = empresas.get(position);
                        Intent i = new Intent(HomeActivity.this, CardapioActivity.class);

                        i.putExtra("empresa", empresaSelecionada);

                        startActivity(i);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        ));



    }


    public void recuperarLocalizacaoUsuario() {

        Log.i("Distancia","Chanou RecuperarLocalização ");
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {



                try {
                    //recuperar latitude e longitude
                    Double latitude = location.getLatitude();
                    Double longitude = location.getLongitude();
                    localUsuario = new LatLng(latitude, longitude);
                    Log.i("Distancia","RecuperarLocalização retornou dados");
                    //Atualizar GeoFire
//                    UsuarioFirebase.atualizarDadosLocalizacao(
//                            location.getLatitude(),
//                            location.getLongitude()
//                    );


                    Log.i("Distancia","RecuperarLocalização latitude: "+localUsuario.latitude);

                    if (latitude != null && longitude != null) {
//                        motorista.setLatitude(latitude);
//                        motorista.setLongitude(longitude);
                        Double total = latitude + longitude;
                        Log.d("localizacao", "latitude --> " + latitude + "longitude --> " + longitude);
//                        adicionaEventoCliqueRecyclerView();
//                        locationManager.removeUpdates(locationListener);
//                        adapter.notifyDataSetChanged();
                        recuperarEmpresas();

                     locationManager.removeUpdates(locationListener);
                     progressBar.setVisibility(View.INVISIBLE);
                    }


                }

                catch (NullPointerException erro){

                    onDestroy();

                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        //Solicitar atualizações de localização
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            1,
                            0,
                            locationListener
                    );
                }
            }

        }




    }


    private void pesquisarEmpresa(String nome) {
        referenciaFirestore
                .collection("empresas")
                .whereArrayContains("nomeFantasia", nome+"\uf8ff")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        empresas.clear();
                        if (task.isSuccessful()){

                            for(QueryDocumentSnapshot document : task.getResult()){
                                if (document.getData() != null){
                                    Empresa empresa =  document.toObject(Empresa.class);
                                    if (empresa.getUrlImagem()!= "" && empresa.getUrlImagem() != null){

                                        String enderecoEmpresa = empresa.getEndereco();
                                        empresas.add(empresa);

                                    }


                                }



                            }
                            adapterEmpresa.notifyDataSetChanged();
                        }

                    }

                });



    }


    private void inicializarComponentes() {
      autenticacao = ConfiguracaoFirebase.getReferenciaAutenticacao();
      referenciaFirestore = ConfiguracaoFirebase.getReferenciaFirestore();
      searchView =  findViewById(R.id.materialSearchView);
      recyclerViewEmpressas = findViewById(R.id.recyclerEmpresas);
        Permissoes.validarPermissoes(permissoes, this, 1);

    }

    //Exibe apena sas empressa no raio de 8KM
    private void recuperarEmpresas() {
        Log.i("Distancia","Entrou no recuperar empresas ");

        referenciaFirestore
                .collection("empresas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        empresas.clear();
                        Log.i("Distancia","Limpou lista empresas");

                        if (task.isSuccessful()){
                            Log.i("Distancia","Sucesso na consulta");
                            for(QueryDocumentSnapshot document : task.getResult()){
                                if (document.getData() != null){

                                    Empresa empresa=  document.toObject(Empresa.class);
                                    if (empresa.getUrlImagem() != "" && empresa.getUrlImagem() != null){
                                        Log.i("Distancia","passou na validação da empresa possui imagem");
                                        Address addressEnderecoEMpresa = recuperarEndereco(empresa.getEndereco());
                                       // -23,541945 -46,934203

                                        //Double latitude = -23.541945;
                                        //Double longitude = -46.934203;
                                       // localUsuario = new LatLng(latitude, longitude);


                                        Log.i("Distancia","Vai verificar se localUsuario possui dados...");
                                        if(localUsuario != null){
                                            Log.i("Distancia","passou --> LocalUsuario possui dados");
                                            if (localUsuario.latitude != 0 && addressEnderecoEMpresa != null){

                                                localEmpresa = new LatLng(addressEnderecoEMpresa.getLatitude(),
                                                        addressEnderecoEMpresa.getLongitude());

                                                //Calcula distancia
                                                double distance = SphericalUtil.computeDistanceBetween(localUsuario, localEmpresa);
                                                Log.i("Distancia","A Distancia é = "+formatNumber(distance));
                                                Double distanciaEncontrada = formatNumber(distance);

                                                if (distanciaEncontrada <= 8.0){
                                                    Log.i("Distancia","Adicionou emepresa");
                                                    empresas.add(empresa);
                                                }
                                               else if (distanciaEncontrada > 8.0){
                                                //(localUsuario.latitude != 0 && addressEnderecoEMpresa == null) {
                                                   // View text = null;
                                                   //    text.setVisibility(View.VISIBLE);
                                                   // Toast.makeText(getApplicationContext(),"Não existe empresas cadastradas em sua região",Toast.LENGTH_SHORT).show();
                                                    tv=(TextView)findViewById(R.id.tv);
                                                    tv.setText("Sem empresas cadastradas em sua localidade");

                                                    img=(ImageView)findViewById(R.id.img);
                                                    img.setImageResource(R.drawable.exclamation);


                                                }
                                            }//else{
                                               // tv=(TextView)findViewById(R.id.tv);
                                                //tv.setText("Sem empresas cadastradas em sua localidade");
                                            //}
                                        }



                                    }


                                }

                            }

                        }
                        adapterEmpresa.notifyDataSetChanged();
                    }
                });

    }


    //Converte endereço em geolocalização
    public Address recuperarEndereco(String endereco) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {

            List<Address> listaEnderecos = geocoder.getFromLocationName(endereco, 1);

            if (listaEnderecos.isEmpty()){
                Log.d("Lista", "Lista Vazia");
            }
            if (listaEnderecos ==  null){
                Log.d("Lista", "Lista nula");
            }

            while (listaEnderecos.size() == 0){
                listaEnderecos = geocoder.getFromLocationName(endereco, 1);
            }

            if (listaEnderecos != null && listaEnderecos.size() > 0) {
                Address address = listaEnderecos.get(0);
                 return address;

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }





    //Formata distancia entre os dois pontos usuário e empresa
    private Double formatNumber(double distance) {
        String unit = "m";
        if (distance >= 1000) {
            distance /= 1000;
            unit = "km";
        }else{
            distance = 1.00;
        }

//        return String.format("%2.0f%s", distance, unit);
          String distanciar = String.format("%2.2f", distance).replaceAll(" ","");

        if (distanciar.contains(",")){
            distanciar = String.format("%2.2f", distance).replaceAll(",",".");
        }
         Double distancia = Double.parseDouble(distanciar);
          return distancia;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_usuario, menu);

        MenuItem item = menu.findItem(R.id.menuPesquisa);
        searchView.setMenuItem(item);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSair:
                deslogarUsuario();
                break;
            case R.id.menuConfiguracoes:
                abrirConfiguracoes();
                break;
            case R.id.menuNovoProduto:

                break;



            case R.id.menuComprasHome:
                Intent itentCompras = new Intent(HomeActivity.this, ComprasActivity.class);
                startActivity(itentCompras);
                break;



        }


        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuario() {
        autenticacao.signOut();
        finish();
    }

    private void abrirConfiguracoes(){

        startActivity(new Intent(HomeActivity.this, ConfiguracoesUsuarioActivity.class));

    }


    @Override
    protected void onRestart() {
        adapterEmpresa.notifyDataSetChanged();
        super.onRestart();
        recuperarLocalizacaoUsuario();
        Log.d("logs","chamou onRestart");
    }


    @Override
    protected void onResume() {
        adapterEmpresa.notifyDataSetChanged();
        Log.d("logs","chamou onResume");
        super.onResume();
        locationManager.removeUpdates(locationListener);
        recuperarLocalizacaoUsuario();


    }




}