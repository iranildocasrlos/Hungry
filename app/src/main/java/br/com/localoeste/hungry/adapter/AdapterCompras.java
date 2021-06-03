package br.com.localoeste.hungry.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import br.com.localoeste.hungry.R;
import br.com.localoeste.hungry.helper.ConfiguracaoFirebase;
import br.com.localoeste.hungry.model.Empresa;
import br.com.localoeste.hungry.model.ItemPedido;
import br.com.localoeste.hungry.model.Pedido;
import br.com.localoeste.hungry.model.Produto;
import br.com.localoeste.hungry.model.Usuario;

public class AdapterCompras extends RecyclerView.Adapter<AdapterCompras.MyViewHolder> {

    private List<Pedido> pedidos;
    private Context context;
    private FirebaseFirestore referenciaFirestore;
    private  String  url,nomeRecuperadoUsuario,tipoUsuario;
    private ImageView imagemEmpresaCompras;
    private String nomeRecuperadoEmpresa;


    public AdapterCompras(List<Pedido> pedidos, Context context) {
        this.pedidos = pedidos;
        this.context = context;

    }

    @NonNull
    @Override
    public AdapterCompras.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_compras, parent, false);
        return new AdapterCompras.MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCompras.MyViewHolder holder, int i) {
        Pedido pedido = pedidos.get(i);


        String idUsuario = pedido.getIdUsuario();
        pesquisarUsuario(idUsuario);
        pesquisarEmpresa(pedido.getIdEmpresa());



        //Carregar imagens
        if (pedido.getStatus().equals(Pedido.STATUS_AGUARDANDO)){

            Picasso.get().load( R.drawable.aguardando ).into( holder.imagemEmpresa );
           holder.linearLayout.setBackgroundColor(Color.GRAY);
           holder.status.setBackgroundColor(Color.GRAY);
           holder.status.setTextColor(Color.WHITE);

        }else if (pedido.getStatus().equals(Pedido.STATUS_PREPARANDO)){

            Picasso.get().load( R.drawable.cooking ).into( holder.imagemEmpresa );
            holder.linearLayout.setBackgroundColor(Color.YELLOW);
            holder.status.setBackgroundColor(Color.YELLOW);


        }else if (pedido.getStatus().equals(Pedido.STATUS_A_CAMINHO)){

            Picasso.get().load( R.drawable.delivery ).into( holder.imagemEmpresa );
            holder.linearLayout.setBackgroundColor(Color.argb(255,255,99,71));
            holder.status.setBackgroundColor(Color.argb(255,255,99,71));
            holder.status.setTextColor(Color.WHITE);

        }else if (pedido.getStatus().equals(Pedido.STATUS_CHEGOU)){

            Picasso.get().load( R.drawable.chegou ).into( holder.imagemEmpresa );
            holder.status.setBackgroundColor(Color.argb(255,0,191,255));
            holder.linearLayout.setBackgroundColor(Color.argb(255,0,191,255));
            holder.status.setTextColor(Color.WHITE);

        }else if (pedido.getStatus().equals(Pedido.STATUS_RECEBIDO)){

            Picasso.get().load( R.drawable.entregue ).into( holder.imagemEmpresa );
            holder.linearLayout.setBackgroundColor(Color.argb(255,0,255,127));
            holder.status.setBackgroundColor(Color.argb(255,0,255,127));

        }



        String nomeProdutos = "";

        for (int x = 0 ; x < pedido.getItens().size(); x++) {
           ItemPedido item = pedido.getItens().get(x);
            nomeProdutos += "\n"+(x+1)+": "+item.getNomeProduto();
        }


        if (!pedido.getUser()){
            holder.nomeEmpresaCompras.setText(pedido.getNome()+"\nEndereço: "+pedido.getEndereco()+" - Fone: "+pedido.getTelefone());
        }else {
            holder.nomeEmpresaCompras.setText(pedido.getNomeEmpresa());
        }

        if (url == null){
            Picasso.get().load( pedido.getUrlLogo()).into( holder.imagemEmpresaCompras );
        }

        holder.nomeEmpresa.setText("Pedido: "+nomeProdutos);
        holder.quantidade.setText("Total de itens: "+String.valueOf(pedido.getItens().size()));
        holder.descricao.setText("Obserrvações: "+pedido.getObservacaoEmpresa());
        holder.preco.setText("R$ " + pedido.getTotal());
        holder.status.setText("Status : "+pedido.getStatus().toUpperCase());



    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imagemEmpresa;
        ImageView imagemEmpresaCompras;
        TextView nomeEmpresa;
        TextView nomeEmpresaCompras;
        TextView quantidade;
        TextView descricao;
        TextView status;
        TextView preco;
        LinearLayout linearLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            nomeEmpresa = itemView.findViewById(R.id.textNomePedidoEmpresa);
            descricao = itemView.findViewById(R.id.textDescricaoPedido);
            quantidade = itemView.findViewById(R.id.quantidade_pedidos);
            status = itemView.findViewById(R.id.status_pedidos_empresa);
            imagemEmpresa = itemView.findViewById(R.id.imagemStatusPedido);
            imagemEmpresaCompras = itemView.findViewById(R.id.imagemEmpresaCompras);
            preco = itemView.findViewById(R.id.textPrecoPedido);
            nomeEmpresaCompras = itemView.findViewById(R.id.textNomeClientePedido);
            linearLayout = itemView.findViewById(R.id.linearLayoutNomeCliente);
        }
    }



    //Método par recuperar empresa
    private void pesquisarUsuario(String id) {



        referenciaFirestore = ConfiguracaoFirebase.getReferenciaFirestore();
        DocumentReference empresaRef = referenciaFirestore
                .collection("usuarios")
                .document(id);

        empresaRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Usuario dadosUsuario = documentSnapshot.toObject(Usuario.class);
                    if (dadosUsuario != null){
                        if (dadosUsuario.getNome()!= null){
                           nomeRecuperadoUsuario = dadosUsuario.getNome();
                           tipoUsuario = dadosUsuario.getTipoUsuario();


                        }

                    }
                }
            }
        });


    }

    private String pesquisarEmpresa(String id) {

        String urlLogo = "";

        referenciaFirestore = ConfiguracaoFirebase.getReferenciaFirestore();
        DocumentReference empresaRef = referenciaFirestore
                .collection("empresas")
                .document(id);

        empresaRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Empresa dadosEmpresa = documentSnapshot.toObject(Empresa.class);
                    if (dadosEmpresa != null){
                        if (dadosEmpresa.getUrlImagem()!= null){
                            url = dadosEmpresa.getUrlImagem();
                            nomeRecuperadoEmpresa = dadosEmpresa.getNomeFantasia();

                        }

                    }
                }
            }
        });
        urlLogo = url;


        return urlLogo;

    }



}
