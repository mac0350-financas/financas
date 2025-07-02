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

const categorias = ['🍽️ Alimentação', '🚗 Transporte', '🩺 Saúde', '🎓 Educação', '🎉 Lazer', '🏠 Moradia', 
                    '👚 Vestuário', '💼 Negócios', '💸 Dívidas', '📈 Investimentos', '💝 Doação e presente', 
                     '🐶 Pets', '✨ Outros'];

function FormularioTransacao(tipo) {
  const [formData, setFormData] = useState({
    data: '',
    descricao: '',
    categoria: '',
    valor: ''
  });

  const tipoTransacaoId = tipo.tipo === 'gasto' ? -1 : 1;

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const validateForm = () => {
    if (!formData.data.trim()) {
        setError('Data é obrigatória');
        return false;
    }
    if (!formData.descricao.trim()) {
        setError('Descrição é obrigatória');
        return false;
    }
    if (!formData.categoria.trim()) {
        setError('Categoria é obrigatória');
        return false;
    }
    if (!formData.valor.trim()) {
      setError('Valor é obrigatório');
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
      const response = await fetch('http://localhost:8080/formulario-transacao', {
          method: 'POST',
          headers: {
              'Content-Type': 'application/json',
              'Accept': 'application/json'
          },
          credentials: 'include',
          body: JSON.stringify({
              data: formData.data,
              descricao: formData.descricao,
              categoria: formData.categoria,
              valor: formData.valor,
              tipoId: tipoTransacaoId,
              usuarioId: null
          }),
      });

      if (response.ok) {
          console.log('Formulário enviado com sucesso');
          setSuccess(true);
          setFormData({ data: '', descricao: '', categoria: '', valor: '' });
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

  return (
    <FormControl component="form" onSubmit={handleSubmit} fullWidth>
      {error && <Alert severity="error" sx={{ marginBottom: '16px', width: '1024px' }}>{error}</Alert>}
      {success && <Alert severity="success" sx={{ marginBottom: '16px', width: '1024px' }}>Transação salva com sucesso!</Alert>}
      <StyledForm>
        <StyledTextField
          label="Data"
          type="date"
          name="data"
          value={formData.data}
          onChange={handleChange}
          fullWidth
          InputLabelProps={{ shrink: true }}
        />
        <StyledTextField
          label="Descrição"
          name="descricao"
          value={formData.descricao}
          onChange={handleChange}
          fullWidth
        />
        <StyledTextField
          label="Categoria"
          name="categoria"
          value={formData.categoria}
          onChange={handleChange}
          select
          fullWidth
        >
          {categorias.map((categoria) => (
            <MenuItem key={categoria} value={categoria}>
              {categoria}
            </MenuItem>
          ))}
        </StyledTextField>
        <StyledTextField
          label="Valor (R$)"
          type="number"
          name="valor"
          value={formData.valor}
          onChange={handleChange}
          fullWidth
        />
        <StyledButton type="submit" variant="contained" disabled={loading}>
          {loading ? 'SALVANDO ' + tipo.tipo.toUpperCase() : 'SALVAR ' + tipo.tipo.toUpperCase()}
        </StyledButton>
      </StyledForm>
    </FormControl>
  );
};

export default FormularioTransacao;
