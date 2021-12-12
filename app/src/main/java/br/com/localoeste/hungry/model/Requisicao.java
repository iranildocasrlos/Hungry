package br.com.localoeste.hungry.model;

import android.widget.EditText;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.com.localoeste.hungry.helper.ConfiguracaoFirebase;
import br.com.localoeste.hungry.helper.EmpresaFirebase;
import br.com.localoeste.hungry.helper.UsuarioFirebase;

public class Requisicao {

    private Usuario cliente;
    private Usuario motoboy;
    private Destino destino;
    private String usuario;
    private String id;
    private String status;
    private  String idUsuario;
    private String cidade;
    private String bairro;
    private  String rua;
    private  String numero;
    private  String latitude;
    private  String longitude;

    private  String empresa;

    private List<Requisicao> requisicoes;

    public static final String STATUS_SELECIONADO = "selecionado";
    public static final String STATUS_PREPARANDO = "preparando";
    public static final String STATUS_A_CAMINHO = "a caminho";
    public static final String STATUS_VIAGEM = "viagem";
    public static final String STATUS_PRONTO = "pronto";
    public static final String STATUS_RECEBIDO = "recebido";
    public static final String STATUS_AGUARDANDO = "aguardando";


    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseFirestore referenciaFirestore =  ConfiguracaoFirebase.getReferenciaFirestore();


    public Requisicao() {
    }
    public void salvarRequisicao(){


        Pedido pedido = new Pedido();

//        DocumentReference documentRef = db.collection("requisicoes").document(getStatus());
//           Map<String, Object> data = new HashMap<>();
        //         data.put("usuario",usuario);
        //    data.put("status",status);
        //   data.put("id",id);
        //   data.put("idUsuario",idUsuario);
        //   data.put("cidade",cidade);
        //   data.put("bairro",bairro);
        //   data.put("rua",rua);
        //   data.put("numero",numero);
        //   data.put("latitude",latitude);
        //   data.put("longitude",longitude);

        //   documentRef.set(data);

     //   CollectionReference requisicoes = db.collection("requisicoes");
        //       Map<String, Object> data = new HashMap<>();

        //        data.put("usuario",usuario);
        //  data.put("status",status);
        //   data.put("id",id);
        //   data.put("cidade",cidade);
        //   data.put("bairro",bairro);
        //   data.put("rua",rua);
        //   data.put("numero",numero);
        //    data.put("latitude",latitude);
        //    data.put("longitude",longitude);
        //    data.put("empresa",empresa);

        //     requisicoes.document("pronto").set(data);
        // requisicoes.set(data);


       // UUID uuid = UUID.randomUUID();
        //String uuidAsString = uuid.toString();
      //  setEmpresa(uuidAsString);
        Task<Void> documentRef = db.collection("requisicoes")
                .document(EmpresaFirebase.getId_empresa())
                //.collection(String.valueOf(new ItemPedido()))
                .collection(pedido.getIdPedido())
                .document(getStatus()).set(this);


    }


    public Usuario getCliente() {
        return cliente;
    }
    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}