package com.edix.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Mostramos el menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Ponemos en escucha a los botones
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Si el boton pulsado es el de +, se muestra dialog para añadir tarea.
        if(item.getItemId() == R.id.addTask){
            EditText newTask = new EditText(this);
            //Configuramos el alert
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Nueva tarea")
                    .setMessage("¿Qué quieres hacer?")
                    .setView(newTask)
                    //Ponemos en escucha al boton de Añadir.
                    .setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Add tarea a bbdd y despues el toast
                            Toast.makeText(MainActivity.this, "Tarea añadida", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //Ponemos en escucha al boton de Cancelar.
                    .setNegativeButton("Cancelar", null)
                    .create();
            dialog.show();
            return true;

            //Si el boton pulsado es el de log out, salimos a la activity login
            //y mostramos toast de log out
        }else if(item.getItemId() == R.id.logOut){
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            Toast.makeText(this, "Log out", Toast.LENGTH_SHORT).show();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }

    }
}