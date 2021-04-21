package com.example.pizzaamericanacliente.Adaptador;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pizzaamericanacliente.Pedidos2Activity;
import com.example.pizzaamericanacliente.R;
import com.example.pizzaamericanacliente.modelo.Pedido;

import java.util.ArrayList;

public class PedidosAdapter extends RecyclerView.Adapter<PedidosAdapter.ViewHolder> {

    private Context context;
    public ArrayList<Pedido> listPedidos;
    public int contadorSeleccionados;
    private final LayoutInflater mlayoutInflater;

    public PedidosAdapter(Context conte, ArrayList<Pedido> listPedidos) {
        context = conte;
        mlayoutInflater = LayoutInflater.from(context);
        this.listPedidos = listPedidos;
        contadorSeleccionados = 0;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = mlayoutInflater.inflate(R.layout.pedido_list, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Pedido pedTemp  = listPedidos.get(i);
        viewHolder.textIdPedido.setText(pedTemp.getIdPedido());
        viewHolder.textInfoPedido.setText(pedTemp.getInfoPedido());
        viewHolder.mCurrentPosition = i;
        viewHolder.itemView.setOnClickListener(new  View.OnClickListener()
                                               {
                                                   @Override
                                                   public void onClick(View view){
                                                       pedTemp.setSelected(!pedTemp.isSelected());
                                                       viewHolder.itemView.setBackgroundColor(pedTemp.isSelected() ? Color.YELLOW : Color.WHITE);
                                                       viewHolder.textInfoPedido.setBackgroundColor(pedTemp.isSelected() ? Color.YELLOW : Color.WHITE);
                                                       viewHolder.textIdPedido.setBackgroundColor(pedTemp.isSelected() ? Color.YELLOW : Color.WHITE);
                                                       //Controlamos si la selección deja el pedido seleccionado
                                                       if(pedTemp.isSelected())
                                                       {
                                                           contadorSeleccionados++;
                                                           //Validamos si el pedido tiene valor en el campo observación
                                                           if(!pedTemp.getObservacion().equals(new String("0")))
                                                           {
                                                               //Creamos el Alert para llamar la atención del domiciliario que va a
                                                               //llevar el pedido
                                                               AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                                                               alertDialog.setTitle("ALERTA PARA EL PEDIDO");
                                                               alertDialog.setMessage(pedTemp.getObservacion());
                                                               alertDialog.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                                                   @Override
                                                                   public void onClick(DialogInterface dialog, int which) {
                                                                       dialog.cancel();
                                                                   }
                                                               });
                                                               AlertDialog dialog = alertDialog.create();
                                                               dialog.show();
                                                           }
                                                       }
                                                       else
                                                       {
                                                           contadorSeleccionados--;
                                                       }
                                                   }
                                               }
        );
    }

    @Override
    public int getItemCount() {
        return (listPedidos.size());
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {

        public final TextView textIdPedido;
        public final TextView textInfoPedido;
        public int mCurrentPosition;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textIdPedido = (TextView) itemView.findViewById(R.id.textidPedido);
            textInfoPedido = (TextView) itemView.findViewById(R.id.textInfoPedido);
            itemView.setOnClickListener(new View.OnClickListener(){
                 @Override
                 public void onClick(View v)
                {
                    //Intent intent = new Intent(context, Pedidos2Activity.class);
                    //context.startActivity(intent);
                }
            }

            );
        }
    }
}
