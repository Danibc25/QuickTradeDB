package com.example.danielbc.dbquicktrade;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.danielbc.dbquicktrade.Modelo.Producto;
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

public class VerMisArticulos extends AppCompatActivity {
    private EditText modNombre, modDescripcion, modCategoria, modPrecio;
    private Button btnMod, btnDel, btnCancel;
    private Spinner spArticulos;

    private String idUsuario, keyUsuario;
    private FirebaseAuth mAuth;
    DatabaseReference database;
    private ArrayList<String> ListasSP = new ArrayList<String>();
    private ArrayList<Producto> ListasDatos = new ArrayList<Producto>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_mis_articulos);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("Productos");
        idUsuario = getIntent().getExtras().getString("UID");

        modNombre = findViewById(R.id.modNombreEt);
        modDescripcion = findViewById(R.id.modDescripcionEt);
        modCategoria = findViewById(R.id.modCategoriaEt);
        modPrecio = findViewById(R.id.modPrecioEt);
        spArticulos = findViewById(R.id.articulosSP);

        btnCancel = findViewById(R.id.btnCancelar);
        btnDel = findViewById(R.id.btnDelete);
        btnMod = findViewById(R.id.btnModificar);


        Query q = database.orderByChild("vendedorId").equalTo(idUsuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
                                             int cont = 0;

                                             @Override
                                             public void onDataChange(DataSnapshot dataSnapshot) {
                                                 ArrayAdapter<String> adaptador;
                                                 for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {

                                                     Producto pr1 = datasnapshot.getValue(Producto.class);
                                                     String prNombre = pr1.getNombre();
                                                     String prDescripcion = pr1.getDescripcion();
                                                     String prCategoria = pr1.getCategoria();
                                                     String prPrecio = pr1.getPrecio();
                                                     String prVendedorId = pr1.getNombre();

                                                     final String clave = datasnapshot.getKey();

                                                     ListasSP.add(prNombre);
                                                     ListasDatos.add(new Producto(prNombre, prDescripcion, prCategoria, prPrecio, prVendedorId, clave));

                                                     cont++;
                                                     Toast.makeText(VerMisArticulos.this, "He encontrado: " + cont, +Toast.LENGTH_SHORT).show();
                                                     adaptador = new ArrayAdapter<String>(VerMisArticulos.this, android.R.layout.simple_list_item_1, ListasSP);
                                                     spArticulos.setAdapter(adaptador);

                                                     spArticulos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                         @Override
                                                         public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                                             Iterator<Producto> nombreIterator = ListasDatos.iterator();
                                                             while (nombreIterator.hasNext()) {
                                                                 Producto elemento = nombreIterator.next();

                                                                 if (spArticulos.getSelectedItem().toString().compareTo(elemento.getNombre()) == 0) {
                                                                     //String usrName = elemento.getUserName();

                                                                     modNombre.setText(elemento.getNombre());
                                                                     modDescripcion.setText(elemento.getDescripcion());
                                                                     modCategoria.setText(elemento.getCategoria());
                                                                     modPrecio.setText(elemento.getPrecio());
                                                                     keyUsuario = elemento.getMykey();

                                                                     Toast.makeText(VerMisArticulos.this, "La Key es " + keyUsuario, Toast.LENGTH_LONG).show();

                                                                 }
                                                             }
                                                         }

                                                         @Override
                                                         public void onNothingSelected(AdapterView<?> parentView) {
                                                             // your code here
                                                         }

                                                     });


                                                 }


                                             }

                                             @Override
                                             public void onCancelled(DatabaseError databaseError) {
                                             }

                                         }

        );

        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();

            }
        });

        btnDel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deleteArt();

            }
        });

        btnMod.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                modificarArt();

            }
        });


    }

    public void deleteArt() {
        // Hacemos una Búsqueda por nombre del Usuario
        Query q = database.orderByChild("mykey").equalTo(keyUsuario);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                    // clave obtiene el id de la fila del JSON
                    String clave = datasnapshot.getKey();
                    database.child(clave).removeValue();
                }
                Toast.makeText(VerMisArticulos.this, "Eliminado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void modificarArt() {


        // Optenemos los 3 valores de los editText
        final String mNombre = modNombre.getText().toString();
        final String mDescripcion = modDescripcion.getText().toString();
        final String mPrecio = modPrecio.getText().toString();
        final String mCategoria = modCategoria.getText().toString();

        // Hacemos una comprobación sobre el si los campos no estan vacios
        if (!TextUtils.isEmpty(mNombre) || !TextUtils.isEmpty(mPrecio) || !TextUtils.isEmpty(mCategoria)) {
            // Hacemos una Búsqueda por nombre del Usuario
            Query q = database.orderByChild("mykey").equalTo(keyUsuario);
            q.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                        // clave obtiene el id de la fila del JSON
                        String clave = datasnapshot.getKey();
                        // En las 3 siguientes líneas lo que hacemos es Cambiar los valores del JSON
                        database.child(clave).child("categoria").setValue(mCategoria);
                        database.child(clave).child("descripcion").setValue(mDescripcion);
                        database.child(clave).child("nombre").setValue(mNombre);
                        database.child(clave).child("precio").setValue(mPrecio);
                    }
                    Toast.makeText(VerMisArticulos.this, "Modificado", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }

}
