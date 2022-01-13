package br.com.localoeste.hungry.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Motoboy implements Serializable {

    private String urlImagemMotoboy;
    private String urlImagemEmpresa;
    private String nomeMotoboy;
    private String marcaMoto;
    private String modeloMoto;
    private String placaMoto;
    private String corMoto;
    private String nomeEmpresa;
    private String descricaoProduto;
    private String IdMotoboy;
    private String idEmpresa;
    private int quantidade;
    private Double precoUnidade;
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();


    public Motoboy() {
    }


    //Métodos Getters e Setters


    private String latitude;
    private String longitude;
    private LatLng meuLocal;

    private String rua;
    private String numero;
    private String cidade;
    private String bairro;


    public String getIdMotoboy() {
        return IdMotoboy;
    }

    public void setIdMotoboy(String idMotoboy) {
        IdMotoboy = idMotoboy;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public LatLng getMeuLocal() {
        return meuLocal;
    }

    public void setMeuLocal(LatLng meuLocal) {
        this.meuLocal = meuLocal;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }


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

    public  String getUrlImagemMotoboy() {
        return urlImagemMotoboy;
    }

    public void setUrlImagemMotoboy(String urlImagemMotoboy) {
        this.urlImagemMotoboy = urlImagemMotoboy;
    }

    public String getNomeMotoboy() {
        return nomeMotoboy;
    }

    public void setNomeMotoboy(String nomeMotoboy) {
        this.nomeMotoboy = nomeMotoboy;
    }

    public String getMarcaMoto() {
        return marcaMoto;
    }

    public void setMarcaMoto(String marcaMoto) { this.marcaMoto = marcaMoto; }


    public String getModeloMoto() {
        return modeloMoto;
    }
    public void setModeloMoto(String modeloMoto) { this.modeloMoto = modeloMoto;    }

    public String getPlacaMoto() {
        return placaMoto;
    }

    public void setPlacaMoto(String placaMoto) {this.placaMoto = placaMoto;  }


    public String getCorMoto() {
        return corMoto;
    }

    public void setCorMoto(String corMoto) {this.corMoto = corMoto;    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void salvar(){

        DocumentReference documentRef = db.collection("motoboy").document(getIdMotoboy());


        documentRef.set(this);
    }




    public void setCep(String postalCode) {
    }
}
