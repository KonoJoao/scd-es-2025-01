import React from "react";
import { Box, IconButton, Paper, useMediaQuery, useTheme } from "@mui/material";

export const IconButtonList = ({ buttons = [] }) => {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down("sm"));

  return (
    <Paper elevation={2} sx={{ borderRadius: 2, overflow: "hidden" }}>
      <Box
        sx={{
          display: "flex",
          gap: 1,
          p: 1,
          overflowX: isMobile ? "auto" : "visible",
          flexWrap: isMobile ? "nowrap" : "wrap",
          "&::-webkit-scrollbar": { display: "none" },
          scrollbarWidth: "none",
        }}
      >
        {buttons.map((button, index) => (
          <IconButton
            key={index}
            color={button.color || "primary"}
            onClick={button.onClick}
            sx={{
              width: 72, // Button size (includes padding)
              height: 72,
              border: "1px solid",
              borderColor: "divider",
              "& svg": {
                fontSize: 42, // Icon size
              },
              "&:hover": {
                backgroundColor: "action.hover",
              },
            }}
            aria-label={button.label}
          >
            {button.icon}
          </IconButton>
        ))}
      </Box>
    </Paper>
  );
};
