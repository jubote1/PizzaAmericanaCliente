package com.example.pizzaamericanacliente;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.example.pizzaamericanacliente.Servicio.BackgroundLogueo;

public class MainActivity extends AppCompatActivity {

    EditText textPassword;
    String urlServicios;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textPassword = (EditText) findViewById(R.id.editTextPassword);
        textPassword.setInputType(InputType.TYPE_NULL);
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
    }

    public void ingresarAplicacion(View view)
    {
        String claveRapida = textPassword.getText().toString();
        BackgroundLogueo bg = new BackgroundLogueo(this);
        bg.execute(claveRapida, urlServicios);
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
}
