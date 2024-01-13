package com.edix.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    Button loginButton;
    TextView signUpButton;

    private FirebaseAuth mAuth;

    EditText emailText, passText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Escondemos la barra de arriba en la activity de login
        getSupportActionBar().hide();

        // Inicializamos Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Inicializamos emailText y passText con el contenido de la caja.
        emailText = findViewById(R.id.textoEmail);
        passText = findViewById(R.id.textoPassword);

        //inicializamos la variable del loginButton y lo ponemos a la escucha
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Login en FiraBase
                // Almacenamos en estas variables y pasamos a string las varibales emailText y passText
                String email = emailText.getText().toString();
                String password = passText.getText().toString();

                // Accedemos con email y pass a nuestra cuenta
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });


        //Inicializamos la variable del signUpButton y la ponemos a la escucha
        signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Almacenamos en estas variables y pasamos a string las varibales emailText y passText
                String email = emailText.getText().toString();
                String password = passText.getText().toString();

                // Crear usuario en FiraBase con email y pass
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            //Al completar la info del nuevo usuario y darle a crear usuario:
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // User registrado correctamente
                                    Toast.makeText(Login.this, "User registrado", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    // No se puede registrar el usuario.
                                    Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });
    }
}