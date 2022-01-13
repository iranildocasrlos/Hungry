package br.com.localoeste.hungry.activy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import br.com.localoeste.hungry.helper.ConfiguracaoFirebase;
import br.com.localoeste.hungry.R;
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


        textViewQuem.setVisibility(View.GONE);

        btAcessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();

                if(!email.isEmpty()){
                    if(!senha.isEmpty()){

                        //verifica o estado do switch
                        //if (tipoAcesso.isChecked()){
                        tipoAcesso.isChecked();
                        autenticacao.createUserWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {


                                    Toast.makeText(AutenticacaoMotoboyActivity.this,
                                            "Cadastro realizado com sucesso!",
                                            Toast.LENGTH_SHORT).show();
                                    abrirTelaCadastroMotoboy();

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
                        Toast.makeText(AutenticacaoMotoboyActivity.this, "Preencha o campo senha", Toast.LENGTH_LONG).show();
                    }
                }else{

                    if (getTipoUsuario() == "M"){
                        abrirTelaCadastroMotoboy();
                    }
                }

            }
        });

    }


    //Cadastro de Usuário
    private void abrirTelaCadastroMotoboy() {

        startActivity(new Intent(getApplicationContext(), CadastroMotoboyActivity.class));
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