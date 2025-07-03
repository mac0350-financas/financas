import React from "react";
import { cores, bordas } from "../../themes/temas";
import { Box, Button, Typography, Card, CardContent } from "@mui/material";

const HeaderBotaoConta = ({ dadosPoupanca, dadosSelic, onVerDetalhes }) => {
  if (!dadosPoupanca.length || !dadosSelic.length) return null;
  
  const totalPoup = dadosPoupanca.at(-1)?.valor || 0;
  const totalSelic = dadosSelic.at(-1)?.valor || 0;
  
  return (
    <Card
      elevation={4}
      sx={{
        flexShrink: 0,
        minWidth: 260,
        flexBasis: { xs: "100%", md: 280 },
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "flex-start",
        p: 3,
        height: "100%",
        backgroundColor: cores.corPrincipal,
        border: `1px solid ${cores.cinzaClaro}`,
        borderRadius: bordas.raioPadrao,
      }}
    >
      <CardContent sx={{ p: 0, width: "100%" }}>
        <Typography variant="h6" fontWeight="bold" gutterBottom color={cores.fundoBranco}>
          Seu dinheiro total será:
        </Typography>
        <Typography sx={{ mb: 0.5, color: cores.fundoBranco }}>
          • Selic:&nbsp;
          <Box component="span" fontWeight="bold" color={cores.fundoBranco}>
            {totalSelic.toLocaleString("pt-BR", { style: "currency", currency: "BRL" })}
          </Box>
        </Typography>
        <Typography sx={{ mb: 2, color: cores.fundoBranco }}>
          • Poupança:&nbsp;
          <Box component="span" fontWeight="bold" color={cores.fundoBranco}>
            {totalPoup.toLocaleString("pt-BR", { style: "currency", currency: "BRL" })}
          </Box>
        </Typography>
        <Button
          variant="outlined"
          onClick={onVerDetalhes}
          fullWidth
          sx={{
            mt: 2,
            borderRadius: bordas.raioPadrao,
            textTransform: "none",
            fontWeight: "medium",
            borderColor: cores.fundoBranco,
            color: cores.fundoBranco,
            '&:hover': {
              borderColor: cores.cinzaClaro,
              backgroundColor: cores.botaoHover,
              color: cores.fundoEscuro,
            }
          }}
        >
          Ver detalhes
        </Button>
      </CardContent>
    </Card>
  );
};

export default HeaderBotaoConta;
