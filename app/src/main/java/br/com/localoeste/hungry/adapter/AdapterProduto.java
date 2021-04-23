package br.com.localoeste.hungry.adapter;
/**
 * Iranildo c. Silva
 * Local Oeste Software House
 */

import android.content.Context;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;

import br.com.localoeste.hungry.R;
import br.com.localoeste.hungry.model.Produto;
import de.hdodenhof.circleimageview.CircleImageView;

import static io.grpc.okhttp.internal.Platform.logger;


public class AdapterProduto extends RecyclerView.Adapter<AdapterProduto.MyViewHolder>{

    private List<Produto> produtos;
    private Context context;
    private DecimalFormat df = new DecimalFormat("0.00");

    public AdapterProduto(List<Produto> produtos, Context context) {
        this.produtos = produtos;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_produto, parent, false);
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

        public MyViewHolder(View itemView) {
            super(itemView);
            imagem = itemView.findViewById(R.id.imagemProdutoAdapter);
            nome = itemView.findViewById(R.id.textNomeRefeicao);
            descricao = itemView.findViewById(R.id.textDescricaoRefeicao);
            valor = itemView.findViewById(R.id.textPreco);
            df.setRoundingMode(RoundingMode.HALF_UP);
        }
    }




}
