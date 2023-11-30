package com.example.ep4rtdb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListaActivity extends AppCompatActivity {
    private ListView listView;
    private DatabaseReference reference;
    private List<Estudiante> estudiantesList;
    private EstudianteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        listView = findViewById(R.id.lsvEstudiantes);
        estudiantesList = new ArrayList<>();
        adapter = new EstudianteAdapter(this, estudiantesList);
        listView.setAdapter(adapter);

        reference = FirebaseDatabase.getInstance().getReference("estudiantes");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                estudiantesList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if (dataSnapshot.getValue() != null){
                        Estudiante estudiante = dataSnapshot.getValue(Estudiante.class);
                        estudiantesList.add(estudiante);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ListaActivity", "Error en la base de datos: " + error.getMessage());
            }
        });
    }

    private class EstudianteAdapter extends ArrayAdapter<Estudiante>{
        private Context context;

        public EstudianteAdapter(Context context, List<Estudiante> estudiantes){
            super(context, 0, estudiantes);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            if (convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.item_estudiante, parent, false);
            }

            Estudiante estudiante = getItem(position);
            TextView txtCodigo = convertView.findViewById(R.id.txtCodigo);
            TextView txtApellido = convertView.findViewById(R.id.txtApellido);
            TextView txtNombre = convertView.findViewById(R.id.txtNombre);
            TextView txtNota1 = convertView.findViewById(R.id.txtNota1);
            TextView txtNota2 = convertView.findViewById(R.id.txtNota2);
            TextView txtNota3 = convertView.findViewById(R.id.txtNota3);
            TextView txtNota4 = convertView.findViewById(R.id.txtNota4);
            TextView txtPromedio = convertView.findViewById(R.id.txtPromedio);
            Button btnEliminar = convertView.findViewById(R.id.btnEliminar);

            txtCodigo.setText("Codigo: " + estudiante.getCodigo());
            txtApellido.setText("Nombre: " + estudiante.getApellido());
            txtNombre.setText("Apellido: " + estudiante.getNombre());
            txtNota1.setText("Nota 1: " + estudiante.getNota1());
            txtNota2.setText("Nota 2: " + estudiante.getNota2());
            txtNota3.setText("Nota 3: " + estudiante.getNota3());
            txtNota4.setText("Nota 4: " + estudiante.getNota4());
            txtPromedio.setText("Promedio: " + estudiante.getPromedio());

            btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String codEstudiante = estudiante.getCodigo();
                    DatabaseReference estudianteRef = FirebaseDatabase.getInstance().getReference("estudiantes").child(codEstudiante);

                    estudianteRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                estudiantesList.remove(estudiante);
                                notifyDataSetChanged();
                                Toast.makeText(context, "Estudiante eliminado.", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(context, "Error al eliminar estudiante.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
            return convertView;
        }
    }
}