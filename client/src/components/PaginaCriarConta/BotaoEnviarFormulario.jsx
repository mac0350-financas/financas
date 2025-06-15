 
import React from 'react';
import { Button } from '@mui/material';
import { styled } from '@mui/system';

const BotaoEstilizado = styled(Button)({
    height: '40px',
    width: '40%',
    margin: '0 auto',
    backgroundColor: 'rgba(109, 122, 130, 0.5)', // Transparência de 50%
    borderRadius: '16px',
    fontFamily: 'Kantumruy Pro, sans-serif',
    fontWeight: 'bold',
    color: '#2F404A',
    '&:hover': {
        backgroundColor: '#D9D9D9', // Cor ao passar o mouse
    },
});

function BotaoEnviarFormulario({ children }) {
    return (
        <BotaoEstilizado 
            type="submit" 
            variant="contained" 
            fullWidth
        >
            {children} {/* Texto interno do botão é um children */}
        </BotaoEstilizado>
    );
}

export default BotaoEnviarFormulario;