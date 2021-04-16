package br.com.localoeste.hungry.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import br.com.localoeste.hungry.helper.ConfiguracaoFirebase;

public class Empresa {

    private String idEmpresa;
    private String nomeEmpresa;
    private String tempoEntrega;
    private Double taxaEntrega;
    private String urlImagem;
    private String horarioAbertura;
    private String horarioFechamento;
    private Boolean inicioAutomatico;
    private String categoria;
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();



    public Empresa() {
    }



    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public String getTempoEntrega() {
        return tempoEntrega;
    }

    public void setTempoEntrega(String tempoEntrega) {
        this.tempoEntrega = tempoEntrega;
    }

    public Double getTaxaEntrega() {
        return taxaEntrega;
    }

    public void setTaxaEntrega(Double taxaEntreg) {
        this.taxaEntrega = taxaEntreg;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public String getHorarioAbertura() {
        return horarioAbertura;
    }

    public void setHorarioAbertura(String horarioAbertura) {
        this.horarioAbertura = horarioAbertura;
    }

    public String getHorarioFechamento() {
        return horarioFechamento;
    }

    public void setHorarioFechamento(String horarioFechamento) {
        this.horarioFechamento = horarioFechamento;
    }

    public Boolean getInicioAutomatico() {
        return inicioAutomatico;
    }

    public void setInicioAutomatico(Boolean inicioAutomatico) {
        this.inicioAutomatico = inicioAutomatico;
    }


    public void salvar(){

        DocumentReference documentRef = db.collection("empresas").document(getIdEmpresa());
        Map<String, Object> data = new HashMap<>();
          documentRef.set(this);


    }



}
