import React, { useEffect, useState } from "react";
import {
  AppBar,
  Toolbar,
  Typography,
  IconButton,
  Grid2 as Grid,
  Button,
  TextField,
  Avatar,
  Badge,
  Container,
} from "@mui/material";
import { useNavigate, useLocation } from "react-router-dom";
import Cookies from "js-cookie";

import Modal from "../Modal";
import { Rows } from "../Lista/Rows";
import { getLocalItem } from "../Funcoes";
import LogoImage from "../../Assets/logo.png";

import PersonAddRoundedIcon from "@mui/icons-material/PersonAddRounded";
import HomeRoundedIcon from "@mui/icons-material/HomeRounded";
import BusinessCenterIcon from "@mui/icons-material/BusinessCenter";
import LoginIcon from "@mui/icons-material/Login";
import LogoutIcon from "@mui/icons-material/Logout";
import FavoriteBorderIcon from "@mui/icons-material/FavoriteBorder";
import ShoppingCartOutlinedIcon from "@mui/icons-material/ShoppingCartOutlined";
import MenuRoundedIcon from "@mui/icons-material/MenuRounded";
import SearchIcon from "@mui/icons-material/Search";
import CloseIcon from "@mui/icons-material/Close";

const { REACT_APP_FRONT_TONSUS } = process.env;

const NavigationBar = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [menuOpen, setMenuOpen] = useState(false);
  const [modal, setModal] = useState(false);
  const [searchValue, setSearchValue] = useState("");
  const [hasScheduling, setHasScheduling] = useState(false);
  const [actions, setActions] = useState([
    {
      titulo: "Privacidade",
      action: () => navigate(`${REACT_APP_FRONT_TONSUS}/home`),
      route: "/login",
      type: "text",
      icon: <LoginIcon />,
    },
    {
      titulo: "Pedidos Recentes",
      icon: <PersonAddRoundedIcon />,
      action: () => navigate(`${REACT_APP_FRONT_TONSUS}/home`),
      type: "button",
      route: "/create",
    },

    {
      titulo: "Favoritos",
      type: "icon",
      icon: <FavoriteBorderIcon />,
      action: () => setModal(true),
    },
    {
      titulo: "Meu carrinho",
      type: "icon",
      icon: <ShoppingCartOutlinedIcon />,
      action: () => setModal(true),
    },
    {
      titulo: "Pesquisar",
      type: "icon",
      icon: <SearchIcon />,
      action: () => setModal(true),
    },
  ]);

  return (
    <>
      <AppBar
        elevation={0}
        sx={{ position: "absolute", left: 0, background: "none", mb: -1 }}
      >
        <Container maxWidth="lg">
          <Toolbar style={{ justifyContent: "space-between" }}>
            <Grid
              container
              spacing={2}
              sx={{
                display: "flex",
                justifyContent: "space-between",
                width: "100%",
                alignItems: "center",
                p: "16px 0px",
                m: "0 -4px",
              }}
            >
              <Grid item>
                <Typography
                  variant="h6"
                  sx={{
                    fontWeight: "bold",
                    cursor: "pointer",
                    display: "flex",
                    alignItems: "center",
                  }}
                  onClick={() => navigate("/home")}
                >
                  <b>
                    <img
                      src={LogoImage}
                      style={{ height: "32px", borderRadius: "5px" }}
                    />
                  </b>
                </Typography>
              </Grid>
              {actions.length > 0 && (
                <Grid item>
                  <Grid
                    container
                    spacing={1}
                    sx={{
                      alignItems: "center",
                      mr: -1,
                      display: { xs: "none", md: "flex" },
                    }}
                  >
                    {actions
                      .filter((item) => location.pathname !== item.route)
                      .map((item, index) =>
                        item.type === "icon" ? (
                          <IconButton
                            key={index}
                            onClick={item.action}
                            color="#fff"
                          >
                            {item.icon}
                          </IconButton>
                        ) : item.type === "text" ? (
                          <Typography
                            href={item.route}
                            onClick={item.action}
                            sx={{
                              m: "5px 10px",
                              color: "#fff",
                              cursor: "pointer",
                              textDecoration: "none",
                              ":hover": {
                                textDecoration: "underline !important",
                              },
                            }}
                          >
                            {item.titulo}
                          </Typography>
                        ) : (
                          <Badge
                            badgeContent={item.notification ?? null}
                            color="warning"
                            showZero={false}
                          >
                            <Button
                              key={index}
                              variant="outlined"
                              sx={{
                                borderColor: "#303030",
                                color: "#FFFFFF",
                                fontWeight: "bold",
                              }}
                              onClick={item.action}
                            >
                              {item.titulo}
                            </Button>
                          </Badge>
                        )
                      )}
                  </Grid>
                </Grid>
              )}
            </Grid>
          </Toolbar>
        </Container>
      </AppBar>
    </>
  );
};

export default NavigationBar;
