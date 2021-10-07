package br.com.localoeste.hungry.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MotoboyFirebase {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseFirestore referenciaFirestore;

    private static String idUsuario,nome,email,endereco,telefone,cep,cpf,tipoUsuario;

    public MotoboyFirebase() {
    }
//++++++++++= Geters e Seters ++++++++++++++++++++++++++++++


    public  String getIdUsuario() {
        return idUsuario;
    }

    public  void setIdUsuario(String idUsuario) {
        MotoboyFirebase.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public  void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public  void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public  String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public  String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        MotoboyFirebase.tipoUsuario = tipoUsuario;
    }

    public  String getCep() {
        return cep;
    }

    public  void setCep(String cep) {
        MotoboyFirebase.cep = cep;
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
        DocumentReference documentRef = db.collection("motoboys").document(getId_Usuario());
        Map<String, Object> data = new HashMap<>();
        data.put("nome", nome);
        data.put("email", email);
        data.put("endereco", endereco);
        data.put("telefone",telefone);
        data.put("cpf", cpf);
        data.put("tipoUsuario", tipoUsuario);
        documentRef.set(data);



    }

    public void atualizarDados(){
        DocumentReference documentRef = db.collection("motoboys").document(idUsuario);
        Map<String, Object> data = new HashMap<>();

        data.put("nome", nome);
        data.put("email", email);
        data.put("endereco", endereco);
        data.put("telefone",telefone);
        data.put("cpf", cpf);
        data.put("cep", cep);
        documentRef.update(data);



    }





}
