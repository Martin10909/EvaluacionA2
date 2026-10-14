package com.example.casholocal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactoAdapter extends RecyclerView.Adapter<ContactoAdapter.ContactoViewHolder> {

    private List<ContactoModel> listaContactos;

    public ContactoAdapter(List<ContactoModel> listaContactos) {
        this.listaContactos = listaContactos;
    }

    @NonNull
    @Override
    public ContactoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactoViewHolder holder, int position) {
        ContactoModel contacto = listaContactos.get(position);
        
        String nombre = contacto.getNombre();
        String inicial = (nombre != null && !nombre.trim().isEmpty()) ? String.valueOf(nombre.trim().charAt(0)).toUpperCase() : "C";
        holder.tvinicialcontacto.setText(inicial);
        holder.tvnombrecontacto.setText(nombre);
        holder.tvdetallescontacto.setText(contacto.getTelefono() != null ? contacto.getTelefono() : "Sin teléfono");
        holder.tvestadodeudacontacto.setText(contacto.getNotas() != null && !contacto.getNotas().isEmpty() ? contacto.getNotas() : "Contacto Frecuente");
    }

    @Override
    public int getItemCount() {
        return listaContactos != null ? listaContactos.size() : 0;
    }

    public void actualizarLista(List<ContactoModel> nuevaLista) {
        this.listaContactos = nuevaLista;
        notifyDataSetChanged();
    }

    static class ContactoViewHolder extends RecyclerView.ViewHolder {
        TextView tvinicialcontacto;
        TextView tvnombrecontacto;
        TextView tvdetallescontacto;
        TextView tvestadodeudacontacto;
        ImageButton btnaccioncontacto;

        public ContactoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvinicialcontacto = itemView.findViewById(R.id.tvinicialcontacto);
            tvnombrecontacto = itemView.findViewById(R.id.tvnombrecontacto);
            tvdetallescontacto = itemView.findViewById(R.id.tvdetallescontacto);
            tvestadodeudacontacto = itemView.findViewById(R.id.tvestadodeudacontacto);
            btnaccioncontacto = itemView.findViewById(R.id.btnaccioncontacto);
        }
    }
}