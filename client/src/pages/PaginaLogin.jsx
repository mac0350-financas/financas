import { Container, Typography } from '@mui/material';
import FormularioLogin from '../components/FormularioLogin';

export default function PaginaLogin() {
  const handleLogin = (e) => {
    e.preventDefault();
    const formData = new FormData(e.currentTarget);
    const email = formData.get('email');
    const senha = formData.get('senha');

    // chamada ao back
    console.log('Login com:', email, senha);
  };

  return (
    <Container maxWidth="sm">
      <Typography variant="h4" gutterBottom>Login</Typography>
      <FormularioLogin onSubmit={handleLogin} />
    </Container>
  );
}
