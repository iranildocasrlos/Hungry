package br.com.localoeste.hungry.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class UsuarioFirebase {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static String nome,email,endereco,telefone,cpf;

    public UsuarioFirebase() {
    }
//++++++++++= Geters e Seters ++++++++++++++++++++++++++++++

    public static String getNome() {
        return nome;
    }

    public  void setNome(String nome) {
        this.nome = nome;
    }

    public static String getEmail() {
        return email;
    }

    public  void setEmail(String email) {
        this.email = email;
    }

    public static String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public static String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public static String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    ///Retorna o id do Usuario logado
    public static String getId_Usuario(){
        FirebaseAuth autenticacao = ConfiguracaoFirebase.getReferenciaAutenticacao();
        return autenticacao.getCurrentUser().getUid();
    }

    ///Retorna o nome do Usuario logado
    public static FirebaseUser getUsuarioAtual(){
        FirebaseAuth usuario = ConfiguracaoFirebase.getReferenciaAutenticacao();
        return usuario.getCurrentUser();
    }


    ///Salva os dados no Firestore
    public void salvarDados(){
        DocumentReference documentRef = db.collection("usuarios").document(getId_Usuario());
        Map<String, Object> data = new HashMap<>();
        data.put("nome", nome);
        data.put("email", email);
        data.put("endereco", endereco);
        data.put("telefone",telefone);
        data.put("cpf", cpf);
        documentRef.set(data);



    }

}
