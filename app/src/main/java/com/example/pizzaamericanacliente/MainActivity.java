package com.example.pizzaamericanacliente;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.pizzaamericanacliente.Servicio.BackgroundLogueo;
import com.example.pizzaamericanacliente.modelo.Usuario;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText textPassword;
    String urlServicios;
    ProgressBar progressBarMain;
    Button ingresar;
    AlertDialog.Builder dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textPassword = (EditText) findViewById(R.id.editTextPassword);
        textPassword.setInputType(InputType.TYPE_NULL);
        ingresar = (Button) findViewById(R.id.buttonIngresar);
        dialog = new AlertDialog.Builder(this);
        dialog.setTitle("ESTADO DE LOGUEO");
    }

    public void actualizarURLServicio()
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        urlServicios = (String) pref.getString("ip_address","");
        System.out.println("valor de la url " + urlServicios);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        actualizarURLServicio();
        textPassword.setText("");
        textPassword.setInputType(InputType.TYPE_NULL);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        textPassword.setText("");
        textPassword.setEnabled(true);
        textPassword.setInputType(InputType.TYPE_NULL);
    }

    public void ingresarAplicacion(View view)
    {
        //Se desactiva el botón mientras estamos en la ejecución del servicio
        ingresar.setClickable(false);
        String claveRapida = textPassword.getText().toString();
        BackgroundLogueo bg = new BackgroundLogueo(this, view);
        setContentView(R.layout.activity_main);
        try
        {
            String respuesta = bg.execute(claveRapida, urlServicios).get();
            //Como primer acción validamos si hay problemas de conexión en cuyo caso se muestra el mensaje con el objetivo de hacerlo
            //más rápido y avisar si hay problemas y así evitar los errores.
            ingresar.setClickable(true);
            if(respuesta.equals(new String("FALSE CONEXION")))
            {
                showNetworkAlert();
                return;
            }
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
                JSONObject jobjt = new JSONObject(respuesta);
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
                System.out.println("ERROR EN EL ACCESO " + e.toString());
            }
            if(idUsuario > 0)
            {
                dialog.setMessage("BIENVENIDO AL SISTEMA " + nombreLargo);
                dialog.show();
                Intent intent_name = new Intent();
                //Pasamos como parámetro serializado el objeto Usuario con la información del usuario logueado
                intent_name.putExtra("usuario", usuario);
                intent_name.putExtra("urlServicios", urlServicios);
                intent_name.setClass(this.getApplicationContext(), Pedidos2Activity.class);
                this.startActivity(intent_name);
            }
            else if(idUsuario  == -2)
            {
                dialog.setMessage("El usuario " + nombreLargo + " debe ser ingresado antes de darse el primer inicio.")
                        .setPositiveButton("OK - REGRESAR", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                finish();
                                startActivity(getIntent());



                            }
                        });
                dialog.show();
            }else
            {
                dialog.setMessage("USUARIO Y/O CONTRASEÑA INVÁLIDOS.")
                        .setPositiveButton("OK - REGRESAR", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                finish();
                                startActivity(getIntent());
                            }
                        });
                dialog.show();
            }
        }catch(Exception e)
        {
            System.out.println("Error en llamado de servicio de pedidos");
        }


    }

    public void ingreso1(View view)
    {
        String password = textPassword.getText().toString();
        password = password + "1";
        textPassword.setText(password);
    }

    public void ingreso2(View view)
    {
        String password = textPassword.getText().toString();
        password = password + "2";
        textPassword.setText(password);
    }

    public void ingreso3(View view)
    {
        String password = textPassword.getText().toString();
        password = password + "3";
        textPassword.setText(password);
    }

    public void ingreso4(View view)
    {
        String password = textPassword.getText().toString();
        password = password + "4";
        textPassword.setText(password);
        Log.d("PULSO 4 ", password);
    }

    public void ingreso5(View view)
    {
        String password = textPassword.getText().toString();
        password = password + "5";
        textPassword.setText(password);
    }

    public void ingreso6(View view)
    {
        String password = textPassword.getText().toString();
        password = password + "6";
        textPassword.setText(password);
    }

    public void ingreso7(View view)
    {
        String password = textPassword.getText().toString();
        password = password + "7";
        textPassword.setText(password);
    }

    public void ingreso8(View view)
    {
        String password = textPassword.getText().toString();
        password = password + "8";
        textPassword.setText(password);
    }

    public void ingreso9(View view)
    {
        String password = textPassword.getText().toString();
        password = password + "9";
        textPassword.setText(password);
    }

    public void ingreso0(View view)
    {
        String password = textPassword.getText().toString();
        password = password + "0";
        textPassword.setText(password);
    }

    public void limpiar(View view)
    {
        textPassword.setText("");
    }

    public void configuracion(View view)
    {
        Intent intent = new Intent(MainActivity.this, ConfiguracionesActivity.class);
        startActivity(intent);
    }

    public void showNetworkAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Por favor revise la conexión a internet o el computador principal.")
                .setPositiveButton("OK - REGRESAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        finish();
                        startActivity(getIntent());
                    }
                });

        builder.setTitle("PROBLEMA DE CONEXIÓN");
        builder.show();

    }

}
