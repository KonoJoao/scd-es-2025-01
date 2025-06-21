package com.ecommerce.notification;

import com.ecommerce.notification.dto.PedidoRecebido;
import com.ecommerce.notification.dto.PedidoRecebidoNotificacao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static com.ecommerce.notification.SendEmailService.gerarHtmlConfirmacaoPedido;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final ObjectMapper objectMapper;

    private final SendEmailService sendEmailService;

    public NotificationService(ObjectMapper objectMapper, MailSender mailSender, SendEmailService sendEmailService) {
        this.objectMapper = objectMapper;
        this.sendEmailService = sendEmailService;
    }

    @KafkaListener(
            properties = "bootstrap.servers:${bootstrap.servers}",
            topics = "${kafka.topic.consumer}",
            groupId = "${group.id}")
    public void consumeMessage(String message) throws JsonProcessingException {
        PedidoRecebidoNotificacao pedido = objectMapper.readValue(message, PedidoRecebidoNotificacao.class);
        Set<String> produtos = pedido.getProdutos().keySet();

        String htmlContent = gerarHtmlConfirmacaoPedido(pedido);

// 2. Envie o e-mail com o conteúdo HTML
        sendEmailService.sendEmail("reis23@discente.ufg.br", "Pedido no Ecommerce " + pedido.getIdPedido(), htmlContent, "", true);
        log.info("Pedido do Ecommerce " + pedido.getIdPedido() + " notificado como " + (pedido.isValido() ? "valido" : "inválido"));
    }
}
