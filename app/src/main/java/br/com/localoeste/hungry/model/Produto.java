package br.com.localoeste.hungry.model;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Produto {

    private String urlImagemProduto;
    private String nomeProduto;
    private String descricaoProduto;
    private Double precoProduto;
    private String idProduto;
    private String idUsuario;
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();


    public Produto() {
    }


    //Métodos Getters e Setters


    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public  String getUrlImagemProduto() {
        return urlImagemProduto;
    }

    public void setUrlImagemProduto(String urlImagemProduto) {
        this.urlImagemProduto = urlImagemProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getDescricaoProduto() {
        return descricaoProduto;
    }

    public void setDescricaoProduto(String descricaoProduto) {
        this.descricaoProduto = descricaoProduto;
    }

    public Double getPrecoProduto() {
        return precoProduto;
    }

    public void setPrecoProduto(Double precoProduto) {
        this.precoProduto = precoProduto;
    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }


    public void salvar(){

        DocumentReference documentRef = db.collection("produtos").document(getIdProduto());
        Map<String, Object> data = new HashMap<>();
        data.put("urlImagemProduto",urlImagemProduto);
        data.put("nomeProduto",nomeProduto);
        data.put("descricaoProduto",descricaoProduto);
        data.put("precoProduto",precoProduto);
        data.put("idUsuario",idUsuario);
        data.put("idProduto", idProduto);
        documentRef.set(data);

    }

    public void atualizar(){

        DocumentReference documentRef = db.collection("produtos").document(getIdProduto());
        Map<String, Object> data = new HashMap<>();
        data.put("nomeProduto",nomeProduto);
        data.put("descricaoProduto",descricaoProduto);
        data.put("precoProduto",precoProduto);
        data.put("idUsuario",idUsuario);
        data.put("idProduto", idProduto);
        documentRef.update(data);

    }


    public void salvarFoto(){
        DocumentReference documentRef = db.collection("produtos").document(getIdProduto());
        Map<String, Object> data = new HashMap<>();

        data.put("urlImagemProduto", urlImagemProduto);
        documentRef.update(data);


    }


}
