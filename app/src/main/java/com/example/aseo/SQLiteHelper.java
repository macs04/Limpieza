package com.example.aseo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class SQLiteHelper extends SQLiteOpenHelper {

    //region Crear Tabla imputado_datos_generales
    private static final String CREATE_TABLE_LIMPIEZA = "CREATE TABLE limpieza(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "Valoracion TEXT, Area TEXT, Fecha TEXT, Observaciones TEXT, Evaluador TEXT)";
    //endregion

    //region Definicion de la base de datos
    private static final String DB_NAME="limpieza.sqlite";
    private static final int DB_VERSION = 1;
    private SQLiteDatabase db;

    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        db=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_LIMPIEZA);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    //endregion

    //region Insertar a Base de Datos
    public void insertarDatos(String valoracion, String area, String fecha, String observaciones, String evaluador){
        ContentValues dato=new ContentValues();
        dato.put("Valoracion", valoracion);
        dato.put("Area", area);
        dato.put("Fecha", fecha);
        dato.put("Observaciones", observaciones);
        dato.put("Evaluador", evaluador);
        db.insert("limpieza", null, dato);
    }
    //endregion

    //region Obtener las areas evaluadas
    public boolean saved(String area, String date){
        //ArrayList<String> lista=new ArrayList<String>();
        Cursor c=db.rawQuery("SELECT _id, Area FROM limpieza WHERE Area = '" + area + "' AND Fecha = '" + date + "'",  null);
        if (c != null && c.getCount() > 0) {
            return true;
            /*c.moveToFirst();
            do {
                String r = c.getString(c.getColumnIndex("Area"));
                lista.add(r);
            } while (c.moveToNext());*/
        }
        //Cerramos el cursor
        c.close();
        return false;
    }
    //endregion

    public Cursor raw() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + DB_NAME , new String[]{});
        return res;
    }
}