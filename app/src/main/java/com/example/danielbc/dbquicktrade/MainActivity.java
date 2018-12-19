package com.example.danielbc.dbquicktrade;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText Usuario, Password;
    private FirebaseAuth mAuth;
    final static int Registro = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button LogBtn = findViewById(R.id.LogBtn);
        Button RegBtn = findViewById(R.id.RegBtn);

        Usuario = findViewById(R.id.UsuEt);
        Password = findViewById(R.id.PassEt);


        LogBtn.setOnClickListener(this);
        RegBtn.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.RegBtn:
                Intent i = new Intent(getApplicationContext(), Registro.class);
                startActivityForResult(i, Registro);
                break;
            case R.id.LogBtn:
                String Correo = Usuario.getText().toString();
                String Pass = Password.getText().toString();
                login(Correo, Pass);
                break;
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent i){

    }


    public void login(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Intent i = new Intent(getApplicationContext(), Ppal.class);
                            FirebaseUser user = mAuth.getCurrentUser();
                            i.putExtra("UID", user.getUid());
                            //Toast.makeText(MainActivity.this, ""+user.getUid(),+ Toast.LENGTH_SHORT).show();
                            startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            Toast.makeText(MainActivity.this, "Fuck off", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
}
