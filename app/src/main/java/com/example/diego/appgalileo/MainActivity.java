
package com.example.diego.appgalileo;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String fileName = "config.txt";
    Button btnDir;
    TextView tvDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDir = (Button) findViewById(R.id.btnDireccion);
        tvDir = (TextView) findViewById(R.id.tvPuertoIp);

        tvDir.setText(readFile(fileName));

        btnDir.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(MainActivity.this,DireccionActivity.class);

            startActivity(intent);
        }
        });
    }

    public String readFile(String file){
        String text = " ";

        try{
            FileInputStream fis = openFileInput(file);
            int size = fis.available();
            byte[] buffer = new byte[size];
            fis.read(buffer);
            fis.close();
            text = new String(buffer);
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
        }


        return text;
    }

}
