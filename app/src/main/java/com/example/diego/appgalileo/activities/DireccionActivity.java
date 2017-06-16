package com.example.diego.appgalileo.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diego.appgalileo.R;

import java.io.FileOutputStream;

public class DireccionActivity extends AppCompatActivity {

    Button btnDir;
    EditText txtPuerto, txtHost;
    String fileName = "config.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_direccion);

        btnDir = (Button) findViewById(R.id.btnGuardar);
        txtPuerto = (EditText) findViewById(R.id.etPuerto);
        txtHost = (EditText) findViewById(R.id.etHost);

        btnDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFile(fileName, "http://" + txtHost.getText().toString() + ":" + txtPuerto.getText().toString());
                Intent intent = new Intent(DireccionActivity.this,MainActivity.class);

                startActivity(intent);
            }
        });
    }

    public void saveFile(String file, String text)
    {
        try{
            FileOutputStream fos = openFileOutput(file, Context.MODE_PRIVATE);
            fos.write(text.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(DireccionActivity.this, "Error saving file",Toast.LENGTH_SHORT).show();
        }
    }



}
