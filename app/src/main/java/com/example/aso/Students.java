package com.example.aso;

import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.aso.entitys.Student;

public class Students extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = Students.this;

    private Spinner levels, carrers;
    private TextInputEditText ced, names;
    private Button save, edit, search, delete;

    private String[] l;
    private String[] c;

    private Student student;
    private ConexionSQLiteHelper conn;

    private ConstraintLayout layout;

    private String level, carrer;

    private ArrayAdapter lAd, cAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);



        l = new String[]{"Seleccione...", "PRIMERO","SEGUNDO","TERCERO","CUARTO","QUINTO","SEXTO","SEPTIMO","OCTAVO","NOVENO","DECIMO"};
        c = new String[]{"Seleccione...", "SISTEMAS", "CONTABILIDAD", "TIC", "EDU.BASICA","AGROPECUARIA"};

        initObjects();
        initViews();
        try {
            levels.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    level = l[position];
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            carrers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    carrer = c[position];
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"error: "+e.toString(),Toast.LENGTH_LONG).show();
        }
        initListeners();


    }

    private void initObjects(){

        conn = new ConexionSQLiteHelper(activity);
        student = new Student();
        lAd = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,l);
        cAd = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,c);
    }

    private void initViews(){
        ced = findViewById(R.id.txtCedS);
        names = findViewById(R.id.txtNameS);
        levels = findViewById(R.id.levels);
        carrers = findViewById(R.id.carrers);

        save = findViewById(R.id.btnSaveS);
        edit = findViewById(R.id.btnEditS);
        search = findViewById(R.id.btnSearchS);
        delete = findViewById(R.id.btnDeleteS);

        layout = findViewById(R.id.students);

        levels.setAdapter(lAd);
        carrers.setAdapter(cAd);

    }

    private void initListeners(){
        save.setOnClickListener(this);
        edit.setOnClickListener(this);
        search.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    private void saveStudent(){
        String cedS = ced.getText().toString().trim();
        String namesS = names.getText().toString().trim();
        String levelS = level;
        String carrerS = carrer;


        if (cedS.matches("") || namesS.matches("")){
            Toast.makeText(getApplicationContext(),"Llene los campos", Toast.LENGTH_LONG).show();
        }else {
            try {
                if(levelS.equals("Seleccione...") || carrerS.equals("Seleccione...")){
                    Toast.makeText(getApplicationContext(),"Seleccione el nivel y carrera",Toast.LENGTH_LONG).show();
                }else {
                    if (!conn.checkStudent(cedS)) {
                        student.setCed(cedS);
                        student.setNames(namesS);
                        student.setLevel(levelS);
                        student.setCarrer(carrerS);

                        conn.addStudent(student);
                        //Toast.makeText(getApplicationContext(),level+" "+carrer,Toast.LENGTH_LONG).show();

                        Snackbar.make(layout, "Estudiante Registrado", Snackbar.LENGTH_LONG).show();
                        emptyInputEditText();
                    }else {
                        Snackbar.make(layout, "Estudiante ya existe", Snackbar.LENGTH_LONG).show();
                    }
                }
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(), "error: "+e.toString(),Toast.LENGTH_LONG).show();
            }

        }
    }

    private void getStudent(){
        String cedS = ced.getText().toString().trim();
        if (cedS.matches("")){
            Toast.makeText(getApplicationContext(),"Ingrese la cédula", Toast.LENGTH_LONG).show();
        }else {

            if (!conn.checkStudent(cedS)){
                Toast.makeText(getApplicationContext(),"Estudiante no encontrado", Toast.LENGTH_LONG).show();
            }else {
                student = conn.getStudent(cedS);
                names.setText(student.getNames());

                levels.post(new Runnable() {
                    @Override
                    public void run() {
                        levels.setSelection(lAd.getPosition(student.getLevel()));
                    }
                });

                carrers.post(new Runnable() {
                    @Override
                    public void run() {
                        carrers.setSelection(cAd.getPosition(student.getCarrer()));
                    }
                });
            }
        }
    }

    private void editStudent(){
        String cedS = ced.getText().toString().trim();
        String namesS = names.getText().toString().trim();
        String levelS = level;
        String carrerS = carrer;
        if (cedS.matches("") || namesS.matches("")){
            Toast.makeText(getApplicationContext(),"Llene los campos", Toast.LENGTH_LONG).show();
        }else {
            if(levelS.equals("Seleccione...") || carrerS.equals("Seleccione...")){
                Toast.makeText(getApplicationContext(),"Seleccione el nivel y carrera",Toast.LENGTH_LONG).show();
            }else {
                student.setCed(cedS);
                student.setNames(namesS);
                student.setLevel(levelS);
                student.setCarrer(carrerS);
                conn.updateStudent(student);

                Snackbar.make(layout, "Estudiante Actualizado", Snackbar.LENGTH_LONG).show();
                emptyInputEditText();
            }
        }
    }

    private void deleteStudent(){
        String cedS = ced.getText().toString().trim();
        if (cedS.matches("")){
            Toast.makeText(getApplicationContext(),"Específique el estudiante", Toast.LENGTH_LONG).show();
        }else {
            student.setCed(cedS);
            conn.deleteStudent(student);

            Snackbar.make(layout, "Estudiante Eliminado", Snackbar.LENGTH_LONG).show();
            emptyInputEditText();
        }
    }

    private void emptyInputEditText() {
        ced.setText(null);
        names.setText(null);
        levels.setSelection(0);
        carrers.setSelection(0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSaveS:
                saveStudent();
                break;
            case R.id.btnEditS:
                editStudent();
                break;
            case R.id.btnSearchS:
                getStudent();
                break;
            case R.id.btnDeleteS:
                deleteStudent();
                break;
        }
    }
}
