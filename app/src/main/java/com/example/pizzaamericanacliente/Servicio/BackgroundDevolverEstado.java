package com.example.pizzaamericanacliente.Servicio;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by faiizii on 11-Feb-18.
 */

public class BackgroundDevolverEstado extends AsyncTask <String, Void,String> {

    AlertDialog dialog;
    Context context;
    public Boolean login = false;
    public BackgroundDevolverEstado(Context context)
    {
        this.context = context;
    }
    String urlServicios = "";

    @Override
    protected void onPreExecute() {

    }
    @Override
    protected void onPostExecute(String s) {

    }
    @Override
    protected String doInBackground(String... voids) {
        String result = "";
        String idUsuario = voids[0];
        String usuario = voids[1];
        String JSONidPedido = voids[2];
        urlServicios = voids[3];
        String connstr = "http://" +urlServicios+"/ProyectoTiendaAmericana/DevolverEstadoDomicilio";

        try {
            URL url = new URL(connstr);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoInput(true);
            http.setDoOutput(true);

            OutputStream ops = http.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops,"UTF-8"));
            String data = URLEncoder.encode("idusuario","UTF-8")+"="+URLEncoder.encode(idUsuario,"UTF-8")+"&"+URLEncoder.encode("usuario","UTF-8")+"="+URLEncoder.encode(usuario,"UTF-8")+"&"+URLEncoder.encode("jsonidpedido","UTF-8")+"="+URLEncoder.encode(JSONidPedido,"UTF-8");
            writer.write(data);
            writer.flush();
            writer.close();
            ops.close();

            InputStream ips = http.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(ips,"ISO-8859-1"));
            String line ="";
            while ((line = reader.readLine()) != null)
            {
                result += line;
            }
            reader.close();
            ips.close();
            http.disconnect();
            return result;

        } catch (MalformedURLException e) {
            result = "Error de URL Mal formada";
        } catch (IOException e) {
            result = e.toString();
        }


        return result;
    }
}
