import React from "react";
import { cores } from "../../themes/temas";
import { Box, Typography, Paper } from "@mui/material";
import { LineChart } from "@mui/x-charts/LineChart";

const BlocoTotalTransacao = ({ dadosPoupanca, dadosSelic }) => {
  if (!dadosPoupanca.length || !dadosSelic.length) return null;
  
  const maiorMes = Math.max(dadosPoupanca.at(-1).mes, dadosSelic.at(-1).mes);
  const xData = Array.from({ length: maiorMes + 1 }, (_, i) => i);
  const seriesPoup = xData.map((mes) => dadosPoupanca.find((d) => d.mes === mes)?.valor ?? null);
  const seriesSelic = xData.map((mes) => dadosSelic.find((d) => d.mes === mes)?.valor ?? null);
  
  return (
    <Paper sx={{ p: 3, flexGrow: 1, backgroundColor: cores.fundoBranco, border: `1px solid ${cores.cinzaClaro}` }} elevation={4}>
      <Typography variant="h5" mb={2} fontWeight="bold" color={cores.fundoEscuro}>Crescimento do patrimônio</Typography>
      <Box sx={{ width: "100%" }}>
        <LineChart
          xAxis={[{ label: "Mês", data: xData }]}
          yAxis={[{ min: 0 }]}
          series={[
            { label: "Poupança", data: seriesPoup, color: "#FF6B35" },
            { label: "Selic", data: seriesSelic, color: "#1976D2" },
          ]}
          height={400}
        />
      </Box>
    </Paper>
  );
};

export default BlocoTotalTransacao;
