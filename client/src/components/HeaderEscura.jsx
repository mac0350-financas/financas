import React from 'react';
import { Box, Divider, Button } from '@mui/material';
import { cores } from '../themes/temas';
import { styled } from '@mui/system';
import { useNavigate } from 'react-router-dom';

const BotaoAcessarConta = styled(Button)({
  backgroundColor: 'transparent',
  color: '#FFFFFF',
  fontWeight: 'bold',
  padding: '8px 16px',
  borderRadius: '8px',
  marginRight: '5px',
  transition: 'background-color 0.3s, color 0.3s',
  '&:hover': {
    backgroundColor: '#FFFFFF',
    color: '#000000',
  },
  '&:active': {
    backgroundColor: '2F404A',
    color: '2F404A',
  },
});

const Header = () => {
  const navigate = useNavigate();

  const handleAcessarConta = () => {
    navigate('/fazer-login'); // ou a rota que você preferir
  };

  return (
    <Box sx={{ backgroundColor: cores.fundoPrimario, pt: 2, px: 3, marginTop: '9px', marginLeft: '10px', marginRight: '10px' }}>
      <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', height: 40 }}>
        <img
          src="/logo-finature-branca.png"
          alt="Finature logo"
          style={{ height: 45}}
        />
        <BotaoAcessarConta onClick={handleAcessarConta}>
          Acessar Conta
        </BotaoAcessarConta>
      </Box>
      <Divider sx={{mt: 2.5, backgroundColor: '#FFFFFF'}} />
    </Box>
  );
};

export default Header;