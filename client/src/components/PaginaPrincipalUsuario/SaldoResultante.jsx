import { Typography, Box, styled } from '@mui/material';
import { useState, useEffect } from 'react';

const BlocoTotal = styled(Box)({
    width: '30vw',
    height: '160px',
    borderRadius: '30px',
    backgroundColor: 'rgba(173, 216, 230, 0.8)', // Azul claro
    border: '1px solid #2F404A',
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'flex-start',
    paddingLeft: '30px',
});

const TextoTotal = styled(Typography)({
    fontFamily: 'Kantumruy Pro, sans-serif',
    fontSize: '24px',
    fontWeight: 'bold',
    color: '#2F404A',
    marginBottom: '10px',
});

const TextoValor = styled(Typography)({
    fontFamily: 'Kantumruy Pro, sans-serif',
    fontSize: '40px',
    fontWeight: 'bold',
    color: '#722F37',
});

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
        <BlocoTotal>
            <TextoTotal>Saldo resultante para {nomeMes} de {ano}</TextoTotal>
            <TextoValor>
                {loading ? 'Carregando...' : `R$ ${valor.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}`}
            </TextoValor>
        </BlocoTotal>
    )

}

export default SaldoResultante;