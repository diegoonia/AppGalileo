
package com.example.diego.appgalileo;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String fileName = "config.txt";
    Button btnDir, btnEncenderLed, btnApagarLed;
    TextView tvDir, tvLEDGalileoEstado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDir = (Button) findViewById(R.id.btnDireccion);
        tvDir = (TextView) findViewById(R.id.tvHostPuerto);
        tvLEDGalileoEstado = (TextView) findViewById(R.id.tvLedGalileoEstado); //Poner en este tv SI o NO
        btnEncenderLed = (Button) findViewById(R.id.btnEncenderLED);
        btnApagarLed = (Button) findViewById(R.id.btnApagarLED);

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

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

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

    public class Conectividad {

        private String datos = null;
        public String GetArduino(String urlString){

            try {
                URL url = new URL(urlString);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                if(httpURLConnection.getResponseCode() == 200){
                    InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    StringBuilder stringBuilder = new StringBuilder();
                    String linea;
                    while ((linea = bufferedReader.readLine()) != null) {
                        stringBuilder.append(linea);
                    }

                    datos = stringBuilder.toString();
                    httpURLConnection.disconnect();
                }

            } catch (IOException error) {
                return null;
            }

            return datos;
        }
    }




}
