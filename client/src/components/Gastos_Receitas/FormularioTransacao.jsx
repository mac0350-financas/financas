import React, { useState } from 'react';
import { TextField, Button, MenuItem, FormControl, FormGroup } from '@mui/material';
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

const FormularioTransacao = ({ tipo, onSubmit, onClose }) => {
  const [formData, setFormData] = useState({
    data: '',
    descricao: '',
    categoria: '',
    valor: ''
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit(formData);
  };

  return (
    <FormControl component="form" onSubmit={handleSubmit} fullWidth>
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
        <StyledButton type="submit" variant="contained" color="primary">
          Salvar {tipo}
        </StyledButton>
      </StyledForm>
    </FormControl>
  );
};

export default FormularioTransacao;
