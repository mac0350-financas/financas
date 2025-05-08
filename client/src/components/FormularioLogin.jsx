import { Button, TextField, Box } from '@mui/material';
import { styled } from '@mui/material/styles';

const BoxLogin = styled(Box)({
    display: 'flex',
    flexDirection: 'column',
    gap: '16px',
    padding: '16px',
    border: '1px solid #ccc',
    borderRadius: '8px',
    backgroundColor: '#f5f5f5',
})


export default function FormularioLogin({ onSubmit }) {
return (
    <BoxLogin component="form" onSubmit={onSubmit}>
        <TextField label="Email" type="email" name="email" fullWidth />
        <TextField label="Senha" type="password" name="senha" fullWidth />
        <Box sx={{ display: 'flex', gap: '8px' }}>
            <Button type="submit" variant="contained" fullWidth>Fazer Login</Button>
            <Button type="button" variant="contained" fullWidth>Fazer Cadastro</Button>
        </Box>
    </BoxLogin>
);
}
