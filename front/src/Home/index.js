import React, { useState } from "react";
import {
  Box,
  Button,
  Card,
  CardActions,
  CardContent,
  Divider,
  Grid2 as Grid,
  List,
  ListItem,
  ListItemText,
  Typography,
  Alert,
  IconButton,
  Badge,
  AppBar,
  Toolbar,
  Container,
  TextField,
} from "@mui/material";

import Image from "../Assets/logo.png";
import Lapis from "../Assets/Produtos/lapis.jpg";
import Caneta from "../Assets/Produtos/caneta.png";
import Rapadura from "../Assets/Produtos/rapadura.jpg";
import Notebook from "../Assets/Produtos/notebook.webp";

import AddIcon from "@mui/icons-material/Add";
import RemoveIcon from "@mui/icons-material/Remove";
import CloseIcon from "@mui/icons-material/Close";

import { ReadOnlyCards } from "../Componentes/Lista/Cards";
import { PaperList } from "../Componentes/Lista/Paper";
import Modal from "../Componentes/Modal";
import axios from "axios";

const products = [
  {
    id: 1,
    name: "Notebook",
    price: 3499.9,
    imagem: Notebook,
    titulo: "Notebook",
    estoque: 18,
  },
  {
    id: 2,
    name: "Lapis",
    price: 0.19,
    imagem: Lapis,
    titulo: "Lápis",
    estoque: 100,
  },
  {
    id: 3,
    name: "Caneta",
    price: 0.39,
    imagem: Caneta,
    titulo: "Caneta",
    estoque: 200,
  },
  {
    id: 4,
    name: "Rapadura",
    price: 2.19,
    imagem: Rapadura,
    titulo: "Rapadura",
    estoque: 1,
  },
];

const Ecommerce = ({ alertCustom }) => {
  const [cart, setCart] = useState([]);
  const [open, setOpen] = useState(false);
  const [orderConfirmed, setOrderConfirmed] = useState(false);

  const checkStock = (productId, quantityToAdd = 0) => {
    const product = products.find((p) => p.id === productId);
    const cartItem = cart.find((item) => item.id === productId);
    const currentQuantity = cartItem ? cartItem.quantity : 0;
    return product.estoque >= currentQuantity + quantityToAdd;
  };

  const addToCart = (product) => {
    if (!checkStock(product.id, 1)) {
      alertCustom(`Estoque insuficiente para ${product.name}`);
      return;
    }

    const existingItem = cart.find((item) => item.id === product.id);
    if (existingItem) {
      setCart(
        cart.map((item) =>
          item.id === product.id
            ? { ...item, quantity: item.quantity + 1 }
            : item
        )
      );
    } else {
      setCart([...cart, { ...product, quantity: 1 }]);
    }
  };

  const removeFromCart = (productId) => {
    setCart(cart.filter((item) => item.id !== productId));
  };

  const decreaseQuantity = (productId) => {
    const existingItem = cart.find((item) => item.id === productId);
    if (existingItem.quantity > 1) {
      setCart(
        cart.map((item) =>
          item.id === productId
            ? { ...item, quantity: item.quantity - 1 }
            : item
        )
      );
    } else {
      removeFromCart(productId);
    }
  };

  const increaseQuantity = (productId) => {
    if (!checkStock(productId, 1)) {
      const product = products.find((p) => p.id === productId);
      alertCustom(`Estoque insuficiente para ${product.name}`);
      return;
    }

    setCart(
      cart.map((item) =>
        item.id === productId ? { ...item, quantity: item.quantity + 1 } : item
      )
    );
  };

  const calculateSubtotal = () => {
    return cart
      .reduce((total, item) => total + item.price * item.quantity, 0)
      .toFixed(2);
  };

  const totalItems = () => {
    return cart.reduce((total, item) => total + item.quantity, 0);
  };

  const handleCheckout = async () => {
    try {
      // Verificar estoque antes de finalizar
      const hasInsufficientStock = cart.some((item) => {
        const product = products.find((p) => p.id === item.id);
        return item.quantity > product.estoque;
      });

      if (hasInsufficientStock) {
        alertCustom("Alguns itens no carrinho excedem o estoque disponível");
        return;
      }

      setOrderConfirmed(true);
      const dadosToSend = {};
      cart.forEach(({ id }) => {
        dadosToSend[id] = cart.find((item) => item.id === id).quantity;
      });

      await axios.post("http://localhost:8079/orders", {
        produtos: dadosToSend,
      });
      setCart([]);
      setOpen(true);
    } catch (error) {
      alertCustom("Erro ao processar o pedido. Tente novamente.");
    } finally {
      setOrderConfirmed(false);
    }
  };

  const handleClose = () => {
    setOpen(false);
  };

  return (
    <Container maxWidth="lg" sx={{ mt: 10 }}>
      <Grid container spacing={2} sx={{ p: 2 }}>
        {/* Seção de Produtos */}
        <Grid size={{ xs: 12, md: 8 }}>
          <Typography variant="h6" gutterBottom>
            Produtos
          </Typography>
          <ReadOnlyCards
            onClick={(product) => addToCart(product)}
            items={products}
            keys={[
              { label: "", value: "name" },
              {
                label: "",
                value: "price",
                format: (value) => `R$ ${value.toFixed(2)}`,
              },
              {
                label: "Restam",
                value: "estoque",
                format: (value) => `${value} unidades`,
              },
            ]}
            aspectRatio="4/3"
            imageHeight="170px"
          />
        </Grid>

        {/* Carrinho de Compras */}
        <Grid size={{ xs: 12, md: 4 }}>
          <PaperList
            items={[
              {
                titulo: "Itens no Carrinho",
                subtitulo: `${totalItems()} ${
                  totalItems() === 1 ? "item" : "itens"
                }`,
              },
              { titulo: "Subtotal", subtitulo: `R$ ${calculateSubtotal()}` },
            ]}
          >
            <Typography variant="h6" sx={{ m: "5px 10px" }}>
              Resumo
            </Typography>
          </PaperList>

          {cart.length === 0 ? (
            <Typography variant="body1" sx={{ textAlign: "center", m: 3 }}>
              Seu carrinho está vazio
            </Typography>
          ) : (
            <>
              <List>
                {cart.map((item) => {
                  const product = products.find((p) => p.id === item.id);
                  const stockExceeded = item.quantity > product.estoque;

                  return (
                    <ListItem
                      key={item.id}
                      secondaryAction={
                        <IconButton
                          edge="end"
                          onClick={() => removeFromCart(item.id)}
                        >
                          <CloseIcon />
                        </IconButton>
                      }
                    >
                      <ListItemText
                        primary={item.name}
                        secondary={
                          <>
                            <span>
                              R$ {(item.price * item.quantity).toFixed(2)}
                            </span>
                          </>
                        }
                      />
                      <Box
                        sx={{ display: "flex", alignItems: "center", ml: 2 }}
                      >
                        <IconButton
                          size="small"
                          onClick={() => decreaseQuantity(item.id)}
                          disabled={item.quantity <= 1}
                        >
                          <RemoveIcon fontSize="small" />
                        </IconButton>
                        <TextField
                          value={item.quantity}
                          size="small"
                          sx={{
                            width: "60px",
                            "& .MuiInputBase-input": {
                              textAlign: "center",
                              py: 0.5,
                            },
                          }}
                          inputProps={{
                            min: 1,
                            style: { padding: "6px" },
                          }}
                          disabled
                        />
                        <IconButton
                          size="small"
                          onClick={() => increaseQuantity(item.id)}
                          disabled={item.quantity >= product.estoque}
                        >
                          <AddIcon fontSize="small" />
                        </IconButton>
                      </Box>
                    </ListItem>
                  );
                })}
              </List>

              <Divider sx={{ my: 2 }} />

              <Box sx={{ display: "flex", justifyContent: "space-between" }}>
                <Typography variant="h6">Subtotal:</Typography>
                <Typography variant="h6">R$ {calculateSubtotal()}</Typography>
              </Box>

              <Button
                variant="contained"
                color="primary"
                fullWidth
                sx={{ mt: 2 }}
                onClick={handleCheckout}
                disabled={
                  orderConfirmed ||
                  cart.some((item) => {
                    const product = products.find((p) => p.id === item.id);
                    return item.quantity > product.estoque;
                  })
                }
              >
                {orderConfirmed ? "Pedido Confirmado!" : "Finalizar Compra"}
              </Button>
            </>
          )}
        </Grid>
      </Grid>

      <Modal
        open={open}
        onClose={handleClose}
        maxWidth="xs"
        titulo={"Pedido Confirmado"}
        component="modal"
      >
        <Typography variant="body1" gutterBottom sx={{ m: 2 }}>
          Pedido realizado com sucesso! Você receberá uma confirmação por
          e-mail.
        </Typography>
      </Modal>
    </Container>
  );
};

export default Ecommerce;
