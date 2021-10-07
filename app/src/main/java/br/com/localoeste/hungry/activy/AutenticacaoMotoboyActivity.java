package br.com.localoeste.hungry.activy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import br.com.localoeste.hungry.R;
import br.com.localoeste.hungry.helper.ConfiguracaoFirebase;
import br.com.localoeste.hungry.helper.EmpresaFirebase;
import br.com.localoeste.hungry.helper.MotoboyFirebase;

public class AutenticacaoMotoboyActivity extends AppCompatActivity {

    private Button btAcessar;
    private EditText campoEmail, campoSenha;
    private Switch tipoAcesso, tipoUsuario;
    private FirebaseAuth  autenticacao;
    private LinearLayout linearTipoUsuario;
    private TextView textViewQuem;
    private FirebaseFirestore firestore ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacao_motoboy);
        // getSupportActionBar().hide();

        inicializarComponenetes();

        ///Metodo para verificar se o usuário está logado
        verificaUsuarioLogado();

        //Esconde ou exibe o swicht abaixo dele
        tipoAcesso.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    linearTipoUsuario.setVisibility(View.VISIBLE);
                    textViewQuem.setVisibility(View.VISIBLE);
                }else{
                    linearTipoUsuario.setVisibility(View.GONE);
                    textViewQuem.setVisibility(View.GONE);
                }
            }
        });


        btAcessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();

                if(!email.isEmpty()){
                    if(!senha.isEmpty()){

                        //verifica o estado do switch
                        if (tipoAcesso.isChecked()){
                            autenticacao.createUserWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {


                                        Toast.makeText(AutenticacaoMotoboyActivity.this,
                                                "Cadastro realizado com sucesso!",
                                                Toast.LENGTH_SHORT).show();

                                        if (getTipoUsuario() == "M"){
                                            abrirTelaCadastroUsuario();
                                        }


                                    }else {
                                        String erroExcecao = "";
                                        try {
                                            throw task.getException();
                                        } catch (FirebaseAuthWeakPasswordException e) {
                                            erroExcecao = "Digite uma senha mais forte!";
                                        } catch (FirebaseAuthInvalidCredentialsException e) {
                                            erroExcecao = "Por favor, digite um e-mail válido";
                                        } catch (FirebaseAuthUserCollisionException e) {
                                            erroExcecao = "Este conta já foi cadastrada";
                                        } catch (Exception e) {
                                            erroExcecao = "ao cadastrar usuário: " + e.getMessage();
                                            e.printStackTrace();
                                        }

                                        Toast.makeText(AutenticacaoMotoboyActivity.this,
                                                "Erro: " + erroExcecao,
                                                Toast.LENGTH_SHORT).show();


                                    }
                                }
                            });
                        }else{

                            autenticacao.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()){

                                        verificaUsuarioFirestore();


                                    }else{
                                        Toast.makeText(AutenticacaoMotoboyActivity.this,
                                                "Erro ao fazer login : " + task.getException() ,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }

                    }else{
                        Toast.makeText(AutenticacaoMotoboyActivity.this, "Preencha o campo senha", Toast.LENGTH_LONG).show();
                    }
                }else{

                    if (getTipoUsuario() == "M"){
                        abrirTelaCadastroUsuario();
                    }
                }

            }
        });

    }

    //Cadastro de Empresa
    private void abrirTelaCadastroEmpresa() {
        startActivity(new Intent(getApplicationContext(), CadastroEmpresaActivity.class));
    }

    //Cadastro de Usuário
    private void abrirTelaCadastroUsuario() {

        startActivity(new Intent(getApplicationContext(), CadastroMotoboyActivity.class));
    }

    //Tela principal Usuário
    private void abrirTelaPrincipal() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        //startActivity(new Intent(getApplicationContext(), CadastroMotoboyActivity.class));
    }

    //Tela principal Usuário
    private void abrirTelaPrincipalEmpresa() {
        startActivity(new Intent(getApplicationContext(), EmpresaActivity.class));
    }

    private void verificaUsuarioLogado() {
        FirebaseUser usuarioAtual = autenticacao.getCurrentUser();
        if (usuarioAtual != null){
            abrirTelaPrincipal();

        }
    }

    private void verificaUsuarioFirestore() {
        FirebaseUser usuarioAtual = autenticacao.getCurrentUser();
        if (usuarioAtual != null){
            firestore = ConfiguracaoFirebase.getReferenciaFirestore();

            DocumentReference usuarios = firestore.collection("motoboys").document(usuarioAtual.getUid());

            if (usuarios != null) {

                usuarios.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        MotoboyFirebase user = documentSnapshot.toObject(MotoboyFirebase.class);

                        if (user != null) {
                            if (user.getTipoUsuario() != null) {
                                if (user.getTipoUsuario().equals("motoboys")) {
                                    abrirTelaPrincipal();
                                    Log.d("HUNGRY", user.getNome() + "==> ID: " + user.getId_Usuario());
                                }
                            }

                        }

                    }
                });

            }
        }
    }


    private String getTipoUsuario(){
        return tipoUsuario.isChecked()? "E" : "M";
    }

    private void inicializarComponenetes() {
        campoEmail = findViewById(R.id.editNomeProduto);
        campoSenha = findViewById(R.id.editTextCategoria);
        btAcessar = findViewById(R.id.btSalvarConfigurcacao);
        tipoAcesso = findViewById(R.id.switchAcesso);
        autenticacao = ConfiguracaoFirebase.getReferenciaAutenticacao();
        tipoUsuario = findViewById(R.id.switchTipoAcesso);
        linearTipoUsuario = findViewById(R.id.linearTipoUsuario);
        textViewQuem = findViewById(R.id.textViewQuem);
        //autenticacao.signOut();
    }





}