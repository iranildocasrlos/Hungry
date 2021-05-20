package br.com.localoeste.hungry.model;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Produto implements Serializable {

    private String urlImagemProduto;
    private String nomeProduto;
    private String descricaoProduto;
    private Double precoProduto;
    private String idProduto;
    private String idEmpresa;
    private int quantidade;
    private Double precoUnidade;
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();


    public Produto() {
    }


    //Métodos Getters e Setters


    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idUsuario) {
        this.idEmpresa = idUsuario;
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

    public Double getPrecoUnidade() {
        return precoUnidade;
    }

    public void setPrecoUnidade(Double precoUnidade) {
        this.precoUnidade = precoUnidade;
    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void salvar(){

        DocumentReference documentRef = db.collection("produtos")
                .document(getIdEmpresa())
                .collection("produtos_disponiveis")
                .document(getIdProduto());
        Map<String, Object> data = new HashMap<>();
        data.put("urlImagemProduto",urlImagemProduto);
        data.put("nomeProduto",nomeProduto);
        data.put("descricaoProduto",descricaoProduto);
        data.put("precoProduto",precoProduto);
        data.put("precoUnidade",precoUnidade);
        data.put("idEmpresa",idEmpresa);
        data.put("idProduto", idProduto);
        documentRef.set(data);

    }

    public void atualizar(){

        DocumentReference documentRef = db.collection("produtos").document(getIdProduto());
        Map<String, Object> data = new HashMap<>();
        data.put("nomeProduto",nomeProduto);
        data.put("descricaoProduto",descricaoProduto);
        data.put("precoProduto",precoProduto);
        data.put("idEmpresa",idEmpresa);
        data.put("idProduto", idProduto);
        documentRef.update(data);

    }


    public void salvarFoto(){
        DocumentReference documentRef = db.collection("produtos")
                .document(idEmpresa)
                .collection("produtos_disponiveis")
                .document(getIdProduto());
        Map<String, Object> data = new HashMap<>();

        data.put("urlImagemProduto", urlImagemProduto);
        documentRef.update(data);


    }


}
