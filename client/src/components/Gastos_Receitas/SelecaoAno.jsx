import React, { useState } from 'react';
import { Button, Menu, MenuItem } from '@mui/material';
import { styled } from '@mui/system';

const StyledButton = styled(Button)({
    margin: '8px',
    padding: '12px 24px',
    fontWeight: 'bold',
    textTransform: 'none',
    width: '128px',
    height: '84px',
    borderRadius: '30px',
    backgroundColor: 'transparent',
    color: '#2F404A',
    fontFamily: 'Kantumruy Pro, sans-serif',
    fontSize: "24px",
    border: '2px solid #2F404A',
    '&:hover': {
        backgroundColor: 'rgba(47, 64, 74, 0.1)',
    }
});

const StyledMenu = styled(Menu)({
    '& .MuiPaper-root': {
        maxHeight: 300,
        width: '128px',
        borderRadius: '15px',
        boxShadow: '0 4px 20px rgba(47, 64, 74, 0.2)',
        border: '1px solid rgba(47, 64, 74, 0.1)',
    }
});

const StyledMenuItem = styled(MenuItem)({
    fontFamily: 'Kantumruy Pro, sans-serif',
    fontSize: '18px',
    padding: '12px 24px',
    color: '#2F404A',
    '&:hover': {
        backgroundColor: 'rgba(47, 64, 74, 0.1)',
    },
    '&.Mui-selected': {
        backgroundColor: 'rgba(47, 64, 74, 0.15)',
        '&:hover': {
            backgroundColor: 'rgba(47, 64, 74, 0.2)',
        }
    }
});

const SelecaoAno = ({ aoSelecionarAno }) => {
    const [anoSelecionado, setAnoSelecionado] = useState('Selecionar Ano');
    const [elementoAncora, setElementoAncora] = useState(null);
    const aberto = Boolean(elementoAncora);

    const anoAtual = new Date().getFullYear();
    const anos = ['Todos'];
    for (let i = 0; i <= 10; i++) {
        anos.push(anoAtual - i);
    }

    const handleClick = (evento) => {
        setElementoAncora(evento.currentTarget);
    };

    const handleClose = () => {
        setElementoAncora(null);
    };

    const handleSelecionarAno = (ano) => {
        setAnoSelecionado(ano);
        setElementoAncora(null);
        aoSelecionarAno(ano);
    };

    return (
        <>
            <StyledButton onClick={handleClick}>
                {anoSelecionado}
            </StyledButton>
            <StyledMenu
                anchorEl={elementoAncora}
                open={aberto}
                onClose={handleClose}
            >
                {anos.map((ano) => (
                    <StyledMenuItem 
                        key={ano} 
                        onClick={() => handleSelecionarAno(ano)}
                        selected={anoSelecionado === ano}
                    >
                        {ano}
                    </StyledMenuItem>
                ))}
            </StyledMenu>
        </>
    );
};

export default SelecaoAno;

