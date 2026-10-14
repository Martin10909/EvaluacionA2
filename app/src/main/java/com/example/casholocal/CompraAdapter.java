package com.example.casholocal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.Locale;

public class CompraAdapter extends RecyclerView.Adapter<CompraAdapter.CompraViewHolder> {

    public interface OnCompraClickListener {
        void onAccionCompra(CompraModel compra);
    }

    private List<CompraModel> listaCompras;
    private OnCompraClickListener listener;

    public CompraAdapter(List<CompraModel> listaCompras, OnCompraClickListener listener) {
        this.listaCompras = listaCompras;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CompraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_purchase, parent, false);
        return new CompraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompraViewHolder holder, int position) {
        CompraModel compra = listaCompras.get(position);
        holder.tvtitulocompra.setText(compra.getTitulo());
        holder.tvcategoriacompra.setText(compra.getCategoria() != null ? compra.getCategoria() : "General");
        holder.tvmontocompra.setText(String.format(Locale.getDefault(), "$%.2f", compra.getMonto()));
        holder.tvestadocompra.setText(compra.isEsRealizada() ? "Realizada" : "Agendada");
        holder.tvubicacioncompra.setText("Fecha: " + (compra.getFecha() != null ? compra.getFecha() : "Programada"));

        holder.btnaccioncompra.setText(compra.isEsRealizada() ? "Detalles" : "Marcar Realizada");
        holder.btnaccioncompra.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAccionCompra(compra);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaCompras != null ? listaCompras.size() : 0;
    }

    public void actualizarLista(List<CompraModel> nuevaLista) {
        this.listaCompras = nuevaLista;
        notifyDataSetChanged();
    }

    static class CompraViewHolder extends RecyclerView.ViewHolder {
        TextView tvtitulocompra;
        TextView tvcategoriacompra;
        TextView tvmontocompra;
        TextView tvestadocompra;
        TextView tvubicacioncompra;
        MaterialButton btnaccioncompra;

        public CompraViewHolder(@NonNull View itemView) {
            super(itemView);
            tvtitulocompra = itemView.findViewById(R.id.tvtitulocompra);
            tvcategoriacompra = itemView.findViewById(R.id.tvcategoriacompra);
            tvmontocompra = itemView.findViewById(R.id.tvmontocompra);
            tvestadocompra = itemView.findViewById(R.id.tvestadocompra);
            tvubicacioncompra = itemView.findViewById(R.id.tvubicacioncompra);
            btnaccioncompra = itemView.findViewById(R.id.btnaccioncompra);
        }
    }
}