package br.com.localoeste.hungry.model;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.com.localoeste.hungry.helper.ConfiguracaoFirebase;

public class Pedido implements Serializable {

    private Destino destino;

    private String idEmpresa;
    private String idUsuario;
    private String idProduto;
    private String idPedido;
    private String nome;
    private Boolean user = false;
    private String nomeEmpresa;
    private String urlLogo;
    private String endereco;
    private String telefone;
    private List<ItemPedido> itens;
    private Double total;
    private Double frete;
    private String status = "selecionado";
    private int metodoPagamento;
    private String observacaoEmpresa;
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final String STATUS_SELECIONADO = "selecionado";
    public static final String STATUS_PREPARANDO = "preparando";
    public static final String STATUS_A_CAMINHO = "a caminho";
    public static final String STATUS_PRONTO = "pronto";
    //public static final String STATUS_FRETE_ACEITO = "a caminho";
    public static final String STATUS_CHEGOU = "chegou";
    public static final String STATUS_RECEBIDO = "recebido";
    public static final String STATUS_AGUARDANDO = "aguardando";
    private Usuario usuario;


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

    public Destino getDestino() {
        return destino;
    }

    public void setDestino(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }

    public void setMetodoPagamento(int metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
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

    public Double getFrete() {
        return frete;
    }

    public void setFrete(Double frete) {
        this.frete = frete;
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
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

    public int getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagemento(int metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public String getObservacaoEmpresa() {
        return observacaoEmpresa;
    }

    public Boolean getUser() {
        return user;
    }

    public void setUser(Boolean user) {
        this.user = user;
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
                .collection("aguardando")
                .document(getIdPedido()).set(this);

    }


    public void salvarPedidoUsuario(){

        setIdPedido(getIdPedido());
        setStatus(Pedido.STATUS_AGUARDANDO);
        setUser(true);
        Task<Void> documentRef = db.collection("meus_pedidos")
                .document("usuarios")
                .collection(idUsuario)
                .document(getIdPedido()).set(this);

    }


    public void atualizarPedidoUsuario(String idPedidoSalvo, Map<String, Object>  data){

        Task<Void> documentRef = db.collection("meus_pedidos")
                .document("usuarios")
                .collection(idUsuario)
                .document(idPedidoSalvo)
                .update(data);

    }

    public void atualizarPedido(String idPedidoSalvo){

        Map<String, Object> data = new HashMap<>();
        data.put("itens", getItens());
        data.put("total",getTotal());
        Task<Void> documentRef = db.collection("pedidos")
                .document(getIdEmpresa())
                .collection("aguardando")
                .document(idPedidoSalvo)
                .update(data);

    }


    public void atualizarStatus(){
        setIdPedido(getIdPedido());
        // setStatus(Pedido.STATUS_PREPARANDO);
        setUser(true);
        Task<Void> documentRef = db.collection("pedidos")
                .document(getIdEmpresa())
                .collection("aguardando")
                .document(getIdPedido()).set(this);

    }

    public void atualizarStatusMeusPedidos(){

        setIdPedido(getIdPedido());
        // setStatus(Pedido.STATUS_PREPARANDO);
        setUser(true);
        Task<Void> documentRef = db.collection("meus_pedidos")
                .document("usuarios")
                .collection(idUsuario)
                .document(getIdPedido()).set(this);

    }


    public void atualizarStatusAcaminho(){
        setIdPedido(getIdPedido());
        // setStatus(Pedido.STATUS_PREPARANDO);
        setUser(true);
        Task<Void> documentRef = db.collection("pedidos")
                .document(getIdEmpresa())
                .collection("aguardando")
                .document(getIdPedido()).set(this);

    }

    public void atualizarStatusMeusPedidosAcaminho(){

        setIdPedido(getIdPedido());
        // setStatus(Pedido.STATUS_PREPARANDO);
        setUser(true);
        Task<Void> documentRef = db.collection("meus_pedidos")
                .document("usuarios")
                .collection(idUsuario)
                .document(getIdPedido()).set(this);

    }


    public void atualizarStatusPedido(String idPedidoSalvo , Map<String, Object>  data){

        data.put("itens", getItens());
        data.put("total",getTotal());
        Task<Void> documentRef = db.collection("pedidos")
                .document(getIdEmpresa())
                .collection("aguardando")
                .document(idPedidoSalvo)
                .update(data);

    }

}
