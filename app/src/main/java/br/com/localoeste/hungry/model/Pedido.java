package br.com.localoeste.hungry.model;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Pedido implements Serializable {

    private String idEmpresa;
    private String idUsuario;
    private String idProduto;
    private String idPedido;
    private String nome;
    private String endereco;
    private List<ItemPedido> itens;
    private Double total;
    private String status = "selecionado";
    private int metodoPagemento;
    private String observacaoEmpresa;
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final String STATUS_SELECIONADO = "selecionado";
    public static final String STATUS_PREPARANDO = "preparando";
    public static final String STATUS_A_CAMINHO = "A caminho";
    public static final String STATUS_CHEGOU = "chegou";
    public static final String STATUS_RECEBIDO = "recebido";


    public Pedido() {

    }

    public Pedido(String idCliente, String idEmp) {
        setIdUsuario(idCliente);
        setIdEmpresa(idEmp);


        DocumentReference documentRef = db.collection("pedido").document(getIdEmpresa());
        Map<String, Object> data = new HashMap<>();
        data.put("iEpresa",idEmp);
        data.put("iEpresa",idCliente);

        documentRef.set(data);

    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdPedido() {
        return idPedido;
    }


    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMetodoPagemento() {
        return metodoPagemento;
    }

    public void setMetodoPagemento(int metodoPagemento) {
        this.metodoPagemento = metodoPagemento;
    }

    public String getObservacaoEmpresa() {
        return observacaoEmpresa;
    }

    public void setObservacaoEmpresa(String observacaoEmpresa) {
        this.observacaoEmpresa = observacaoEmpresa;
    }


    public void salvar(){
        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();
        setIdPedido(uuidAsString);
        Task<Void> documentRef = db.collection("pedidos")
                .document(getIdEmpresa())
                .collection(idUsuario)
                .document(getIdPedido()).set(this);




    }

    public void atualizarPedido(String idPedidoSalvo){

        Map<String, Object> data = new HashMap<>();
        data.put("itens", getItens());
        data.put("total",getTotal());
        Task<Void> documentRef = db.collection("pedidos")
                .document(getIdEmpresa())
                .collection(getIdUsuario())
                .document(idPedidoSalvo)
                .update(data);

    }

}
