package com.ecommerce.inventory.persistance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface ProdutoRepository extends JpaRepository<Produto, String> {
    List<Produto> findAllById(String[] ids);}