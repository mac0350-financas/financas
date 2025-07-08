import React, { useEffect, useState } from 'react';
import { Box, Typography } from '@mui/material';
import { cores } from '../../themes/temas';
import CardMeta from './CardMeta';
import BotaoNovaMeta from './BotaoNovaMeta';

function GradeMetas({ onSuccess, tipo }) {
    const [metas, setMetas] = useState([]);
    const [loading, setLoading] = useState(false);

    const fetchMetas = async () => {
        if (!tipo) return;

        setLoading(true);
        try {
            const tipoNumero = tipo === 'gastos' ? -1 : 1;
            const response = await fetch(`/api/metas/lista?tipo=${tipoNumero}`);
            const data = await response.json();
            setMetas(Array.isArray(data.lista) ? data.lista : []);
        } catch (error) {
            console.error('Erro ao buscar metas:', error);
            setMetas([]);
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        fetchMetas(); 
    }, [tipo]);


    return (
        <Box sx={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: 4 }}>
            <BotaoNovaMeta onSuccess={onSuccess} tipo={tipo === 'gastos' ? 'gasto' : 'receita'} />
            {loading ? (
                <Typography sx={{ gridColumn: 'span 4', textAlign: 'center' }}>Carregando metas...</Typography>
            ) : (
                metas.map((meta, index) => (
                    <CardMeta key={index} tipo={meta.tipoId} dataInicial={meta.dataInicial} dataFinal={meta.dataFinal} 
                              valorAtual={meta.valorAtual} valorLimite={meta.valorLimite} categoria={meta.categoria}/>
                ))
            )}
        </Box>
    );
}

export default GradeMetas;