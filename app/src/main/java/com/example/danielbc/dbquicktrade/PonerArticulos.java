package com.example.danielbc.dbquicktrade;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.danielbc.dbquicktrade.Modelo.Producto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PonerArticulos extends AppCompatActivity {
    private EditText nomArticulo, desArticulo, precioArticulo;
    private Spinner categoriaArticulo;
    private String idUsuario;
    private Button Anadir, Cancelar;

    private FirebaseAuth mAuth;
    DatabaseReference databaseART;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poner_articulos);

        //Toast con el id de usuario
        idUsuario = getIntent().getExtras().getString("UID");
        Toast.makeText(this, "El id de ususario es: " + idUsuario, +Toast.LENGTH_SHORT).show();

        mAuth = FirebaseAuth.getInstance();
        databaseART = FirebaseDatabase.getInstance().getReference("Productos");

        nomArticulo = findViewById(R.id.nomArtEt);
        desArticulo = findViewById(R.id.descripArtEt);
        precioArticulo = findViewById(R.id.precioArtEt);
        categoriaArticulo = findViewById(R.id.categoriaSP);

        Anadir = findViewById(R.id.btnAñadir);
        Cancelar = findViewById(R.id.btnCancel);


        //Ajustes Spinner
        String[] items = new String[]{"Tecnología", "Coches", "Hogar"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        categoriaArticulo.setAdapter(adapter);


        Cancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();

            }
        });
        Anadir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    String Clave = databaseART.push().getKey();
                    Producto p1 = new Producto(nomArticulo.getText().toString(), desArticulo.getText().toString(), categoriaArticulo.getSelectedItem().toString(), precioArticulo.getText().toString(), idUsuario, Clave);
                    databaseART.child(Clave).setValue(p1);
                    Toast.makeText(PonerArticulos.this, "Exito!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(PonerArticulos.this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}







