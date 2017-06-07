
package com.example.diego.appgalileo;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String comandoVoz = "";
    String fileName = "config.txt";
    Button btnDir, btnEncenderLed, btnApagarLed;
    TextView tvDir, tvLEDGalileoEstado, tvMic;
    ImageButton imgBtnMic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDir = (Button) findViewById(R.id.btnDireccion);
        tvDir = (TextView) findViewById(R.id.tvHostPuerto);
        tvLEDGalileoEstado = (TextView) findViewById(R.id.tvLedGalileoEstado); //Poner en este tv SI o NO
        tvMic = (TextView) findViewById(R.id.tvMicrofono);
        btnEncenderLed = (Button) findViewById(R.id.btnEncenderLED);
        btnApagarLed = (Button) findViewById(R.id.btnApagarLED);
        imgBtnMic = (ImageButton) findViewById(R.id.imgBtnMicrofono);

        tvDir.setText(readFile(fileName));

        btnDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DireccionActivity.class);
                startActivity(intent);
            }
        });

        btnEncenderLed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solicitud("cmd=encenderLedGalileo");
            }
        });

        btnApagarLed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solicitud("cmd=apagarLedGalileo");
            }
        });

        imgBtnMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es");
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "es");
                intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, "es");
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);

                try {
                    startActivityForResult(intent, 1);
                } catch (ActivityNotFoundException error) {
                    Toast.makeText(getApplicationContext(), "Reconocimiento de voz no soportado", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent datos) {
        tvMic = (TextView) findViewById(R.id.tvMicrofono);
        if(resultCode == Activity.RESULT_OK && datos != null) {
            ArrayList<String> text = datos.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            comandoVoz = text.get(0);
            tvMic.setText(comandoVoz.toUpperCase());

            if(comandoVoz.toUpperCase().equals("ENCENDER LED")) {
                solicitud("cmd=encenderLedGalileo");
            }

            if(comandoVoz.toUpperCase().equals("APAGAR LED")) {
                solicitud("cmd=apagarrLedGalileo");
            }

        }
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

    public void solicitud(String comando) {

        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        String url = readFile(fileName) + "/modo=&" + comando;

        if(networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask().execute(url);
        } else {
            System.out.println("No hay conexión de red disponible");
        }
    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            Conectividad conectividad = new Conectividad();
            return conectividad.GetArduino(urls[0]);
        }
        @Override
        protected void onPostExecute(String result) {
            if(result != null) {
                System.out.println(result);
            } else {
                System.out.println("No network connection available.");
            }
        }
    }




}


