package com.example.pizzaamericanacliente.modelo;

public class Pedido {

    private String idPedido;
    private String infoPedido;
    private boolean isSelected = false;

    public String getIdPedido() {
        return idPedido;
    }

    public String getInfoPedido() {
        return infoPedido;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public void setInfoPedido(String infoPedido) {
        this.infoPedido = infoPedido;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Pedido(String idPedido, String infoPedido) {
        this.idPedido = idPedido;
        this.infoPedido = infoPedido;
    }
}
