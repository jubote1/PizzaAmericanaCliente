package com.example.pizzaamericanacliente;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pizzaamericanacliente.Adaptador.PedidosAdapter;
import com.example.pizzaamericanacliente.Servicio.BackgroundCambioEstado;
import com.example.pizzaamericanacliente.Servicio.BackgroundDevolverEstado;
import com.example.pizzaamericanacliente.Servicio.BackgroundLlegadaDomicilios;
import com.example.pizzaamericanacliente.Servicio.BackgroundPedidos;
import com.example.pizzaamericanacliente.Servicio.BackgroundSalidaDomicilios;
import com.example.pizzaamericanacliente.modelo.Pedido;
import com.example.pizzaamericanacliente.modelo.Usuario;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;

public class Pedidos2Activity extends AppCompatActivity {


    RecyclerView recyclerView;
    LinearLayoutManager pedidosLayoutManager;
    //Importamos la clase usuario
    Usuario usuario;
    String urlServicios;
    ArrayList<Pedido> datosPedidos;
    private PedidosAdapter pedidosAdapter;
    Button btnSalida;
    Button btnLlegada;
    Button btnCambioEstado;
    Button btnDevolverEstado;
    Button btnDisponible;
    TextView txtViewDom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos2);
        recyclerView = (RecyclerView)findViewById(R.id.list_pedidos);
        btnSalida = (Button)findViewById(R.id.buttonSalida);
        btnLlegada = (Button)findViewById(R.id.buttonLlegada);
        btnCambioEstado = (Button)findViewById(R.id.buttonDisponible);
        btnDevolverEstado = (Button)findViewById(R.id.buttonDevolverEstado);
        btnDisponible = (Button)findViewById(R.id.buttonDisponible);
        txtViewDom = (TextView)findViewById(R.id.txtViewDom);
        pedidosLayoutManager =  new LinearLayoutManager(this);
        recyclerView.setLayoutManager(pedidosLayoutManager);
        //Llevamos a la variable general el usuario Logueado en la aplicación
        usuario = (Usuario)getIntent().getExtras().getSerializable("usuario");
        //Mostramos en el textView el nombre del domiciliario
        txtViewDom.setText(usuario.getNombreLargo());
        urlServicios = getIntent().getExtras().getString("urlServicios");
        //Vamos aquí a recuperar la información de los pedidos según el perfil de usuario.
        BackgroundPedidos bg = new BackgroundPedidos(this);
        //Definimos el tipo de consulta que vamos a realizar
        int tipoConsulta;
        if(usuario.getEstadoDomiciliario() == 1)
        {
            tipoConsulta = 1;
            btnCambioEstado.setEnabled(true);
            btnSalida.setEnabled(false);
            btnLlegada.setEnabled(true);
            btnDevolverEstado.setEnabled(true);
        }else
        {
            tipoConsulta = 2;
            btnCambioEstado.setEnabled(false);
            btnSalida.setEnabled(true);
            btnLlegada.setEnabled(false);
            btnDevolverEstado.setEnabled(false);
        }
        AlertDialog dialogPedidos;
        dialogPedidos = new AlertDialog.Builder(this).create();
        dialogPedidos.setTitle("CONSULTA PEDIDOS");
        String pedidosJSON = "";
        try
        {
            pedidosJSON = bg.execute(Integer.toString(usuario.getIdUsuario()), Integer.toString(tipoConsulta), urlServicios).get();
            //bg.execute(Integer.toString(usuario.getIdUsuario()), Integer.toString(tipoConsulta), urlServicios);
            //dialogPedidos.setMessage(" RESULTADO CONSULTA " + pedidosJSON);
            //dialogPedidos.show();
            JSONArray listPedidos = new JSONArray(pedidosJSON);
            //Instanciamos el arreglo de String que contendrá la información
            datosPedidos = new ArrayList<Pedido>();
            String idPedido, infoPedido, observacion;
            for(int i = 0; i < listPedidos.length(); i++)
            {
                Pedido pedTemp = new Pedido("", "", "");
                JSONObject listCadaPedido = (JSONObject) listPedidos.get(i);
                idPedido = listCadaPedido.getString("idpedido");
                infoPedido = listCadaPedido.getString("nombres") + "  " + listCadaPedido.getString("direccion") + "  Promesa: " + listCadaPedido.getString("tp") + " " + listCadaPedido.getString("tiempo");
                observacion = listCadaPedido.getString("observacionespecial");
                pedTemp = new Pedido(idPedido, infoPedido, observacion);
                datosPedidos.add(pedTemp);
            }
            //Realizamos la asignación de los datos al tableView
        }catch(Exception e)
        {
            System.out.println("Error en llamado de servicio de pedidos");
            dialogPedidos.setMessage("FALLO LA CONSULTA " + pedidosJSON);
            dialogPedidos.show();
        }
        //Definimos que el TableView es Clickable
        pedidosAdapter = new PedidosAdapter(this,datosPedidos);
        recyclerView.setAdapter(pedidosAdapter);
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        pedidosAdapter.notifyDataSetChanged();
    }

    public void llegadaDomicilios(View view)
    {
        //Se desactiva botón mientras se está realizando la acción.
        btnLlegada.setClickable(false);
        btnDisponible.setClickable(false);
        btnDevolverEstado.setClickable(false);
        BackgroundLlegadaDomicilios bg = new BackgroundLlegadaDomicilios(this);
        String strResultado= "";
        try
        {
            strResultado = bg.execute(Integer.toString(usuario.getIdUsuario()), usuario.getNombreUsuario(), urlServicios).get();
            //Se vuelve a activar el botón
            btnLlegada.setClickable(true);
            btnDisponible.setClickable(true);
            btnDevolverEstado.setClickable(true);
            JSONObject resultado = new JSONObject(strResultado);
            if(resultado.getString("resultado").equals(new String("exitoso")))
            {
                Intent intent_name = new Intent();
                intent_name.setClass(this.getApplicationContext(), MainActivity.class);
                this.startActivity(intent_name);
            }
        }catch(Exception e)
        {
            System.out.println("Error en llamado de servicio de pedidos");
            AlertDialog dialogPedidos;
            dialogPedidos = new AlertDialog.Builder(this).create();
            dialogPedidos.setTitle("ERRORES EN PROCESO");
            dialogPedidos.setMessage("FALLO LA CONSULTA " + strResultado);
            dialogPedidos.show();
        }

    }


    public void devolverEstado(View view)
    {
        btnLlegada.setClickable(false);
        btnDisponible.setClickable(false);
        btnDevolverEstado.setClickable(false);
        BackgroundDevolverEstado bg = new BackgroundDevolverEstado(this);
        String strResultado= "";
        try
        {
            //Sacaremos un JSON con todos los idPedido de los cuales deseamos dar salida
            JSONArray JSONpedidos = new JSONArray();
            JSONObject JSONpedido;
            //En este punto recorremos el arraylist con los pedidos para saber si están seleccionados o no
            for(int i = 0; i < pedidosAdapter.listPedidos.size(); i++)
            {
                Pedido pedTemp = pedidosAdapter.listPedidos.get(i);
                if(pedTemp.isSelected())
                {
                    JSONpedido = new JSONObject();
                    JSONpedido.put("idpedido",pedTemp.getIdPedido());
                    JSONpedidos.put(JSONpedido);
                }
            }
            strResultado = bg.execute(Integer.toString(usuario.getIdUsuario()), usuario.getNombreUsuario(), JSONpedidos.toString(), urlServicios).get();
            btnLlegada.setClickable(true);
            btnDisponible.setClickable(true);
            btnDevolverEstado.setClickable(true);
            JSONObject resultado = new JSONObject(strResultado);
            if(resultado.getString("resultado").equals(new String("exitoso")))
            {
                Intent intent_name = new Intent();
                intent_name.setClass(this.getApplicationContext(), MainActivity.class);
                this.startActivity(intent_name);
            }
        }catch(Exception e)
        {
            System.out.println("Error en llamado de servicio de pedidos");
            AlertDialog dialogPedidos;
            dialogPedidos = new AlertDialog.Builder(this).create();
            dialogPedidos.setTitle("ERRORES EN PROCESO");
            dialogPedidos.setMessage("FALLO LA CONSULTA " + strResultado);
            dialogPedidos.show();
        }

    }

    public void salidaDomicilios(View view)
    {
        if(pedidosAdapter.contadorSeleccionados > 2)
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("ALERTA SALIDA DOMICILIOS");
            alertDialog.setMessage("Un domiciliario NO DEBE SALIR CON MÁS DE DOS PEDIDOS");
            alertDialog.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog dialog = alertDialog.create();
            dialog.show();
            return;
        }
        btnSalida.setClickable(false);
        BackgroundSalidaDomicilios bg = new BackgroundSalidaDomicilios(this);
        String strResultado= "";
        try
        {
            //Sacaremos un JSON con todos los idPedido de los cuales deseamos dar salida
            JSONArray JSONpedidos = new JSONArray();
            JSONObject JSONpedido;
            //En este punto recorremos el arraylist con los pedidos para saber si están seleccionados o no
            for(int i = 0; i < pedidosAdapter.listPedidos.size(); i++)
            {
                Pedido pedTemp = pedidosAdapter.listPedidos.get(i);
                if(pedTemp.isSelected())
                {
                    JSONpedido = new JSONObject();
                    JSONpedido.put("idpedido",pedTemp.getIdPedido());
                    JSONpedidos.put(JSONpedido);
                }
            }
            strResultado = bg.execute(Integer.toString(usuario.getIdUsuario()), usuario.getNombreUsuario(), JSONpedidos.toString(), urlServicios).get();
            btnSalida.setClickable(true);
            JSONObject resultado = new JSONObject(strResultado);
            if(resultado.getString("resultado").equals(new String("exitoso")))
            {
                Intent intent_name = new Intent();
                intent_name.setClass(this.getApplicationContext(), MainActivity.class);
                this.startActivity(intent_name);
            }
        }catch(Exception e)
        {
            System.out.println("Error en llamado de servicio de pedidos");
            AlertDialog dialogPedidos;
            dialogPedidos = new AlertDialog.Builder(this).create();
            dialogPedidos.setTitle("ERRORES EN PROCESO");
            dialogPedidos.setMessage("FALLO LA CONSULTA " + strResultado);
            dialogPedidos.show();
        }

    }

    public void cambioEstadoDomiciliario(View View)
    {
        btnLlegada.setClickable(false);
        btnDisponible.setClickable(false);
        btnDevolverEstado.setClickable(false);
        BackgroundCambioEstado bg = new BackgroundCambioEstado(this);
        String strResultado= "";
        try
        {
            strResultado = bg.execute(Integer.toString(usuario.getIdUsuario()), urlServicios).get();
            btnLlegada.setClickable(true);
            btnDisponible.setClickable(true);
            btnDevolverEstado.setClickable(true);
            JSONObject resultado = new JSONObject(strResultado);
            if(resultado.getString("resultado").equals(new String("exitoso")))
            {
                Intent intent_name = new Intent();
                //Pasamos como parámetro serializado el objeto Usuario con la información del usuario logueado
                usuario.setEstadoDomiciliario(0);
                intent_name.putExtra("usuario", usuario);
                intent_name.putExtra("urlServicios", urlServicios);
                intent_name.setClass(this, Pedidos2Activity.class);
                this.startActivity(intent_name);
            }
        }catch(Exception e) {
            System.out.println("Error en llamado de servicio de pedidos");
            AlertDialog dialogPedidos;
            dialogPedidos = new AlertDialog.Builder(this).create();
            dialogPedidos.setTitle("ERRORES EN PROCESO");
            dialogPedidos.setMessage("FALLO LA CONSULTA " + strResultado);
            dialogPedidos.show();
        }
    }

    public void salir(View View)
    {
        Intent intent_name = new Intent();
        intent_name.setClass(this.getApplicationContext(), MainActivity.class);
        this.startActivity(intent_name);
    }


}
