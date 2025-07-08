import { Typography, Box, styled } from '@mui/material';
import { useState, useEffect } from 'react';

const BlocoTotal = styled(Box)({
    width: '30vw',
    height: '160px',
    borderRadius: '30px',
    backgroundColor: 'transparent',
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

function TotalTransacoes({ tipo, mes, ano }) {
    const [valor, setValor] = useState(0);
    const [loading, setLoading] = useState(false);

    const BlocoTotalDynamic = styled(BlocoTotal)({
        backgroundColor: tipo === 'gastos' ? '#FDECEC' : '#E8F5E9',
    });

    const fetchTotal = async () => {
        if (!mes || !ano || !tipo) return;
        
        setLoading(true);
        try {
            const tipoNumero = tipo === 'gastos' ? -1 : 1;
            const response = await fetch(`/api/transacoes/total?tipo=${tipoNumero}&mes=${mes}&ano=${ano}`);
            const data = await response.json();
            setValor(data.total || 0);
        } catch (error) {
            console.error('Erro ao buscar total:', error);
            setValor(0);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchTotal();
    }, [tipo, mes, ano]);

    return (
        <BlocoTotalDynamic>
            <TextoTotal>Total de {tipo?.toLowerCase()}</TextoTotal>
            <TextoValor>
                {loading ? 'Carregando...' : `R$ ${valor.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}`}
            </TextoValor>
        </BlocoTotalDynamic>
    );
}

export default TotalTransacoes;