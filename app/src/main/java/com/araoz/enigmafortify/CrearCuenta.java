package com.araoz.enigmafortify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class CrearCuenta extends AppCompatActivity {

    Button btn_Registrar;
    EditText usuario, correo, contraseña;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;

    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        usuario = findViewById(R.id.usuario);
        correo = findViewById(R.id.correo);
        contraseña = findViewById(R.id.contraseña);
        btn_Registrar = findViewById(R.id.btn_Registrar);

        mDialog = new SpotsDialog.Builder().setContext(CrearCuenta.this).setMessage("Espere un momento").build();

        btn_Registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameUser = usuario.getText().toString().trim();
                String emailUser = correo.getText().toString().trim();
                String passUser = contraseña.getText().toString().trim();

                if (nameUser.isEmpty() || emailUser.isEmpty() || passUser.isEmpty()){
                    Toast.makeText(CrearCuenta.this, "Complete los datos", Toast.LENGTH_SHORT).show();
                } else {
                    mDialog.show();
                    RegistrarUsuario(nameUser, emailUser, passUser);
                }
            }
        });

        // Configurar el TextView para registro
        TextView registerTextView = findViewById(R.id.registerTextView2);
        String text = "¿Ya tiene una cuenta? Inicie sesión";
        SpannableString spannableString = new SpannableString(text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(CrearCuenta.this, Login.class);
                startActivity(intent);
            }
        };

        // Color de la palabra "Inicie sesión"
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.GrisClaro));

        // Índice de la palabra "Inicie sesión"
        int startIndex = text.indexOf("Inicie sesión");
        int endIndex = startIndex + "Inicie sesión".length();

        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(colorSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        registerTextView.setText(spannableString);
        registerTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void RegistrarUsuario(String nameUser, String emailUser, String passUser) {
        mAuth.createUserWithEmailAndPassword(emailUser, passUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mDialog.dismiss(); // Se oculta el diálogo al completar la tarea
                if (task.isSuccessful()) {
                    String id = mAuth.getCurrentUser().getUid();
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", id);
                    map.put("name", nameUser);
                    map.put("password", passUser);
                    map.put("email", emailUser);

                    mFirestore.collection("user").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(CrearCuenta.this, "Usuario Registrado con éxito", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(CrearCuenta.this, MainActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CrearCuenta.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                            Log.e("CrearCuenta", "Error al guardar en Firestore", e);
                        }
                    });
                } else {
                    Toast.makeText(CrearCuenta.this, "Error al registrar", Toast.LENGTH_SHORT).show();
                    Log.e("CrearCuenta", "Error al registrar", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mDialog.dismiss(); // Se oculta el diálogo al fallar la tarea
                Toast.makeText(CrearCuenta.this, "Error al registrar", Toast.LENGTH_SHORT).show();
                Log.e("CrearCuenta", "Error al registrar", e);
            }
        });
    }
}
