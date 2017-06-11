package com.example.diego.appgalileo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class TemperaturaActivity extends AppCompatActivity {

    int i;
    TextView tvi;
    Button btnSetTemp, btnMas, btnMenos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_temperatura);

        btnSetTemp = (Button) findViewById(R.id.btnSetTemp);
        btnMas = (Button) findViewById(R.id.btnMas);
        btnMenos = (Button) findViewById(R.id.btnMenos);
        tvi = (TextView) findViewById(R.id.tvNumero);

        //ver si esto funca, es para pasar el parametro de la temperatura del MainActivity a este
        //Bundle bundle = getIntent().getExtras();
        //tvi.setText(bundle.getString("tempIdeal"));


        btnSetTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Aca hago la solicitud y le concateno eso
                //solicitud("cmd=encenderLedGalileo");
                Intent intent = new Intent(TemperaturaActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        btnMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tvValue = (String) tvi.getText();

                if (!tvValue.equals("")) {
                    i = Integer.parseInt(tvValue);

                    if( i < 30 ) {
                        i++;
                        tvi.setText(String.valueOf(i));
                    }
                }
            }
        });


        btnMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tvValue = (String) tvi.getText();

                if (!tvValue.equals("")) {
                    i = Integer.parseInt(tvValue);

                    if (i > 20) {
                        i--;
                        tvi.setText(String.valueOf(i));
                    }
                }
            }
        });


    }
}
