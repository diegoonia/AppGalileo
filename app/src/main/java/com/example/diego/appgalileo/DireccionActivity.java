package com.example.diego.appgalileo;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class DireccionActivity extends AppCompatActivity {

    Button btnDir;
    EditText txtPuerto, txtDom;
    String fileName = "config.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direccion);

        btnDir = (Button) findViewById(R.id.btnGuardar);
        txtPuerto = (EditText) findViewById(R.id.etPuerto);
        txtDom = (EditText) findViewById(R.id.etIp);

        btnDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFile(fileName, "http://" + txtDom.getText().toString() + ":" + txtPuerto.getText().toString());
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
