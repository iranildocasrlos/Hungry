package br.com.localoeste.hungry.activy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import br.com.localoeste.hungry.R;
import br.com.localoeste.hungry.helper.ConfiguracaoFirebase;
import br.com.localoeste.hungry.helper.UsuarioFirebase;

public class ConfiguracoesUsuarioActivity extends AppCompatActivity {

    private EditText nomeUsuario;
    private EditText telefoneUsuario;
    private EditText emailUsuario;
    private EditText cepUsuario;
    private EditText cpfUsuario;
    private EditText enderecoUsuario;
    private String idUsuarioLogado ;
    private FirebaseAuth auth;
    private final UsuarioFirebase usuario = new UsuarioFirebase();
    private FirebaseFirestore referenciaFirestore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes_usuario);

        //Configurações da Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações da conta");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inicializarComponentes();
        buscarDadosUsuario();
    }

    private void inicializarComponentes() {

        nomeUsuario = findViewById(R.id.configuracaoNomeUsuario);
        telefoneUsuario = findViewById(R.id.configuracaoTelefoneUsuario);
        emailUsuario = findViewById(R.id.configuracaoEmailUsuario);
        cepUsuario = findViewById(R.id.configuracaoCepUsuario);
        enderecoUsuario = findViewById(R.id.configuracaoEnderecoUsuario);
        cpfUsuario = findViewById(R.id.configuracaoCpfUsuario);
        referenciaFirestore = ConfiguracaoFirebase.getReferenciaFirestore();
        auth = ConfiguracaoFirebase.getReferenciaAutenticacao();
        idUsuarioLogado = auth.getUid();


    }

    private void buscarDadosUsuario(){

        DocumentReference docRef =  referenciaFirestore.collection("usuarios")
                .document(idUsuarioLogado);

       docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
           @Override
           public void onSuccess(DocumentSnapshot documentSnapshot) {
               if (documentSnapshot.exists()){
                 UsuarioFirebase  usuarioFirebase = documentSnapshot.toObject(UsuarioFirebase.class);
                 if(usuarioFirebase != null){
                     if (usuarioFirebase.getNome() != null) {
                         nomeUsuario.setText(usuarioFirebase.getNome());

                     }
                     if (usuarioFirebase.getTelefone() != null){
                         telefoneUsuario.setText(usuarioFirebase.getTelefone());

                     }
                     if (usuarioFirebase.getEndereco() != null){
                         enderecoUsuario.setText(usuarioFirebase.getEndereco());

                     }
                     if (usuarioFirebase.getEmail() != null){
                         emailUsuario.setText(usuarioFirebase.getEmail());

                     }
                     if (usuarioFirebase.getCep() != null){
                         cepUsuario.setText(usuarioFirebase.getCep());

                     }
                     if (usuarioFirebase.getCpf() != null){
                         cpfUsuario.setText(usuarioFirebase.getCpf());

                     }
                 }
               }
           }
       });



    }

    public void salvarDados(View view){
       usuario.setIdUsuario(idUsuarioLogado);
        if (nomeUsuario.getText() != null) {

            usuario.setNome(nomeUsuario.getText().toString());
        }
        if (telefoneUsuario.getText() != null){

            usuario.setTelefone(telefoneUsuario.getText().toString());
        }
        if (enderecoUsuario.getText() != null){

            usuario.setEndereco(enderecoUsuario.getText().toString());
        }
        if (emailUsuario.getText() != null){

            usuario.setEmail(emailUsuario.getText().toString());
        }
        if (cepUsuario.getText() != null){

            usuario.setCep(cepUsuario.getText().toString());
        }
        if (cpfUsuario.getText() != null){

            usuario.setCpf(cpfUsuario.getText().toString());
        }

       usuario.atualizarDados();
       exibirMensagem("Dados atualizado com sucesso!");



    }

    private  void exibirMensagem(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }


}