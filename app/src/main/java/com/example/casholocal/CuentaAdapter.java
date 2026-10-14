package com.example.casholocal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class CuentaAdapter extends RecyclerView.Adapter<CuentaAdapter.CuentaViewHolder> {

    private List<CuentaModel> listaCuentas;

    public CuentaAdapter(List<CuentaModel> listaCuentas) {
        this.listaCuentas = listaCuentas;
    }

    @NonNull
    @Override
    public CuentaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account_card, parent, false);
        return new CuentaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CuentaViewHolder holder, int position) {
        CuentaModel cuenta = listaCuentas.get(position);
        holder.tvnombrecuenta.setText(cuenta.getNombre());
        holder.tvtipocuenta.setText(cuenta.getTipo());
        holder.tvsaldoestadocuenta.setText(String.format(Locale.getDefault(), "$%.2f", cuenta.getSaldoInicial()));
    }

    @Override
    public int getItemCount() {
        return listaCuentas != null ? listaCuentas.size() : 0;
    }

    public void actualizarLista(List<CuentaModel> nuevaLista) {
        this.listaCuentas = nuevaLista;
        notifyDataSetChanged();
    }

    static class CuentaViewHolder extends RecyclerView.ViewHolder {
        TextView tvnombrecuenta;
        TextView tvtipocuenta;
        TextView tvsaldoestadocuenta;

        public CuentaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvnombrecuenta = itemView.findViewById(R.id.tvnombrecuenta);
            tvtipocuenta = itemView.findViewById(R.id.tvtipocuenta);
            tvsaldoestadocuenta = itemView.findViewById(R.id.tvsaldoestadocuenta);
        }
    }
}