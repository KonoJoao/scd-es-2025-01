package com.ecommerce.inventory.dto;

import org.antlr.v4.runtime.misc.Interval;

import java.util.Map;

public class PedidoRecebido {
    private String idPedido;
    private Map<String, String> produtos;
    private String data;

    public PedidoRecebido(String idPedido, Map<String, String> produtos, String data) {
        this.idPedido = idPedido;
        this.produtos = produtos;
        this.data = data;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public Map<String, String> getProdutos() {
        return produtos;
    }

    public String getData() {
        return data;
    }
}
//{"idPedido":"bffcaa7e-cf1e-4f2e-ada2-2d4c34946048","produtos":{"1":"Notebook"},"data":"2025-06-20T02:43:16.617664800Z"}