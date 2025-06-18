package com.ecommerce.orders.persistance;

import com.ecommerce.orders.persistance.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}