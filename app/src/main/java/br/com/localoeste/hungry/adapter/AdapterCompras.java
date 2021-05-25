package br.com.localoeste.hungry.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import br.com.localoeste.hungry.R;
import br.com.localoeste.hungry.helper.ConfiguracaoFirebase;
import br.com.localoeste.hungry.model.Empresa;
import br.com.localoeste.hungry.model.ItemPedido;
import br.com.localoeste.hungry.model.Pedido;
import br.com.localoeste.hungry.model.Produto;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterCompras extends RecyclerView.Adapter<AdapterCompras.MyViewHolder> {

    private List<Pedido> pedidos;
    private Context context;
    private FirebaseFirestore referenciaFirestore;
    private  String  url,nomeRecuperadoEmpresa;

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

        String idEmpresa = pedido.getIdEmpresa();



        //Carregar imagens
        if (pedido.getStatus().equals(Pedido.STATUS_AGUARDANDO)){
            Picasso.get().load( R.drawable.aguardando ).into( holder.imagemEmpresa );
        }else if (pedido.getStatus().equals(Pedido.STATUS_PREPARANDO)){
            Picasso.get().load( R.drawable.cooking ).into( holder.imagemEmpresa );
        }else if (pedido.getStatus().equals(Pedido.STATUS_A_CAMINHO)){
            Picasso.get().load( R.drawable.delivery ).into( holder.imagemEmpresa );
        }else if (pedido.getStatus().equals(Pedido.STATUS_CHEGOU)){
            Picasso.get().load( R.drawable.chegou ).into( holder.imagemEmpresa );
        }else if (pedido.getStatus().equals(Pedido.STATUS_RECEBIDO)){
            Picasso.get().load( R.drawable.entregue ).into( holder.imagemEmpresa );
        }



        String nomeProdutos = "";

        for (int x = 0 ; x < pedido.getItens().size(); x++) {
           ItemPedido item = pedido.getItens().get(x);
            nomeProdutos += "\n"+item.getNomeProduto();
        }



        holder.nomeEmpresaCompras.setText(pedido.getNomeEmpresa());
        holder.nomeEmpresa.setText("Pedido: "+nomeProdutos);
        holder.quantidade.setText("Total de itens: "+String.valueOf(pedido.getItens().size()));
        holder.descricao.setText("Obserrvações: "+pedido.getObservacaoEmpresa());
        holder.preco.setText("R$ " + pedido.getTotal());
        holder.status.setText("Status : "+pedido.getStatus().toUpperCase());

        if (pedido.getUrlLogo() != null){
            Picasso.get().load( pedido.getUrlLogo()).into( holder.imagemEmpresaCompras );
        }




    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imagemEmpresa;
        TextView nomeEmpresa;
        ImageView imagemEmpresaCompras;
        TextView nomeEmpresaCompras;
        TextView quantidade;
        TextView descricao;
        TextView status;
        TextView preco;

        public MyViewHolder(View itemView) {
            super(itemView);

            nomeEmpresa = itemView.findViewById(R.id.textNomeCompras);
            descricao = itemView.findViewById(R.id.textDescricaoCompras);
            quantidade = itemView.findViewById(R.id.quantidade_Compras);
            status = itemView.findViewById(R.id.status_compras);
            imagemEmpresa = itemView.findViewById(R.id.imagemCompras);
            preco = itemView.findViewById(R.id.textPrecoCompras);
            imagemEmpresaCompras = itemView.findViewById(R.id.imagemEmpresaCompras);
            nomeEmpresaCompras = itemView.findViewById(R.id.textNomeEmpresaCompras);
        }
    }



    //Método par recuperar empresa
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
