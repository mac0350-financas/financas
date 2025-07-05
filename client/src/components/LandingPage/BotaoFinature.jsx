import React from 'react';
import { Button } from '@mui/material';
import { styled } from '@mui/system';
import { cores } from '../../themes/temas';

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

const BotaoFinature = ({ onClick, children }) => (
  <BotaoSerFinature onClick={onClick}>
    {children}
  </BotaoSerFinature>
);

export default BotaoFinature;
