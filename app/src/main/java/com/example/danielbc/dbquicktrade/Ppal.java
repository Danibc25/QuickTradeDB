package com.example.danielbc.dbquicktrade;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.danielbc.dbquicktrade.Modelo.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class Ppal extends AppCompatActivity {

    private EditText etNombre, etApellido, etDireccion;
    private Button Modificar, Vender, VerMisArt, VerCategorias;
    private String idUsuario;
    private FirebaseAuth mAuth;
    final static int Ppal = 3;

    DatabaseReference database;
    private ArrayList<Usuario> Listas = new ArrayList<Usuario>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppal);


        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance().getReference("Usuarios");

        idUsuario = getIntent().getExtras().getString("UID");


        etNombre = findViewById(R.id.pNombreEt);
        etApellido = findViewById(R.id.pApellidoEt);
        etDireccion = findViewById(R.id.pDieccionEt);
        Modificar = findViewById(R.id.btnModificar);
        Vender = findViewById(R.id.btnVender);
        VerMisArt = findViewById(R.id.btnModArticulo);
        VerCategorias = findViewById(R.id.btnCategoria);


        Modificar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                modificarUser();
            }
        });

        Vender.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PonerArticulos.class);

                i.putExtra("UID", idUsuario);
                startActivityForResult(i, Ppal);


            }
        });

        VerMisArt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), VerMisArticulos.class);

                i.putExtra("UID", idUsuario);
                startActivityForResult(i, Ppal);


            }
        });

        VerCategorias.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), VerPorCategoria.class);

                i.putExtra("UID", idUsuario);
                startActivityForResult(i, Ppal);


            }
        });

        Query q = database.orderByChild("userId").equalTo(idUsuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
                                             int cont = 0;

                                             @Override
                                             public void onDataChange(DataSnapshot dataSnapshot) {
                                                 for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                                                     Usuario usr = datasnapshot.getValue(Usuario.class);
                                                     String userName = usr.getUserName();
                                                     String nombre = usr.getNombre();
                                                     String apellido = usr.getApellido();
                                                     String direccion = usr.getDireccion();
                                                     String userId = usr.getUserId();
                                                     Listas.add(new Usuario(userName, nombre, apellido, direccion, userId));


                                                     cont++;
                                                     Toast.makeText(Ppal.this, "He encontrado: " + cont, +Toast.LENGTH_SHORT).show();

                                                     Iterator<Usuario> nombreIterator = Listas.iterator();
                                                     while (nombreIterator.hasNext()) {
                                                         Usuario elemento = nombreIterator.next();

                                                         // En el IF comprobamos de que el user logueado actualmente coincide con el que esté en el ArrayList, y si esta que almacene/sette el texto de los valores
                                                         if (elemento.getUserId().compareTo(idUsuario) == 0) {
                                                             //String usrName = elemento.getUserName();

                                                             etNombre.setText(elemento.getNombre());
                                                             etApellido.setText(elemento.getApellido());
                                                             etDireccion.setText(elemento.getDireccion());

                                                         }
                                                     }


                                                 }


                                             }

                                             @Override
                                             public void onCancelled(DatabaseError databaseError) {
                                             }

                                         }

        );

    }

    public void modificarUser() {

        // Optenemos los 3 valores de los editText
        final String sNombre = etNombre.getText().toString();
        final String sApellidos = etApellido.getText().toString();
        final String sDireccion = etDireccion.getText().toString();

        // Hacemos una comprobación sobre el si los campos no estan vacios
        if (!TextUtils.isEmpty(sNombre) || !TextUtils.isEmpty(sApellidos) || !TextUtils.isEmpty(sDireccion)) {
            // Hacemos una Búsqueda por nombre del Usuario
            Query q = database.orderByChild("userId").equalTo(idUsuario);
            q.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                        // clave obtiene el id de la fila del JSON
                        String clave = datasnapshot.getKey();
                        // En las 3 siguientes líneas lo que hacemos es Cambiar los valores del JSON
                        database.child(clave).child("nombre").setValue(sNombre);
                        database.child(clave).child("apellido").setValue(sApellidos);
                        database.child(clave).child("direccion").setValue(sDireccion);
                    }
                    Toast.makeText(Ppal.this, "Modificado", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

}

