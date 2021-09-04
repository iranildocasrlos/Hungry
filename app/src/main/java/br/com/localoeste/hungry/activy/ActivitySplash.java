package br.com.localoeste.hungry.activy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import br.com.localoeste.hungry.R;
import br.com.localoeste.hungry.helper.ConfiguracaoFirebase;
import br.com.localoeste.hungry.helper.EmpresaFirebase;
import br.com.localoeste.hungry.helper.UsuarioFirebase;

public class ActivitySplash extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private FirebaseFirestore firestore ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
       // getSupportActionBar().hide();
        autenticacao = ConfiguracaoFirebase.getReferenciaAutenticacao();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                verificaUsuarioLogado();
                // abrirAutenticacao();
            }
        }, 1000);

    }

    private void verificaUsuarioLogado() {
        FirebaseUser usuarioAtual = autenticacao.getCurrentUser();
        if (usuarioAtual != null){
            firestore = ConfiguracaoFirebase.getReferenciaFirestore();

            DocumentReference usuarios = firestore.collection("usuarios").document(usuarioAtual.getUid());

            if (usuarios != null){

                usuarios.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        UsuarioFirebase user = documentSnapshot.toObject(UsuarioFirebase.class);

                        if (user != null){
                            if (user.getTipoUsuario()!= null){
                                if (user.getTipoUsuario().equals("cliente")){
                                    abrirTelaPrincipal();
                                    Log.d("HUNGRY", user.getNome() + "==> ID: "+user.getId_Usuario());
                                }else if (user.getTipoUsuario().equals("empresa")){

                                    if (user.getCpf() != null){
                                        abrirTelaPrincipalEmpresa();
                                    }else{
                                        abrirTelaCadastroEmpresa();
                                    }

                                    Log.d("HUNGRY", user.getNome() + "==> ID: "+user.getId_Usuario());
                                }
                            }

                        }else {

                            DocumentReference empresas = firestore.collection("empresas").document(usuarioAtual.getUid());
                            empresas.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    EmpresaFirebase user = documentSnapshot.toObject(EmpresaFirebase.class);

                                    if (user.getCnpj() != null) {

                                        abrirTelaPrincipalEmpresa();
                                    } else {
                                        abrirTelaCadastroEmpresa();
                                    }

                                }


                            });




                        }

                    }
                });

            }else{

                DocumentReference empresas = firestore.collection("empresas").document(usuarioAtual.getUid());
                empresas.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        EmpresaFirebase user = documentSnapshot.toObject(EmpresaFirebase.class);

                        if (user.getCnpj()!= null){

                                    abrirTelaPrincipalEmpresa();
                                }else{
                                    abrirTelaCadastroEmpresa();
                                }

                            }


                });


            }



        }else{
            abrirAutenticacao();
        }
    }

    //Tela principal Usuário
    private void abrirTelaPrincipal() {
        startActivity(new Intent(ActivitySplash.this, HomeActivity.class));
    }

    //Tela principal Usuário
    private void abrirTelaPrincipalEmpresa() {
        startActivity(new Intent(ActivitySplash.this, EmpresaActivity.class));
    }

    private void abrirAutenticacao(){
        Intent i = new Intent(ActivitySplash.this, AutenticacaoActivity.class );
        startActivity(i);
        finish();
    }
    private void abrirTelaCadastroEmpresa() {
        startActivity(new Intent(getApplicationContext(), CadastroEmpresaActivity.class));
    }

}