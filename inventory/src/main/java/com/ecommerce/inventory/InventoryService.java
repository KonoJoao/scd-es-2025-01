package com.ecommerce.inventory;

import com.ecommerce.inventory.dto.PedidoEnviadoNotificacao;
import com.ecommerce.inventory.dto.PedidoRecebido;
import com.ecommerce.inventory.persistance.Produto;
import com.ecommerce.inventory.persistance.ProdutoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
public class InventoryService {
    private final ObjectMapper objectMapper;
    private final ProdutoRepository produtoRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic}")
    private String topico;

    public InventoryService(ObjectMapper objectMapper, ProdutoRepository produtoRepository) {
        this.objectMapper = objectMapper;
        this.produtoRepository = produtoRepository;
    }

    @KafkaListener(
            properties = "bootstrap.servers:${bootstrap.servers}",
            topics = "${kafka.topic.consumer}",
            groupId = "${group.id}")
    public void consumeMessage(String message) throws JsonProcessingException {
        System.out.println("Mensagem recebida=========\n" + message);
        PedidoRecebido pedido = objectMapper.readValue(message, PedidoRecebido.class);
        Set<String> produtos = pedido.getProdutos().keySet();
        List<String> produtosList = produtos.stream().toList();
        Iterable<Produto> result = produtoRepository.findAllById(produtosList);

        for(Produto produto : result) {
            PedidoEnviadoNotificacao pedidoNotificao = new PedidoEnviadoNotificacao(pedido.getIdPedido(),
                    pedido.getProdutos(),pedido.getData(), true, "");
            if(produto.getQuantidade() == 0) {
                pedidoNotificao.setValido(false);
                pedidoNotificao.setObservacao("O produto " + produto.getNome() + " não possui estoque.");
            }

            produto.setQuantidade(produto.getQuantidade() - 1);
            produtoRepository.save(produto);
            kafkaTemplate.send(this.topico, produto.getId().toString(), objectMapper.writeValueAsString(pedidoNotificao));
        }
    }
}