package br.com.localoeste.hungry.activy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import br.com.localoeste.hungry.R;
import br.com.localoeste.hungry.helper.ConfiguracaoFirebase;
import br.com.localoeste.hungry.model.Usuario;

public class CadastroUsuarioActivity extends AppCompatActivity {


    private EditText nome ;
    private EditText email;
    private EditText endereco;
    private EditText telefone;
    private EditText cpf;
    private FirebaseFirestore firebaseFirestore;
    private Button btCadastroUsuario;
    private Usuario usuario = new Usuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        iniciaComponentes();


        btCadastroUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth autenticacao = ConfiguracaoFirebase.getReferenciaAutenticacao();
               String idUsuario = String.valueOf(autenticacao.getCurrentUser().getUid());
                String nomeU =  nome.getText().toString();
                String emailU =  email.getText().toString();
                String enderecoU =  endereco.getText().toString();
                String telefoneU =  telefone.getText().toString();
                String cpfU =  cpf.getText().toString();

                usuario.setIdUsuario(idUsuario);
                usuario.setNome(nomeU);
                usuario.setEmail(emailU);
                usuario.setEndereco(enderecoU);
                usuario.setTelefone(telefoneU);
                usuario.setCpf(cpfU);
                usuario.setTipoUsuario("cliente");
                usuario.salvarDados();

                abrirTelaPrincipal();
                finish();


            }
        });



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
    private void abrirTelaPrincipal() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }




}