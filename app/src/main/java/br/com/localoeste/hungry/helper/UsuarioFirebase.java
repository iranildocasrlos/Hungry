package br.com.localoeste.hungry.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class UsuarioFirebase {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static String nome,email;

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
    public static void salvarDados(String nome, String email){
        DocumentReference documentRef = db.collection("usuarios").document(getId_Usuario());
        Map<String, Object> data = new HashMap<>();
        data.put("nome", nome);
        data.put("email", email);
        documentRef.set(data);



    }

}
