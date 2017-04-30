package com.example.diego.appgalileo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DireccionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direccion);


        Button btnDir = (Button) findViewById(R.id.btnGuardar);

        btnDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DireccionActivity.this,MainActivity.class);

                startActivity(intent);
            }
        });
    }
}
