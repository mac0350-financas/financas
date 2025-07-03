import React from "react";
import { cores } from "../../themes/temas";
import {
  Typography,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from "@mui/material";

const HeaderBotaoPaginas = ({ detalhesPoupanca, detalhesSelic }) => {
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

export default HeaderBotaoPaginas;
