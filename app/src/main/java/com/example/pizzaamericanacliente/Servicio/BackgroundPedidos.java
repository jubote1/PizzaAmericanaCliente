package com.example.pizzaamericanacliente.Servicio;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.example.pizzaamericanacliente.PedidosActivity;
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
import java.net.URLEncoder;

/**
 * Created by faiizii on 11-Feb-18.
 */

public class BackgroundPedidos extends AsyncTask <String, Void,String> {

    AlertDialog dialog;
    Context context;
    public Boolean login = false;
    public BackgroundPedidos(Context context)
    {
        this.context = context;
    }
    String urlServicios = "";

    @Override
    protected void onPreExecute() {
        dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle("CONSULTA PEDIDOS");
    }
    @Override
    protected void onPostExecute(String s) {

       try
        {
            JSONObject jobjt = new JSONObject(s);

        }catch(Exception e)
        {
            System.out.println("ERROR PARSEANDO EL JSON " + e.toString());
        }
        //dialog.setMessage("BIENVENIDO AL SISTEMA " + s);
        //dialog.show();
    }
    @Override
    protected String doInBackground(String... voids) {
        String result = "";
        String idUsuario = voids[0];
        int tipoConsulta = Integer.parseInt(voids[1]);
        urlServicios = voids[2];
        String connstr = "http://" +urlServicios+"/ProyectoTiendaAmericana/ConsultarPedidosDomiciliario";

        try {
            URL url = new URL(connstr);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoInput(true);
            http.setDoOutput(true);

            OutputStream ops = http.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops,"UTF-8"));
            String data = URLEncoder.encode("idusuario","UTF-8")+"="+URLEncoder.encode(idUsuario,"UTF-8")+"&"+URLEncoder.encode("tipoconsulta","UTF-8")+"="+URLEncoder.encode(Integer.toString(tipoConsulta),"UTF-8");
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
