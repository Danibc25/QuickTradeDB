package com.example.danielbc.dbquicktrade;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private String idUsuario;
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

                                                     ListasSP.add(prNombre);
                                                     ListasDatos.add(new Producto(prNombre, prDescripcion, prCategoria, prPrecio, prVendedorId));

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

    }
}
