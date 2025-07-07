import { Box } from "@mui/material";
import { styled } from "@mui/system";
import { LinearProgress, Typography } from "@mui/material";

const CardContainer = styled(Box)({
    width: '100%',
    height: '200px',
    backgroundColor: '#f0f0f0',
    borderRadius: '16px',
    boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)',
});

const StyledLinearProgress = styled(LinearProgress)(({ value, tipo }) => {
    let barColor;
    if (tipo === -1) { // Gasto: quanto menor o progresso, melhor
        if (value <= 50) {
            barColor = '#4caf50'; // Verde
        } else if (value <= 75) {
            barColor = '#ff9800'; // Laranja
        } else if (value <= 100) {
            barColor = '#f44336'; // Vermelho
        } else {
            barColor = '#d32f2f'; // Vermelho escuro (excedido)
        }
    } else if (tipo === 1) { // Receita: quanto maior o progresso, melhor
        if (value <= 50) {
            barColor = '#f44336'; // Vermelho
        } else if (value <= 75) {
            barColor = '#ff9800'; // Laranja
        } else if (value <= 100) {
            barColor = '#4caf50'; // Verde
        } else {
            barColor = '#2e7d32'; // Verde escuro (excedido)
        }
    }

    return {
        height: 20, 
        borderRadius: 16,
        backgroundColor: '#e0e0e0',
        '& .MuiLinearProgress-bar': {
            borderRadius: 5,
            backgroundColor: barColor,
        },
    };
});

const ValueText = styled(Typography)({
    fontSize: '16px', 
    fontWeight: 'bold',
    color: '#000',
    textAlign: 'center',
    marginBottom: '8px', 
});

function CardMeta({ tipo, dataInicial, dataFinal, valorAtual, valorLimite, categoria }) {
    const progresso = Math.min((valorAtual / valorLimite) * 100, 100); // Limita o progresso a 100%

    return (
        <CardContainer>
            <Box sx={{ padding: 2 }}>
                <Typography sx={{ fontWeight: 'bold', fontSize: '18px', marginBottom: 1 }}>
                    Meta de {tipo === -1 ? 'Gasto' : 'Receita'} - {categoria}
                </Typography>
                <Typography sx={{ color: '#555', marginBottom: 4 }}>
                    {`Período: ${dataInicial} - ${dataFinal}`}
                </Typography>
                <ValueText sx={{ marginTop: 2 }}>
                    {`R$ ${valorAtual.toFixed(2)} / R$ ${valorLimite.toFixed(2)}`}
                </ValueText>
                <StyledLinearProgress sx={{ marginTop: 1 }} variant="determinate" value={progresso} tipo={tipo} />
            </Box>
        </CardContainer>
    );
}

export default CardMeta;