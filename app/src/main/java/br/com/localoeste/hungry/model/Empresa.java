package br.com.localoeste.hungry.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import br.com.localoeste.hungry.helper.ConfiguracaoFirebase;

public class Empresa implements Serializable {

    private String nomeConta;
    private String idEmpresa;
    private String nomeFantasia;
    private String tempoEntrega;
    private Double taxaEntrega;
    private String urlImagem;
    private String horarioAbertura;
    private String horarioFechamento;
    private Boolean inicioAutomatico;
    private String categoria;
    private Boolean status;
    private String endereco;
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();



    public Empresa() {
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNomeConta() {
        return nomeConta;
    }

    public void setNomeConta(String nomeConta) {
        this.nomeConta = nomeConta;
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

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void salvar(){

        DocumentReference documentRef = db.collection("empresas").document(getIdEmpresa());
        Map<String, Object> data = new HashMap<>();
          documentRef.set(this);


    }

    public void atualizar(){

        DocumentReference documentRef = db.collection("empresas").document(getIdEmpresa());
        Map<String, Object> data = new HashMap<>();
        data.put("nomeFantasia", nomeFantasia);
        data.put("idEmpresa", idEmpresa);
        data.put("tempoEntrega", tempoEntrega);
        data.put("taxaEntrega",taxaEntrega);
        data.put("categoria", categoria);
        data.put("horarioAbertura", horarioAbertura);
        data.put("horarioFechamento", horarioFechamento);
        data.put("inicioAutomatico", inicioAutomatico);

        //Verifica o status
        if (inicioAutomatico.booleanValue()){
            data.put("status",true);
        }else{
            data.put("status",false);
        }
        documentRef.update(data);

    }

    public void atualizarLogo(){
        DocumentReference documentRef = db.collection("empresas").document(getIdEmpresa());
        Map<String, Object> data = new HashMap<>();

        data.put("urlImagem", urlImagem);
        documentRef.update(data);


    }


}
