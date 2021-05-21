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
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import br.com.localoeste.hungry.R;
import br.com.localoeste.hungry.model.Empresa;
import br.com.localoeste.hungry.model.Pedido;
import br.com.localoeste.hungry.model.Produto;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterCompras extends RecyclerView.Adapter<AdapterCompras.MyViewHolder> {

    private List<Empresa> pedidos;

    public AdapterCompras(List<Empresa> pedidos) {
        this.pedidos = pedidos;
    }

    @NonNull
    @Override
    public AdapterCompras.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_compras, parent, false);
        return new AdapterCompras.MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCompras.MyViewHolder holder, int i) {
//        Pedido pedido = pedidos.get(i);
//        holder.nomeEmpresa.setText(pedido.getNome());
//        holder.categoria.setText(empresa.getCategoria() + " - ");
//        holder.tempo.setText(empresa.getTempoEntrega() + " Min");
//        holder.entrega.setText("R$ " + empresa.getTaxaEntrega().toString());
//
//        //Carregar imagem
//        String urlImagem = empresa.getUrlImagem();
//        Picasso.get().load( urlImagem ).into( holder.imagemEmpresa );

    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imagemEmpresa;
        TextView nomeEmpresa;
        TextView categoria;
        TextView tempo;
        TextView entrega;

        public MyViewHolder(View itemView) {
            super(itemView);

            nomeEmpresa = itemView.findViewById(R.id.textNomeEmpresa);
            categoria = itemView.findViewById(R.id.textCategoriaEmpresa);
            tempo = itemView.findViewById(R.id.textTempoEmpresa);
            entrega = itemView.findViewById(R.id.textEntregaEmpresa);
            imagemEmpresa = itemView.findViewById(R.id.imageEmpresa);
        }
    }
}
