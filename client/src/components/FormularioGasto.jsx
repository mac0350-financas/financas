// src/components/FormularioGasto.jsx
import { Box, TextField, Button } from '@mui/material';
import { styled } from '@mui/system';


export default function FormularioGasto({ onSubmit }) {
  return (
    <Box component="form" onSubmit={onSubmit} sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
      <TextField label="Valor" type="number" name="valor" fullWidth required />
      <TextField label="Categoria" type="text" name="categoria" fullWidth required />
      <Button type="submit" variant="contained">Registrar Gasto</Button>
    </Box>
  );
}
 