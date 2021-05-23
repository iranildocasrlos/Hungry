package br.com.localoeste.hungry.adapter;
/**
 * Iranildo c. Silva
 * Local Oeste Software House
 */

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import br.com.localoeste.hungry.R;
import br.com.localoeste.hungry.model.Produto;
import de.hdodenhof.circleimageview.CircleImageView;


public class AdapterCarrinho extends RecyclerView.Adapter<AdapterCarrinho.MyViewHolder>{

    private List<Produto> produtos;
    private Context context;
    private DecimalFormat df = new DecimalFormat("0.00");

    //Criando a variável do listener
    private OnRecyclerViewClickListener listener;
    private int quantidadeEscolhida;
    private Double precoProduto;

    //Criando a iterface para implementar o listener de clique no Recycler
    public interface OnRecyclerViewClickListener{
        void OnItemClick(int position, int quantidadeEscolhida, Double precoProduto);

    }

  //  Criando o construtor da interface
    public void OnRecyclerViewClickListener(OnRecyclerViewClickListener listener){
        this.listener = listener;
        this.quantidadeEscolhida = quantidadeEscolhida;
        this.precoProduto = precoProduto;
    }


    public AdapterCarrinho(List<Produto> produtos, Context context) {
        this.produtos = produtos;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_carrinho, parent, false);
        return new MyViewHolder(itemLista, listener);
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
                holder.nome.setTextColor(Color.RED);
                holder.nome.setText("CLIQUE AQUI PARA ATUALIZAR TOTAL");
                Double resultado = produto.getPrecoProduto()*produto.getQuantidade();
                precoProduto = resultado;


            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (produto.getQuantidade() > 1){
                    produto.setQuantidade(produto.getQuantidade()-1);
                    String valor = holder.valor.getText().toString();
                    String valorLimpo = valor.substring(3,7).replaceAll(",",".");
                    Double totalProduto = Double.parseDouble(valorLimpo);
                    holder.valor.setText("R$ " + df.format(totalProduto - produto.getPrecoUnidade()));
                    holder.quantidade.setText("Quantidade: "+produto.getQuantidade());
                    holder.nome.setTextColor(Color.RED);
                    holder.nome.setText("CLIQUE AQUI PARA ATUALIZAR TOTAL");
                    precoProduto = Double.parseDouble(String.valueOf((totalProduto - produto.getPrecoProduto())));
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
        TextView nome, nomeBackup;
        TextView descricao;
        TextView valor;
        TextView quantidade;
        ImageView add;
        ImageView remove;
        String quant;


        public MyViewHolder(View itemView, OnRecyclerViewClickListener listener) {
            super(itemView);
            imagem = itemView.findViewById(R.id.imagemCompras);
            nome = itemView.findViewById(R.id.textNomeCompras);
            descricao = itemView.findViewById(R.id.textDescricaoCompras);
            valor = itemView.findViewById(R.id.textPrecoCompras);
            quantidade = itemView.findViewById(R.id.status_compras);
            df.setRoundingMode(RoundingMode.HALF_UP);
            add = itemView.findViewById(R.id.imageAdapterAdd);
            remove = itemView.findViewById(R.id.imageAdapterRemove);
            String valorUnitario = valor.getText().toString().substring(3,8);
            precoProduto = Double.parseDouble(valorUnitario);




            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String numeros = quantidade.getText().toString().substring(11,13).trim();
                    quantidadeEscolhida = Integer.parseInt(numeros);
                    if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION){
                        listener.OnItemClick(getAdapterPosition(), quantidadeEscolhida,precoProduto);
                        nome.setTextColor(Color.BLACK);
                       String nomeProd =  produtos.get(getAdapterPosition()).getNomeProduto();
                        nome.setText(nomeProd);

                    }
                }
            });

        }


    }

}


