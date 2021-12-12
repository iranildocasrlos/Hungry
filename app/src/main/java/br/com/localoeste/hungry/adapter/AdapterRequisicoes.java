package br.com.localoeste.hungry.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

import br.com.localoeste.hungry.R;
import br.com.localoeste.hungry.activy.RequisicoesActivity;
import br.com.localoeste.hungry.model.Empresa;
import br.com.localoeste.hungry.model.Produto;
import br.com.localoeste.hungry.model.Requisicao;
import br.com.localoeste.hungry.model.Usuario;


public class AdapterRequisicoes extends RecyclerView.Adapter<AdapterRequisicoes.MyViewHolder> {

    private List<Requisicao> requisicoes;
    private Context context;



    public AdapterRequisicoes(List<Requisicao> requisicoes, Context context) {
        this.requisicoes = requisicoes;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_requisicoes, parent, false);
        return new MyViewHolder( itemLista ) ;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Requisicao requisicao = requisicoes.get( position );
        Usuario cliente = requisicao.getCliente();
        holder.nome.setText( cliente.getNome() );
        holder.distancia.setText( cliente.getEndereco());

    }

    @Override
    public int getItemCount() {
        return requisicoes.size();
    }

    public class MyViewHolder<textRequisicaoNome> extends RecyclerView.ViewHolder{

        TextView nome;
        TextView distancia;

        public MyViewHolder(View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.textRequisicaoNome);
            distancia = itemView.findViewById(R.id.textRequisicaoDistancia);




            nome.setText("usuario");
        }

    }

}
