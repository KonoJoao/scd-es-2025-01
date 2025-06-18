package com.ecommerce.orders.dto;

import java.time.Instant;
import java.util.Map;

public class PedidoConfirmadoDTO {
    String idPedido;
    Map<String, String> produtos;
    Instant data;

    public PedidoConfirmadoDTO(String idPedido, Map<String, String> produtos) {
    this.idPedido = idPedido;
    this.produtos = produtos;
    this.data = Instant.now();
    }

    public String getIdPedido() {
        return idPedido;
    }

    public Map<String, String> getProdutos() {
        return produtos;
    }

    public Instant getData() {
        return data;
    }
}
