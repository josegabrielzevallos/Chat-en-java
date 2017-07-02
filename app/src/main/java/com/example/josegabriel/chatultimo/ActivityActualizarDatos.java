package com.example.josegabriel.chatultimo;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ActivityActualizarDatos extends AppCompatActivity implements View.OnClickListener {

    EditText editNom;
    EditText editCor;
    Button btActualizar;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_datos);

        Bundle bundle = getIntent().getExtras();
        pos = bundle.getInt("ID");

        editNom = (EditText) findViewById(R.id.editNom);
        editCor = (EditText) findViewById(R.id.editCor);
        btActualizar = (Button) findViewById(R.id.btActualizar);

        DBAdapter db = new DBAdapter(this);
        db.open();
        Cursor c = db.getContact(pos);
        editNom.setText(c.getString(1));
        editCor.setText(c.getString(2));
        db.close();

        btActualizar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btActualizar:
                DBAdapter db = new DBAdapter(this);
                db.open();
                db.updateContact(pos, editNom.getText().toString(), editCor.getText().toString());
                db.close();
                finish();
                break;

            default:
                break;
        }
    }
}