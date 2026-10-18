package com.example.casholocal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.Locale;

public class SharedExpenseAdapter extends RecyclerView.Adapter<SharedExpenseAdapter.DeudaViewHolder> {

    public interface OnDeudaActionListener {
        void onMarcarPagada(DeudaModel deuda);
        void onEliminarDeuda(DeudaModel deuda);
    }

    private List<DeudaModel> listaDeudas;
    private OnDeudaActionListener listener;

    public SharedExpenseAdapter(List<DeudaModel> listaDeudas, OnDeudaActionListener listener) {
        this.listaDeudas = listaDeudas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DeudaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shared_expense, parent, false);
        return new DeudaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeudaViewHolder holder, int position) {
        DeudaModel deuda = listaDeudas.get(position);
        holder.tvitemtitulo.setText(deuda.getConcepto());
        holder.tvitemmetodo.setText("Participante: " + deuda.getContactoNombre());
        holder.tvitemmonto.setText(String.format(Locale.getDefault(), "$%.2f", deuda.getMonto()));
        holder.tvitemnotas.setText("Estado: " + (deuda.isEsPagada() ? "Saldada" : "Pendiente de cobro/pago"));

        if (deuda.isEsPagada()) {
            holder.btncompartircuenta.setText("Saldada");
            holder.btncompartircuenta.setEnabled(false);
        } else {
            holder.btncompartircuenta.setText("Saldar Cuenta");
            holder.btncompartircuenta.setEnabled(true);
            holder.btncompartircuenta.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMarcarPagada(deuda);
                }
            });
        }

        // Al hacer clic en la tarjeta, ver el detalle de los participantes y opción de eliminar
        holder.itemView.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
            builder.setTitle("Detalle de Gasto Compartido");
            builder.setMessage("Concepto: " + deuda.getConcepto() + "\n" +
                    "Participante: " + deuda.getContactoNombre() + "\n" +
                    "Monto Cuota: " + String.format(Locale.getDefault(), "$%.2f", deuda.getMonto()) + "\n" +
                    "Tipo: " + ("me_deben".equals(deuda.getTipoDeuda()) ? "Por Cobrar" : "Por Pagar") + "\n" +
                    "Estado: " + (deuda.isEsPagada() ? "Saldada" : "Pendiente"));

            builder.setPositiveButton("Aceptar", null);
            builder.setNegativeButton("Eliminar", (dialog, which) -> {
                if (listener != null) {
                    listener.onEliminarDeuda(deuda);
                }
            });
            builder.show();
        });
    }

    @Override
    public int getItemCount() {
        return listaDeudas != null ? listaDeudas.size() : 0;
    }

    public void actualizarLista(List<DeudaModel> nuevaLista) {
        this.listaDeudas = nuevaLista;
        notifyDataSetChanged();
    }

    static class DeudaViewHolder extends RecyclerView.ViewHolder {
        TextView tvitemtitulo;
        TextView tvitemmetodo;
        TextView tvitemmonto;
        TextView tvitemnotas;
        MaterialButton btncompartircuenta;

        public DeudaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvitemtitulo = itemView.findViewById(R.id.tvitemtitulo);
            tvitemmetodo = itemView.findViewById(R.id.tvitemmetodo);
            tvitemmonto = itemView.findViewById(R.id.tvitemmonto);
            tvitemnotas = itemView.findViewById(R.id.tvitemnotas);
            btncompartircuenta = itemView.findViewById(R.id.btncompartircuenta);
        }
    }
}