import React from "react";
import {
  Grid2 as Grid,
  Card,
  CardContent,
  CardMedia,
  Typography,
  CardActionArea,
} from "@mui/material";
import notFounImage from "../../Assets/vt.png";

export const ReadOnlyCards = ({
  items,
  keys,
  aspectRatio = "1 / 1", // Proporção padrão quadrada
  imageHeight = "60%", // Altura padrão da imagem
  onClick,
}) => {
  return (
    <Grid
      container
      spacing={2}
      sx={{ justifyContent: "start", flexWrap: "wrap" }}
    >
      {items.map((item) => (
        <Grid size={{ xs: 12, md: 4 }} key={item.id}>
          <CardActionArea
            sx={{ borderRadius: "10px !important" }}
            onClick={() => onClick && onClick(item)}
          >
            <Card
              variant="outlined"
              sx={{
                position: "relative",
                borderRadius: "10px",
                overflow: "hidden",
                width: "100%",
              }}
            >
              <CardMedia
                component="img"
                sx={{
                  width: "100%",
                  height: imageHeight,
                  objectFit: "cover",
                }}
                image={
                  item?.imagem?.includes("undefined") ||
                  item?.imagem?.includes("null")
                    ? notFounImage
                    : item.imagem
                }
                alt={item.titulo || "Imagem do item"}
              />

              <CardContent sx={{ p: 2 }}>
                {keys ? (
                  keys.map((key, index) => (
                    <Typography
                      key={`${item.id}-${index}`}
                      variant="body1"
                      sx={{ width: "100%", mb: -0.2 }}
                    >
                      {key.label && <strong>{key.label}: </strong>}
                      {key.format
                        ? key.format(item[key.value])
                        : item[key.value]}
                    </Typography>
                  ))
                ) : (
                  <Typography variant="h6" component="div">
                    {item.titulo}
                  </Typography>
                )}
              </CardContent>
            </Card>
          </CardActionArea>
        </Grid>
      ))}
    </Grid>
  );
};
