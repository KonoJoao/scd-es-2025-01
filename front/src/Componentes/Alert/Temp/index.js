import { Snackbar } from "@mui/material";
import { isMobile } from "../../Funcoes";

const Alerta = ({ alert, setAlert }) => {
  const handleClose = () => {
    setAlert((prev) => ({ ...prev, open: false }));
  };

  return (
    <Snackbar
      anchorOrigin={{ vertical: "bottom", horizontal: "left" }}
      open={alert.open}
      message={alert.message}
      onClose={handleClose}
      autoHideDuration={5000}
      sx={{
        "& .MuiSnackbarContent-root": {
          backgroundColor: "#353535",
          color: "#fff",

          ...(isMobile ? { position: "fixed", top: "70px" } : {}),
          borderRadius: "10px",
        },
      }}
    />
  );
};

export default Alerta;
