package com.example.danielbc.dbquicktrade;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danielbc.dbquicktrade.Modelo.Producto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class VerPorCategoria extends AppCompatActivity {
    private TextView catNombre, catDescripcion, catCategoria, catPrecio;
    private Button btnMod, btnDel, btnCancel;
    private Spinner spvCategoria, spvProducto;

    private String idUsuario, keyUsuario;
    private FirebaseAuth mAuth;
    DatabaseReference database;
    private ArrayList<String> ListasSP = new ArrayList<String>();
    private ArrayList<Producto> ListasDatos = new ArrayList<Producto>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_por_categoria);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("Productos");
        idUsuario = getIntent().getExtras().getString("UID");

        catNombre = findViewById(R.id.vCatNombreEt);
        catDescripcion = findViewById(R.id.vCatDescripcionEt);
        catCategoria = findViewById(R.id.vCatCategoriaEt);
        catPrecio = findViewById(R.id.vCatPrecioEt);
        spvCategoria = findViewById(R.id.categoriaSPver);
        spvProducto = findViewById(R.id.productoSPver);

        btnCancel = findViewById(R.id.btnCancelar);


        //Ajustes Spinner
        String[] items = new String[]{"Tecnología", "Coches", "Hogar"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spvCategoria.setAdapter(adapter);
        spvCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ListasSP.clear();
                Query q = database.orderByChild("categoria").equalTo(spvCategoria.getSelectedItem().toString());

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
                            Toast.makeText(VerPorCategoria.this, "He encontrado: " + cont, +Toast.LENGTH_SHORT).show();
                            adaptador = new ArrayAdapter<String>(VerPorCategoria.this, android.R.layout.simple_list_item_1, ListasSP);
                            spvProducto.setAdapter(adaptador);

                            spvProducto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                    Iterator<Producto> nombreIterator = ListasDatos.iterator();
                                    while (nombreIterator.hasNext()) {
                                        Producto elemento = nombreIterator.next();

                                        if (spvProducto.getSelectedItem().toString().compareTo(elemento.getNombre()) == 0) {
                                            //String usrName = elemento.getUserName();

                                            catNombre.setText(elemento.getNombre());
                                            catDescripcion.setText(elemento.getDescripcion());
                                            catCategoria.setText(elemento.getCategoria());
                                            catPrecio.setText(elemento.getPrecio());
                                            keyUsuario = elemento.getMykey();

                                            Toast.makeText(VerPorCategoria.this, "La Key es " + keyUsuario, Toast.LENGTH_LONG).show();

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

                });


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


    }
}
