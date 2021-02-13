package com.example.tresenraya;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {

    String sqlTablaPartidas = "CREATE TABLE IF NOT EXISTS partidas(id_Partida integer PRIMARY KEY, nombre1 text, nombre2 text, dificultad text, resultado text)";
    String sqlTablaUsuarios = "CREATE TABLE IF NOT EXISTS usuarios(id_Usuario integer PRIMARY KEY, nombre text, puntos_totales integer, num_partidas integer)";


    public SQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlTablaPartidas);
        db.execSQL(sqlTablaUsuarios);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS partidas");
        db.execSQL(sqlTablaPartidas);
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL(sqlTablaUsuarios);
    }
}
