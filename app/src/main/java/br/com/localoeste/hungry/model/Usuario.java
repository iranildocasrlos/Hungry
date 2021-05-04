package br.com.localoeste.hungry.model;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import br.com.localoeste.hungry.helper.ConfiguracaoFirebase;

public class Usuario {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseFirestore referenciaFirestore =  ConfiguracaoFirebase.getReferenciaFirestore();
    private  String idUsuario,nome,email,endereco,telefone,cep,cpf,tipoUsuario;

    public Usuario() {
    }

    public  String getIdUsuario() {
        return idUsuario;
    }

    public  void setIdUsuario(String idUsuario) {
        idUsuario = idUsuario;
    }

    public  String getNome() {
        return nome;
    }

    public  void setNome(String nome) {
        nome = nome;
    }

    public  String getEmail() {
        return email;
    }

    public  void setEmail(String email) {
        email = email;
    }

    public  String getEndereco() {
        return endereco;
    }

    public  void setEndereco(String endereco) {
        endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public  void setTelefone(String telefone) {
        telefone = telefone;
    }

    public  String getCep() {
        return cep;
    }

    public  void setCep(String cep) {
        cep = cep;
    }

    public  String getCpf() {
        return cpf;
    }

    public  void setCpf(String cpf) {
       cpf = cpf;
    }

    public  String getTipoUsuario() {
        return tipoUsuario;
    }

    public  void setTipoUsuario(String tipoUsuario) {
        tipoUsuario = tipoUsuario;
    }

    public  Usuario recuperarDadosUsuario(String id) {


        DocumentReference docRef = referenciaFirestore.collection("usuarios")
                .document(id);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Usuario usuarioRecuperado = documentSnapshot.toObject(Usuario.class);
                    setIdUsuario(usuarioRecuperado.getIdUsuario());
                    setEndereco(usuarioRecuperado.getEndereco());
                    setNome(usuarioRecuperado.getNome());
                    setTelefone(usuarioRecuperado.getTelefone());


                }

            }
        });


        return this;


    }

}
