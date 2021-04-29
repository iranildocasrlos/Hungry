package br.com.localoeste.hungry.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EmpresaFirebase {

    private static String nome,email,endereco,telefone,cnpj,idProprietario;
    private Boolean status;
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public EmpresaFirebase() {
    }


    //++++++++++= Geters e Seters ++++++++++++++++++++++++++++++



    public String getNome() {
        return nome;
    }

    public  void setNome(String nome) {
        EmpresaFirebase.nome = nome;
    }

    public  String getEmail() {
        return email;
    }

    public  void setEmail(String email) {
        EmpresaFirebase.email = email;
    }

    public  String getEndereco() {
        return endereco;
    }

    public  void setEndereco(String endereco) {
        EmpresaFirebase.endereco = endereco;
    }

    public  String getTelefone() {
        return telefone;
    }

    public  void setTelefone(String telefone) {
        EmpresaFirebase.telefone = telefone;
    }

    public  String getCnpj() {
        return cnpj;
    }

    public  void setCnpj(String cnpj) {
        EmpresaFirebase.cnpj = cnpj;
    }

    public  String getIdProprietario() {
        return idProprietario;
    }

    public  void setIdProprietario(String idProprietario) {
        EmpresaFirebase.idProprietario = idProprietario;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }



    //Métodos

    public void salvarDados() {
        DocumentReference documentRef = db.collection("empresas").document(idProprietario);
        Map<String, Object> data = new HashMap<>();
        data.put("nomeConta", nome);
        data.put("email", email);
        data.put("endereco", endereco);
        data.put("telefone",telefone);
        data.put("cnpj", cnpj);
        data.put("idProprietario", idProprietario);
        data.put("status",false);
        documentRef.set(data);
    }

    public void atualizarDados() {
        DocumentReference documentRef = db.collection("empresas").document(idProprietario);
        Map<String, Object> data = new HashMap<>();
        data.put("nomeConta", nome);
        data.put("email", email);
        data.put("endereco", endereco);
        data.put("telefone",telefone);
        data.put("cnpj", cnpj);
        data.put("idProprietario", idProprietario);
        documentRef.update(data);
    }


    ///Retorna o id do Usuario logado
    public static String getId_empresa(){
        FirebaseAuth autenticacao = ConfiguracaoFirebase.getReferenciaAutenticacao();
        return autenticacao.getCurrentUser().getUid();
    }

}



