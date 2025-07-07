import React, { useState } from 'react';
import { TextField, Button, MenuItem, FormControl, FormGroup, Alert } from '@mui/material';
import { styled } from '@mui/system';

const StyledTextField = styled(TextField)({
  marginBottom: '16px',
  '& .MuiInputBase-root': {
    borderRadius: '8px',
    backgroundColor: '#f9f9f9',
  },
  '& .MuiInputLabel-root': {
    fontFamily: 'Kantumruy Pro, sans-serif',
    fontSize: '18px',
    color: '#2F404A',
  },
  '& .MuiInputBase-input': {
    fontFamily: 'Kantumruy Pro, sans-serif',
    fontSize: '16px',
    color: '#2F404A',
  },
});

const StyledForm = styled(FormControl)({
  margin: '24px',
  padding: '32px',
  borderRadius: '8px',
  backgroundColor: '#ffffff',
  boxShadow: '0 4px 12px rgba(0, 0, 0, 0.1)',
  width: '100%',
  maxWidth: '800px',
  alignSelf: 'center',
  fontFamily: 'Kantumruy Pro, sans-serif', 
});

const StyledButton = styled(Button)({
  marginTop: '16px',
  padding: '12px 24px',
  fontWeight: 'bold',
  textTransform: 'none',
  borderRadius: '8px',
  fontSize: '16px',
  fontFamily: 'Kantumruy Pro, sans-serif', 
  backgroundColor: '#2F404A',
});

function FormularioMeta({ onSuccess, tipo }) {
  const [formData, setFormData] = useState({
    categoria: '',
    dataInicial: '',
    dataFinal: '',
    valorFinal: ''
  });

  const tipoTransacaoId = tipo === 'gasto' ? -1 : 1;

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const validateForm = () => {
    if (!formData.categoria.trim()) {
        setError('Categoria é obrigatória');
        return false;
    }
    if (!formData.dataInicial.trim()) {
        setError('Data inicial da meta é obrigatória');
        return false;
    }
    if (!formData.dataFinal.trim()) {
        setError('Data final da meta é obrigatória');
        return false;
    }
    if (!formData.valorFinal.trim()) {
      setError('Valor de objetivo é obrigatório');
      return false;
    }
    return true;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess(false);

    if (!validateForm()) return;
    
    setLoading(true);

    try {
      const response = await fetch('http://localhost:8080/formulario-meta', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        },
        credentials: 'include',
        body: JSON.stringify({
          usuarioId: 0, 
          categoria: formData.categoria,
          valorLimite: parseFloat(formData.valorFinal),
          valorAtual: 0,
          dataInicial: formData.dataInicial,
          dataFinal: formData.dataFinal,
          tipoId: tipoTransacaoId
        }),
      });

      if (response.ok) {
          console.log('Formulário enviado com sucesso');
          setSuccess(true);
          setFormData({ categoria: '', dataInicial: '', dataFinal: '', valorFinal: '' });
          if (onSuccess) onSuccess();
      } 
      else {
          const data = await response.json();
          throw new Error(data.message || 'Erro ao salvar transação');
      }

  } 
  
  catch (error) {
      console.error('Erro:', error);
      setError(error.message || 'Erro de conexão. Tente novamente.');
  } 
  
  finally {
      setLoading(false);
  }

};

  const categoriasG = ['🍽️ Alimentação', '🚗 Transporte', '🩺 Saúde', '🎓 Educação', '🎉 Lazer', '🏠 Moradia', 
                      '👚 Vestuário', '💼 Negócios', '💸 Dívidas', '🐶 Pets', '🛒 Mercado', '✈️ Viagem',
                      '✨ Outros'];
  
  const categoriasR = ['💼 Salário', '💰 Freelance', '📈 Investimentos', '🎁 Presente', '💸 Reembolso', 
                      '🏠 Aluguel', '🛒 Venda', '🤝 Parceria', '🎥 Streaming', '✨ Outros'];
  
  const categoriasAtual = tipoTransacaoId === -1 ? categoriasG : categoriasR;

  return (
    <FormControl component="form" onSubmit={handleSubmit} fullWidth>
      {error && <Alert severity="error" sx={{ marginBottom: '16px', width: '1024px' }}>{error}</Alert>}
      {success && <Alert severity="success" sx={{ marginBottom: '16px', width: '1024px' }}>Meta salva com sucesso!</Alert>}
      <StyledForm>
        <StyledTextField
          label="Categoria"
          name="categoria"
          value={formData.categoria}
          onChange={handleChange}
          select
          fullWidth 
        >
          {categoriasAtual.map((categoria) => (
            <MenuItem key={categoria} value={categoria}>
              {categoria}
            </MenuItem>
          ))}
        </StyledTextField>
        <StyledTextField
          label="Data inicial"
          type="date"
          name="dataInicial"
          value={formData.dataInicial}
          onChange={handleChange}
          fullWidth
          InputLabelProps={{ shrink: true }}
        />
        <StyledTextField
          label="Data final"
          type="date"
          name="dataFinal"
          value={formData.dataFinal}
          onChange={handleChange}
          fullWidth
          InputLabelProps={{ shrink: true }}
        />
        <StyledTextField
          label="Valor limite (R$)"
          type="number"
          name="valorFinal"
          value={formData.valorFinal}
          onChange={handleChange}
          fullWidth
        />
        <StyledButton type="submit" variant="contained" disabled={loading}>
          {loading ? 'SALVANDO META'  : 'SALVAR META' }
        </StyledButton>
      </StyledForm>
    </FormControl>
  );
};

export default FormularioMeta;
