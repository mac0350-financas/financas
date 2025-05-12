// src/pages/PaginaRegistrarGasto.jsx
import FormularioGasto from '../components/FormularioGasto';
import { Container, Typography } from '@mui/material';
import axios from 'axios'; // Para fazer a chamada POST

export default function PaginaRegistrarGasto() {
  async function handleRegistro(event) {
    event.preventDefault();
    const formData = new FormData(event.currentTarget);
    const gasto = {
      valor: formData.get('valor'),
      categoria: formData.get('categoria'),
    };

    try {
      await axios.post('http://localhost:8080/transacao-saida', gasto);
      alert('Gasto registrado com sucesso!');
    } catch (error) {
      console.error('Erro ao registrar gasto:', error);
      alert('Erro ao registrar gasto.');
    }
  }

  return (
    <Container>
      <Typography variant="h4" gutterBottom>Registrar Gasto</Typography>
      <FormularioGasto onSubmit={handleRegistro} />
    </Container>
  );
}
 