package br.com.localoeste.hungry.activy;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import br.com.localoeste.hungry.helper.ConfiguracaoFirebase;
import br.com.localoeste.hungry.R;
import br.com.localoeste.hungry.helper.UsuarioFirebase;
import br.com.localoeste.hungry.model.Destino;
import br.com.localoeste.hungry.model.Pedido;
import br.com.localoeste.hungry.model.Usuario;


public class CadastroUsuarioActivity extends AppCompatActivity {


    private EditText nome ;
    private EditText email;
    private EditText endereco;
    private EditText telefone;
    private EditText cpf;
    private String rua;
    private FirebaseFirestore firebaseFirestore;
    private Button btCadastroUsuario;
    private Usuario usuario = new Usuario();
    private LatLng meuLocal;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        iniciaComponentes();

    }

    public void btCadastroUsuario(View view){


        String enderecoDestino = endereco.getText().toString();

        if( !enderecoDestino.equals("") || enderecoDestino != null ){

            Address addressDestino = recuperarEndereco( enderecoDestino );
            if( addressDestino != null ){

                FirebaseAuth autenticacao = ConfiguracaoFirebase.getReferenciaAutenticacao();
                String idUsuario = String.valueOf(autenticacao.getCurrentUser().getUid());
                String nomeU =  nome.getText().toString();
                String emailU =  email.getText().toString();
                String enderecoU =  endereco.getText().toString();
                String telefoneU =  telefone.getText().toString();
                String cpfU =  cpf.getText().toString();
                String cidade = usuario.getCidade();

                final Usuario usuario = new Usuario();
                usuario.setIdUsuario(idUsuario);
                usuario.setNome(nomeU);
                usuario.setEmail(emailU);
                usuario.setEndereco(enderecoU);
                usuario.setTelefone(telefoneU);
                usuario.setCpf(cpfU);
                usuario.setCidade( addressDestino.getAdminArea() );
                usuario.setCep( addressDestino.getPostalCode() );
                usuario.setBairro( addressDestino.getSubLocality() );
                usuario.setRua( addressDestino.getThoroughfare() );
                usuario.setNumero( addressDestino.getFeatureName() );
                usuario.setLatitude( String.valueOf(addressDestino.getLatitude()) );
                usuario.setLongitude( String.valueOf(addressDestino.getLongitude()) );
                usuario.setTipoUsuario("cliente");
                usuario.salvarDados();

                Intent i = new Intent(getApplicationContext(), CarrinhoActivity.class);
                i.putExtra("pegaNome", nome.getText().toString());
                i.putExtra("pegaEndereco", endereco.getText().toString());
                //  abrirTelaPrincipal();
                //finish();


                Intent intent = new Intent(getApplicationContext(), PedidosActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString("nomeUsuario", nomeU);
                bundle.putString("nomeRua", enderecoU);
                bundle.putString("numeroId", idUsuario);

                intent.putExtras(bundle);
                startActivity(intent);

//endereço

                final Destino destino = new Destino();
                destino.setCep( addressDestino.getPostalCode() );
                destino.setBairro( addressDestino.getSubLocality() );
                destino.setRua( addressDestino.getThoroughfare() );
                destino.setNumero( addressDestino.getFeatureName() );
                destino.setLatitude( String.valueOf(addressDestino.getLatitude()) );
                destino.setLongitude( String.valueOf(addressDestino.getLongitude()) );
                // destino.setTipoUsuario("Destino");
                //destino.salvarDados();

                StringBuilder mensagem = new StringBuilder();
                mensagem.append( "Cidade: " + usuario.getCidade() );
                mensagem.append( "\nRua: " + usuario.getRua() );
                mensagem.append( "\nBairro: " + usuario.getBairro() );
                mensagem.append( "\nNúmero: " + usuario.getNumero() );
                mensagem.append( "\nCep: " + usuario.getCep() );

                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle("Confirme seu endereco!")
                        .setMessage(mensagem)
                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //salvar requisição
                                //salvarRequisicao( destino );

                            }
                        }).setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

            }

        }else {
            Toast.makeText(this,
                    "Informe o endereço de destino!",
                    Toast.LENGTH_SHORT).show();
        }

        finish();
    }


    //salvando requisição
    //  private void salvarRequisicao(Destino destino){
    //Requisicao requisicao = new Requisicao();
    //    Pedido pedido = new Pedido();
    //    pedido.setDestino( destino );

    //    Usuario usuarioCliente = UsuarioFirebase.getDadosUsuarioLogado();
    //    usuarioCliente.setLatitude( String.valueOf( meuLocal.latitude ) );
    //    usuarioCliente.setLongitude( String.valueOf( meuLocal.longitude ) );

    //pedido.setIdUsuario( idUsuario );
    //pedido.setNome(nome);
    //  pedido.setStatus( Pedido.STATUS_AGUARDANDO );
    //    pedido.salvar();
    //   }



    private Address recuperarEndereco(String endereco){

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> listaEnderecos = geocoder.getFromLocationName(endereco, 1);
            if( listaEnderecos != null && listaEnderecos.size() > 0 ){
                Address address = listaEnderecos.get(0);

                return address;

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    private void iniciaComponentes(){
        nome = findViewById(R.id.nomeUsuario);
        telefone = findViewById(R.id.telefoneUsuario);
        email = findViewById(R.id.emailUsuario);
        endereco =findViewById(R.id.enderecoUsuario);
        cpf = findViewById(R.id.cpfUsuario);
        btCadastroUsuario = findViewById(R.id.buttonCriarUconta);


    }


    //Tela principal Usuário
    //  private void abrirTelaPrincipal() {
    //    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    //}




}