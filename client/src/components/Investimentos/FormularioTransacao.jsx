import React, { useState } from "react";
import { cores, bordas } from "../../themes/temas";
import {
  Grid,
  TextField,
  Button,
  Typography,
  Paper,
  CircularProgress,
} from "@mui/material";

const FormularioTransacao = ({ onSubmit, carregando }) => {
  const [form, setForm] = useState({ aporteInicial: "1000", aporteMensal: "500", tempoMeses: "120" });
  const [erros, setErros] = useState({});
  
  const handleChange = (campo) => (e) => {
    setForm((f) => ({ ...f, [campo]: e.target.value }));
    if (erros[campo]) {
      setErros((prev) => ({ ...prev, [campo]: null }));
    }
  };
  
  const handleSubmit = (e) => {
    e.preventDefault();
    const dados = Object.fromEntries(Object.entries(form).map(([k, v]) => [k, Number(v)]));
    const novosErros = {};
    
    if (dados.aporteInicial < 1) {
      novosErros.aporteInicial = "Deve ser ≥ 1";
    }
    if (dados.aporteMensal < 1) {
      novosErros.aporteMensal = "Deve ser ≥ 1";
    }
    if (dados.tempoMeses < 1 || dados.tempoMeses > 500) {
      novosErros.tempoMeses = "Meses entre 1 e 500";
    }
    
    if (Object.keys(novosErros).length > 0) {
      setErros(novosErros);
      return;
    }
    
    setErros({});
    onSubmit(dados);
  };
  
  return (
    <Paper sx={{ p: 3, backgroundColor: cores.fundoBranco, border: `1px solid ${cores.cinzaClaro}` }} elevation={4}>
      <Typography variant="h6" mb={2} fontWeight="bold" color={cores.fundoEscuro}>Parâmetros da simulação</Typography>
      <form onSubmit={handleSubmit}>
        <Grid container spacing={2}>
          {[
            { label: "Aporte inicial (R$)", key: "aporteInicial", step: "0.01" },
            { label: "Aporte mensal (R$)", key: "aporteMensal", step: "0.01" },
            { label: "Tempo (meses)", key: "tempoMeses", step: "1" },
          ].map(({ label, key, step }) => (
            <Grid item xs={12} md={4} key={key}>
              <TextField 
                label={label} 
                fullWidth 
                required 
                value={form[key]} 
                onChange={handleChange(key)} 
                type="number" 
                inputProps={{ step: step }}
                error={!!erros[key]}
                helperText={erros[key]}
              />
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

export default FormularioTransacao;
