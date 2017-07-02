package com.example.josegabriel.chatultimo;
import android.bluetooth.BluetoothHealthAppConfiguration;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout contenedor;
    FloatingActionButton Insertar;
    TextView numC;
    int Ultimo = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //contenedor = (LinearLayout) findViewById(R.id.contenedor);
        Insertar = (FloatingActionButton) findViewById(R.id.Insertar);
        numC = (TextView) findViewById(R.id.numC);

        startActivity(getIntent());

        Insertar.setOnClickListener(this);


    }

    @Override
    protected void onResume() {

        contenedor = (LinearLayout) findViewById(R.id.contenedor);
        DBAdapter db = new DBAdapter(this);
        int numeroDeContactos = 0;

        db.open();
        Cursor c = db.getAllContacts();
        if (c.moveToFirst())
        {
            do {
                numeroDeContactos++;
                if (c.getInt(0) > Ultimo) {
                    final LinearLayout lineal = new LinearLayout(this);
                    lineal.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                            LinearLayout.LayoutParams.FILL_PARENT));
                    lineal.setOrientation(LinearLayout.HORIZONTAL);

                    final Button boton = new Button(this);
                    boton.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                    boton.setAllCaps(false);
                    boton.setText(c.getString(1));
                    boton.setId(c.getInt(0));
                    boton.setBackgroundColor(0xffffffff);

                    boton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            contenedor.removeAllViewsInLayout();
                            Ultimo = 0;

                            Bundle bundle = new Bundle();
                            bundle.putInt("ID", boton.getId());

                            Intent intent= new Intent(MainActivity.this, ActivityMensajes.class);
                            intent.putExtras(bundle);

                            startActivity(intent);
                        }
                    });

                    final Button botonX = new Button(this);
                    botonX.setText("X");
                    botonX.setId(c.getInt(0));
                    botonX.setLayoutParams(new LayoutParams(90, LayoutParams.WRAP_CONTENT));
                    botonX.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            elim(botonX.getId());
                            contenedor.removeAllViewsInLayout();
                            Ultimo = 0;
                            onResume();
                        }
                    });

                    lineal.addView(botonX);
                    lineal.addView(boton);

                    contenedor.addView(lineal);

                    Ultimo = c.getInt(0);
                }
            } while (c.moveToNext());
        }
        db.close();
        numC.setText("N. de contactos "+Integer.toString(numeroDeContactos));

        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Insertar:
                onPause();
                //Creamos el Intent
                Intent intent= new Intent(MainActivity.this, ActivityNuevoContacto.class);
                //Iniciamos la nueva actividad
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    public void elim(int pos){
        DBAdapter db = new DBAdapter(this);
        db.open();
        db.deleteContact(pos);
        db.close();
    }
}