import React, { useEffect, useState } from "react";
import { CircularProgress, Box } from "@mui/material";
import { Navigate, useNavigate } from "react-router-dom";
import PublicPage from "../../Home";

export function RouteElement({ path, alertCustom }) {
  const [pathsAllowed, setPathsAllowed] = useState(["/home"]);
  const [isLoading, setIsLoading] = useState(false); // Controle de carregamento
  const navigate = useNavigate();

  const paths = {
    "/home": <PublicPage alertCustom={alertCustom} />,
  };

  if (isLoading) {
    return (
      <Box
        display="flex"
        flexDirection="column"
        alignItems="center"
        justifyContent="center"
        height="100vh"
        sx={{ paddingTop: "50px" }}
      >
        <CircularProgress />
      </Box>
    );
  }

  const pathF = pathsAllowed.find((rota) => `/${path}`.includes(rota));
  if (!pathF) {
    return <Navigate to="/home" />;
  } else {
    return paths[pathF];
  }
}

export function Redirect() {
  return <Navigate to="/home" />;
}
