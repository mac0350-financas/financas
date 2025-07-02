import React from 'react';
import { Button, Dialog, DialogTitle, DialogContent, DialogActions } from '@mui/material';
import { styled } from '@mui/system';
import FormularioTransacao from './FormularioTransacao';

const StyledButton = styled(Button)({
    margin: '8px',
    padding: '12px 24px',
    fontWeight: 'bold',
    textTransform: 'none',
    width: '354px',
    height: '84px',
    borderRadius: '30px',
    backgroundColor: 'transparent',
    color: '#2F404A',
    fontFamily: 'Kantumruy Pro, sans-serif',
    fontSize: "24px"
});

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

const BotaoInserirTransacao = ({ texto, tipo }) => {
  const [formAberto, setFormAberto] = React.useState(false);

  const handleClick = () => {
    setFormAberto(true);
  };

  return (
    <>
      <StyledButton
        variant="contained"
        onClick={handleClick}
      >
        {texto}
      </StyledButton>
      
      {formAberto && (
        <StyledDialog 
          open={formAberto} 
          onClose={() => setFormAberto(false)} 
          maxWidth="md" 
          fullWidth
        >
          <StyledDialogTitle>Inserir novo {tipo}! ✨</StyledDialogTitle>
          <StyledDialogContent>
            <FormularioTransacao
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
};

export default BotaoInserirTransacao;

