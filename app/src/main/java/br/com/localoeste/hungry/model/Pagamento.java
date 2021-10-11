package br.com.localoeste.hungry.model;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

public class Pagamento {

    Map<String, Object> jsonMap;
    String idUsuario;
    String idEmpresa;
    String idPedido;
    String dataTransacao;
    Boolean devido = false;
    String nomeEmpreea;
    Double valor;
    Double ganhos;
    Double frete;
    int porcentagem = 15;
    int metodoPagamento;
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String stringDate;





    public Pagamento() {
    }


    public Map<String, Object> getJsonMap() {
        return jsonMap;
    }

    public void setJsonMap(Map<String, Object> jsonMap) {
        this.jsonMap = jsonMap;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getDataTransacao() {
        return dataTransacao;
    }

    public void setDataTransacao(String dataTransacao) {
        this.dataTransacao = dataTransacao;
    }

    public Boolean getDevido() {
        return devido;
    }

    public void setDevido(Boolean devido) {
        this.devido = devido;
    }

    public String getNomeEmpreea() {
        return nomeEmpreea;
    }

    public void setNomeEmpreea(String nomeEmpreea) {
        this.nomeEmpreea = nomeEmpreea;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Double getGanhos() {
        return ganhos;
    }

    public void setGanhos(Double ganhos) {
        this.ganhos = ganhos;
    }

    public int getPorcentagem() {
        return porcentagem;
    }

    public void setPorcentagem(int porcentagem) {
        this.porcentagem = porcentagem;
    }

    public Double getFrete() {
        return frete;
    }

    public void setFrete(Double frete) {
        this.frete = frete;
    }

    public int getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(int metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }



    //Métodos trasação

    public void salvarPagamento(){

        //get Month and Year current
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        String mesAno = (String.valueOf(month+1)+String.valueOf(year));


        //Displaying the actual date
        Date date = new Date();
        SimpleDateFormat  DateFor = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        stringDate = DateFor.format(date);
        setDataTransacao(stringDate);


        //Calcular nossa porcentagem
        Double valorTrasacao = getValor();
        Double resultado = (porcentagem % 100) * valorTrasacao;
        Double valorDaPorcentagem =  resultado;

        //Formatando o ressultado para o padrão brasileiro de casas decimais
        String valorFormatado = new DecimalFormat("##,00")
                .format(valorDaPorcentagem)
                .replaceAll(",",".");


        setGanhos(Double.parseDouble(valorFormatado));

        if (getMetodoPagamento() == 1){
            setDevido(true);
        }

         db.collection("pagamentos")
                .document(getIdEmpresa())
                .collection(mesAno)
               .document(getIdPedido()).set(this);
    }



    public void salvarJsonStripe(){
        setIdPedido(idPedido);
        setJsonMap(jsonMap);

        //get Month and Year current
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        String mesAno = (String.valueOf(month+1)+String.valueOf(year));


        db.collection("pagamentos")
                .document(getIdEmpresa())
                .collection(mesAno)
                .document(getIdPedido())
                .update("jsonMap",jsonMap);
    }





}
