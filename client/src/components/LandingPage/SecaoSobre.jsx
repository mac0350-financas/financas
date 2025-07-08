import React from 'react';
import { Box, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { cores, espacamento } from '../../themes/temas';
import BotaoFinature from './BotaoFinature';

const SecaoSobre = () => {
  const navigate = useNavigate();

  const handleButtonClick = () => {
    navigate('/criar-conta');
  };

  const handleSaibaMaisClick = () => {
    const funcionalidadesSection = document.getElementById('funcionalidades-section');
    if (funcionalidadesSection) {
      funcionalidadesSection.scrollIntoView({ behavior: 'smooth' });
    }
  };

  return (
    <Box 
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'flex-start',
        textAlign: 'left',
        paddingLeft: '90px',
      }}
    >
      <Typography
        variant="h6"
        fontWeight="bold"
        align='left'
        sx={{ 
          color: cores.cinzaClaro, 
          fontSize: '40px', 
          mb: 1, 
          alignSelf: 'flex-start'
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

      <BotaoFinature onClick={handleButtonClick}>
        QUERO SER FINATURE
      </BotaoFinature>

      <Typography
        variant="body1"
        align="center"
        sx={{
          color: cores.cinzaClaro,
          fontSize: '16px',
          mt: 3,
          cursor: 'pointer',
          textDecoration: 'underline',
          width: '95%',
          '&:hover': {
            color: '#FFFFFF',
          },
        }}
        onClick={handleSaibaMaisClick}
      >
        Conheça nossos diferenciais
      </Typography>
    </Box>
  );
};

export default SecaoSobre;
