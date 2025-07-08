import React from "react";
import { Box, Typography, Button, Card, CardContent, Divider } from "@mui/material";
import { cores } from "../../themes/temas";
import TrendingUpIcon from '@mui/icons-material/TrendingUp';
import AccountBalanceIcon from '@mui/icons-material/AccountBalance';

function HeaderBotaoConta({ dadosPoupanca, dadosSelic, onVerDetalhes }) {
  const formatarMoeda = (valor) => {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(valor);
  };

  const valorFinalSelic = dadosSelic.length > 0 ? dadosSelic[dadosSelic.length - 1]?.valor || 0 : 0;
  const valorFinalPoupanca = dadosPoupanca.length > 0 ? dadosPoupanca[dadosPoupanca.length - 1]?.valor || 0 : 0;

  return (
    <Card 
      elevation={3}
      sx={{ 
        minWidth: 350,
        maxHeight: 400,
        background: 'linear-gradient(135deg,rgb(60, 76, 97) 0%,rgb(29, 56, 55) 100%)',
        color: 'white',
        borderRadius: 3,
        overflow: 'visible',
        position: 'relative',
        '&::before': {
          content: '""',
          position: 'absolute',
          top: 0,
          left: 0,
          right: 0,
          bottom: 0,
          background: 'linear-gradient(135deg, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0.05) 100%)',
          borderRadius: 3,
          pointerEvents: 'none'
        }
      }}
    >
      <CardContent sx={{ p: 4, position: 'relative', zIndex: 1 }}>
        <Box sx={{ display: 'flex', alignItems: 'center', mb: 3 }}>
          <TrendingUpIcon sx={{ fontSize: 28, mr: 1.5, opacity: 0.9 }} />
          <Typography 
            variant="h5" 
            fontWeight="600"
            sx={{ 
              fontSize: '1.4rem',
              letterSpacing: '0.5px'
            }}
          >
            Resultado da Simulação
          </Typography>
        </Box>

        <Box sx={{ mb: 3 }}>
          <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
            <AccountBalanceIcon sx={{ fontSize: 20, mr: 1, opacity: 0.8 }} />
            <Typography variant="body2" sx={{ opacity: 0.9, fontWeight: 500 }}>
              Tesouro Selic
            </Typography>
          </Box>
          <Typography 
            variant="h4" 
            fontWeight="700"
            sx={{ 
              mb: 1,
              fontSize: '1.8rem',
              textShadow: '0 2px 4px rgba(0,0,0,0.1)'
            }}
          >
            {formatarMoeda(valorFinalSelic)}
          </Typography>
        </Box>

        <Divider sx={{ my: 2, backgroundColor: 'rgba(255,255,255,0.2)' }} />

        <Box sx={{ mb: 4 }}>
          <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
            <AccountBalanceIcon sx={{ fontSize: 20, mr: 1, opacity: 0.8 }} />
            <Typography variant="body2" sx={{ opacity: 0.9, fontWeight: 500 }}>
              Poupança
            </Typography>
          </Box>
          <Typography 
            variant="h4" 
            fontWeight="700"
            sx={{ 
              fontSize: '1.8rem',
              textShadow: '0 2px 4px rgba(0,0,0,0.1)'
            }}
          >
            {formatarMoeda(valorFinalPoupanca)}
          </Typography>
        </Box>

        <Button
          variant="contained"
          onClick={onVerDetalhes}
          fullWidth
          sx={{
            backgroundColor: 'rgba(255,255,255,0.15)',
            color: 'white',
            fontWeight: 600,
            fontSize: '1rem',
            py: 1.5,
            borderRadius: 2,
            textTransform: 'none',
            backdropFilter: 'blur(10px)',
            border: '1px solid rgba(255,255,255,0.2)',
            '&:hover': {
              backgroundColor: 'rgba(255,255,255,0.25)',
              transform: 'translateY(-2px)',
              boxShadow: '0 8px 25px rgba(0,0,0,0.15)',
            },
            transition: 'all 0.3s cubic-bezier(0.4, 0, 0.2, 1)',
          }}
        >
          Ver Detalhes Completos
        </Button>
      </CardContent>
    </Card>
  );
}

export default HeaderBotaoConta;
