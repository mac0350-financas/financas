import React, { useState, useRef } from "react";
import axios from "axios";
import HeaderComMenu from "../components/HeaderComMenu";
import { cores, espacamento, bordas } from "../themes/temas";
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
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
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
    <Paper sx={{ p: 3, backgroundColor: cores.fundoBranco, border: `1px solid ${cores.cinzaClaro}` }} elevation={4}>
      <Typography variant="h6" mb={2} fontWeight="bold" color={cores.fundoEscuro}>Parâmetros da simulação</Typography>
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
            <Button 
              variant="contained" 
              type="submit" 
              disabled={carregando} 
              fullWidth
              sx={{
                backgroundColor: cores.fundoEscuro,
                color: cores.fundoBranco,
                '&:hover': {
                  backgroundColor: cores.corPrincipal,
                },
                '&:disabled': {
                  backgroundColor: cores.cinzaClaro,
                },
                borderRadius: bordas.raioPadrao,
              }}
            >
              {carregando ? <CircularProgress size={24} color="inherit" /> : "Simular"}
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
    <Paper sx={{ p: 3, flexGrow: 1, backgroundColor: cores.fundoBranco, border: `1px solid ${cores.cinzaClaro}` }} elevation={4}>
      <Typography variant="h6" mb={2} fontWeight="bold" color={cores.fundoEscuro}>Crescimento do patrimônio</Typography>
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

const SummaryBox = ({ dadosPoupanca, dadosSelic, onVerDetalhes }) => {
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

const DetailTable = ({ detalhesPoupanca, detalhesSelic }) => {
  console.log("Detalhes Poupança:", detalhesPoupanca);
  console.log("Detalhes Selic:", detalhesSelic);
  
  if (!detalhesPoupanca || !detalhesSelic) return null;

  const formatarMoeda = (valor) => 
    valor.toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
  
  const formatarPercentual = (valor) => 
    (valor * 100).toLocaleString("pt-BR", { minimumFractionDigits: 2, maximumFractionDigits: 2 }) + "%";

  const rows = [
    { 
      label: "Valor bruto acumulado", 
      selic: formatarMoeda(detalhesSelic.valorBruto),
      poupanca: formatarMoeda(detalhesPoupanca.valorBruto)
    },
    { 
      label: "Rentabilidade bruta", 
      selic: formatarPercentual(detalhesSelic.rentabilidadeBruta),
      poupanca: formatarPercentual(detalhesPoupanca.rentabilidadeBruta)
    },
    { 
      label: "Valor pago em IR", 
      selic: formatarMoeda(detalhesSelic.valorIR),
      poupanca: formatarMoeda(detalhesPoupanca.valorIR)
    },
    { 
      label: "Valor líquido acumulado", 
      selic: formatarMoeda(detalhesSelic.valorLiquido),
      poupanca: formatarMoeda(detalhesPoupanca.valorLiquido)
    },
    { 
      label: "Rentabilidade líquida", 
      selic: formatarPercentual(detalhesSelic.rentabilidadeLiquida),
      poupanca: formatarPercentual(detalhesPoupanca.rentabilidadeLiquida)
    },
    { 
      label: "Ganho líquido", 
      selic: formatarMoeda(detalhesSelic.ganhoLiquido),
      poupanca: formatarMoeda(detalhesPoupanca.ganhoLiquido),
      highlight: true 
    },
  ];

  return (
    <Paper elevation={4} sx={{ backgroundColor: cores.fundoBranco, border: `1px solid ${cores.cinzaClaro}` }}>
      <TableContainer>
        <Table>
          <TableHead>
            <TableRow sx={{ backgroundColor: cores.fundoEscuro }}>
              <TableCell sx={{ color: cores.fundoBranco, fontWeight: "bold", py: 2 }}>
                <Typography variant="subtitle1" fontWeight="bold"></Typography>
              </TableCell>
              <TableCell align="center" sx={{ color: cores.fundoBranco, fontWeight: "bold", py: 2 }}>
                <Typography variant="subtitle1" fontWeight="bold">Tesouro Selic</Typography>
              </TableCell>
              <TableCell align="center" sx={{ color: cores.fundoBranco, fontWeight: "bold", py: 2 }}>
                <Typography variant="subtitle1" fontWeight="bold">Poupança</Typography>
              </TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {rows.map(({ label, selic, poupanca, highlight }) => (
              <TableRow 
                key={label} 
                sx={{ 
                  backgroundColor: highlight ? cores.fundoSecundario : cores.fundoBranco,
                  "&:hover": { backgroundColor: highlight ? cores.botaoHover : cores.fundoSecundario }
                }}
              >
                <TableCell sx={{ fontWeight: "medium", py: 1.5 }}>
                  <Typography variant="body2" fontWeight={highlight ? "bold" : "medium"} color={cores.fundoEscuro}>
                    {label}
                  </Typography>
                </TableCell>
                <TableCell align="center" sx={{ py: 1.5 }}>
                  <Typography variant="body2" fontWeight={highlight ? "bold" : "normal"} color={cores.fundoEscuro}>
                    {selic}
                  </Typography>
                </TableCell>
                <TableCell align="center" sx={{ py: 1.5 }}>
                  <Typography variant="body2" fontWeight={highlight ? "bold" : "normal"} color={cores.fundoEscuro}>
                    {poupanca}
                  </Typography>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Paper>
  );
};

function PaginaInvestimentos() {
  const [dadosPoupanca, setDadosPoupanca] = useState([]);
  const [dadosSelic, setDadosSelic] = useState([]);
  const [detalhesPoupanca, setDetalhesPoupanca] = useState(null);
  const [detalhesSelic, setDetalhesSelic] = useState(null);
  const [carregandoSimulacao, setCarregandoSimulacao] = useState(false);
  const [erro, setErro] = useState(null);
  
  const detailTableRef = useRef(null);

  const scrollToDetails = () => {
    detailTableRef.current?.scrollIntoView({
      behavior: "smooth",
      block: "start"
    });
  };

  const handleSimular = async (valores) => {
    try {
      setErro(null);
      setCarregandoSimulacao(true);
      const response = await axios.post("http://localhost:8080/api/taxas/simular", valores, 
          { headers: { "Content-Type": "application/json", Accept: "application/json" } });
      
      console.log("Resposta completa:", response.data);
      
      if (response.data.error) throw new Error(response.data.error);
      
      setDadosPoupanca(response.data.poupanca);
      setDadosSelic(response.data.selic);
      setDetalhesPoupanca(response.data.detalhesPoupanca);
      setDetalhesSelic(response.data.detalhesSelic);
    } catch (err) {
      console.error("Erro completo:", err);
      setErro(err.response?.data?.error || "Não foi possível realizar a simulação. Tente novamente mais tarde.");
    } finally {
      setCarregandoSimulacao(false);
    }
  };

  return (
    <Box sx={{ minHeight: "100vh", backgroundColor: cores.fundoBranco }}>
      <HeaderComMenu />
      <Box sx={{ p: espacamento.paddingInterno }}>
        <Typography variant="h4" fontWeight="bold" mb={4} color={cores.fundoEscuro}>Simulador de Investimentos</Typography>
        <Grid container direction="column" spacing={4}>
          <Grid item xs={12}>
            <InvestmentForm onSubmit={handleSimular} carregando={carregandoSimulacao} />
          </Grid>
          {erro && (
            <Grid item xs={12}>
              <Paper sx={{ p: 3, backgroundColor: "#fff0f0", border: "1px solid #ffcdd2" }}>
                <Typography color="error">{erro}</Typography>
              </Paper>
            </Grid>
          )}
          <Grid item xs={12}>
            <Stack direction={{ xs: "column", md: "row" }} spacing={4} alignItems="stretch" sx={{ width: "100%" }}>
              <InvestmentCharts dadosPoupanca={dadosPoupanca} dadosSelic={dadosSelic} />
              <SummaryBox dadosPoupanca={dadosPoupanca} dadosSelic={dadosSelic} onVerDetalhes={scrollToDetails} />
            </Stack>
          </Grid>
          <Grid item xs={12} ref={detailTableRef}>
            <DetailTable detalhesPoupanca={detalhesPoupanca} detalhesSelic={detalhesSelic} />
          </Grid>
        </Grid>
      </Box>
    </Box>
  );
}

export default PaginaInvestimentos;
