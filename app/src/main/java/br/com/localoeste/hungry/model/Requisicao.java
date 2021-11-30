package br.com.localoeste.hungry.model;

import android.widget.EditText;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import br.com.localoeste.hungry.helper.ConfiguracaoFirebase;

public class Requisicao {

    private Usuario cliente;
    private Usuario motoboy;
    private Destino destino;
    private String usuario;
    private String bairro;
    private String id;
    private String status;
    private  String idUsuario;

    public static final String STATUS_SELECIONADO = "selecionado";
    public static final String STATUS_PREPARANDO = "preparando";
    public static final String STATUS_A_CAMINHO = "a caminho";
    public static final String STATUS_CHEGOU = "chegou";
    public static final String STATUS_RECEBIDO = "recebido";
    public static final String STATUS_AGUARDANDO = "aguardando";


    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseFirestore referenciaFirestore =  ConfiguracaoFirebase.getReferenciaFirestore();

    public Requisicao() {
    }
    public void salvarRequisicao(){
        DocumentReference documentRef = db.collection("requisicao")
                .document(getStatus());
        Map<String, Object> data = new HashMap<>();
        data.put("usuario",usuario);
        data.put("status",status);
        data.put("bairro",bairro);
        documentRef.set(data);

    }


    public Usuario getCliente() {
        return cliente;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }

    public Usuario getMotoboy() {
        return motoboy;
    }

    public void setMotoboy(Usuario motoboy) {
        this.motoboy = motoboy;
    }

    public Destino getDestino() {
        return destino;
    }

    public void setDestino(Destino destino) {
        this.destino = destino;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }


}