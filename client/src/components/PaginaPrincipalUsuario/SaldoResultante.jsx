import { Typography, Box, styled, Paper } from '@mui/material';
import { useState, useEffect } from 'react';

// Remove BotaoInserirTransacao import 

const BlocoTotal = styled(Paper)({
    width: '100vw',
    height: '240px',
    borderRadius: '20px',
    backgroundColor: 'rgba(247, 247, 247, 0.95)',
    boxShadow: '0 8px 32px rgba(31, 38, 135, 0.15)',
    backdropFilter: 'blur(4px)',
    border: '1px solid rgba(255, 255, 255, 0.18)',
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center', // Changed from space-between to center
    padding: '35px',
    position: 'relative',
    overflow: 'hidden',
    '&::before': {
        content: '""',
        position: 'absolute',
        top: 0,
        left: 0,
        width: '10px',
        height: '100%',
        background: 'linear-gradient(180deg, #8E44AD 0%, #3498DB 100%)',
    }
});

const TextoTotal = styled(Typography)({
    fontFamily: '"Poppins", "Roboto", sans-serif',
    fontSize: '30px',
    fontWeight: '600',
    color: '#2F3542',
    marginBottom: '15px',
    position: 'relative',
    paddingLeft: '10px',
});

const TextoValor = styled(Typography)({
    fontFamily: '"Poppins", "Roboto", sans-serif',
    fontSize: '46px',
    fontWeight: '700',
    background: 'linear-gradient(45deg, #3498DB, #8E44AD)',
    WebkitBackgroundClip: 'text',
    WebkitTextFillColor: 'transparent',
    letterSpacing: '1px',
    paddingLeft: '10px', // Added padding to match TextoTotal
    position: 'relative', // Added position to match TextoTotal
});

// Remove BotoesContainer styled component

function SaldoResultante({ mes, ano }) {

    const [valor, setValor] = useState(0);
    const [loading, setLoading] = useState(false);

    const fetchTotal = async () => {
        if (!mes || !ano) return;
        
        setLoading(true);
        try {
            const response1 = await fetch(`/api/transacoes/total?tipo=${1}&mes=${mes}&ano=${ano}`);
            const data1 = await response1.json();
            const response2 = await fetch(`/api/transacoes/total?tipo=${-1}&mes=${mes}&ano=${ano}`);
            const data2 = await response2.json();
            setValor(data1.total - data2.total || 0);
        } catch (error) {
            console.error('Erro ao buscar total:', error);
            setValor(0);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchTotal();
    }, [mes, ano]);

    const nomeMes = new Date(ano, mes - 1).toLocaleString('pt-BR', { month: 'long' });

    return(
        <BlocoTotal elevation={4}>
            <Box>
                <TextoTotal>Esse é o seu saldo resultante para {nomeMes} de {ano}</TextoTotal>
                <TextoValor>
                    {loading ? 'Carregando...' : `R$ ${valor.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}`}
                </TextoValor>
            </Box>
            
            {/* Remove BotoesContainer and buttons */}
        </BlocoTotal>
    )

}

export default SaldoResultante;