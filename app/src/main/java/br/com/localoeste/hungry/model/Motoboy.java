package br.com.localoeste.hungry.model;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Motoboy implements Serializable {

    private String urlImagemMotoboy;
    private String urlImagemEmpresa;
    private String nomeMotoboy;
    private String nomeEmpresa;
    private String descricaoMotoboy;
    private Double precoMotoboy;
    private String idMotoboy;
    private String idEmpresa;

    private int quantidade;
    private Double precoUnidade;
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();


    public Motoboy() {
    }


    //Métodos Getters e Setters


    public String getUrlImagemEmpresa() {
        return urlImagemEmpresa;
    }

    public void setUrlImagemEmpresa(String urlImagemEmpresa) {
        this.urlImagemEmpresa = urlImagemEmpresa;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idUsuario) {
        this.idEmpresa = idUsuario;
    }

    public  String geturlImagemMotoboy() {
        return urlImagemMotoboy;
    }

    public void seturlImagemMotoboy(String urlImagemMotoboy) {
        this.urlImagemMotoboy = urlImagemMotoboy;
    }

    public String getnomeMotoboy() {
        return nomeMotoboy;
    }

    public void setnomeMotoboy(String nomeMotoboy) {
        this.nomeMotoboy = nomeMotoboy;
    }
    


    public Double getPrecoUnidade() {
        return precoUnidade;
    }


    public String getIdMotoboy() {
        return idMotoboy;
    }

    public void setIdMotoboy(String idMotoboy) {
        this.idMotoboy = idMotoboy;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void salvar(){

        DocumentReference documentRef = db.collection("motoboys")
                .document(getIdEmpresa())
                .collection("motoboys_disponiveis")
                .document(getIdMotoboy());
        Map<String, Object> data = new HashMap<>();
        data.put("urlImagemMotoboy",urlImagemMotoboy);
        data.put("nomeMotoboy",nomeMotoboy);
        data.put("descricaoMotoboy",descricaoMotoboy);
        data.put("precoMotoboy",precoMotoboy);
        data.put("precoUnidade",precoUnidade);
        data.put("idEmpresa",idEmpresa);
        data.put("idMotoboy", idMotoboy);
        data.put("urlImagemEmpresa", urlImagemEmpresa);
        data.put("nomeEmpresa", nomeEmpresa);
        documentRef.set(data);

    }

    public void atualizar(){

        DocumentReference documentRef = db.collection("motoboys").document(getIdMotoboy());
        Map<String, Object> data = new HashMap<>();
        data.put("nomeMotoboy",nomeMotoboy);
        data.put("descricaoMotoboy",descricaoMotoboy);
        data.put("precoMotoboy",precoMotoboy);
        data.put("idEmpresa",idEmpresa);
        data.put("idMotoboy", idMotoboy);
        documentRef.update(data);

    }


    public void salvarFoto(){
        DocumentReference documentRef = db.collection("motoboys")
                .document(idEmpresa)
                .collection("motoboys_disponiveis")
                .document(getIdMotoboy());
        Map<String, Object> data = new HashMap<>();

        data.put("urlImagemMotoboy", urlImagemMotoboy);
        documentRef.update(data);


    }


    public String getUrlImagemMotoboy() {
        return urlImagemMotoboy;
    }

    public String getNomeMotoboy() {
        return nomeMotoboy;
    }

    public double getPrecoMotoboy() {
        return precoMotoboy;
    }

    public String getDescricaoMotoboy() {
        return descricaoMotoboy;
    }

    public void setNomeMotoboy(String nomeMotoboy) {
    }

    public void setDescricaoProduto(String descricaoProduto) {
    }

    public void setUrlImagemMotoboy(String urlImagemSelecionada) {
    }
}
