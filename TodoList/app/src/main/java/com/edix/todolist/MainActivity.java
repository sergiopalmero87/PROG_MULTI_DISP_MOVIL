package com.edix.todolist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String idUser;
    ListView listViewtareas;
    ArrayAdapter<String> adapterTareas;
    List<String> listaTareas = new ArrayList<>();
    List<String>listaIdTareas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializamos Firebase Auth que es la autenticacion
        mAuth = FirebaseAuth.getInstance();
        //Inicializamos FirabaseFirestore que es la bbdd
        db = FirebaseFirestore.getInstance();
        //Inicializamos usuario por su ID
        idUser = mAuth.getCurrentUser().getUid();
        //Inicializamos listView
        listViewtareas = findViewById(R.id.listTareas);

        actualizarUI();


        //Toast de bienvenida a la app.
        Toast.makeText(MainActivity.this, "Welcome 👋🏼", Toast.LENGTH_SHORT).show();
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
                    .setTitle("New task")
                    .setMessage("What do you want to do?")
                    .setView(newTask)
                    //Ponemos en escucha al boton de Añadir.
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Add tarea a bbdd y despues el toast
                            String miTarea = newTask.getText().toString();
                            Map<String, Object> data = new HashMap<>();
                            data.put("nombreTarea", miTarea);
                            data.put("usuario", idUser);

                            db.collection("Tareas")
                                    .add(data)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(MainActivity.this, "Task added", Toast.LENGTH_SHORT).show();
                                            return;                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(MainActivity.this, "Task creation failed", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                        }
                    })
                    //Ponemos en escucha al boton de Cancelar.
                    .setNegativeButton("Cancel", null)
                    .create();
            dialog.show();
            return true;

            //Si el boton pulsado es el de log out, salimos a la activity login
            //y mostramos toast de log out
        }else if(item.getItemId() == R.id.logOut){
            mAuth.signOut();
            Toast.makeText(this, "Bye!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }

    }

    //Metodo que nos sirve para actualizar la UI y escuchar cambios en tiempo real
    private void actualizarUI(){
        //Accedemos a la bbdd llama Tareas
        db.collection("Tareas")
                //Solo nos devuelve aquellos en los que el "usuario"(valor que aparece en firebase)
                //sea igual a la variable idUser
                .whereEqualTo("usuario", idUser)
                //Listener para los cambios en tiempo real
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            //Si hay un fallo salimos
                            return;
                        }

                        //Limpiamos las listas para que no haya documentos duplicados
                        listaTareas.clear();
                        listaIdTareas.clear();

                        //Iteramos sobre los documentos obtenidos en la consulta
                        for (QueryDocumentSnapshot doc : value) {
                            // Añadimos el nombre de la tarea a la lista listaTareas
                            listaTareas.add(doc.getString("nombreTarea"));
                            // Añadimos el ID de la tarea a la lista listaIdTareas
                            listaIdTareas.add(doc.getId());

                        }

                        //El listView se llena atraves del adapter
                        //Si no tengo datos en la lista el adapter es null
                        //Si tengo datos en la lista el adapter se llena
                        if(listaTareas.size() == 0){
                            listViewtareas.setAdapter(null);
                        }else{
                            //Creamos un array.
                            // 1º param esta clase, 2º param el layout de item tarea, 3º param el id del view y 4º param con que lo lleno
                            adapterTareas = new ArrayAdapter<>(MainActivity.this, R.layout.item_tarea, R.id.nombreTarea, listaTareas);
                            listViewtareas.setAdapter(adapterTareas);
                        }

                    }
                });
    }

    //Cuando hacemos click en el boton done de la tarea, borramos la tarea del UI y tambien de la BBDD
    //Si hacemos click en done, podemos obtener el padre y del padre, el hijo con findViewByID
    //Y la variable tarea es el contenido de la caja de texto.
    public void borrarTarea(View view){//Esta view es el boton de done
        View parent = (View) view.getParent();
        TextView tareaText = parent.findViewById(R.id.nombreTarea);
        String tarea = tareaText.getText().toString();

        //guardamos en posicion el index de la tarea
        int posicion = listaTareas.indexOf(tarea);

        //En la bbdd, entramos en la coleccion, entramos al documento, entramos al arrayIdTareas y obtenemos la posicion.
        //Tiene que coincidir la posicion de la tarea del arrayTareas con la posicion del id del arrayIdtarea.
        db.collection("Tareas").document(listaIdTareas.get(posicion)).delete();
        Toast.makeText(this, "¡Task done!",Toast.LENGTH_SHORT).show();
    }


    //Cuando hacemos click en el boton edit de la tarea, editamos la tarea del UI y tambien de la BBDD
    //Si hacemos click en edit, podemos obtener el padre y del padre, el hijo con findViewByID
    //Y la variable tarea es el contenido de la caja de texto.
    public void editarTarea(View view) {
        View parent = (View) view.getParent();
        TextView tareaText = parent.findViewById(R.id.nombreTarea);
        String tarea = tareaText.getText().toString();

        // Guardamos en posicion el índice de la tarea
        int posicion = listaTareas.indexOf(tarea);

        // Creamos un EditText para que el usuario ingrese el nuevo texto
        EditText newTask = new EditText(this);
        newTask.setText(tarea);  // Establecer el texto actual de la tarea

        //Configuramos el alert
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Edit task")
                .setMessage("What do you want to do?")
                .setView(newTask)
                // Ponemos en escucha al botón de Editar
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //nuevoTexto es lo que haya en el contenido de la caja de texto
                        String nuevoTexto = newTask.getText().toString();

                        // En la bbdd, entramos en la coleccion, entramos al documento, entramos al arrayIdTareas y obtenemos la posicion.
                        // Tiene que coincidir la posicion de la tarea del arrayTareas con la posicion del id del arrayIdtarea.
                        // Actualizamos y en nombreTarea ponemos el nuevoTexto
                        // Al dar click en el boton editar se actualiza la tarea en UI y BBDD
                        db.collection("Tareas").document(listaIdTareas.get(posicion))
                                .update("nombreTarea", nuevoTexto)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Actualizamos la lista
                                        listaTareas.set(posicion, nuevoTexto);
                                        Toast.makeText(MainActivity.this, "Task edited", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MainActivity.this, "Task editing failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                // Ponemos en escucha al botón de Cancelar
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }
}