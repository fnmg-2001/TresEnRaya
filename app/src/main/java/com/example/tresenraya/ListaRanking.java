package com.example.tresenraya;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ListaRanking extends AppCompatActivity {

    ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_ranking);

        SQLiteHelper helper = new SQLiteHelper(this, "bdTresEnRaya.db", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("usuarios", null, null, null, null, null, null);

        String nombre;
        int puntosTotales, numPartidas;
        cursor.moveToFirst();

        ArrayList<String> datos = new ArrayList<String>();

        for (int i = 0; i < cursor.getCount(); i++){
            nombre = cursor.getString(1);
            puntosTotales = cursor.getInt(2);
            numPartidas = cursor.getInt(3);
            datos.add("Usuario: " + nombre + " - " + "Puntos Totales: " + String.valueOf(puntosTotales) + " - " + "Numero de partidas: " + String.valueOf(numPartidas));
            cursor.moveToNext();
        }

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, datos);
        lista = findViewById(R.id.lstRanking);
        lista.setAdapter(adaptador);
        db.close();
    }
}