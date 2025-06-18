package com.ecommerce.orders;

import com.ecommerce.orders.dto.PedidoConfirmadoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@Controller
@RequestMapping("/orders")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    @PostMapping
    public ResponseEntity<PedidoConfirmadoDTO> addOrder(@RequestBody ArrayList<String> orderedItemId) throws IOException {
        return ResponseEntity.ok(ordersService.addOrder(orderedItemId));
    }
}
