import React from 'react';
import { Box, Typography, Paper, Grid, Container } from '@mui/material';
import { styled } from '@mui/system';
import { cores, titulo, espacamento, bordas, tamanhos, gradientes, sombras, transicoes } from '../../themes/temas';

const SecaoContainer = styled(Box)({
  backgroundColor: cores.fundoLandingSecao,
  padding: espacamento.paddingSecao,
  position: 'relative',
  '&::before': {
    content: '""',
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
    height: tamanhos.alturaLinha,
    background: gradientes.linha,
  }
});

const CartaoFuncionalidade = styled(Paper)(({ theme }) => ({
  padding: espacamento.paddingCard,
  height: tamanhos.alturaCard,
  minHeight: tamanhos.alturaCard,
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  textAlign: 'center',
  border: `1px solid ${cores.bordaCard}`,
  borderRadius: bordas.raioCard,
  background: gradientes.card,
  transition: transicoes.padrao,
  cursor: 'pointer',
  position: 'relative',
  overflow: 'hidden',
  margin: '0 auto',
  width: '100%',
  maxWidth: tamanhos.larguraMaxCard,
  '&::before': {
    content: '""',
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
    height: tamanhos.alturaGradiente,
    background: gradientes.principal,
    transform: 'scaleX(0)',
    transformOrigin: 'left',
    transition: transicoes.transform,
  },
  '&:hover': {
    transform: 'translateY(-8px)',
    boxShadow: sombras.cardHover,
    borderColor: cores.bordaCardHover,
    '&::before': {
      transform: 'scaleX(1)',
    },
    '& .icone-funcionalidade': {
      transform: 'scale(1.1)',
    },
    '& .titulo-funcionalidade': {
      color: cores.corPrincipal,
    }
  }
}));

const IconeFuncionalidade = styled(Box)({
  width: tamanhos.iconeSize,
  height: tamanhos.iconeSize,
  borderRadius: bordas.raioIcone,
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  fontSize: tamanhos.iconeFont,
  marginBottom: espacamento.marginCard,
  background: gradientes.principal,
  color: cores.fundoBranco,
  transition: transicoes.transform,
  boxShadow: sombras.icone,
});

const TituloSecao = styled(Typography)({
  fontSize: titulo.tamanhoSecao,
  fontWeight: titulo.pesoNegrito,
  color: cores.textoEscuro,
  textAlign: 'center',
  marginBottom: titulo.marginTitulo,
  background: gradientes.texto,
  backgroundClip: 'text',
  WebkitBackgroundClip: 'text',
  WebkitTextFillColor: 'transparent',
  letterSpacing: titulo.letraSpacer,
});

const SubtituloSecao = styled(Typography)({
  fontSize: titulo.tamanhoSubtitulo,
  color: cores.textoSecundario,
  textAlign: 'center',
  marginBottom: espacamento.marginSecao,
  maxWidth: tamanhos.larguraMaxSubtitulo,
  margin: `0 auto ${espacamento.marginSecao} auto`,
  lineHeight: '1.6',
});

const funcionalidades = [
    {
        icone: '📈',
        titulo: 'Dashboard Inteligente',
        descricao: 'Analisamos seus gastos e ofereçemos insights personalizados para otimizar suas finanças.'
    },
    {
        icone: '🎯',
        titulo: 'Metas Financeiras',
        descricao: 'Defina metas para poupar dinheiro e conquistar aquela viagem dos sonhos e muito mais.'
    },
    {
        icone: '📊',
        titulo: 'Investimentos Guiados',
        descricao: 'Simulações precisas para que você compare investimentos e tome a melhor decisão.'
    },
    {
        icone: '📝',
        titulo: 'Registro de movimentações',
        descricao: 'Cadastre suas transações de forma rápida e fácil, mantendo o controle total das suas finanças.'
    }
];

const SecaoFuncionalidades = () => {
  return (
    <SecaoContainer id="funcionalidades-section">
      <Container maxWidth="lg">
        <TituloSecao variant="h2">
          Ferramentas que transformam sua vida financeira
        </TituloSecao>
        <SubtituloSecao variant="h6">
          Queremos te ajudar a assumir o controle das suas finanças com tecnologia de ponta, oferecendo as ferramentas para que você tome as decisões mais inteligentes e consiga alcançar seus sonhos.
        </SubtituloSecao>
        
        <Grid container spacing={espacamento.spacingGridLarge} justifyContent="center" alignItems="stretch">
          {funcionalidades.map((func, index) => (
            <Grid item xs={12} sm={6} lg={3} key={index} sx={{ display: 'flex', justifyContent: 'center' }}>
              <CartaoFuncionalidade elevation={0}>
                <IconeFuncionalidade className="icone-funcionalidade">
                  {func.icone}
                </IconeFuncionalidade>
                <Typography 
                  variant="h6" 
                  className="titulo-funcionalidade"
                  sx={{ 
                    fontWeight: titulo.pesoSemiBold,
                    color: cores.textoTitulo,
                    marginBottom: titulo.marginTitulo,
                    fontSize: titulo.tamanhoCard,
                    transition: transicoes.cor
                  }}
                >
                  {func.titulo}
                </Typography>
                <Typography 
                  variant="body2" 
                  sx={{ 
                    color: cores.textoSecundario,
                    lineHeight: '1.6',
                    fontSize: '18px',
                    flex: 1
                  }}
                >
                  {func.descricao}
                </Typography>
              </CartaoFuncionalidade>
            </Grid>
          ))}
        </Grid>
      </Container>
    </SecaoContainer>
  );
};

export default SecaoFuncionalidades;
