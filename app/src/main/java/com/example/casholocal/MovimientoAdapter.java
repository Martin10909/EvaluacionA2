package com.example.casholocal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class MovimientoAdapter extends RecyclerView.Adapter<MovimientoAdapter.MovimientoViewHolder> {

    private List<MovimientoModel> listaMovimientos;

    public MovimientoAdapter(List<MovimientoModel> listaMovimientos) {
        this.listaMovimientos = listaMovimientos;
    }

    @NonNull
    @Override
    public MovimientoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new MovimientoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovimientoViewHolder holder, int position) {
        MovimientoModel mov = listaMovimientos.get(position);
        holder.tvcategoriatransaccion.setText(mov.getCategoria() != null ? mov.getCategoria() : "Movimiento");
        holder.tvnotatransaccion.setText(mov.getDescripcion());
        
        boolean esIngreso = "ingreso".equalsIgnoreCase(mov.getTipo());
        String signo = esIngreso ? "+" : "-";
        holder.tvmontotransaccion.setText(signo + FormatoMoneda.aPesoChileno(mov.getMonto()));
        holder.tvmontotransaccion.setTextColor(esIngreso ? 0xFF2E7D32 : 0xFFC62828);

        holder.tvcuentatransaccion.setText(mov.getFecha() != null ? mov.getFecha() : "Hoy");
        if (mov.getLatitud() != null && mov.getLongitud() != null) {
            holder.tvubicaciontransaccion.setText(String.format(Locale.getDefault(), "GPS: %.4f, %.4f", mov.getLatitud(), mov.getLongitud()));
        } else {
            holder.tvubicaciontransaccion.setText("Sin GPS");
        }

        holder.tvetiquetatipotransaccion.setText(esIngreso ? "Ingreso" : "Gasto");
        holder.tvetiquetatipotransaccion.setTextColor(esIngreso ? 0xFF2E7D32 : 0xFFC62828);
    }

    @Override
    public int getItemCount() {
        return listaMovimientos != null ? listaMovimientos.size() : 0;
    }

    public void actualizarLista(List<MovimientoModel> nuevaLista) {
        this.listaMovimientos = nuevaLista;
        notifyDataSetChanged();
    }

    static class MovimientoViewHolder extends RecyclerView.ViewHolder {
        TextView tvcategoriatransaccion;
        TextView tvnotatransaccion;
        TextView tvmontotransaccion;
        TextView tvcuentatransaccion;
        TextView tvubicaciontransaccion;
        TextView tvetiquetatipotransaccion;

        public MovimientoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvcategoriatransaccion = itemView.findViewById(R.id.tvcategoriatransaccion);
            tvnotatransaccion = itemView.findViewById(R.id.tvnotatransaccion);
            tvmontotransaccion = itemView.findViewById(R.id.tvmontotransaccion);
            tvcuentatransaccion = itemView.findViewById(R.id.tvcuentatransaccion);
            tvubicaciontransaccion = itemView.findViewById(R.id.tvubicaciontransaccion);
            tvetiquetatipotransaccion = itemView.findViewById(R.id.tvetiquetatipotransaccion);
        }
    }
}