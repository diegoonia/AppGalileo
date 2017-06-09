package com.example.diego.appgalileo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;


//CLASE PARA RECOJER EL CODIGO EN BRUTO DEL JSON
public class JSONParser {

   static String response = null;
   public final static int GET = 1;
   public final static int POST = 2;
   //CONSTRUCTOR
   public JSONParser() {
   }
   
   public String makeServiceCall(String url, int method) {
       return this.makeServiceCall(url, method, null);
   }
   //METODO PARA ESTABLECER CONEXI�N
   public String makeServiceCall(String url, int method,List<NameValuePair> params) {
       try {
           //HTTP CLIENT
           DefaultHttpClient httpClient = new DefaultHttpClient();
           HttpEntity httpEntity = null;
           HttpResponse httpResponse = null;
            
           // A�ADIMOS PARAMETROS AL METODO POST
           if (method == POST) {
               HttpPost httpPost = new HttpPost(url);
               // adding post params
               if (params != null) {
                   httpPost.setEntity(new UrlEncodedFormEntity(params));
               }

               httpResponse = httpClient.execute(httpPost);

           } else if (method == GET) {
               // A�ADIMOS PARAMETROS AL METODO GET
               if (params != null) {
                   String paramString = URLEncodedUtils.format(params, "utf-8");
                   url += "?" + paramString;
               }
               //METODO GET
               HttpGet httpGet = new HttpGet(url);
               httpResponse = httpClient.execute(httpGet);

           }
           httpEntity = httpResponse.getEntity();
           response = EntityUtils.toString(httpEntity);
           //EXCEPCIONES
       } catch (UnsupportedEncodingException e) {
           e.printStackTrace();
       } catch (ClientProtocolException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       }
       //DEVOLVEMOS RESPUESTA
        return response;

   }
}