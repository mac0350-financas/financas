import React from 'react';
import { Box, Button, Dialog, DialogTitle, DialogContent, DialogActions } from "@mui/material";
import { styled } from "@mui/system";
import FormularioMeta from './FormularioMeta';

const ButtonContainer = styled(Box)(({ tipo }) => ({
    width: '100%',
    height: '200px',
    backgroundColor: 'transparent', // Remove a cor de fundo padrão
    borderRadius: '16px',
    border: `2px dashed ${tipo === "gasto" ? '#f28b82' : '#a8e6cf'}`, // Vermelho suave para "gasto", verde suave caso contrário
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    cursor: 'pointer',
    transition: 'background-color 0.3s, border-color 0.3s',
    '&:hover': {
        backgroundColor: tipo === "gasto" ? '#fdecea' : '#e8f5e9', // Fundo vermelho suave para "gasto", verde suave caso contrário
        borderColor: tipo === "gasto" ? '#f28b82' : '#a8e6cf',
    },
}));

const StyledButton = styled(Button)(({ tipo }) => ({
    width: '100%',
    height: '200px',
    borderRadius: '16px',
    fontSize: '20px', // Aumenta o tamanho da fonte
    fontWeight: 'bold',
    color: tipo === "gasto" ? '#f28b82' : '#a8e6cf', // Vermelho suave para "gasto", verde suave caso contrário
    textTransform: 'none',
}));

const StyledDialog = styled(Dialog)({
    '& .MuiDialog-paper': {
        borderRadius: '16px',
        padding: '24px',
        boxShadow: '0 4px 12px rgba(0, 0, 0, 0.1)',
        backgroundColor: '#ffffff',
    },
});

const StyledDialogTitle = styled(DialogTitle)({
    fontWeight: 'bold',
    fontSize: '32px',
    color: '#2F404A',
    marginBottom: '16px',
    fontFamily: 'Kantumruy Pro, sans-serif',
});

const StyledDialogContent = styled(DialogContent)({
    padding: '16px 24px',
    fontFamily: 'Kantumruy Pro, sans-serif',
});

const StyledDialogActions = styled(DialogActions)({
    justifyContent: 'flex-end',
    padding: '16px 24px',
    fontFamily: 'Kantumruy Pro, sans-serif',
});

function BotaoNovaMeta({ tipo }) {
    const [formAberto, setFormAberto] = React.useState(false);

    const handleClick = () => {
        setFormAberto(true);
    };

    return (
        <>
            <ButtonContainer tipo={tipo}>
                <StyledButton tipo={tipo} onClick={handleClick}>
                    + Nova Meta
                </StyledButton>
            </ButtonContainer>

            {formAberto && (
                <StyledDialog 
                open={formAberto} 
                onClose={() => setFormAberto(false)} 
                maxWidth="md" 
                fullWidth
                >
                <StyledDialogTitle>Inserir nova meta! ✨</StyledDialogTitle>
                <StyledDialogContent>
                    <FormularioMeta
                    tipo={tipo}
                    />
                </StyledDialogContent>
                <StyledDialogActions>
                    <Button onClick={() => setFormAberto(false)} color="secondary">
                    Fechar
                    </Button>
                </StyledDialogActions>
                </StyledDialog>
            )}
        </>
    );
}

export default BotaoNovaMeta;
