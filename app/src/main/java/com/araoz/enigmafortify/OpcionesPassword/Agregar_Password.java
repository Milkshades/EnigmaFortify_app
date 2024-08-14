package com.araoz.enigmafortify.OpcionesPassword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.araoz.enigmafortify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;


public class Agregar_Password extends AppCompatActivity {

    EditText EtTitulo, EtCuenta, EtNombreUsuario, EtPassword, EtSitioWeb, EtNota;
    FirebaseFirestore db;

    private static final String TAG = "Agregar_Password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_password);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
        }

        InicializarVariables();
        // Inicializar Firebase Firestore
        db = FirebaseFirestore.getInstance();
    }

    private void InicializarVariables() {
        EtTitulo = findViewById(R.id.EtTitulo);
        EtCuenta = findViewById(R.id.EtCuenta);
        EtNombreUsuario = findViewById(R.id.EtNombreUsuario);
        EtPassword = findViewById(R.id.EtPassword);
        EtSitioWeb = findViewById(R.id.EtSitioWeb);
        EtNota = findViewById(R.id.EtNota);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_guardar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.Guardar_Password) {
            String titulo = EtTitulo.getText().toString().trim();
            String cuenta = EtCuenta.getText().toString().trim();
            String nombreUsuario = EtNombreUsuario.getText().toString().trim();
            String password = EtPassword.getText().toString().trim();
            String sitioWeb = EtSitioWeb.getText().toString().trim();
            String nota = EtNota.getText().toString().trim();

            if (!titulo.isEmpty() && !cuenta.isEmpty() && !nombreUsuario.isEmpty() && !password.isEmpty()) {
                Map<String, Object> passwordData = new HashMap<>();
                passwordData.put("titulo", titulo);
                passwordData.put("cuenta", cuenta);
                passwordData.put("nombreUsuario", nombreUsuario);
                passwordData.put("password", password);
                passwordData.put("sitioWeb", sitioWeb);
                passwordData.put("nota", nota);

                // Agregar el ID del usuario actual
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                passwordData.put("userId", userId);

                db.collection("passwords")
                        .add(passwordData)
                        .addOnSuccessListener(documentReference -> {
                            Log.d(TAG, "Password guardado con ID: " + documentReference.getId());
                            Toast.makeText(this, "Password guardado", Toast.LENGTH_SHORT).show();
                            Intent resultIntent = new Intent();
                            setResult(RESULT_OK, resultIntent); // Devolver resultado al fragmento
                            finish(); // Finalizar actividad
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Error al guardar password", e);
                            Toast.makeText(this, "Error al guardar password", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}