package com.example.pizzaamericanacliente.Servicio;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.example.pizzaamericanacliente.Pedidos2Activity;
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

public class BackgroundLogueo extends AsyncTask <String, Void,String> {

    AlertDialog dialog;
    Context context;
    public Boolean login = false;
    public BackgroundLogueo(Context context)
    {
        this.context = context;
    }
    String urlServicios = "";

    @Override
    protected void onPreExecute() {
        dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle("Login Status");
    }
    @Override
    protected void onPostExecute(String s) {

        int idUsuario = 0;
        String nombreUsuario;
        String contrasena;
        String nombreLargo = "";
        int idTipoEmpleado = 0;
        String tipoInicio;
        boolean administrador;
        int estadoDomiciliario;
        Usuario usuario = new Usuario(0,"", "", "", 0,"", false);
        try
        {
            JSONObject jobjt = new JSONObject(s);
            idUsuario = jobjt.getInt("idusuario");
            nombreUsuario = jobjt.getString("nombreusuario");
            nombreLargo = jobjt.getString("nombrelargo");
            idTipoEmpleado = jobjt.getInt("idtipoempleado");
            tipoInicio = jobjt.getString("tipoinicio");
            administrador = jobjt.getBoolean("administrador");
            estadoDomiciliario = jobjt.getInt("estadodomiciliario");
            usuario = new Usuario(idUsuario, nombreUsuario, "", nombreLargo, idTipoEmpleado, tipoInicio, administrador);
            usuario.setEstadoDomiciliario(estadoDomiciliario);
        }catch(Exception e)
        {
            System.out.println("ERROR PARSEANDO EL JSON " + e.toString());
        }

        if(!nombreLargo.isEmpty())
        {
            dialog.setMessage("BIENVENIDO AL SISTEMA " + nombreLargo);
            dialog.show();
            Intent intent_name = new Intent();
            //Pasamos como parámetro serializado el objeto Usuario con la información del usuario logueado
            intent_name.putExtra("usuario", usuario);
            intent_name.putExtra("urlServicios", urlServicios);
            intent_name.setClass(context.getApplicationContext(), Pedidos2Activity.class);
            context.startActivity(intent_name);
        }
        else
        {
            dialog.setMessage("USUARIO Y/O CONTRASEÑA INVÁLIDOS ");
            dialog.show();
        }
    }
    @Override
    protected String doInBackground(String... voids) {
        String result = "";
        String claveRapida = voids[0];
        urlServicios = voids[1];
        String connstr = "http://" +urlServicios+"/ProyectoTiendaAmericana/ValidarIngreso";

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
}
