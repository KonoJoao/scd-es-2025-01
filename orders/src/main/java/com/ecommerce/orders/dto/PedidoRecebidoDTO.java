package com.ecommerce.orders.dto;

import java.util.Map;

public class PedidoRecebidoDTO {
    private Map<String, Integer> produtos;

    public PedidoRecebidoDTO(Map<String, Integer> produtos) {
        this.produtos = produtos;
    }

    public Map<String, Integer> getProdutos() {
        return produtos;
    }

    public void setProdutos(Map<String, Integer> produtos) {
        this.produtos = produtos;
    }
}
