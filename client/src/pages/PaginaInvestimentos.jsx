import React, { useState } from "react";
import axios from "axios";
import HeaderComMenu from "../components/HeaderComMenu";
import {
  Box,
  Grid,
  TextField,
  Button,
  Typography,
  Paper,
  CircularProgress,
  Card,
  CardContent,
  Stack,
} from "@mui/material";
import { LineChart } from "@mui/x-charts/LineChart";

const InvestmentForm = ({ onSubmit, carregando }) => {
  const [form, setForm] = useState({ aporteInicial: "1000", aporteMensal: "500", tempoMeses: "120" });
  const handleChange = (campo) => (e) => setForm((f) => ({ ...f, [campo]: e.target.value }));
  const handleSubmit = (e) => {
    e.preventDefault();
    const dados = Object.fromEntries(Object.entries(form).map(([k, v]) => [k, Number(v)]));
    if (Object.values(dados).some((v) => Number.isNaN(v) || v < 0)) return alert("Insira números positivos");
    onSubmit(dados);
  };
  return (
    <Paper sx={{ p: 3 }} elevation={4}>
      <Typography variant="h6" mb={2} fontWeight="bold">Parâmetros da simulação</Typography>
      <form onSubmit={handleSubmit}>
        <Grid container spacing={2}>
          {[
            { label: "Aporte inicial (R$)", key: "aporteInicial" },
            { label: "Aporte mensal (R$)", key: "aporteMensal" },
            { label: "Tempo (meses)", key: "tempoMeses" },
          ].map(({ label, key }) => (
            <Grid item xs={12} md={4} key={key}>
              <TextField label={label} fullWidth required value={form[key]} onChange={handleChange(key)} type="number" inputProps={{ min: 0, step: "0.01" }} />
            </Grid>
          ))}
          <Grid item xs={12}>
            <Button variant="contained" color="primary" type="submit" disabled={carregando} fullWidth>
              {carregando ? <CircularProgress size={24} /> : "Simular"}
            </Button>
          </Grid>
        </Grid>
      </form>
    </Paper>
  );
};

const InvestmentCharts = ({ dadosPoupanca, dadosSelic }) => {
  if (!dadosPoupanca.length || !dadosSelic.length) return null;
  const maiorMes = Math.max(dadosPoupanca.at(-1).mes, dadosSelic.at(-1).mes);
  const xData = Array.from({ length: maiorMes + 1 }, (_, i) => i);
  const seriesPoup = xData.map((mes) => dadosPoupanca.find((d) => d.mes === mes)?.valor ?? null);
  const seriesSelic = xData.map((mes) => dadosSelic.find((d) => d.mes === mes)?.valor ?? null);
  return (
    <Paper sx={{ p: 3, flexGrow: 1 }} elevation={4}>
      <Typography variant="h6" mb={2} fontWeight="bold">Crescimento do patrimônio</Typography>
      <Box sx={{ width: "100%" }}>
        <LineChart
          xAxis={[{ label: "Mês", data: xData }]}
          yAxis={[{ min: 0 }]}
          series={[
            { label: "Poupança", data: seriesPoup },
            { label: "Selic", data: seriesSelic },
          ]}
          height={400}
        />
      </Box>
    </Paper>
  );
};

const SummaryBox = ({ dadosPoupanca, dadosSelic }) => {
  if (!dadosPoupanca.length || !dadosSelic.length) return null;
  const totalPoup = dadosPoupanca.at(-1)?.valor || 0;
  const totalSelic = dadosSelic.at(-1)?.valor || 0;
  return (
    <Card
      elevation={4}
      sx={{
        flexShrink: 0,
        minWidth: 260,
        flexBasis: { xs: "100%", md: 300 },
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "flex-start",
        p: 3,
        height: "100%",
        background: (theme) =>
          `linear-gradient(135deg, ${theme.palette.grey[50]} 0%, ${theme.palette.grey[100]} 100%)`,
      }}
    >
      <CardContent sx={{ p: 0 }}>
        <Typography variant="h6" fontWeight="bold" gutterBottom>
          Seu dinheiro total será:
        </Typography>
        <Typography sx={{ mb: 0.5 }}>
          • Selic:&nbsp;
          <Box component="span" fontWeight="bold">
            {totalSelic.toLocaleString("pt-BR", { style: "currency", currency: "BRL" })}
          </Box>
        </Typography>
        <Typography>
          • Poupança:&nbsp;
          <Box component="span" fontWeight="bold">
            {totalPoup.toLocaleString("pt-BR", { style: "currency", currency: "BRL" })}
          </Box>
        </Typography>
      </CardContent>
    </Card>
  );
};

function PaginaInvestimentos() {
  const [dadosPoupanca, setDadosPoupanca] = useState([]);
  const [dadosSelic, setDadosSelic] = useState([]);
  const [carregandoSimulacao, setCarregandoSimulacao] = useState(false);
  const [erro, setErro] = useState(null);

  const handleSimular = async (valores) => {
    try {
      setErro(null);
      setCarregandoSimulacao(true);
      const response = await axios.post("http://localhost:8080/api/taxas/simular", valores, 
          { headers: { "Content-Type": "application/json", Accept: "application/json" } });
      if (response.data.error) throw new Error(response.data.error);
      setDadosPoupanca(response.data.poupanca);
      setDadosSelic(response.data.selic);
    } catch (err) {
      setErro(err.response?.data?.error || "Não foi possível realizar a simulação. Tente novamente mais tarde.");
    } finally {
      setCarregandoSimulacao(false);
    }
  };

  return (
    <Box sx={{ minHeight: "100vh", backgroundColor: "#FFFFFF" }}>
      <HeaderComMenu />
      <Box sx={{ p: 4 }}>
        <Typography variant="h4" fontWeight="bold" mb={4}>Simulador de Investimentos</Typography>
        <Grid container direction="column" spacing={4}>
          <Grid item xs={12}>
            <InvestmentForm onSubmit={handleSimular} carregando={carregandoSimulacao} />
          </Grid>
          {erro && (
            <Grid item xs={12}>
              <Paper sx={{ p: 3, backgroundColor: "#fff0f0" }}><Typography color="error">{erro}</Typography></Paper>
            </Grid>
          )}
          <Grid item xs={12}>
            <Stack direction={{ xs: "column", md: "row" }} spacing={4} alignItems="stretch" sx={{ width: "100%" }}>
              <InvestmentCharts dadosPoupanca={dadosPoupanca} dadosSelic={dadosSelic} />
              <SummaryBox dadosPoupanca={dadosPoupanca} dadosSelic={dadosSelic} />
            </Stack>
          </Grid>
        </Grid>
      </Box>
    </Box>
  );
}

export default PaginaInvestimentos;
