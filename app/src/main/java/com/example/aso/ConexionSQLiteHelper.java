package com.example.aso;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.aso.entitys.Service;
import com.example.aso.entitys.Student;
import com.example.aso.entitys.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "bd_pi.db";
    public static final int DATABASE_VERSION = 1;

    public static final String USER_TABLE = "users";
    public static final String USER_ID_FIELD = "id";
    public static final String USER_USER_FIELD = "user";
    public static final String USER_PASSWORD_FIELD = "password";
    public static final String USER_CED_FIELD = "ced";

    public static final String STUDENT_TABLE = "students";
    public static final String STUDENT_ID_FIELD = "id";
    public static final String STUDENT_CED_FIELD = "ced";
    public static final String STUDENT_NAMES_FIELD = "names";
    public static final String STUDENT_LEVEL_FIELD = "level";
    public static final String STUDENT_CARRER_FIELD = "carrer";

    public static final String SERVICE_TABLE = "services";
    public static final String SERVICE_ID_FIELD= "id";
    public static final String SERVICE_IDSTUDENT_FIELD= "idstudent";
    public static final String SERVICE_TYPECOLOR_FIELD= "typecolor";
    public static final String SERVICE_TYPEACTION_FIELD= "typeaction";
    public static final String SERVICE_NUM_FIELD= "num";
    public static final String SERVICE_DATE_FIELD = "currentdate";

    public static final String CREATE_TABLE_USER = "CREATE TABLE "+USER_TABLE+"("+USER_ID_FIELD+" INTEGER PRIMARY KEY AUTOINCREMENT, "+USER_USER_FIELD+" TEXT, "+USER_PASSWORD_FIELD+" TEXT, "+USER_CED_FIELD+" TEXT)";

    public static final String CREATE_TABLE_STUDENT = "CREATE TABLE "+STUDENT_TABLE+"("+STUDENT_ID_FIELD+" INTEGER PRIMARY KEY AUTOINCREMENT, "+STUDENT_CED_FIELD+" TEXT, "+STUDENT_NAMES_FIELD+" TEXT, "+STUDENT_LEVEL_FIELD+" TEXT, "+STUDENT_CARRER_FIELD+" TEXT)";

    public static final String CREATE_TABLE_SERVICES = "CREATE TABLE "+SERVICE_TABLE+"("+SERVICE_ID_FIELD+" INTEGER PRIMARY KEY AUTOINCREMENT, "+SERVICE_IDSTUDENT_FIELD+" INTEGER, "+SERVICE_TYPECOLOR_FIELD+" TEXT, "+SERVICE_TYPEACTION_FIELD+" TEXT NOT NULL, "+SERVICE_NUM_FIELD+" INTEGER, "+SERVICE_DATE_FIELD+" TEXT NOT NULL, FOREIGN KEY("+SERVICE_IDSTUDENT_FIELD+") REFERENCES "+STUDENT_TABLE+"("+STUDENT_ID_FIELD+"))";


    public ConexionSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_STUDENT);
        db.execSQL(CREATE_TABLE_SERVICES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+STUDENT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+SERVICE_TABLE);
        onCreate(db);
    }


    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(USER_USER_FIELD, user.getUser());
        values.put(USER_PASSWORD_FIELD, user.getPassword());
        values.put(USER_CED_FIELD, user.getCed());

        db.insert(USER_TABLE,null,values);
        db.close();
    }

    public boolean checkUser(String user, String password){
        String[] columns = {USER_ID_FIELD};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = USER_USER_FIELD+"=?"+" AND "+USER_PASSWORD_FIELD+"=?";
        String[] selectionArgs = {user,password};

        Cursor cursor = db.query(USER_TABLE,columns,selection,selectionArgs,null,null,null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if (count > 0){
            return true;
        }

        return false;
    }

    public void addStudent(Student student){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(STUDENT_CED_FIELD, student.getCed());
        values.put(STUDENT_NAMES_FIELD, student.getNames());
        values.put(STUDENT_LEVEL_FIELD, student.getLevel());
        values.put(STUDENT_CARRER_FIELD, student.getCarrer());

        db.insert(STUDENT_TABLE,null,values);
        db.close();
    }

    public boolean checkStudent(String ced){
        String[] columns = {STUDENT_ID_FIELD};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = STUDENT_CED_FIELD+"=?";
        String[] selectionArgs = {ced};

        Cursor cursor = db.query(STUDENT_TABLE,columns,selection,selectionArgs,null,null,null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if (count > 0){
            return true;
        }

        return false;
    }

    public Student getStudent(String ced) {
        Student student = new Student();
        String[] columns = {
            STUDENT_NAMES_FIELD,
            STUDENT_LEVEL_FIELD,
            STUDENT_CARRER_FIELD
        };
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(STUDENT_TABLE,columns,STUDENT_CED_FIELD+"=?", new String[]{ced},null,null,null);

        if (cursor.moveToFirst()) {
            student.setNames(cursor.getString(0));
            student.setLevel(cursor.getString(1));
            student.setCarrer(cursor.getString(2));
        }
        cursor.close();
        db.close();

        return student;
    }

    public void updateStudent(Student student){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(STUDENT_CED_FIELD, student.getCed());
        values.put(STUDENT_NAMES_FIELD, student.getNames());
        values.put(STUDENT_LEVEL_FIELD, student.getLevel());
        values.put(STUDENT_CARRER_FIELD, student.getCarrer());

        db.update(STUDENT_TABLE,values,STUDENT_CED_FIELD+"=?",new String[]{student.getCed()});
        db.close();
    }

    public void deleteStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(STUDENT_TABLE, STUDENT_CED_FIELD + " = ?",
                new String[]{student.getCed()});
        db.close();
    }

    public void saveService(Service service){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Date date = new Date();
        SimpleDateFormat fomatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = fomatter.format(date);

        values.put(SERVICE_IDSTUDENT_FIELD, service.getIdstudent());
        values.put(SERVICE_NUM_FIELD, service.getNum());
        values.put(SERVICE_TYPEACTION_FIELD, service.getTypeaction());
        values.put(SERVICE_TYPECOLOR_FIELD, service.getTypecolor());
        values.put(SERVICE_DATE_FIELD, strDate);

        db.insert(SERVICE_TABLE,null,values);
        db.close();
    }

    public int findIdStudent(String ced){
        int id = 0;
        String[] columns = {STUDENT_ID_FIELD};
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(STUDENT_TABLE,columns,STUDENT_CED_FIELD+"=?", new String[]{ced},null,null,null);

        if (cursor.moveToFirst()){
            id = cursor.getInt(0);
        }
        cursor.close();
        db.close();

        return id;
    }

    public List<String> reportByLevel(){
        List<String> report = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT students.names,students.level,services.typeaction,services.typecolor,services.num FROM students INNER JOIN services ON students.id = services.idstudent";
        Cursor c = db.rawQuery(query, null);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            report.add(c.getString(c.getColumnIndex(STUDENT_NAMES_FIELD)));
            report.add(c.getString(c.getColumnIndex(STUDENT_LEVEL_FIELD)));
            report.add(c.getString(c.getColumnIndex(SERVICE_TYPEACTION_FIELD)));
            report.add(c.getString(c.getColumnIndex(SERVICE_TYPECOLOR_FIELD)));
            report.add(String.valueOf(c.getInt(c.getColumnIndex(SERVICE_NUM_FIELD))));
        }
        c.close();
        db.close();

        return report;
    }

    public List<String> reportByCarrer(){
        List<String> report = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT students.names,students.carrer,services.typeaction,services.typecolor,services.num FROM services INNER JOIN students ON students.id = services.idstudent";
        Cursor c = db.rawQuery(query, null);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            report.add(c.getString(c.getColumnIndex(STUDENT_NAMES_FIELD)));
            report.add(c.getString(c.getColumnIndex(STUDENT_CARRER_FIELD)));
            report.add(c.getString(c.getColumnIndex(SERVICE_TYPEACTION_FIELD)));
            report.add(c.getString(c.getColumnIndex(SERVICE_TYPECOLOR_FIELD)));
            report.add(String.valueOf(c.getInt(c.getColumnIndex(SERVICE_NUM_FIELD))));
        }
        c.close();
        db.close();

        return report;
    }

    public List<String> reportByDate(String date){
        List<String> report = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT students.names, services.currentdate,services.typeaction,services.typecolor,services.num FROM students INNER JOIN services ON students.id = services.idstudent WHERE services.currentdate = ?";
        Cursor c = db.rawQuery(query, new String[]{date});

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            report.add(c.getString(c.getColumnIndex(STUDENT_NAMES_FIELD)));
            report.add(c.getString(c.getColumnIndex(SERVICE_DATE_FIELD)));
            report.add(c.getString(c.getColumnIndex(SERVICE_TYPEACTION_FIELD)));
            report.add(c.getString(c.getColumnIndex(SERVICE_TYPECOLOR_FIELD)));
            report.add(String.valueOf(c.getInt(c.getColumnIndex(SERVICE_NUM_FIELD))));
        }
        c.close();
        db.close();

        return report;
    }


}
