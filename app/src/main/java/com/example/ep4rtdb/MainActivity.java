package com.example.ep4rtdb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    EditText edtCodigo, edtNombre, edtApellido, edtNota1, edtNota2, edtNota3, edtNota4, edtPromedio;
    Button btnGuardar, btnVerTodo;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtCodigo = findViewById(R.id.edtCodigo);
        edtNombre = findViewById(R.id.edtNombre);
        edtApellido = findViewById(R.id.edtApellido);
        edtNota1 = findViewById(R.id.edtNota1);
        edtNota2 = findViewById(R.id.edtNota2);
        edtNota3 = findViewById(R.id.edtNota3);
        edtNota4 = findViewById(R.id.edtNota4);
        edtPromedio = findViewById(R.id.edtPromedio);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnVerTodo = findViewById(R.id.btnVerListado);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("estudiantes");

                String cod = edtCodigo.getText().toString().trim();
                String name = edtNombre.getText().toString().trim();
                String ape = edtApellido.getText().toString().trim();
                int n1 = Integer.parseInt(edtNota1.getText().toString().trim());
                int n2 = Integer.parseInt(edtNota2.getText().toString().trim());
                int n3 = Integer.parseInt(edtNota3.getText().toString().trim());
                int n4 = Integer.parseInt(edtNota4.getText().toString().trim());

                int[] notas = {n1, n2, n3, n4};
                Arrays.sort(notas);
                int suma = notas[1] + notas[2] + notas[3];
                int prom = suma / 3;

                Estudiante estudiante = new Estudiante(cod, name, ape, n1, n2, n3, n4, prom);
                reference.child(cod).setValue(estudiante);
                Toast.makeText(MainActivity.this, "Registro exitoso.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, ListaActivity.class));
                clearFields();
            }
        });

        btnVerTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ListaActivity.class));
            }
        });
    }

    public void clearFields(){
        edtCodigo.setText("");
        edtApellido.setText("");
        edtNombre.setText("");
        edtNota1.setText("");
        edtNota2.setText("");
        edtNota3.setText("");
        edtNota4.setText("");
        edtPromedio.setText("");
    }
}