package com.edix.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    Button loginButton;
    TextView signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Escondemos la barra de arriba en la activity de login
        getSupportActionBar().hide();

        //inicializamos la variable del loginButton y lo ponemos a la escucha
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            //Cuando damos al boton pasamos a la activity main. Creamos un nuevo intent
            @Override
            public void onClick(View v) {
                //TODO: Login en FiraBase
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
            }
        });


        //Inicializamos la variable del signUpButton y la ponemos a la escucha
        signUpButton = findViewById(R.id.signUp);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Crear usuario en FiraBase
                Toast.makeText(Login.this, "User registrado", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}