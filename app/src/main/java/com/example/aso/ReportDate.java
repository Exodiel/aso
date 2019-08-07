package com.example.aso;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ReportDate extends AppCompatActivity {
    private final AppCompatActivity activity = ReportDate.this;
    private EditText txtDate;
    private Button btnDate;
    private ConstraintLayout layout;
    private Calendar calendar;
    private ConexionSQLiteHelper conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_date);
        txtDate = findViewById(R.id.txtDateSearch);
        btnDate = findViewById(R.id.btnDateSearch);
        layout = findViewById(R.id.reportDateLayout);

        conn = new ConexionSQLiteHelper(activity);

        calendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                updateLabel();
            }
        };

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ReportDate.this,date,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isStoragePermissionGranted()){
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                }else {
                    pdfDate();
                }

            }
        });
    }

    private void pdfDate(){
        String date = txtDate.getText().toString().trim();
        if (date.matches("")){
            Toast.makeText(getApplicationContext(),"Elija una fecha",Toast.LENGTH_LONG).show();
        }else {
            List<String> report;
            Document mDoc = new Document();
            final String mFileName = new SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())+".pdf";
            final File pdfFolder = new File(Environment.getExternalStorageDirectory()+"/Documents");
            if(!pdfFolder.exists()){
                pdfFolder.mkdir();
            }
            final File mFile = new File(pdfFolder.getAbsolutePath(),mFileName);
            try{

                PdfPTable table = new PdfPTable(new float[]{3,3,3,3,3});
                table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                table.getDefaultCell().setFixedHeight(50);
                table.setTotalWidth(PageSize.A4.getWidth());
                table.setWidthPercentage(100);
                table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell("Estudiante");
                table.addCell("Fecha");
                table.addCell("Servicio");
                table.addCell("Categoría");
                table.addCell("Cantidad");
                table.setHeaderRows(1);
                PdfPCell[] cells = table.getRow(0).getCells();
                for (int i = 0; i < cells.length; i++) {
                    cells[i].setBackgroundColor(BaseColor.LIGHT_GRAY);
                }

                report = conn.reportByDate(date);

                if (report.size() < 0){
                    Toast.makeText(this, "No existen registros", Toast.LENGTH_LONG).show();
                }else {
                    for (String data : report){
                        table.addCell(data);
                    }
                    OutputStream output = new FileOutputStream(mFile);
                    PdfWriter.getInstance(mDoc, output);
                    mDoc.open();

                    mDoc.addAuthor("ASO");
                    Font f = new Font(Font.FontFamily.TIMES_ROMAN,30.0f,Font.BOLD,BaseColor.BLACK);
                    mDoc.add(new Paragraph("Servicios por Fecha\n\n",f));


                    mDoc.add(table);

                    mDoc.close();

                    Snackbar snackbar = Snackbar.make(layout,"PDF guardado.",Snackbar.LENGTH_LONG);
                    snackbar.setAction("Abrir", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            File file = new File(pdfFolder.getAbsolutePath()+"/"+mFileName);
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setDataAndType(Uri.fromFile(file),"application/pdf");
                            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                            Intent intent = Intent.createChooser(i,"Abrir archivo!");
                            try {
                                startActivity(intent);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(getApplicationContext(),"Instale un lector de PDF",Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                    snackbar.show();
                }


            }
            catch (Exception e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            pdfDate();

            //Toast.makeText(this, "Permisos otorgados", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Permisos denegados...!", Toast.LENGTH_LONG).show();
        }
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "Permisos otorgados", Toast.LENGTH_LONG).show();
                return true;
            } else {

                activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Toast.makeText(this, "Permisos denegados...!", Toast.LENGTH_LONG).show();
            return true;
        }
    }

    private void updateLabel() {
        String format = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        txtDate.setText(simpleDateFormat.format(calendar.getTime()));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
