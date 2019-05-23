package com.example.pizzaamericanacliente;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.pizzaamericanacliente.Servicio.BackgroundLogueo;
import com.example.pizzaamericanacliente.Servicio.BackgroundPedidos;
import com.example.pizzaamericanacliente.modelo.Usuario;

import org.json.JSONArray;
import org.json.JSONObject;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class PedidosActivity extends AppCompatActivity {

    //Importamos la clase usuario
    Usuario usuario;
    String urlServicios;
    TableView<String[]> tb;
    //Definimos los encabezados del TableView
    String[] encabezados = {"IdPedido","Datos del Pedido"};
    String[][] datosPedidos;
    TextView tvNombreLargo;
    Button btnSalida;
    Button btnLlegada;

    @Override
    protected void onResume()
    {
        super.onResume();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        //Obtenemos el TableView donde dejaremos la información retornada de los pedidos
        tb = (TableView<String[]>)findViewById(R.id.pedidos);
        btnSalida = (Button)findViewById(R.id.buttonSalida);
        btnLlegada = (Button)findViewById(R.id.buttonLlegada);
        //Fijamos el número de columnas
        tb.setColumnCount(2);
        tb.setColumnWeight(0,1);
        tb.setColumnWeight(1,4);
        tb.setHeaderAdapter(new SimpleTableHeaderAdapter(this,encabezados));

        //Llevamos a la variable general el usuario Logueado en la aplicación
        usuario = (Usuario)getIntent().getExtras().getSerializable("usuario");
        //Desplegamos en pantalla la información del usuario
        tvNombreLargo = (TextView)findViewById(R.id.textViewNombreLargo);
        tvNombreLargo.setText(usuario.getNombreLargo());
        urlServicios = getIntent().getExtras().getString("urlServicios");
        //Vamos aquí a recuperar la información de los pedidos según el perfil de usuario.
        BackgroundPedidos bg = new BackgroundPedidos(this);
        //Definimos el tipo de consulta que vamos a realizar
        int tipoConsulta;
        if(usuario.getEstadoDomiciliario() == 1)
        {
            tipoConsulta = 1;
            btnSalida.setEnabled(false);
            btnLlegada.setEnabled(true);
        }else
        {
            tipoConsulta = 2;
            btnSalida.setEnabled(true);
            btnLlegada.setEnabled(false);
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
            datosPedidos = new String[listPedidos.length()][2];
            for(int i = 0; i < listPedidos.length(); i++)
            {
                JSONObject listCadaPedido = (JSONObject) listPedidos.get(i);
                datosPedidos[i][0] = listCadaPedido.getString("idpedido");
                datosPedidos[i][1] = listCadaPedido.getString("nombres") + "  " + listCadaPedido.getString("direccion") + "  Promesa: " + listCadaPedido.getString("tp") + " " + listCadaPedido.getString("tiempo");
            }
            //Realizamos la asignación de los datos al tableView
            tb.setDataAdapter(new SimpleTableDataAdapter(this,datosPedidos));
        }catch(Exception e)
        {
            System.out.println("Error en llamado de servicio de pedidos");
            dialogPedidos.setMessage("FALLO LA CONSULTA " + pedidosJSON);
            dialogPedidos.show();
        }
        //Definimos que el TableView es Clickable
        tb.setClickable(true);
        //Definimos los eventos para la tabla
        tb.addDataClickListener(new TableDataClickListener<String[]>() {
            @Override
            public void onDataClicked(int rowIndex, String[] clickedData) {

            }
        });

    }
}
