package com.example.danielbc.dbquicktrade;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.danielbc.dbquicktrade.Modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;




public class Registro extends AppCompatActivity implements View.OnClickListener {

    private EditText Nombre, UserName, Apellido, Direccion, Correo, Password, PasswordCop;
    private boolean existe = false;

    //FireBase
    private FirebaseAuth mAuth;
    DatabaseReference database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        UserName = findViewById(R.id.UsrNamEt);
        Nombre = findViewById(R.id.NomEt);
        Apellido = findViewById(R.id.ApellEt);
        Direccion = findViewById(R.id.DirecEt);
        Correo = findViewById(R.id.UsuEt);
        Password = findViewById(R.id.PassEt);
        PasswordCop = findViewById(R.id.Pass2Et);

        Button CancelBtn = findViewById(R.id.CancelBtn);
        Button SendBtn = findViewById(R.id.SendBtn);

        CancelBtn.setOnClickListener(this);
        SendBtn.setOnClickListener(this);

        //FireBase
        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance().getReference("Usuarios");
        //FireBaseFin

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.CancelBtn:
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                break;

            case R.id.SendBtn:

                if (ComprobarEntrada() == true) {
                    usuarioExiste();
                }
                break;
        }

    }

    public boolean ComprobarEntrada() {

        String Nom = Nombre.getText().toString();
        String UsNam = UserName.getText().toString();
        String Apell = Apellido.getText().toString();
        String Direc = Direccion.getText().toString();
        String Usu = Correo.getText().toString();
        String Ps1 = Password.getText().toString();
        String Ps2 = PasswordCop.getText().toString();

        if (Nom.compareTo("") != 0
                && UsNam.compareTo("") != 0
                && Apell.compareTo("") != 0
                && Direc.compareTo("") != 0
                && Usu.compareTo("") != 0
                && Ps1.compareTo("") != 0
                && Ps2.compareTo("") != 0) {

            if (Ps1.compareTo(Ps2) == 0) {

                Toast.makeText(this, "Todo de bien", Toast.LENGTH_SHORT).show();
                return true;

            } else {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return false;

            }

        } else {
            Toast.makeText(this, "Tienen que estar todos los campos completados", Toast.LENGTH_SHORT).show();
            return false;

        }

    }


    //FireBase
    public void registrar(String Usu, String Ps1) {
        mAuth.createUserWithEmailAndPassword(Usu, Ps1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            introducirDatos(mAuth.getCurrentUser());
                            Toast.makeText(Registro.this, "Exito.", Toast.LENGTH_SHORT).show();
                            mAuth.getInstance().signOut();
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(Registro.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            Toast.makeText(Registro.this, "Fuck off", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_FIRST_USER);
                            finish();
                        }

                    }
                });

    }
    //FireBaseFin

    private void introducirDatos(FirebaseUser user) {

        Usuario usuario = new Usuario(UserName.getText().toString(), Nombre.getText().toString(), Apellido.getText().toString(), Direccion.getText().toString(),user.getUid());
        database.child(user.getUid()).setValue(usuario);
    }

   public void usuarioExiste() {


        String txt = UserName.getText().toString();

        Query q = database.orderByChild("userName").equalTo(txt);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            int cont=0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                    cont++;
                    Toast.makeText(Registro.this, "He encontrado: "+cont,+ Toast.LENGTH_SHORT).show();

                }
                if(cont==0){

                        registrar(Correo.getText().toString(), Password.getText().toString());

                }else{
                    Toast.makeText(Registro.this, "El usuario ya existe", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        }

        );

    }

}
