package com.ecommerce.notification.dto;

import java.util.Map;

public class PedidoRecebidoNotificacao extends PedidoRecebido {
    private boolean valido;
    private String observacao;

    public PedidoRecebidoNotificacao(String idPedido, Map<String, String> produtos, String data, boolean valido, String observacao) {
        super(idPedido, produtos, data);
        this.valido = valido;
        this.observacao = observacao;
    }
    public void setValido(boolean valido) {
        this.valido = valido;
    }

    public boolean isValido() {
        return valido;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
