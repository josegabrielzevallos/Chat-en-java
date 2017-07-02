package com.example.josegabriel.chatultimo;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Timer;

public class ActivityMensajes extends AppCompatActivity implements View.OnClickListener {

    LinearLayout contenedor;
    Button enviar;
    Button datos;
    EditText ediMen;
    int imput;
    int Ultimo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajes);

        Bundle bundle = getIntent().getExtras();
        imput = bundle.getInt("ID");

        contenedor = (LinearLayout) findViewById(R.id.contenedor);
        enviar = (Button) findViewById(R.id.enviar);
        datos = (Button) findViewById(R.id.datos);
        ediMen = (EditText) findViewById(R.id.ediMen);

        DBAdapter db = new DBAdapter(this);

        datos.setAllCaps(false);

        enviar.setOnClickListener(this);
        datos.setOnClickListener(this);

    }

    @Override
    protected void onResume() {

        DBAdapter db = new DBAdapter(this);
        db.open();
        datos.setText("  " + db.getContact(imput).getString(1));
        Cursor c = db.getMensajes(imput);

        if (c.moveToFirst())
        {
            do {
                if (c.getInt(0)>Ultimo) {
                    final Button boton = new Button(this);
                    boton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    boton.setTextColor(Color.parseColor("#000000"));
                    boton.setAllCaps(false);
                    boton.setText(c.getString(2));
                    boton.setId(c.getInt(0));
                    boton.setBackgroundColor(0xffffffff);
                    boton.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    boton.setPadding(20,0,0,0);
                    contenedor.addView(boton);

                    registerForContextMenu(boton);
                    boton.setOnLongClickListener(new View.OnLongClickListener() {

                        @Override
                        public boolean onLongClick(View v) {
                            eliMensaje(boton.getId());
                            contenedor.removeAllViewsInLayout();
                            Ultimo = 0;
                            Toast.makeText(getApplicationContext(), "El mensaje: '"+boton.getText()+"' fue eliminado.",
                                    Toast.LENGTH_LONG).show();
                            onResume();
                            return true;
                        }
                    });

                    Ultimo = c.getInt(0);
                }
            } while (c.moveToNext());
        }
        db.close();

        super.onResume();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // Creamos el objeto que debe inflar la vista del menú en la pantalla.
        MenuInflater inflater = new MenuInflater(this);

        inflater.inflate(R.menu.menu_mensaje, menu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.enviar:
                DBAdapter db = new DBAdapter(this);
                db.open();
                db.insertMensaje(imput, ediMen.getText().toString());
                db.close();
                ediMen.setText("");
                onResume();
                break;

            case R.id.datos:
                Bundle bundle = new Bundle();
                bundle.putInt("ID", imput);

                Intent intent= new Intent(ActivityMensajes.this, ActivityActualizarDatos.class);
                intent.putExtras(bundle);

                startActivity(intent);
            default:
                break;
        }
    }

    public void eliMensaje(int pos){
        DBAdapter db = new DBAdapter(this);
        db.open();
        db.deleteMensaje(pos);
        db.close();
    }
}
