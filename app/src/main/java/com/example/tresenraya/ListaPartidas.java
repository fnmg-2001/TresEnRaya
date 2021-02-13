package com.example.tresenraya;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListaPartidas extends AppCompatActivity {

    ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_partidas);

        SQLiteOpenHelper helper = new SQLiteHelper(this, "bdTresEnRaya.db",null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("partidas", null, null, null, null, null, null);
        List<String> datos = new ArrayList<String>();

        String nombre1;
        String nombre2;
        String dificultad;
        String resultado;

        cursor.moveToFirst();

        for (int i = 0; i<cursor.getCount(); i++){
            nombre1=cursor.getString(1);
            nombre2=cursor.getString(2);
            dificultad=cursor.getString(3);
            resultado=cursor.getString(4);
            datos.add("Jugador1: " + nombre1 + " - " + "Jugador 2: " + nombre2 + " - " + " Dificultad: " + dificultad + " - " + "Resultado: " + resultado);
            cursor.moveToNext();
        }

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, datos);
        lista=findViewById(R.id.lstVista);
        lista.setAdapter(adaptador);
        db.close();
    }
}