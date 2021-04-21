package com.example.pizzaamericanacliente.Servicio;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.pizzaamericanacliente.MainActivity;
import com.example.pizzaamericanacliente.Pedidos2Activity;
import com.example.pizzaamericanacliente.PedidosActivity;
import com.example.pizzaamericanacliente.R;
import com.example.pizzaamericanacliente.modelo.Usuario;

import org.json.JSONObject;

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
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by faiizii on 11-Feb-18.
 */

public class BackgroundLogueo extends AsyncTask <String, Void,String> {

    AlertDialog.Builder dialog;
    Context context;
    View rootView;
    public Boolean login = false;
    public BackgroundLogueo(Context context, View rootView)
    {
        this.context = context;
        System.out.print("VALOR DEL CONTEXTO " + context);
        this.rootView = rootView;
        //progressBar = (ProgressBar) ((Activity) context).findViewById(R.id.progressBarMain);
        //progressBar = (ProgressBar)ontext.getResources().findViewById(R.id.progressBarMain);
    }
    String urlServicios = "";



    @Override
    protected void onPreExecute() {

        dialog = new AlertDialog.Builder(context);
        dialog.setTitle("ESTADO DE LOGUEO");
    }
    @Override
    protected void onPostExecute(String s) {

    }
    @Override
    protected String doInBackground(String... voids) {
        String result = "";
        String claveRapida = voids[0];
        urlServicios = voids[1];
        String connstr = "http://" +urlServicios+"/ProyectoTiendaAmericana/ValidarIngreso";
        //Validamos la conexión al servidor
        String urlPrueba = "http://" +urlServicios+"/ProyectoTiendaAmericana/Index.html";
        int timeout = 5000;
        boolean hayConexion =  isConnectedToServer(urlPrueba, timeout);
        if(!hayConexion)
        {
            return("FALSE CONEXION");
        }
        try {
            URL url = new URL(connstr);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoInput(true);
            http.setDoOutput(true);

            OutputStream ops = http.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops,"UTF-8"));
            String data = URLEncoder.encode("claverapida","UTF-8")+"="+URLEncoder.encode(claveRapida,"UTF-8");
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
            result = e.getMessage();
        } catch (IOException e) {
            result = e.getMessage();
        }


        return result;
    }

    //Método que nos ayudará a verificar la conexión y con esto saber si hay conexión o no  descargar rápido
    public boolean isConnectedToServer(String url, int timeout) {
        try{
            URL myUrl = new URL(url);
            URLConnection connection = myUrl.openConnection();
            connection.setConnectTimeout(timeout);
            connection.connect();
            return true;
        } catch (Exception e) {
            // Handle your exceptions
            return false;
        }
    }


}
