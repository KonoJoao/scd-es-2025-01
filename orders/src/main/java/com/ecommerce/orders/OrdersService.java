package com.ecommerce.orders;

import com.ecommerce.orders.dto.PedidoConfirmadoDTO;
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

    public PedidoConfirmadoDTO addOrder(ArrayList<String> orderedItemIds) throws IOException {
        Map<String, String> produtos = new HashMap<>();

        for (String itemId : orderedItemIds) {
            // Busca cada produto no banco de dados
            Produto produto = produtoRepository.findById(Long.parseLong(itemId))
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + itemId));

            produtos.put(produto.getId(), produto.getNome()); // Quantidade fixa para exemplo
        }

//        Map<String, Object> kafkaMessage = new HashMap<>();
        String orderId = UUID.randomUUID().toString();
//        kafkaMessage.put("orderId", orderId);
//        kafkaMessage.put("items", orderedItemIds);
//        kafkaMessage.put("status", "EM_ANALISE");

        PedidoConfirmadoDTO pedido = new PedidoConfirmadoDTO(orderId, produtos);
        String messageJson = objectMapper.writeValueAsString(pedido);
        kafkaTemplate.send(topico, orderId, messageJson);
        return pedido;
    }
}
