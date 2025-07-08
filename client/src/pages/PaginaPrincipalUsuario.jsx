import { useEffect, useState } from 'react';
import axios from 'axios';
import { Typography, Box } from '@mui/material';
import { cores, espacamento } from '../themes/temas';
import HeaderComMenu from '../components/HeaderComMenu';
import BotaoInserirTransacao from '../components/Gastos_Receitas/BotaoInserirTransacao';
import TotalTransacoes from '../components/Gastos_Receitas/TotalTransacoes';
import SaldoResultante from '../components/PaginaPrincipalUsuario/SaldoResultante';

function PaginaPrincipalUsuario() {

    const [usuario, setUsuario] = useState(null);

    useEffect(() => {
        axios.get('http://localhost:8080/usuario-logado', { withCredentials: true })
            .then(response => {
                setUsuario(response.data);
            })
            .catch(error => {
                console.error('Erro ao buscar o usuário logado:', error);
            });
    }, []);

    const currentHour = new Date().getHours();
    const saudacao = (() => {
        if (currentHour < 12) {
            return 'Bom dia';
        } else if (currentHour < 18) {
            return 'Boa tarde';
        } else {
            return 'Boa noite';
        }
    })();

    const dataAtual = new Date();
    const mesAtual = dataAtual.getMonth() + 1;
    const anoAtual = dataAtual.getFullYear();

    return (
        <div>
            <HeaderComMenu />
            <Box sx={{ p: espacamento.paddingInterno }}>
                <Typography variant="h3" fontWeight="bold" color={cores.fundoEscuro}>
                    {saudacao + ', ' + (usuario ? usuario.nome : 'Usuário') + '!' + 
                    (currentHour < 12 ? ' ☀️' : currentHour < 18 ? ' 🌤️' : ' 🌙')}
                </Typography>
                <Box sx={{ mt: 10, display: 'flex', justifyContent: 'space-between' }}>
                    <Box>
                        <TotalTransacoes tipo="receitas" mes={mesAtual} ano={anoAtual} />
                        <BotaoInserirTransacao texto=" Nova Receita" tipo="receita" />
                    </Box>
                    <Box>
                        <TotalTransacoes tipo="gastos" mes={mesAtual} ano={anoAtual} />
                        <BotaoInserirTransacao texto=" Novo Gasto" tipo="gasto" />
                    </Box>
                    <Box>
                        <SaldoResultante mes={mesAtual} ano={anoAtual} />
                    </Box>
                </Box>
            </Box>
        </div>
    );
}

export default PaginaPrincipalUsuario; 