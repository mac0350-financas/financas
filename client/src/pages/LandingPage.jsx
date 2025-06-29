// LandingPage.jsx  (trecho relevante)

import React from 'react';
import { Box, Typography, Button, Container, Grid, Paper } from '@mui/material';
// import Carousel from 'react-material-ui-carousel';
import { styled } from '@mui/system';
import { cores, titulo } from '../themes/temas';
import HeaderEscura from '../components/HeaderEscura';
import { useNavigate } from 'react-router-dom';

const BotaoSerFinature = styled(Button)({
  height: '50px',
  width: '95%',
  backgroundColor: cores.cinzaClaro, 
  borderRadius: '10px',
  fontFamily: 'Kantumruy Pro, sans-serif',
  alignContent: 'center',
  fontWeight: 'bold',
  color: '#2F404A',
  fontSize: '20px',
  '&:hover': {
      backgroundColor: '#D9D9D9', 
  },
  '&:disabled': {
      backgroundColor: 'rgba(109, 122, 130, 0.3)',
      color: '#999',
      cursor: 'not-allowed',
  },
});

const CarrosselItem = ({ title, description }) => (
  <Box sx={{ textAlign: 'center', p: 4 }}>
      <Typography variant="h6" gutterBottom>
      {title}
      </Typography>
      <Typography variant="body1">{description}</Typography>
  </Box>
);

// Componente do carrossel funcional
// const Carrossel = () => {
//   const funcionalidades = [
//     {
//       title: 'Controle de Gastos',
//       description: 'Monitore suas despesas e receitas de forma simples e intuitiva',
//     },
//     {
//       title: 'Metas Mensais',
//       description: 'Defina limites de gasto e acompanhe seu progresso em tempo real',
//     },
//     {
//       title: 'Investimentos CDI',
//       description: 'Faça simulações rápidas e acompanhe seus rendimentos',
//     },
//   ];

//   return (
//     <Box sx={{ 
//       width: '100%',
//       maxWidth: '800px',
//       padding: '20px',
//       alignItems: 'center'
//     }}>
//       <Typography 
//         variant="h4" 
//         textAlign="center" 
//         sx={{ 
//           color: '#2F404A',
//           fontWeight: 'bold',
//           mb: 4 
//         }}
//       >
//         Funcionalidades da Finature
//       </Typography>
//       <Carousel
//         autoPlay={true}
//         indicators={true}
//         navButtonsAlwaysVisible={true}
//         animation="slide"
//         duration={500}
//         interval={4000}
//         alignItems="center"
//       >
//         {funcionalidades.map((item, i) => (
//           <CarrosselItem key={i} {...item} />
//         ))}
//       </Carousel>
//     </Box>
//   );
// };

const MainContainer = styled('div')({
  display: 'flex',
  flexDirection: 'column',
  minHeight: '100vh',
  width: '100%',
  margin: 0,
  padding: 0,
  backgroundColor: '#2F404A',
  position: 'absolute',
  top: 0,
  left: 0,
  right: 0,
  bottom: 0,
});

const MetadeEsquerda = styled('div')({
  flex: 1, // 60% of the total width (3 out of 5 parts)
  display: 'flex',
  height: '90vh', // Ensure it takes full height
  flexDirection: 'column', // To stack MetadeCima and MetadeBaixo vertically
  alignItems: 'center',
  justifyContent: 'center',
  marginLeft: '50px', // Add some margin to the left
});

const MetadeDireita = styled('div')({
  flex: 1, // 40% of the total width (2 out of 5 parts)
  display: 'flex',
  height: '90vh', // Ensure it takes full height
  alignItems: 'center',
  justifyContent: 'center',
  marginRight: '40px', // Add some margin to the right
  //backgroundColor: '#f5f5f5', 
});

const MetadeCima = styled('div')({
  flex: 2, // 50% of the height of MetadeEsquerda
  display: 'flex',
  alignItems: 'top',
  marginTop: '90px', // Add some margin to the top
  justifyContent: 'center',
  width: '100%', // Ensure it spans the full width
});

const MetadeBaixo = styled('div')({
  flex: 3, // 50% of the height of MetadeEsquerda
  display: 'flex',
  alignItems: 'top',
  justifyContent: 'center',
  marginTop: '20px', // Add some margin to the top
  marginLeft: '80px', // Add some margin to the left
  marginRight: '12%', // Add some margin to the right
  width: '100%', // Ensure it spans the full width
});

/* ——————————————————————————————— */
const Titulo = () => (
  <Typography
      variant="h4"
      align="left"
      sx={{
        color: titulo.cor,
        fontSize: titulo.tamanho,
        fontFamily: titulo.fontePadrao,
        fontWeight: titulo.pesoNegrito,
        mb: 4,            // espaço antes do bloco AboutFinature
      }}
    >
      Obtenha já o controle de toda a sua vida financeira. <br/>Seu eu do futuro agradece! 🚀
  </Typography>
);

/* 1)  Mini-componente como constante */
const AboutFinature = (props) => {
  const navigate = useNavigate();

  const handleButtonClick = () => {
    navigate('/criar-conta'); // Redireciona para a página de cadastro
  };

  const handleSaibaMaisClick = () => {
    // Scroll suave até a seção do carrossel
    const carrosselSection = document.getElementById('carrossel-section');
    if (carrosselSection) {
      carrosselSection.scrollIntoView({ behavior: 'smooth' });
    }
  };

  return (
    <Box
      {...props}
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'left',
        textAlign: 'left',
      }}
    >
      <Typography
        variant="h6"
        fontWeight="bold"
        align='left'
        sx={{ 
          color: cores.cinzaClaro, 
          fontSize: '40px', 
          //marginLeft: '25px', // Adiciona margem à esquerda
          mb: 1, 
          alignSelf: 'flex-start' // Alinha o texto à esquerda
        }}
      >
        De onde vem a Finature? 📍
      </Typography>

      <Typography
        fontWeight="bold"
        sx={{ color: cores.textoPrimario, fontSize: '25px', mb: 3 }}
      >
        Do sonho de auxiliar pessoas a se organizarem com suas
        finanças de forma simples e intuitiva, sem complicações.
      </Typography>

      <BotaoSerFinature onClick={handleButtonClick}>
        QUERO SER FINATURE
      </BotaoSerFinature>

      <Typography
        variant="body1"
        align="center"
        sx={{
          color: cores.cinzaClaro,
          fontSize: '16px',
          mt: 3,
          cursor: 'pointer',
          textDecoration: 'underline',
          '&:hover': {
            color: '#FFFFFF',
          },
        }}
        onClick={handleSaibaMaisClick}
      >
        Saiba mais as nossas funcionalidades
      </Typography>
    </Box>
  );
};
/* ——————————————————————————————— */

const Conteudo = styled('div')({
  display: 'flex',
  //flex: 1,
  flex: '0 0 auto',
  minHeight: '100vh',        // 👉 força scroll para ver o carrossel
  backgroundColor: '#2F404A' // 👉 verde só nesta faixa
});

const Imagem = styled('img')({
  maxWidth: '100%',
  maxHeight: '100%',
  width: '85%', // Aumentado de 70% para 85%
  height: 'auto',
  objectFit: 'contain',
  borderRadius: '16px',
  filter: 'drop-shadow(0px 0px 20px rgba(255, 255, 255, 0.3))',
  // Ou use box-shadow se preferir:
  // boxShadow: '0px 0px 30px rgba(255, 255, 255, 0.4)',
});


const SecaoInferior = styled('div')({
  backgroundColor: '#F2F2F2', // só a "faixa"
  padding: '60px 0',          // espaço interno
  display: 'flex',
  justifyContent: 'center',
});

function LandingPage() {
  return (
    <MainContainer>
      <HeaderEscura/>

      <Conteudo>
          <MetadeEsquerda>
          <MetadeCima>
          <Titulo />
          </MetadeCima>
          <MetadeBaixo>
              <AboutFinature />
          </MetadeBaixo>
          </MetadeEsquerda>
          <MetadeDireita>
            <Imagem src="../../public/landing-branca.png"/>
          </MetadeDireita>
      </Conteudo>
      
      {/* <SecaoInferior id="carrossel-section">
        <Container maxWidth="lg" sx={{ display: 'flex', justifyContent: 'center' }}>
        </Container>
      </SecaoInferior> */}
    </MainContainer>
  );
}

export default LandingPage;