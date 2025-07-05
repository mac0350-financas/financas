import React, { useState } from 'react';
import { Button, Menu, MenuItem } from '@mui/material';
import { styled } from '@mui/system';

const StyledButton = styled(Button)({
    margin: '8px',
    padding: '12px 24px',
    fontWeight: 'bold',
    textTransform: 'none',
    width: '256px',
    height: '84px',
    borderRadius: '30px',
    backgroundColor: 'transparent',
    color: '#2F404A',
    fontFamily: 'Kantumruy Pro, sans-serif',
    fontSize: "24px",
});

const StyledMenu = styled(Menu)({
    '& .MuiPaper-root': {
        maxHeight: 300,
        width: '256px',
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

const SelecaoMes = ({ aoSelecionarMes }) => {
    const [mesSelecionado, setMesSelecionado] = useState(
        new Date().toLocaleString('pt-BR', { month: 'long' }).charAt(0).toUpperCase() + 
        new Date().toLocaleString('pt-BR', { month: 'long' }).slice(1)
    );
    const [elementoAncora, setElementoAncora] = useState(null);
    const aberto = Boolean(elementoAncora);

    const meses = [
        'Todos',
        'Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho',
        'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'
    ];

    const handleClick = (evento) => {
        setElementoAncora(evento.currentTarget);
    };

    const handleClose = () => {
        setElementoAncora(null);
    };

    const handleSelecionarMes = (mes, indice) => {
        setMesSelecionado(mes);
        setElementoAncora(null);
        aoSelecionarMes(mes, indice === 0 ? 0 : indice);
    };

    return (
        <>
            <StyledButton variant="contained" onClick={handleClick}>
                {mesSelecionado}
            </StyledButton>
            <StyledMenu
                anchorEl={elementoAncora}
                open={aberto}
                onClose={handleClose}
            >
                {meses.map((mes, indice) => (
                    <StyledMenuItem 
                        key={mes} 
                        onClick={() => handleSelecionarMes(mes, indice)}
                        selected={mesSelecionado === mes}
                    >
                        {mes}
                    </StyledMenuItem>
                ))}
            </StyledMenu>
        </>
    );
};

export default SelecaoMes;

