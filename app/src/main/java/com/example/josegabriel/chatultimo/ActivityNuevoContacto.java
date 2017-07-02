package com.example.josegabriel.chatultimo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ActivityNuevoContacto extends AppCompatActivity implements View.OnClickListener {

    Button btAgregar;
    EditText editNom;
    EditText editCor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_contacto);

        btAgregar = (Button) findViewById(R.id.btAgregar);
        editNom = (EditText) findViewById(R.id.editNom);
        editCor = (EditText) findViewById(R.id.editCor);

        btAgregar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btAgregar:
                DBAdapter db = new DBAdapter(this);
                db.open();
                long id = db.insertContact(editNom.getText().toString(), editCor.getText().toString());
                db.close();
                finish();
                break;

            default:
                break;
        }
    }
}