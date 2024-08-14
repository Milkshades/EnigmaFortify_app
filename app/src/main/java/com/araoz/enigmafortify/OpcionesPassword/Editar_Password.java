package com.araoz.enigmafortify.OpcionesPassword;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.araoz.enigmafortify.Modelos.Password;
import com.araoz.enigmafortify.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Editar_Password extends AppCompatActivity {

    private EditText etTitulo, etUsuario, etPassword, etSitioWeb, etNota;
    private Button btnGuardar;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String passwordId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_password);

        etTitulo = findViewById(R.id.etTitulo);
        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
        etSitioWeb = findViewById(R.id.etSitioWeb);
        etNota = findViewById(R.id.etNota);
        btnGuardar = findViewById(R.id.btnGuardar);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        passwordId = getIntent().getStringExtra("passwordId");

        if (passwordId != null) {
            cargarDatos();
        } else {
            Toast.makeText(this, "ID de contraseña no válido", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarPassword();
            }
        });
    }

    private void cargarDatos() {
        if (auth.getCurrentUser() != null) {
            String userId = auth.getCurrentUser().getUid();
            db.collection("passwords").document(passwordId)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Password password = document.toObject(Password.class);
                                    if (password != null) {
                                        etTitulo.setText(password.getTitulo());
                                        etUsuario.setText(password.getUsuario());
                                        etPassword.setText(password.getPassword());
                                        etSitioWeb.setText(password.getSitioWeb());
                                        etNota.setText(password.getNota());
                                    }
                                } else {
                                    Toast.makeText(Editar_Password.this, "No se encontró la contraseña", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Editar_Password.this, "Error al cargar datos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void actualizarPassword() {
        if (auth.getCurrentUser() != null) {
            String userId = auth.getCurrentUser().getUid();
            Password password = new Password(
                    passwordId,  // Aquí usamos el ID
                    etTitulo.getText().toString(),
                    etUsuario.getText().toString(),
                    etPassword.getText().toString(),
                    etSitioWeb.getText().toString(),
                    etNota.getText().toString(),
                    userId
            );
            db.collection("passwords").document(passwordId)
                    .set(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Editar_Password.this, "Contraseña actualizada", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK); // Asegúrate de que el resultado sea el correcto
                                finish();
                            } else {
                                Toast.makeText(Editar_Password.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

}