package br.com.localoeste.hungry.activy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import br.com.localoeste.hungry.R;
import br.com.localoeste.hungry.helper.UsuarioFirebase;

public class CadastroUsuarioActivity extends AppCompatActivity {


    private TextView nome ;
    private TextView email;
    private TextView endereco;
    private TextView telefone;
    private TextView cpf;
    private FirebaseFirestore firebaseFirestore;
    private Button btCadastroUsuario;
    private UsuarioFirebase usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        iniciaComponentes();


        btCadastroUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usuario.setNome(nome.getText().toString());
                usuario.setEmail(email.getText().toString());
                usuario.setEndereco(endereco.getText().toString());
                usuario.setTelefone(telefone.getText().toString());
                usuario.setCpf(cpf.getText().toString());
                usuario.setTipoUsuario("cliente");
                usuario.salvarDados();

                abrirTelaPrincipal();
                finish();


            }
        });



    }


    private void iniciaComponentes(){
        nome = findViewById(R.id.nomeEmpresa);
        telefone = findViewById(R.id.telefoneEmpresa);
        email = findViewById(R.id.emailEmpresa);
        endereco =findViewById(R.id.enderecoEmpresa);
        cpf = findViewById(R.id.cpfProprietario);
        btCadastroUsuario = findViewById(R.id.buttonCriarUconta);
        usuario = new UsuarioFirebase();

    }


    //Tela principal Usuário
    private void abrirTelaPrincipal() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }




}