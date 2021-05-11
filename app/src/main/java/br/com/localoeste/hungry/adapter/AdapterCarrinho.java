package br.com.localoeste.hungry.adapter;
/**
 * Iranildo c. Silva
 * Local Oeste Software House
 */

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import br.com.localoeste.hungry.R;
import br.com.localoeste.hungry.activy.CardapioActivity;
import br.com.localoeste.hungry.activy.CarrinhoActivity;
import br.com.localoeste.hungry.model.Produto;
import de.hdodenhof.circleimageview.CircleImageView;


public class AdapterCarrinho extends RecyclerView.Adapter<AdapterCarrinho.MyViewHolder>{

    private List<Produto> produtos;
    private Context context;
    private DecimalFormat df = new DecimalFormat("0.00");

    public AdapterCarrinho(List<Produto> produtos, Context context) {
        this.produtos = produtos;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_carrinho, parent, false);
        return new MyViewHolder(itemLista);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder( MyViewHolder holder, int i) {
        Produto produto = produtos.get(i);

        Picasso.get()
                .load(produto.getUrlImagemProduto())
                .into(holder.imagem);

        holder.nome.setText(produto.getNomeProduto());
        holder.descricao.setText(produto.getDescricaoProduto());
        holder.valor.setText("R$ " + df.format(produto.getPrecoProduto()));
        holder.quantidade.setText("Quantidade: "+produto.getQuantidade());


        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                produto.setQuantidade(produto.getQuantidade()+1);
                holder.valor.setText("R$ " + df.format(produto.getPrecoProduto()*produto.getQuantidade()));
                holder.quantidade.setText("Quantidade: "+produto.getQuantidade());
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (produto.getQuantidade() > 1){
                    produto.setQuantidade(produto.getQuantidade()-1);
                    String valor = holder.valor.getText().toString();
                    String valorLimpo = valor.substring(3,7);
                    Double totalProduto = Double.parseDouble(valorLimpo);
                    holder.valor.setText("R$ " + df.format(totalProduto - produto.getPrecoProduto()));
                    holder.quantidade.setText("Quantidade: "+produto.getQuantidade());
                }

            }
        });



    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imagem;
        TextView nome;
        TextView descricao;
        TextView valor;
        TextView quantidade;
        ImageView add;
        ImageView remove;


        public MyViewHolder(View itemView) {
            super(itemView);
            imagem = itemView.findViewById(R.id.imagemProdutoAdapter);
            nome = itemView.findViewById(R.id.textNomeRefeicao);
            descricao = itemView.findViewById(R.id.textDescricaoRefeicao);
            valor = itemView.findViewById(R.id.textPreco);
            quantidade = itemView.findViewById(R.id.quantidade_Carrinho_Adapter);
            df.setRoundingMode(RoundingMode.HALF_UP);
            add = itemView.findViewById(R.id.imageAdapterAdd);
            remove = itemView.findViewById(R.id.imageAdapterRemove);

        }
    }




}
