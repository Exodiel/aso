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
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.aso.entitys.Service;

public class Services extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = Services.this;
    private TextInputEditText ced,can;
    private Button save, search, exit;
    private RadioButton color, b_n;
    private  Spinner services;
    private String[] s;
    private ArrayAdapter sA;

    private String ser, c, bn;
    private ConstraintLayout layout;

    private Service service;
    private ConexionSQLiteHelper conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        s = new String[]{"Seleccione...","COPIAS","IMPRESION"};

        initObjects();
        initViews();
        save = findViewById(R.id.btnSaveService);
        exit = findViewById(R.id.btnExitService);

        services.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ser = s[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchStudent();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveService();
//                Toast.makeText(getApplicationContext(),"Hola",Toast.LENGTH_LONG).show();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });

        initListeners();
    }

    private void initViews(){
        ced = findViewById(R.id.txtCedService);
        can = findViewById(R.id.txtQuantity);

        search = findViewById(R.id.btnSearchStudentS);
        exit = findViewById(R.id.btnExitService);
        b_n = findViewById(R.id.bn);
        color = findViewById(R.id.color);
        services = findViewById(R.id.servicesSpinner);

        layout = findViewById(R.id.services);

        services.setAdapter(sA);
    }

    private void initListeners(){

        color.setOnClickListener(this);
        b_n.setOnClickListener(this);
    }

    private void initObjects(){
        conn = new ConexionSQLiteHelper(activity);
        service = new Service();
        sA = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,s);
    }

    private void saveService(){

        String q = can.getText().toString().trim();


        if (q.matches("") || service.getIdstudent() == null) {
            Toast.makeText(getApplicationContext(),"Llene todos los campos o busque el estudiante",Toast.LENGTH_LONG).show();
        }
        else {

            String serviceType = ser;
            if (serviceType.equals("Seleccione...")){
                Toast.makeText(getApplicationContext(),"Elija un servicio",Toast.LENGTH_LONG).show();
            }
            else {
                if (service.getTypecolor() == null){
                    Toast.makeText(getApplicationContext(),"Elija una categoría",Toast.LENGTH_LONG).show();
                }
                else {
                    try{
                        int student = service.getIdstudent();
                        int quantity = Integer.parseInt(can.getText().toString().trim());
                        service.setIdstudent(student);
                        service.setNum(quantity);
                        service.setTypeaction(serviceType);

                        conn.saveService(service);
                        Snackbar.make(layout, "Servicio registrado", Snackbar.LENGTH_LONG).show();
                        emptyInputEditText();
                    }catch(Exception e){
                        Toast.makeText(getApplicationContext(),""+e.toString(),Toast.LENGTH_LONG).show();
                    }
                }
            }

        }

    }

    private void searchStudent(){
        String cedS = ced.getText().toString().trim();

        if (cedS.matches("")){
            Toast.makeText(getApplicationContext(),"Llene el campo de la cédula",Toast.LENGTH_LONG).show();
        }
        else {

            try{
                int id = conn.findIdStudent(cedS);

                if (id > 0) {
                    service.setIdstudent(id);
                    Snackbar.make(layout, "Estudiante : "+id+", puede seguir llenando los datos faltantes", Snackbar.LENGTH_LONG).show();

                    //ced.setText(null);
                }else if(service.getIdstudent() != null){
                    Snackbar.make(layout, "El estudiante ya fue buscado", Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(layout, "Estudiante no encontrado", Snackbar.LENGTH_LONG).show();
                }
            } catch (Exception e){
                Toast.makeText(getApplicationContext(),""+e.toString(),Toast.LENGTH_LONG).show();
            }
        }
    }

    private void emptyInputEditText() {
        can.setText("");
        services.setSelection(0);
        color.setChecked(false);
        b_n.setChecked(false);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public void onClick(View v) {
        boolean checked = ((RadioButton) v).isChecked();
        switch (v.getId()){

            case R.id.bn:
                if (checked){
                    bn = "bn";
                    service.setTypecolor(bn);
                }
                break;

            case R.id.color:
                if (checked){
                    c = "color";
                    service.setTypecolor(c);
                }
                break;

        }
    }
}
