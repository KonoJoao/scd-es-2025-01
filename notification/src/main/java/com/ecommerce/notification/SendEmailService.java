package com.ecommerce.notification;

import com.ecommerce.notification.dto.PedidoRecebidoNotificacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

@Service
public class SendEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String emailFrom;
    public SendEmailService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    public void sendEmail(String toEmail, String subject, String body, String book, boolean isHtml) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(new InternetAddress(toEmail));
            helper.setFrom(new InternetAddress(emailFrom));
            helper.setSubject(subject);
            helper.setText(body, isHtml);
        } catch (MessagingException e){
            e.printStackTrace();
        }

        try {
            mailSender.send(mimeMessage);
        }
        catch (MailException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public static String gerarHtmlConfirmacaoPedido(PedidoRecebidoNotificacao pedido) {
        // Formata a data atual
        SimpleDateFormat sdf = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("pt", "BR"));
        String dataAtual = pedido.getData() != null ? pedido.getData() : sdf.format(new Date());

        // Constrói o HTML
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>")
                .append("<html lang=\"pt-BR\">")
                .append("<head>")
                .append("<meta charset=\"UTF-8\">")
                .append("<title>Confirmação de Pedido</title>")
                .append("<style>")
                .append("body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; }")
                .append("h1 { color: #2c3e50; }")
                .append(".header { background-color: #f8f9fa; padding: 15px; border-radius: 5px; margin-bottom: 20px; }")
                .append(".pedido-info { margin-bottom: 20px; }")
                .append(".tabela-produtos { width: 100%; border-collapse: collapse; margin: 20px 0; }")
                .append(".tabela-produtos th { background-color: #f8f9fa; text-align: left; padding: 10px; }")
                .append(".tabela-produtos td { padding: 10px; border-bottom: 1px solid #ddd; }")
                .append(".total { font-weight: bold; font-size: 1.1em; text-align: right; }")
                .append(".footer { margin-top: 30px; font-size: 0.9em; color: #777; }")
                .append(".status { padding: 10px; border-radius: 5px; margin-bottom: 20px; }")
                .append(".valido { background-color: #d4edda; color: #155724; }")
                .append(".invalido { background-color: #f8d7da; color: #721c24; }")
                .append("</style>")
                .append("</head>")
                .append("<body>")
                .append("<div class=\"header\">")
                .append("<h1>Confirmação de Pedido</h1>")
                .append("</div>");

        // Adiciona status do pedido
        html.append("<div class=\"status ").append(pedido.isValido() ? "valido" : "invalido").append("\">")
                .append("<p>Status do pedido: ").append(pedido.isValido() ? "VÁLIDO" : "INVÁLIDO").append("</p>");

        if (!pedido.isValido() && pedido.getObservacao() != null) {
            html.append("<p>Motivo: ").append(pedido.getObservacao()).append("</p>");
        }
        html.append("</div>");

        html.append("<div class=\"pedido-info\">")
                .append("<p>Olá, Cliente!</p>") // Você pode adicionar o nome do cliente se tiver essa informação
                .append("<p>Seu pedido <strong>#").append(pedido.getIdPedido()).append("</strong> foi processado.</p>")
                .append("<p>Abaixo estão os detalhes do seu pedido:</p>")
                .append("</div>")
                .append("<table class=\"tabela-produtos\">")
                .append("<thead>")
                .append("<tr>")
                .append("<th>ID do Produto</th>")
                .append("<th>Quantidade</th>")
                .append("</tr>")
                .append("</thead>")
                .append("<tbody>");

        // Adiciona os itens do pedido (Map<String, String> onde a chave é o ID e o valor é a quantidade)
        double totalPedido = 0;
        for (Map.Entry<String, String> item : pedido.getProdutos().entrySet()) {
            html.append("<tr>")
                    .append("<td>").append(item.getKey()).append("</td>")
                    .append("<td>").append(item.getValue()).append("</td>")
                    .append("</tr>");

            // Se você tiver acesso aos preços dos produtos, pode calcular o total aqui
            // totalPedido += preco * quantidade;
        }

        html.append("</tbody>")
                .append("</table>");

        // Se você tiver o total do pedido
        // html.append("<div class=\"total\">")
        //     .append("<p>Total do Pedido: R$ ").append(String.format("%.2f", totalPedido)).append("</p>")
        //     .append("</div>");

        html.append("<div class=\"footer\">")
                .append("<p>Data do pedido: ").append(dataAtual).append("</p>")
                .append("<p>Agradecemos pela sua compra!</p>")
                .append("<p>Equipe de Vendas</p>")
                .append("</div>")
                .append("</body>")
                .append("</html>");

        return html.toString();
    }
}