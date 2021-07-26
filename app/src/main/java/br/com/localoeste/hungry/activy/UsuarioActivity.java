package br.com.localoeste.hungry.activy;

import androidx.appcompat.app.AppCompatActivity;

import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import br.com.localoeste.hungry.R;
import br.com.localoeste.hungry.helper.ConfiguracaoFirebase;
import br.com.localoeste.hungry.model.Usuario;

public class UsuarioActivity extends AppCompatActivity implements OnMapReadyCallback {

    //Componentes
    private EditText editDestino;
    private LinearLayout linearLayoutDestino;
    private Button buttonChamarUber;

    private GoogleMap mMap;
    private FirebaseAuth autenticacao;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private LatLng localUsuario;
    //private boolean cancelarUber = false;
    private DatabaseReference firebaseRef;
    //private Requisicao requisicao;
    private Usuario passageiro;
    private String statusRequisicao;
    //private Destino destino;
    private Marker marcadorMotorista;
    private Marker marcadorUsuario;
    private Marker marcadorDestino;
    private Usuario Empresa;
    private LatLng localEmpresa;

    public static void redirecionaUsuarioLogado(MainActivity mainActivity) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        inicializarComponentes();

        //Adiciona listener para status da requisição
        //verificaStatusRequisicao();
    }

    //private void verificaStatusRequisicao() {
    //}

    private void inicializarComponentes() {

        //Configurações iniciais
        // autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        // firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        // SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        //       .findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);
    }

    private void adicionaMarcadorPassageiro(LatLng localizacao, String titulo){

        if( marcadorUsuario != null )
            marcadorUsuario.remove();

        marcadorUsuario = mMap.addMarker(
                new MarkerOptions()
                        .position(localizacao)
                        .title(titulo)
                // .icon(BitmapDescriptorFactory.fromResource(R.drawable.usuario))
        );

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Recuperar localizacao do usuário
        // recuperarLocalizacaoUsuario();


    }
}