import React, { useState, useRef } from "react";
import HeaderComMenu from "../components/HeaderComMenu";
import FormularioTransacao from "../components/Investimentos/FormularioTransacao";
import BlocoTotalTransacao from "../components/Investimentos/BlocoTotalTransacao";
import HeaderBotaoConta from "../components/Investimentos/HeaderBotaoConta";
import HeaderBotaoPaginas from "../components/Investimentos/HeaderBotaoPaginas";
import { cores, espacamento } from "../themes/temas";
import {
  Box,
  Grid,
  Typography,
  Paper,
  Stack,
} from "@mui/material";

function PaginaInvestimentos() {
  const [dadosPoupanca, setDadosPoupanca] = useState([]);
  const [dadosSelic, setDadosSelic] = useState([]);
  const [detalhesPoupanca, setDetalhesPoupanca] = useState(null);
  const [detalhesSelic, setDetalhesSelic] = useState(null);
  const [carregandoSimulacao, setCarregandoSimulacao] = useState(false);
  const [erro, setErro] = useState(null);
  const [mostrarDetalhes, setMostrarDetalhes] = useState(false);
  
  const detailTableRef = useRef(null);

  const scrollToDetails = () => {
    // Only show details if simulation data exists
    if (dadosPoupanca.length > 0 || dadosSelic.length > 0) {
      setMostrarDetalhes(true);
      setTimeout(() => {
        detailTableRef.current?.scrollIntoView({
          behavior: "smooth",
          block: "start"
        });
      }, 100);
    }
  };

  const handleSimular = async (valores) => {
    try {
      setErro(null);
      setCarregandoSimulacao(true);
      setMostrarDetalhes(false); 
      
      const response = await fetch("http://localhost:8080/api/investimento/simular", {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        },
        credentials: 'include',
        body: JSON.stringify(valores)
      });
      
      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.error || errorData.message || 'Erro ao realizar simulação');
      }
      
      const data = await response.json();
      console.log("Resposta completa:", data);
      
      if (data.error) throw new Error(data.error);
      
      setDadosPoupanca(data.poupanca);
      setDadosSelic(data.selic);
      setDetalhesPoupanca(data.detalhesPoupanca);
      setDetalhesSelic(data.detalhesSelic);
    } catch (err) {
      console.error("Erro completo:", err);
      setErro(err.message || "Não foi possível realizar a simulação. Tente novamente mais tarde.");
    } finally {
      setCarregandoSimulacao(false);
    }
  };

  return (
    <Box sx={{ minHeight: "100vh", backgroundColor: cores.fundoBranco }}>
      <HeaderComMenu />
      <Box sx={{ p: espacamento.paddingInterno }}>
        <Typography variant="h3" fontWeight="bold" mb={4} color={cores.fundoEscuro}>Simulador de Investimentos</Typography>
        <Grid container direction="column" spacing={4}>
          <Grid item xs={12}>
            <FormularioTransacao onSubmit={handleSimular} carregando={carregandoSimulacao} />
          </Grid>
          {erro && (
            <Grid item xs={12}>
              <Paper sx={{ p: 3, backgroundColor: "#fff0f0", border: "1px solid #ffcdd2" }}>
                <Typography color="error" sx={{ fontSize: '1.1rem' }}>{erro}</Typography>
              </Paper>
            </Grid>
          )}
          {(dadosPoupanca.length > 0 || dadosSelic.length > 0) && (
            <Grid item xs={12}>
              <Stack direction={{ xs: "column", md: "row" }} spacing={4} alignItems="stretch" sx={{ width: "100%" }}>
                <BlocoTotalTransacao dadosPoupanca={dadosPoupanca} dadosSelic={dadosSelic} />
                <HeaderBotaoConta dadosPoupanca={dadosPoupanca} dadosSelic={dadosSelic} onVerDetalhes={scrollToDetails} />
              </Stack>
            </Grid>
          )}
          {mostrarDetalhes && (dadosPoupanca.length > 0 || dadosSelic.length > 0) && (
            <Grid item xs={12} ref={detailTableRef}>
              <HeaderBotaoPaginas detalhesPoupanca={detalhesPoupanca} detalhesSelic={detalhesSelic} />
            </Grid>
          )}
        </Grid>
      </Box>
    </Box>
  );
}

export default PaginaInvestimentos;
