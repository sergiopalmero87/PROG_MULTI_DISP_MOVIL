package com.edix.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash extends AppCompatActivity implements Animation.AnimationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();

        // Almacenamos en variables las vistas que hay en los splash(anim) mediante su ID
        TextView textSplash = findViewById(R.id.appNameSplash);
        // Objeto animation donde se carga la animacion
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash);
        // Arrancamos la animacion
        textSplash.startAnimation(animation);


        // Almacenamos en variables las vistas que hay en los splash(anim) mediante su ID
        ImageView imageSplash = findViewById(R.id.splashImageApp);
        // Objeto animation donde se carga la animacion
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.anim_image);
        // Arrancamos la animacion
        imageSplash.startAnimation(animation1);


        // Ponemos a la escucha a las animaciones.
        // This porque lo hemos implementado en esta misma clase
        animation.setAnimationListener(this);
        animation1.setAnimationListener(this);

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        // Cuanda acaben las animaciones, se crea un nuevo intent y vamos desde this hasta login
        Intent intent = new Intent(Splash.this, Login.class);
        startActivity(intent);

        // Finish() para destruir el splash y no poder volver a el desde la app.
        finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}