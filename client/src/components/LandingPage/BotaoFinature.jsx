import React from 'react';
import { Button } from '@mui/material';
import { styled } from '@mui/system';
import { cores, titulo, bordas } from '../../themes/temas';

const BotaoSerFinature = styled(Button)({
  height: '50px',
  width: '95%',
  backgroundColor: cores.cinzaClaro, 
  borderRadius: bordas.raioPadrao,
  fontFamily: titulo.fontePadrao,
  alignContent: 'center',
  fontWeight: titulo.pesoNegrito,
  color: cores.fundoEscuro,
  fontSize: '20px',
  '&:hover': {
    backgroundColor: cores.botaoHover, 
  },
  '&:disabled': {
    backgroundColor: cores.botaoPrimario,
    color: '#999',
    cursor: 'not-allowed',
  },
});

const BotaoFinature = ({ onClick, children }) => (
  <BotaoSerFinature onClick={onClick}>
    {children}
  </BotaoSerFinature>
);

export default BotaoFinature;
