package com.edix.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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

                if(email.isEmpty()){
                    emailText.setError("The email can not be empty");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailText.setError("Incorect email");
                } else if(password.length() < 6){
                    passText.setError("Minimum 6 characters");
                } else{
                    // Accedemos con email y pass a nuestra cuenta
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // User registrado correctamente
                                        Intent intent = new Intent(Login.this, MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(Login.this, "Something went wrong. Try it again", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
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

                if(email.isEmpty()){
                    emailText.setError("The email can not be empty");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailText.setError("Incorrect email");
                } else if(password.length() < 6){
                    passText.setError("Minimum 6 characters");
                }else {
                    // Crear usuario en FiraBase con email y pass
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                //Al completar la info del nuevo usuario y darle a crear usuario:
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // User registrado correctamente
                                        Toast.makeText(Login.this, "User registered", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Login.this, MainActivity.class);
                                        startActivity(intent);
                                    } else{
                                        Toast.makeText(Login.this, "Something went wrong. Try it again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }


            }
        });
    }
}