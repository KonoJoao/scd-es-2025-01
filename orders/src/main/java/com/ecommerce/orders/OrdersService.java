package com.ecommerce.orders;

import com.ecommerce.orders.dto.PedidoConfirmadoDTO;
import com.ecommerce.orders.dto.PedidoRecebidoDTO;
import com.ecommerce.orders.persistance.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.lang.reflect.Array;
import java.util.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.core.env.Environment;


@Service
public class OrdersService {
    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${kafka.topic}")
    private String topico;

    public PedidoConfirmadoDTO addOrder(PedidoRecebidoDTO pedido) throws IOException {
        Map<String, String> produtos = new HashMap<>();

        pedido.getProdutos().forEach((idStr, quantidade) -> {
            Long id = Long.parseLong(idStr);
            Produto produto = produtoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + id));
            produtos.put(produto.getId(), quantidade.toString());
        });

        String orderId = UUID.randomUUID().toString();

        PedidoConfirmadoDTO pedidoConfirmado = new PedidoConfirmadoDTO(orderId, produtos);
        String messageJson = objectMapper.writeValueAsString(pedidoConfirmado);
        kafkaTemplate.send(topico, orderId, messageJson);
        return pedidoConfirmado;
    }
}
