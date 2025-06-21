import React, { useState, useEffect } from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  CircularProgress,
  Typography,
  Stack,
} from "@mui/material";
import axios from "axios";
import Modal from "../../../Componentes/Modal";

const LocationModal = ({ alertCustom, address, onLocationSelected }) => {
  const [showModal, setShowModal] = useState(false);
  const [loading, setLoading] = useState({
    currentLocation: false,
    addressLocation: false,
  });

  useEffect(() => {
    setShowModal(true);
  }, []);

  const getCoordsFromAddress = async (address) => {
    try {
      const response = await axios.get(
        `https://nominatim.openstreetmap.org/search`,
        {
          params: {
            format: "json",
            q: address,
          },
        }
      );

      if (response.data?.length > 0) {
        return {
          latitude: parseFloat(response.data[0].lat),
          longitude: parseFloat(response.data[0].lon),
        };
      }
      throw new Error("Endereço não encontrado");
    } catch (err) {
      console.error("Erro na busca de coordenadas:", err);
      throw new Error("Erro ao buscar coordenadas do endereço");
    }
  };

  const handleUseCurrentLocation = () => {
    if (!navigator.geolocation) {
      alertCustom("Geolocalização não suportada");
      return;
    }

    setLoading((prev) => ({ ...prev, currentLocation: true }));
    navigator.geolocation.getCurrentPosition(
      async (position) => {
        try {
          const { latitude, longitude } = position.coords;
          onLocationSelected({
            source: "current",
            coordinates: { latitude, longitude },
          });
          setShowModal(false);
        } catch (err) {
          alertCustom(err.message);
        } finally {
          setLoading((prev) => ({ ...prev, currentLocation: false }));
        }
      },
      (err) => {
        alertCustom(err.message);
        setLoading((prev) => ({ ...prev, currentLocation: false }));
      },
      {
        enableHighAccuracy: true,
        timeout: 10000,
        maximumAge: 0,
      }
    );
  };

  const handleUseProvidedAddress = async () => {
    if (!address) {
      alertCustom("Nenhum endereço disponível");
      return;
    }

    setLoading((prev) => ({ ...prev, addressLocation: true }));

    try {
      const coordinates = await getCoordsFromAddress(address);
      onLocationSelected({
        source: "address",
        coordinates,
        address,
      });
      setShowModal(false);
    } catch (err) {
      alertCustom(err.message);
    } finally {
      setLoading((prev) => ({ ...prev, addressLocation: false }));
    }
  };

  return (
    <Modal
      open={showModal}
      onClose={() => setShowModal(false)}
      titulo=" "
      component="modal"
      maxWidth="xs"
      buttons={[
        {
          titulo: "Minha localização",
          variant: "contained",
          color: "primary",
          action: handleUseCurrentLocation,
        },
        {
          titulo: "Do trabalho",
          variant: "text",
          color: "secondary",
          action: handleUseProvidedAddress,
        },
      ]}
      buttonStyle={{
        width: { md: "47%", xs: "100%" },
        mr: { md: "1%", xs: 0 },
      }}
      loading={loading.currentLocation || loading.addressLocation}
    >
      <Stack spacing={1} sx={{ width: "100%", textAlign: "center" }}>
        <Typography variant="h6" sx={{ fontWeight: 600 }}>
          Sua localização
        </Typography>

        <Typography variant="body1" sx={{ pb: 4 }}>
          Ajude seus clientes a encontrarem você! Qual localização você deseja
          usar?
        </Typography>
      </Stack>
    </Modal>
  );
};

export default LocationModal;
