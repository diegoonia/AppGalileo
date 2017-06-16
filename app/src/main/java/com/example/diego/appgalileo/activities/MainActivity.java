package com.example.diego.appgalileo.activities;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diego.appgalileo.Conectividad;
import com.example.diego.appgalileo.JSONParser;
import com.example.diego.appgalileo.R;
import com.example.diego.appgalileo.Sensores;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements SensorEventListener {

    String comandoVoz = "";
    String fileName = "config.txt";
    Button btnDir, btnTemp;
    TextView tvDir, tvMic, tvTempIdeal, tvTempReal;
    ImageButton imgBtnMic;
    private ListView listaa;
    Handler handler;
    String URL = "http://jlado.ddns.net:8080/modo=json";
    Sensores sensoresItem;
    ImageView imgHombrecito;
    int tempRealAnt, tempIdealAnt;
    long last_update = 0, last_movement = 0;
    float prevX = 0, prevY = 0, prevZ = 0;
    float curX = 0, curY = 0, curZ = 0;

    Activity a;
    Context context;
    JSONArray sensor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        a=this;
        context=getApplicationContext();
        sensoresItem=new Sensores();

        btnDir = (Button) findViewById(R.id.btnDireccion);
        btnTemp = (Button) findViewById(R.id.btnTemp);
        tvDir = (TextView) findViewById(R.id.tvHostPuerto);
        tvMic = (TextView) findViewById(R.id.tvMicrofono);
        tvTempIdeal = (TextView) findViewById(R.id.tvTempIdeal);
        tvTempReal = (TextView) findViewById(R.id.tvTempReal);
        imgBtnMic = (ImageButton) findViewById(R.id.imgBtnMicrofono);
        imgHombrecito = (ImageView) findViewById(R.id.imgHombrecito);

        try{
            tvDir.setText(readFile(fileName));
        } catch (Exception e) {}


        btnDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DireccionActivity.class);
                startActivity(intent);
            }
        });

        btnTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TemperaturaActivity.class);
                intent.putExtra("tempIdeal", sensoresItem.getTemperaturaIdeal());
                startActivity(intent);
            }
        });

        handler = new Handler();

        Runnable refresh = new Runnable()
        {
            @Override
            public void run()
            {
                new GetContacts().execute();
                //Se ejecuta cada 1 segundo el codigo del metodo run de la interfaz Runnable refresh
                handler.postDelayed(this, 1000);
            }
        };
        //Se ejecuta una vez el codigo del metodo run de la interfaz Runnable refresh
        handler.postDelayed(refresh, 1000);


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

    // LOGICA DE MICROFONO *********************************************************************
    public void onActivityResult(int requestCode, int resultCode, Intent datos) {
        tvMic = (TextView) findViewById(R.id.tvMicrofono);
        if(resultCode == Activity.RESULT_OK && datos != null) {
            ArrayList<String> text = datos.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            comandoVoz = "";
            for(int i=0; i<text.size(); i++)
            {
                comandoVoz += text.get(i);
            }

            tvMic.setText(comandoVoz.toUpperCase());
            //ACA HABRIA QUE CAMBIARLOS Y QUE SEAN "PRENDER VENTILADOR, PRENDER ESTUFA, ETC"
            if(comandoVoz.toUpperCase().equals("ENCENDER LED")) {
                solicitud("cmd=encenderLedGalileo");
            }

            if(comandoVoz.toUpperCase().equals("APAGAR LED")) {
                solicitud("cmd=apagarLedGalileo");
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
        String url = "http://192.168.1.101:8080" + "/modo=&" + comando; // http://192.168.4.104:8080

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

    // LOGICA DE JSON *********************************************************************
    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // CREAMOS LA INSTANCIA DE LA CLASE
            JSONParser sh = new JSONParser();

            String jsonStr = sh.makeServiceCall(URL, JSONParser.GET);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    sensor = jsonObj.getJSONArray("galileo");

                    // looping through All Equipos
                    for (int i = 0; i < sensor.length(); i++) {
                        JSONObject c = sensor.getJSONObject(i);

                        //Boolean aLedGalileo = c.getBoolean("aLedGalileo");
                        Boolean aLedMin = c.getBoolean("aLedMin");
                        Boolean aLedMax = c.getBoolean("aLedMax");
                        Boolean aMotorMin = c.getBoolean("aMotorMin");
                        Boolean aMotorMax = c.getBoolean("aMotorMax");
                        String aPantalla = c.getString("aPantalla");
                        //int sPotenciometro = c.getInt("sPotenciometro");
                        //Boolean sPresencia = c.getBoolean("sPresencia");
                        //int temp = c.getInt("sTemperatura");

                        // SEPARO LAS TEMPERATURAS DE LA PANTALLA EN DOS STRING
                        String[] separated = aPantalla.split(":");
                        String temperaturaIdeal = separated[0]; //
                        String temperaturaReal = separated[1];

                        //e.setaLedGalileo(aLedGalileo);
                        sensoresItem.setaLedMin(aLedMin);
                        sensoresItem.setaLedMax(aLedMax);
                        sensoresItem.setaMotorMin(aMotorMin);
                        sensoresItem.setaMotorMax(aMotorMax);
                        sensoresItem.setaPantalla(aPantalla);
                        //e.setsPotenciometro(sPotenciometro);
                        //e.setsPresencia(sPresencia);
                        //e.setsTemperatura(temp);
                        sensoresItem.setTemperaturaIdeal(temperaturaIdeal);
                        sensoresItem.setTemperaturaReal(temperaturaReal);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Esta habiendo problemas para cargar el JSON");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            tvTempIdeal.setText("TEMPERATURA IDEAL :      "+ sensoresItem.getTemperaturaIdeal());
            tvTempReal.setText ("TEMPERATURA REAL :       "+ sensoresItem.getTemperaturaReal());

            int tempIdeal = Integer.parseInt(sensoresItem.getTemperaturaIdeal());
            int tempReal = Integer.parseInt(sensoresItem.getTemperaturaReal());

            // PARA QUE NO REFRESHEE LA IMAGEN TOD EL TIEMPO, SOLO LO HACE CUANDO HAY CAMBIOS DE TEMPERATURA
            if ( tempIdealAnt != tempIdeal || tempRealAnt != tempRealAnt )
            {
                tempIdealAnt = tempIdeal;
                tempRealAnt = tempReal;

                if ( tempReal >= tempIdeal+1 ) {
                    imgHombrecito.setImageResource(R.drawable.hombre_calor2);
                } else {
                    if ( tempReal > tempIdeal+3 ) {
                        imgHombrecito.setImageResource(R.drawable.hombre_calor1);
                    } else {
                        if ( tempReal < tempIdeal-3 ) {
                            imgHombrecito.setImageResource(R.drawable.hombre_frio2);
                        } else {
                            if ( tempReal <= tempIdeal-1 ) {
                                imgHombrecito.setImageResource(R.drawable.hombre_frio1);
                            } else {
                                imgHombrecito.setImageResource(R.drawable.hombre_ok);
                            }
                        }
                    }
                }
            }
        }
    }

    //CUANDO SACUDO EL CELULAR *********************************************************************

    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {
            long current_time = event.timestamp;

            curX = event.values[0];
            curY = event.values[1];
            curZ = event.values[2];

            if (prevX == 0 && prevY == 0 && prevZ == 0) {
                last_update = current_time;
                last_movement = current_time;
                prevX = curX;
                prevY = curY;
                prevZ = curZ;
            }

            long time_difference = current_time - last_update;
            if(time_difference > 0) {
                float movement = Math.abs((curX + curY + curZ) -
                        (prevX - prevY - prevZ)) / time_difference;
                int limit = 2000;
                float min_movement = 3E-6f;
                if (movement > min_movement) {
                    if (current_time - last_movement >= limit) {
                        Toast.makeText(getApplicationContext()
                                , "Hay movimiento de " + movement, Toast.LENGTH_SHORT).show();
                        /*
                        if(estadoVentana == 0) {
                            solicitud("?abrirVentana");
                            estadoVentana = 1;
                        } else {
                            solicitud("?cerrarVentana");
                            estadoVentana = 0;
                        }
                        */

                    }
                    last_movement = current_time;
                }
                prevX = curX;
                prevY = curY;
                prevZ = curZ;
                last_update = current_time;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if(sensors.size() > 0) {
            sensorManager.registerListener(this, sensors.get(0), SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    protected void  onStop() {
        super.onStop();
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }
}