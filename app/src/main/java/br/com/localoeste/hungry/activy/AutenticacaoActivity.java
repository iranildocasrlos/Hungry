package br.com.localoeste.hungry.activy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import br.com.localoeste.hungry.R;
import br.com.localoeste.hungry.helper.ConfiguracaoFirebase;
import br.com.localoeste.hungry.helper.UsuarioFirebase;

public class AutenticacaoActivity extends AppCompatActivity {

    private Button btAcessar;
    private EditText campoEmail, campoSenha;
    private Switch tipoAcesso, tipoUsuario;
    private FirebaseAuth  autenticacao;
    private LinearLayout linearTipoUsuario;
    private TextView textViewQuem;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacao);
        getSupportActionBar().hide();

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


                                        Toast.makeText(AutenticacaoActivity.this,
                                                "Cadastro realizado com sucesso!",
                                                Toast.LENGTH_SHORT).show();

                                        if (getTipoUsuario() == "U"){
                                            abrirTelaCadastroUsuario();
                                        }else{
                                            abrirTelaCadastroEmpresa();
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

                                        Toast.makeText(AutenticacaoActivity.this,
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
                                        if (getTipoUsuario() == "U"){

                                            abrirTelaPrincipal();


                                        }else{
                                            abrirTelaPrincipalEmpresa();
                                        }

                                    }else{
                                        Toast.makeText(AutenticacaoActivity.this,
                                                "Erro ao fazer login : " + task.getException() ,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }

                    }else{
                        Toast.makeText(AutenticacaoActivity.this, "Preencha o campo senha", Toast.LENGTH_LONG).show();
                    }
                }else{

                    if (getTipoUsuario() == "U"){
                        abrirTelaCadastroUsuario();
                    }else{
                        abrirTelaCadastroEmpresa();
                    }
//                    Toast.makeText(AutenticacaoActivity.this, "Preencha o campo email", Toast.LENGTH_LONG).show();
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

        startActivity(new Intent(getApplicationContext(), CadastroUsuarioActivity.class));
    }

    //Tela principal Usuário
    private void abrirTelaPrincipal() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
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


    private String getTipoUsuario(){
        return tipoUsuario.isChecked()? "E" : "U";
    }

    private void inicializarComponenetes() {
        campoEmail = findViewById(R.id.editTextCadastroEmail);
        campoSenha = findViewById(R.id.editTextTextPassword);
        btAcessar = findViewById(R.id.btAcesso);
        tipoAcesso = findViewById(R.id.switchAcesso);
        autenticacao = ConfiguracaoFirebase.getReferenciaAutenticacao();
        tipoUsuario = findViewById(R.id.switchTipoAcesso);
        linearTipoUsuario = findViewById(R.id.linearTipoUsuario);
        textViewQuem = findViewById(R.id.textViewQuem);
        //autenticacao.signOut();
    }





}