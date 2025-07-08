import React, { useEffect, useState } from 'react';
import { PieChart } from '@mui/x-charts/PieChart';
import { Typography, Box, styled } from '@mui/material';

const StyledLoadingText = styled(Typography)(({ theme }) => ({
    textAlign: 'center',
    color: theme.palette.text.secondary,
    fontStyle: 'italic',
}));

const StyledChartContainer = styled(Box)(({ theme }) => ({
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    padding: theme.spacing(3),
    borderRadius: '24px',
    background: `linear-gradient(135deg, ${theme.palette.background.default} 0%, ${theme.palette.grey[200]} 100%)`,
    border: `1px solid ${theme.palette.divider}`,
    boxShadow: '0px 6px 15px rgba(0, 0, 0, 0.1)',
    '& .MuiChartsLegend-root': {
        '& text': {
            fontSize: '16px',
            fontWeight: 600,
        }
    }
}));

const StyledPieChart = styled(PieChart)(({ theme }) => ({
    '& .MuiChartsAxis-root': {
        '& .MuiChartsAxis-tickLabel': {
            fontSize: '14px',
            fontWeight: 500,
        }
    },
    '& .MuiChartsTooltip-root': {
        backgroundColor: theme.palette.background.paper,
        border: `1px solid ${theme.palette.divider}`,
        borderRadius: theme.shape.borderRadius,
        boxShadow: '0px 4px 10px rgba(0, 0, 0, 0.2)',
        '& .MuiChartsTooltip-table': {
            '& td': {
                fontSize: '14px',
                fontWeight: 500,
            }
        }
    },
    '& .MuiChartsPieArc-root': {
        filter: 'drop-shadow(0px 4px 8px rgba(0,0,0,0.15))',
        transition: 'all 0.3s ease-in-out',
        '&:hover': {
            filter: 'drop-shadow(0px 6px 12px rgba(0,0,0,0.25))',
            transform: 'scale(1.05)',
        }
    },
    '& .MuiChartsLegend-series': {
        '& text': {
            fontSize: '16px !important',
            fontWeight: '600 !important',
            fill: theme.palette.text.primary,
        }
    }
}));

function GraficoTransacoes({ tipo, mes, ano }) {
    const [dados, setDados] = useState([]);
    const [loading, setLoading] = useState(false);

    const fetchDados = async () => {
        if (!tipo || !mes || !ano) return;

        setLoading(true);
        try {
            const tipoNumero = tipo === 'gastos' ? -1 : 1;
            const response = await fetch(`/api/transacoes/grafico?tipo=${tipoNumero}&mes=${mes}&ano=${ano}`);
            const data = await response.json();
            const lista = data.listaGrafico || data;
            const formatado = lista.map((item, index) => ({
                id: index,
                value: item.total,
                label: item.categoria,
            }));
            setDados(formatado);
        } 
        
        catch (err) {
            console.error('Erro ao buscar gráfico:', err);
            setDados([]);
        } 
        
        finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchDados();
    }, [tipo, mes, ano]);

    const modernColors = [
        '#FF1744', '#00BCD4', '#FF9800', '#4CAF50', '#9C27B0',
        '#795548', '#607D8B', '#E91E63', '#2196F3', '#8BC34A',
        '#FFC107', '#3F51B5', '#FF5722', '#009688', '#CDDC39',
        '#673AB7', '#F44336', '#00E676', '#FFD600', '#1976D2'
    ];

    return (
        <Box sx={{ width: '96%', height: '100%' }}>
            {loading ? (
                <StyledLoadingText>Carregando gráfico...</StyledLoadingText>
            ) : dados.length > 0 ? (
                <StyledChartContainer sx={{ width: '100%', height: '400px' }}>
                    <StyledPieChart
                        series={[{
                            data: dados,
                            highlightScope: { faded: 'global', highlighted: 'item' },
                            faded: { innerRadius: 30, additionalRadius: -30, color: 'gray' },
                            innerRadius: 20,
                            outerRadius: 120,
                            paddingAngle: 2,
                            cornerRadius: 5,
                        }]}
                        colors={modernColors}
                        slotProps={{
                            legend: {
                                direction: 'column',
                                position: { vertical: 'middle', horizontal: 'right' },
                                padding: 0,
                            },
                        }}
                    />
                </StyledChartContainer>
            ) : (
                <StyledLoadingText>Nenhum dado disponível para exibir</StyledLoadingText>
            )}
        </Box>
    );
}

export default GraficoTransacoes;
