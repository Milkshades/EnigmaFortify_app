package com.araoz.enigmafortify;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.araoz.enigmafortify.Modelos.Password;
import com.araoz.enigmafortify.OpcionesPassword.Editar_Password;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PasswordAdapter extends RecyclerView.Adapter<PasswordAdapter.PasswordViewHolder> {

    private List<Password> passwordList;
    private Context context;

    public PasswordAdapter(List<Password> passwordList, Context context) {
        this.passwordList = passwordList;
        this.context = context;
    }

    @NonNull
    @Override
    public PasswordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_password, parent, false);
        return new PasswordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PasswordViewHolder holder, int position) {
        Password password = passwordList.get(position);
        holder.tvTitulo.setText(password.getTitulo());
        holder.tvUsuario.setText(password.getUsuario());

        holder.ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, holder.ivMenu);
                popup.inflate(R.menu.menu_password);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int adapterPosition = holder.getAdapterPosition();
                        if (adapterPosition == RecyclerView.NO_POSITION) return false;

                        Password password = passwordList.get(adapterPosition);

                        switch (item.getItemId()) {
                            case R.id.action_edit:
                                Intent intent = new Intent(context, Editar_Password.class);
                                intent.putExtra("passwordId", password.getId());
                                context.startActivity(intent);
                                return true;
                            case R.id.action_delete:
                                FirebaseFirestore.getInstance().collection("passwords").document(password.getId())
                                        .delete()
                                        .addOnSuccessListener(aVoid -> {
                                            passwordList.remove(adapterPosition);
                                            notifyItemRemoved(adapterPosition);
                                            Toast.makeText(context, "Contraseña eliminada", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> Toast.makeText(context, "Error al eliminar la contraseña", Toast.LENGTH_SHORT).show());
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return passwordList.size();
    }

    public static class PasswordViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitulo, tvUsuario;
        ImageView ivMenu;

        public PasswordViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvUsuario = itemView.findViewById(R.id.tvUsuario);
            ivMenu = itemView.findViewById(R.id.ivMenu);
        }
    }
}