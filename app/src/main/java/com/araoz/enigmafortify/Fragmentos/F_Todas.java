package com.araoz.enigmafortify.Fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.araoz.enigmafortify.PasswordAdapter;
import com.araoz.enigmafortify.Modelos.Password;
import com.araoz.enigmafortify.OpcionesPassword.Agregar_Password;
import com.araoz.enigmafortify.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class F_Todas extends Fragment {

    private static final String TAG = "F_Todas";
    private static final int REQUEST_CODE_ADD_PASSWORD = 1;

    private RecyclerView recyclerViewPasswords;
    private PasswordAdapter passwordAdapter;
    private List<Password> passwordList;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_f__todas, container, false);

        FloatingActionButton FAB_AgregarPassword = view.findViewById(R.id.FAB_AgregarPassword);
        recyclerViewPasswords = view.findViewById(R.id.recyclerViewPasswords);
        recyclerViewPasswords.setLayoutManager(new LinearLayoutManager(getContext()));
        passwordList = new ArrayList<>();
        passwordAdapter = new PasswordAdapter(passwordList, getContext());
        recyclerViewPasswords.setAdapter(passwordAdapter);

        db = FirebaseFirestore.getInstance();

        FAB_AgregarPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), Agregar_Password.class), REQUEST_CODE_ADD_PASSWORD);
            }
        });

        cargarPasswords();

        return view;
    }

    private void cargarPasswords() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        CollectionReference passwordsRef = db.collection("passwords");

        passwordsRef.whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            passwordList.clear(); // Limpiar la lista antes de agregar nuevos datos
                            for (DocumentSnapshot document : task.getResult()) {
                                Password password = document.toObject(Password.class);
                                if (password != null) {
                                    password.setId(document.getId());  // Guardamos el ID del documento
                                    passwordList.add(password);
                                    Log.d(TAG, "Password cargado: " + password.getTitulo());
                                }
                            }
                            passwordAdapter.notifyDataSetChanged();
                        } else {
                            Log.e(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_PASSWORD) {
            if (resultCode == getActivity().RESULT_OK) {
                cargarPasswords(); // Recargar las contraseñas cuando se agrega una nueva
            }
        }
    }
}