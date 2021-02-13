package com.example.tresenraya;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    Button botonMostrarPartidas, botonMostrarRanking, añadirUsuario, add, aceptar, cancelar;
    ImageButton media;
    EditText edtNombre;
    TextView txtNombre1, txtNombre2;
    String mensaje;
    RadioButton normal, dificil, extremo;
    boolean sonido;

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sonido = true;

        //Inicializamos el array con cada casilla del tablero
        CASILLAS= new int[9];
        CASILLAS[0]=R.id.a1;
        CASILLAS[1]=R.id.a2;
        CASILLAS[2]=R.id.a3;
        CASILLAS[3]=R.id.b1;
        CASILLAS[4]=R.id.b2;
        CASILLAS[5]=R.id.b3;
        CASILLAS[6]=R.id.c1;
        CASILLAS[7]=R.id.c2;
        CASILLAS[8]=R.id.c3;

        SQLiteOpenHelper helper = new SQLiteHelper(this, "bdTresEnRaya.db", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();

        botonMostrarPartidas = findViewById(R.id.botonMostrarPartidas);
        botonMostrarRanking = findViewById(R.id.botonMostarRanking);
        añadirUsuario = findViewById(R.id.btnNuevoUsuario);
        add = findViewById(R.id.btnAdd);
        cancelar = findViewById(R.id.btnCancelar);
        aceptar = findViewById(R.id.btnAceptar);
        edtNombre = findViewById(R.id.edtNombre);
        normal = findViewById(R.id.normal);
        dificil = findViewById(R.id.dificil);
        extremo = findViewById(R.id.imposible);
        media = findViewById(R.id.media);

        Cursor cursor = db.query("partidas",null,null,null,null,null,null);
        cursor.moveToLast();
        String nombre1=cursor.getString(1);
        String nombre2=cursor.getString(2);
        String dificultad=cursor.getString(3);
        String resultado=cursor.getString(4);

        Toast toast = Toast.makeText(this, "Ultima partida " +"\n"+ nombre1 + " \nVS " + "\n" + nombre2 + " \ndificultad: " + dificultad + " \nGanador: " + resultado, Toast.LENGTH_LONG);
        toast.show();

        media.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sonido==true){
                    sonido=false;
                    media.setImageResource(R.drawable.speakermute);
                } else if (sonido==false){
                    sonido=true;
                    media.setImageResource(R.drawable.speaker);
                }
            }
        });



        //db.delete("usuarios",null,null);

        añadirUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtNombre.getVisibility() != View.VISIBLE && add.getVisibility() != View.VISIBLE){
                    edtNombre.setVisibility(View.VISIBLE);
                    add.setVisibility(View.VISIBLE);
                    añadirUsuario.setVisibility(View.INVISIBLE);
                    cancelar.setVisibility(View.VISIBLE);
                }

            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = db.query("usuarios",null,null,null,null,null,null);
                List<String> nombres = new ArrayList<String>();
                String nombre;
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++){
                    nombre = cursor.getString(1);
                    nombres.add(nombre);
                    cursor.moveToNext();
                }
                if (edtNombre.getVisibility() == View.VISIBLE && !edtNombre.getText().equals("")){
                    if (nombres.isEmpty()){
                        ContentValues values = new ContentValues();
                        values.put("nombre", edtNombre.getText().toString());
                        values.put("puntos_totales", 0);
                        values.put("num_partidas", 0);
                        db.insert("usuarios", null, values);
                        edtNombre.setVisibility(View.INVISIBLE);
                        edtNombre.getText().clear();
                        add.setVisibility(View.INVISIBLE);
                        añadirUsuario.setVisibility(View.VISIBLE);
                        cancelar.setVisibility(View.INVISIBLE);
                        Toast toast = Toast.makeText(getApplicationContext(), "Se ha creado el usuario con el nombre " + edtNombre.getText().toString(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                    else if (!nombres.isEmpty()){
                        for(String nombreLista : nombres) {
                            if (!nombres.contains(edtNombre.getText().toString()) && !edtNombre.getText().toString().equals("")) {
                                ContentValues values = new ContentValues();
                                values.put("nombre", edtNombre.getText().toString());
                                values.put("puntos_totales", 0);
                                values.put("num_partidas", 0);
                                db.insert("usuarios", null, values);
                                Toast toast = Toast.makeText(getApplicationContext(), "Se ha creado el usuario con el nombre " + edtNombre.getText().toString(), Toast.LENGTH_LONG);
                                toast.show();
                                edtNombre.setVisibility(View.INVISIBLE);
                                edtNombre.getText().clear();
                                add.setVisibility(View.INVISIBLE);
                                añadirUsuario.setVisibility(View.VISIBLE);
                                cancelar.setVisibility(View.INVISIBLE);
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), "El usuario " + edtNombre.getText().toString() + " ya existe.", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }
                    }
                }
            }
        });

        botonMostrarRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ListaRanking.class);
                startActivity(i);
            }
        });

        botonMostrarPartidas.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ListaPartidas.class);
                startActivity(i);
            }
        });


        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtNombre.getVisibility() != View.INVISIBLE && add.getVisibility() != View.INVISIBLE){
                    edtNombre.setVisibility(View.INVISIBLE);
                    edtNombre.getText().clear();
                    add.setVisibility(View.INVISIBLE);
                    aceptar.setVisibility(View.INVISIBLE);
                    añadirUsuario.setVisibility(View.VISIBLE);
                    cancelar.setVisibility(View.INVISIBLE);
                }
            }
        });


    }

    public void guardar(int resultado){
                SQLiteHelper helper = new SQLiteHelper(getApplicationContext(), "bdTresEnRaya.db", null, 1);
                SQLiteDatabase db = helper.getWritableDatabase();
                Cursor cursor = db.query("usuarios", null, null, null, null, null, null);
                Cursor cursor2 = db.query("partidas", null, null, null, null, null, null);

                //evaluamos la dificultad

        RadioGroup configDificultad=(RadioGroup)findViewById(R.id.grupoDificultad);

        int id=configDificultad.getCheckedRadioButtonId();

        int dificultad=0;

        if(id==R.id.normal){
            dificultad=1;
        }else if(id==R.id.dificil){
            dificultad=2;
        }else if(id==R.id.imposible){
            dificultad=3;
        }


        txtNombre1=findViewById(R.id.txtNombre1);
        String nombre1 = txtNombre1.getText().toString();
        cursor.moveToFirst();
                if(resultado==1 && dificultad==1){
                    for (int i = 0; i < cursor.getCount();i++){
                        String nombreBuscado = cursor.getString(1);
                        if (nombre1.equals(nombreBuscado)){
                            mensaje="Han ganado los círculos";

                            ContentValues values = new ContentValues();
                            int partidas = cursor.getInt(3)+1;
                            values.put("num_partidas",partidas);
                            db.update("usuarios", values, "nombre='"+nombre1+"'", null);

                            ContentValues values2 = new ContentValues();
                            int puntos = cursor.getInt(2)+1;
                            values2.put("puntos_totales",puntos);
                            db.update("usuarios", values2, "nombre='"+nombre1+"'", null);

                            ContentValues values3 = new ContentValues();
                            values3.put("nombre1", nombre1);
                            values3.put("nombre2", "Maquina");
                            values3.put("dificultad", "Normal");
                            values3.put("resultado", txtNombre1.getText().toString());
                            db.insert("partidas", null, values3);
                        } else {
                            cursor.moveToNext();
                        }

                    }



                }else if (resultado==1 && dificultad==2){
                    for (int i = 0; i < cursor.getCount();i++){
                        String nombreBuscado = cursor.getString(1);
                        if (nombre1.equals(nombreBuscado)){
                            mensaje="Han ganado los círculos";

                            ContentValues values = new ContentValues();
                            int partidas = cursor.getInt(3)+1;
                            values.put("num_partidas",partidas);
                            db.update("usuarios", values, "nombre='"+nombre1+"'", null);

                            ContentValues values2 = new ContentValues();
                            int puntos = cursor.getInt(2)+3;
                            values2.put("puntos_totales",puntos);
                            db.update("usuarios", values2, "nombre='"+nombre1+"'", null);

                            ContentValues values3 = new ContentValues();
                            values3.put("nombre1", nombre1);
                            values3.put("nombre2", "Maquina");
                            values3.put("dificultad", "Normal");
                            values3.put("resultado", txtNombre1.getText().toString());
                            db.insert("partidas", null, values3);
                        } else {
                            cursor.moveToNext();
                        }

                    }
                }else if (resultado==1 && dificultad==3){
                    for (int i = 0; i < cursor.getCount();i++){
                        String nombreBuscado = cursor.getString(1);
                        if (nombre1.equals(nombreBuscado)){
                            mensaje="Han ganado los círculos";

                            ContentValues values = new ContentValues();
                            int partidas = cursor.getInt(3)+1;
                            values.put("num_partidas",partidas);
                            db.update("usuarios", values, "nombre='"+nombre1+"'", null);

                            ContentValues values2 = new ContentValues();
                            int puntos = cursor.getInt(2)+5;
                            values2.put("puntos_totales",puntos);
                            db.update("usuarios", values2, "nombre='"+nombre1+"'", null);

                            ContentValues values3 = new ContentValues();
                            values3.put("nombre1", nombre1);
                            values3.put("nombre2", "Maquina");
                            values3.put("dificultad", "Normal");
                            values3.put("resultado", txtNombre1.getText().toString());
                            db.insert("partidas", null, values3);
                        } else {
                            cursor.moveToNext();
                        }

                    }
                }else if (resultado==2 && dificultad==1){
                    mensaje="Han ganado las aspas";

                    //ContentValues values = new ContentValues();
                    //values.put("num_partidas",cursor.getInt(3)+1);
                    //db.update("usuarios", values, "nombre="+txtNombre1.getText().toString(), null);

                    //ContentValues values2 = new ContentValues();
                    //values.put("puntos_totales", cursor.getInt(2)+1);
                    //db.update("usuarios", values2, "nombre="+txtNombre1.getText().toString(), null);

                    ContentValues values3 = new ContentValues();
                    values3.put("nombre1", nombre1);
                    values3.put("nombre2", "Maquina");
                    values3.put("dificultad", "Normal");
                    values3.put("resultado", "Maquina");
                    db.insert("partidas", null, values3);


                }else if (resultado==2  && dificultad==2){
                    mensaje="Han ganado las aspas";
                    //ContentValues values = new ContentValues();
                    //values.put("num_partidas",cursor.getInt(3)+1);
                    //db.update("usuarios", values, "nombre="+txtNombre1.getText().toString(), null);

                    //ContentValues values2 = new ContentValues();
                    //values.put("puntos_totales", cursor.getInt(2)+1);
                    //db.update("usuarios", values2, "nombre="+txtNombre1.getText().toString(), null);

                    ContentValues values3 = new ContentValues();
                    values3.put("nombre1", nombre1);
                    values3.put("nombre2", "Maquina");
                    values3.put("dificultad", "Dificil");
                    values3.put("resultado", "Maquina");
                    db.insert("partidas", null, values3);
                }else if (resultado==2  && dificultad==3){
                    mensaje="Han ganado las aspas";
                    //ContentValues values = new ContentValues();
                    //values.put("num_partidas",cursor.getInt(3)+1);
                    //db.update("usuarios", values, "nombre="+txtNombre1.getText().toString(), null);

                    //ContentValues values2 = new ContentValues();
                    //values.put("puntos_totales", cursor.getInt(2)+1);
                    //db.update("usuarios", values2, "nombre="+txtNombre1.getText().toString(), null);

                    ContentValues values3 = new ContentValues();
                    values3.put("nombre1", nombre1);
                    values3.put("nombre2", "Maquina");
                    values3.put("dificultad", "Extremo");
                    values3.put("resultado", "Maquina");
                    db.insert("partidas", null, values3);
                }else if (resultado == 3 && dificultad==1){
                    mensaje="Empate";
                    ContentValues values3 = new ContentValues();
                    values3.put("nombre1", nombre1);
                    values3.put("nombre2", "Maquina");
                    values3.put("dificultad", "Normal");
                    values3.put("resultado", "Empate");
                    db.insert("partidas", null, values3);
                }else if (resultado == 3 && dificultad==2){
                    ContentValues values3 = new ContentValues();
                    values3.put("nombre1", nombre1);
                    values3.put("nombre2", "Maquina");
                    values3.put("dificultad", "Dificil");
                    values3.put("resultado", "Empate");
                    db.insert("partidas", null, values3);
                }else if (resultado == 3 && dificultad==3){
                    ContentValues values3 = new ContentValues();
                    values3.put("nombre1", nombre1);
                    values3.put("nombre2", "Maquina");
                    values3.put("dificultad", "Extremo");
                    values3.put("resultado", "Empate");
                    db.insert("partidas", null, values3);
                }


                db.close();
    }

    public void Jugar(View v){

        //reseteamos el tablero
        ImageView imagen;

        for (int casilla:CASILLAS){
            imagen=(ImageView)findViewById(casilla);

            imagen.setImageResource(R.drawable.casilla);
        }

        //establecemos los jugadores que van a jugar (1 o 2 jugadores)
        jugadores=1;
        //el metodo Jugar será llamado tanto en el botón de un jugador como en el de dos
        //por eso comprobamos la vista que entra como parámetro

        //if(v.getId()==R.id.dosjugadores){
            //jugadores=2;
        //}

        txtNombre1 = findViewById(R.id.txtNombre1);
        txtNombre2 = findViewById(R.id.txtNombre2);

        List<String> listaNombres = new ArrayList<String>();
        SQLiteHelper helper = new SQLiteHelper(this, "bdTresEnRaya.db", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();

        Cursor cursor = db.query("usuarios",null,null,null,null,null,null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++){
            listaNombres.add(cursor.getString(1));
            cursor.moveToNext();
        }

        if (jugadores==1){
            edtNombre.setVisibility(View.VISIBLE);
            aceptar.setVisibility(View.VISIBLE);
            añadirUsuario.setVisibility(View.INVISIBLE);

            aceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //evaluamos la dificultad
                    RadioGroup configDificultad=(RadioGroup)findViewById(R.id.grupoDificultad);

                    int id=configDificultad.getCheckedRadioButtonId();

                    int dificultad=0;

                    if(id==R.id.normal){
                        dificultad=1;
                    }else if(id==R.id.dificil){
                        dificultad=2;
                    }else if(id==R.id.imposible){
                        dificultad=3;
                    }
                    if (listaNombres.contains(edtNombre.getText().toString())){
                        txtNombre1.setText(edtNombre.getText().toString());
                        if (dificultad == 1){
                            txtNombre2.setText("Maquina Normal");
                            partida=new Partida(dificultad);
                            //deshabilitamos los botones del tablero
                            ((Button)findViewById(R.id.unjugador)).setEnabled(false);
                            //((Button)findViewById(R.id.dosjugadores)).setEnabled(false);
                            ((RadioGroup)findViewById(R.id.grupoDificultad)).setAlpha(1);

                        } else if (dificultad == 2){
                            txtNombre2.setText("Maquina Dificil");
                            partida=new Partida(dificultad);
                            //deshabilitamos los botones del tablero
                            ((Button)findViewById(R.id.unjugador)).setEnabled(false);
                            //((Button)findViewById(R.id.dosjugadores)).setEnabled(false);
                            ((RadioGroup)findViewById(R.id.grupoDificultad)).setAlpha(0);

                        } else if (dificultad == 3){
                            txtNombre2.setText("Maquina Extremo");
                            partida=new Partida(dificultad);
                            //deshabilitamos los botones del tablero
                            ((Button)findViewById(R.id.unjugador)).setEnabled(false);
                            //((Button)findViewById(R.id.dosjugadores)).setEnabled(false);
                            ((RadioGroup)findViewById(R.id.grupoDificultad)).setAlpha(0);

                        }  else {
                            edtNombre.setVisibility(View.INVISIBLE);
                            aceptar.setVisibility(View.INVISIBLE);
                            añadirUsuario.setVisibility(View.VISIBLE);
                            cancelar.setVisibility(View.INVISIBLE);
                            Toast t = Toast.makeText(getApplicationContext(), "Debe seleccionar seleccionar dificultad", Toast.LENGTH_SHORT);
                            t.show();

                        }

                    } else {
                        edtNombre.setVisibility(View.INVISIBLE);
                        aceptar.setVisibility(View.INVISIBLE);
                        añadirUsuario.setVisibility(View.VISIBLE);
                        cancelar.setVisibility(View.INVISIBLE);
                        Toast t = Toast.makeText(getApplicationContext(), "Debe seleccionar un nombre valido y \n seleccionar dificultad", Toast.LENGTH_SHORT);
                        t.show();

                    }

                }
            });

        }

    }

    //creamos el método que se lanza al pulsar cada casilla
    public void toqueCasilla(View v){
        if (sonido==true){
            if (mediaPlayer==null){
                mediaPlayer = MediaPlayer.create(this, R.raw.pop);

            }
            if (!mediaPlayer.isPlaying()){
                mediaPlayer.start();
            }
        }


        //hacemos que sólo se ejecute cuando la variable partida no sea null
        if(partida==null){
            return;
        }else{
            int casilla=0;
            //recorremos el array donde tenemos almacenada cada casilla
            for(int i=0;i<9;i++){
                if(CASILLAS[i]==v.getId()){
                    casilla=i;
                    break;
                }
            }

            //creamos un mensaje toast
           /* Toast mensaje= Toast.makeText(this,"has pulsado la casilla "+ casilla,Toast.LENGTH_LONG);
            mensaje.setGravity(Gravity.CENTER,0,0);
            mensaje.show();*/

           //si la casilla pulsada ya está ocupada salimos del método
           if(partida.casilla_libre(casilla)==false){
               return;
           }
           //llamamos al método para marcar la casilla que se ha tocado
            marcaCasilla(casilla);

            int resultado=partida.turno();

            if(resultado>0){
                terminar_partida(resultado);
                return;
            }

            //realizamos el marcado de la casilla que elige el programa
            if (jugadores==1){
                casilla=partida.ia();

                while (partida.casilla_libre(casilla)!=true){
                    casilla=partida.ia();
                }

                marcaCasilla(casilla);

                resultado=partida.turno();

                if(resultado>0){
                    terminar_partida(resultado);
                }
            }
        }
    }

    private void terminar_partida(int res){

        if(res==1) {
            mensaje="Han ganado los círculos";
        }

        else if(res==2) {
            mensaje="Han ganado los aspas";
        }

        else  {
            mensaje="Empate";
        };


        Toast toast= Toast.makeText(this,mensaje,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();

        //terminamos el juego
        partida=null;

        //habilitamos los botones del tablero
        ((Button)findViewById(R.id.unjugador)).setEnabled(true);
        //((Button)findViewById(R.id.dosjugadores)).setEnabled(true);
        ((RadioGroup)findViewById(R.id.grupoDificultad)).setAlpha(1);
        aceptar.setVisibility(View.INVISIBLE);
        edtNombre.setVisibility(View.INVISIBLE);
        edtNombre.getText().clear();
        añadirUsuario.setVisibility(View.VISIBLE);

        guardar(res);
    }

    //metodo para marcar las casillas
    private void marcaCasilla(int casilla){
        ImageView imagen;
        imagen=(ImageView)findViewById(CASILLAS[casilla]);

        if(partida.jugador==1){
            imagen.setImageResource(R.drawable.circulo);
        }else{
            imagen.setImageResource(R.drawable.aspa);
        }

    }

    //creamos un campo de clase para almacenar cuantos jugadores hay
    private int jugadores;
    //para guardar la casilla pulsada
    private int[] CASILLAS;

    private Partida partida;

}