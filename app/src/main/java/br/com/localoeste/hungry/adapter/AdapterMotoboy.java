package br.com.localoeste.hungry.adapter;
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

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import br.com.localoeste.hungry.R;
import br.com.localoeste.hungry.model.Motoboy;
import de.hdodenhof.circleimageview.CircleImageView;


public class AdapterMotoboy extends RecyclerView.Adapter<AdapterMotoboy.MyViewHolder>{

    private List<Motoboy> motoboys;
    private Context context;
    private DecimalFormat df = new DecimalFormat("0.00");

    public AdapterMotoboy(List<Motoboy> motoboys, Context context) {
        this.motoboys = motoboys;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_motoboy, parent, false);
        return new MyViewHolder(itemLista);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder( MyViewHolder holder, int i) {
        Motoboy motoboy = motoboys.get(i);

        Picasso.get()
                .load(motoboy.getUrlImagemMotoboy())
                .into(holder.imagem);

        holder.nome.setText(motoboy.getNomeMotoboy());
        holder.descricao.setText(motoboy.getDescricaoMotoboy());
        holder.valor.setText("R$ " + df.format(motoboy.getPrecoMotoboy()));
    }

    @Override
    public int getItemCount() {
        return motoboys.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imagem;
        TextView nome;
        TextView descricao;
        TextView valor;

        public MyViewHolder(View itemView) {
            super(itemView);
            imagem = itemView.findViewById(R.id.imagemStatusPedido);
            nome = itemView.findViewById(R.id.textNomePedidoEmpresa);
            descricao = itemView.findViewById(R.id.textDescricaoPedido);
            valor = itemView.findViewById(R.id.textPrecoPedido);
            df.setRoundingMode(RoundingMode.HALF_UP);
        }
    }




}
