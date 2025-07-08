import { useEffect, useState } from 'react';
import axios from 'axios';
import { Typography, Box, styled, Container, Paper, Card, CardContent, List, ListItem, ListItemText, ListItemIcon, Divider, Chip } from '@mui/material';
import { cores, espacamento } from '../themes/temas';
import HeaderComMenu from '../components/HeaderComMenu';
import BotaoInserirTransacao from '../components/Gastos_Receitas/BotaoInserirTransacao';
import SaldoResultante from '../components/PaginaPrincipalUsuario/SaldoResultante';
import InfoOutlinedIcon from '@mui/icons-material/InfoOutlined';
import SavingsOutlinedIcon from '@mui/icons-material/SavingsOutlined';
import CompareArrowsIcon from '@mui/icons-material/CompareArrows';
import TrendingUpIcon from '@mui/icons-material/TrendingUp';
import HomeOutlinedIcon from '@mui/icons-material/HomeOutlined';
import RestaurantOutlinedIcon from '@mui/icons-material/RestaurantOutlined';
import AccountBalanceWalletOutlinedIcon from '@mui/icons-material/AccountBalanceWalletOutlined';
import LightbulbOutlinedIcon from '@mui/icons-material/LightbulbOutlined';

// Styled component for buttons container
const BotoesContainer = styled(Box)(({ theme }) => ({
    position: 'relative',
    display: 'flex',
    justifyContent: 'center',
    gap: '16px',
    width: '100%',
    '&::before': {
        content: '""',
        position: 'absolute',
        top: '-25px',
        left: '50%',
        transform: 'translateX(-50%)',
        width: '150px',
        height: '3px',
        background: 'linear-gradient(90deg, rgba(52, 152, 219, 0), rgba(52, 152, 219, 1), rgba(52, 152, 219, 0))',
        borderRadius: '3px',
    }
}));

// Styled component for modern card
const ModernCard = styled(Card)(({ theme }) => ({
    backgroundColor: 'white',
    borderRadius: '12px',
    boxShadow: '0 4px 12px rgba(0, 0, 0, 0.08)',
    marginBottom: '16px',
    transition: 'transform 0.3s ease, box-shadow 0.3s ease',
    '&:hover': {
        transform: 'translateY(-5px)',
        boxShadow: '0 8px 16px rgba(0, 0, 0, 0.12)',
    },
}));

function PaginaPrincipalUsuario() {

    const [usuario, setUsuario] = useState(null);
    const [taxas, setTaxas] = useState({
        poupancaAnual: 0,
        selicAnual: 0
    });

    useEffect(() => {
        axios.get('http://localhost:8080/usuario-logado', { withCredentials: true })
            .then(response => {
                setUsuario(response.data);
            })
            .catch(error => {
                console.error('Erro ao buscar o usuário logado:', error);
            });

        axios.get('http://localhost:8080/api/investimento/taxas')
            .then(response => {
                setTaxas(response.data);
            })
            .catch(error => {
                console.error('Erro ao buscar as taxas:', error);
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

    const [reloadTrigger, setReloadTrigger] = useState(0);
    const handleTransacaoEnviada = () => {
        setReloadTrigger(prev => prev + 1); // incrementa para forçar reload nos filhos
    };

    return (
        <div>
            <HeaderComMenu />
            <Box sx={{ p: espacamento.paddingInterno }}>
                <Typography variant="h2" fontWeight="bold" color={cores.fundoEscuro}>
                    {saudacao + ', ' + (usuario ? usuario.nome : 'Usuário') + '!' + 
                    (currentHour < 12 ? ' ☀️' : currentHour < 18 ? ' 🌤️' : ' 🌙')}
                </Typography>
                
                <Container maxWidth="100vw">
                    <Box sx={{ 
                        mt: 8, 
                        display: 'flex',
                        flexDirection: 'row',
                        gap: 4,
                        width: '100%',
                        minHeight: '450px', // Maintain consistent minimum height
                        alignItems: 'stretch', // Ensure children stretch to fill container height
                    }}>
                        {/* Left Side */}
                        <Box sx={{ 
                            display: 'flex',
                            flexDirection: 'column',
                            alignItems: 'flex-start',
                            width: '50%',
                            justifyContent: 'space-between',
                            height: '100%'
                        }}>
                            <Box sx={{ 
                                width: '100%', 
                                display: 'flex', 
                                justifyContent: 'center',
                                flexGrow: 1,
                                mb: 4
                            }}>
                                <SaldoResultante key={reloadTrigger} mes={mesAtual} ano={anoAtual} />
                            </Box>
                            
                            <BotoesContainer sx={{ paddingTop: '30px' }}>
                                <BotaoInserirTransacao onSuccess={handleTransacaoEnviada} texto="Nova Receita" tipo="receita" />
                                <BotaoInserirTransacao onSuccess={handleTransacaoEnviada} texto="Novo Gasto" tipo="gasto" />
                            </BotoesContainer>
                        </Box>

                        {/* Right Side - Financial Tips */}
                        <Box sx={{ 
                            display: 'flex',
                            flexDirection: 'column',
                            width: '50%',
                            height: '100%', // Ensure full height
                        }}>
                            {/* Enhanced Financial Tip - Match height exactly */}
                            <ModernCard sx={{ 
                                backgroundColor: 'rgba(52, 152, 219, 0.1)',
                                height: '100%', // Fill all available height
                                display: 'flex',
                                flexDirection: 'column',
                                marginBottom: 0,
                                overflow: 'hidden',
                            }}>
                                <CardContent sx={{ 
                                    flex: 1,
                                    display: 'flex',
                                    flexDirection: 'column',
                                    p: 3, // Slightly reduced padding for better content sizing
                                }}>
                                    <Box sx={{
                                        display: 'flex',
                                        flexDirection: 'column',
                                        height: '100%',
                                    }}>
                                        <Box sx={{ 
                                            display: 'flex', 
                                            alignItems: 'center', 
                                            mb: 2,
                                            borderBottom: '2px solid rgba(52, 152, 219, 0.3)',
                                            paddingBottom: 1.5
                                        }}>
                                            <LightbulbOutlinedIcon sx={{ color: '#FFD700', mr: 1, fontSize: 36 }} />
                                            <Typography variant="h4" fontWeight="600" color={cores.fundoEscuro}>
                                                Dica do dia 💡
                                            </Typography>
                                        </Box>
                                        
                                        <Box sx={{ 
                                            flex: 1, // Make this box fill available space
                                            display: 'flex',
                                            flexDirection: 'column',
                                            justifyContent: 'space-evenly', // Even spacing between children
                                        }}>
                                            <Typography variant="h5" fontWeight="600" sx={{ textAlign: 'center', mb: 2 }}>
                                                Divida sua renda: Regra 50/30/20
                                            </Typography>
                                            
                                            <Box sx={{ 
                                                display: 'flex',
                                                justifyContent: 'space-around',
                                                gap: 2,
                                                flexWrap: 'wrap',
                                                py: 2, // Add padding instead of margin for better sizing
                                            }}>
                                                {/* 50% Box - Adjusted height */}
                                                <Box sx={{ 
                                                    borderRadius: 3,
                                                    p: 2,
                                                    width: '30%',
                                                    minWidth: '120px',
                                                    minHeight: '140px', // Set consistent height
                                                    textAlign: 'center',
                                                    backgroundColor: 'rgba(52, 152, 219, 0.15)',
                                                    display: 'flex',
                                                    flexDirection: 'column',
                                                    alignItems: 'center',
                                                    justifyContent: 'center'
                                                }}>
                                                    <Typography variant="h2" fontWeight="bold" color="#1976D2" sx={{ mb: 1 }}>
                                                        50%
                                                    </Typography>
                                                    <HomeOutlinedIcon sx={{ fontSize: 38, color: '#1976D2', mb: 1.5 }} />
                                                    <Typography variant="body1" sx={{ fontSize: '1.2rem', fontWeight: 500 }}>
                                                        Necessidades
                                                    </Typography>
                                                </Box>

                                                {/* 30% Box - Adjusted height */}
                                                <Box sx={{ 
                                                    borderRadius: 3,
                                                    p: 2,
                                                    width: '30%',
                                                    minWidth: '120px',
                                                    minHeight: '140px', // Set consistent height
                                                    textAlign: 'center',
                                                    backgroundColor: 'rgba(155, 89, 182, 0.15)',
                                                    display: 'flex',
                                                    flexDirection: 'column',
                                                    alignItems: 'center',
                                                    justifyContent: 'center'
                                                }}>
                                                    <Typography variant="h2" fontWeight="bold" color="#8E44AD" sx={{ mb: 1 }}>
                                                        30%
                                                    </Typography>
                                                    <RestaurantOutlinedIcon sx={{ fontSize: 38, color: '#8E44AD', mb: 1.5 }} />
                                                    <Typography variant="body1" sx={{ fontSize: '1.2rem', fontWeight: 500 }}>
                                                        Desejos
                                                    </Typography>
                                                </Box>

                                                {/* 20% Box - Adjusted height */}
                                                <Box sx={{ 
                                                    borderRadius: 3,
                                                    p: 2,
                                                    width: '30%',
                                                    minWidth: '120px',
                                                    minHeight: '140px', // Set consistent height
                                                    textAlign: 'center',
                                                    backgroundColor: 'rgba(46, 204, 113, 0.15)',
                                                    display: 'flex',
                                                    flexDirection: 'column',
                                                    alignItems: 'center',
                                                    justifyContent: 'center'
                                                }}>
                                                    <Typography variant="h2" fontWeight="bold" color="#27AE60" sx={{ mb: 1 }}>
                                                        20%
                                                    </Typography>
                                                    <AccountBalanceWalletOutlinedIcon sx={{ fontSize: 38, color: '#27AE60', mb: 1.5 }} />
                                                    <Typography variant="body1" sx={{ fontSize: '1.2rem', fontWeight: 500 }}>
                                                        Poupança
                                                    </Typography>
                                                </Box>
                                            </Box>

                                            <Typography variant="body1" sx={{ 
                                                fontSize: '1.3rem', 
                                                textAlign: 'center', 
                                                mt: 2 
                                            }}>
                                                O segredo para um orçamento equilibrado é dividir sua renda nessas três categorias.
                                            </Typography>
                                        </Box>
                                    </Box>
                                </CardContent>
                            </ModernCard>
                        </Box>
                    </Box>
                    
                    {/* Taxas de Investimento */}
                    <Box sx={{ mt: 10, mb: 6 }}>
                        <ModernCard sx={{ 
                            background: 'linear-gradient(145deg, rgba(255,255,255,1) 0%, rgba(240,249,255,1) 100%)',
                            borderLeft: '5px solid #4CAF50',
                        }}>
                            <CardContent sx={{ p: 4 }}>
                                <Box sx={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between', flexWrap: 'wrap', gap: 4 }}>
                                    <Box sx={{ flex: '1 1 500px' }}>
                                        <Box sx={{ display: 'flex', alignItems: 'center', mb: 3 }}>
                                            <SavingsOutlinedIcon sx={{ color: '#4CAF50', mr: 2, fontSize: 36 }} />
                                            <Typography variant="h4" fontWeight="600" color={cores.fundoEscuro}>
                                                Oportunidades de Investimento
                                            </Typography>
                                        </Box>
                                        
                                        <Typography variant="body1" sx={{ mb: 3, fontSize: '1.25rem', lineHeight: 1.5 }}>
                                            Você sabia que investir seu dinheiro pode trazer rendimentos significativamente maiores do que deixá-lo parado? 
                                            Compare as taxas atuais e comece a fazer seu dinheiro trabalhar para você!
                                        </Typography>
                                    </Box>
                                    
                                    <Box sx={{ 
                                        display: 'flex', 
                                        flex: '1 1 400px',
                                        justifyContent: 'center',
                                        gap: 4, 
                                        p: 3,
                                        borderRadius: 4,
                                        background: 'linear-gradient(135deg, rgba(255,255,255,0.9) 0%, rgba(240,249,255,0.9) 100%)',
                                        boxShadow: '0 8px 32px rgba(0,0,0,0.08)',
                                        position: 'relative',
                                        overflow: 'hidden',
                                        '&::before': {
                                            content: '""',
                                            position: 'absolute',
                                            top: 0,
                                            left: 0,
                                            width: '100%',
                                            height: '5px',
                                            background: 'linear-gradient(90deg, #1976D2, #4CAF50)',
                                        }
                                    }}>
                                        <Box sx={{ 
                                            textAlign: 'center',
                                            display: 'flex',
                                            flexDirection: 'column',
                                            justifyContent: 'center',
                                            alignItems: 'center',
                                            padding: 2,
                                            borderRadius: 3,
                                            backgroundColor: 'rgba(25, 118, 210, 0.08)',
                                            width: '180px',
                                            height: '180px',
                                        }}>
                                            <Typography variant="h6" fontWeight="500" sx={{ mb: 1, fontSize: '1.3rem' }}>
                                                Poupança
                                            </Typography>
                                            <Typography variant="h3" fontWeight="700" color="#1976D2" sx={{ mb: 0.5 }}>
                                                {taxas.poupancaAnual.toFixed(2)}%
                                            </Typography>
                                            <Typography variant="body1" color="text.secondary" sx={{ fontSize: '1.1rem' }}>
                                                ao ano
                                            </Typography>
                                        </Box>
                                        
                                        <Box sx={{ 
                                            display: 'flex', 
                                            alignItems: 'center', 
                                            justifyContent: 'center'
                                        }}>
                                            <CompareArrowsIcon sx={{ color: 'text.secondary', fontSize: 36 }} />
                                        </Box>
                                        
                                        <Box sx={{ 
                                            textAlign: 'center',
                                            display: 'flex',
                                            flexDirection: 'column',
                                            justifyContent: 'center',
                                            alignItems: 'center',
                                            padding: 2,
                                            borderRadius: 3,
                                            backgroundColor: 'rgba(76, 175, 80, 0.08)',
                                            width: '180px',
                                            height: '180px',
                                        }}>
                                            <Typography variant="h6" fontWeight="500" sx={{ mb: 1, fontSize: '1.3rem' }}>
                                                Selic
                                            </Typography>
                                            <Typography variant="h3" fontWeight="700" color="#4CAF50" sx={{ mb: 0.5 }}>
                                                {taxas.selicAnual.toFixed(2)}%
                                            </Typography>
                                            <Typography variant="body1" color="text.secondary" sx={{ fontSize: '1.1rem' }}>
                                                ao ano
                                            </Typography>
                                        </Box>
                                    </Box>
                                </Box>
                                
                                <Box sx={{ 
                                    display: 'flex', 
                                    alignItems: 'center', 
                                    mt: 4, 
                                    pt: 3,
                                    borderTop: '1px dashed rgba(0, 0, 0, 0.12)',
                                    backgroundColor: 'rgba(76, 175, 80, 0.05)',
                                    padding: 2,
                                    borderRadius: 2
                                }}>
                                    <TrendingUpIcon sx={{ color: '#4CAF50', mr: 2, fontSize: 32 }} />
                                    <Typography variant="body1" fontWeight="500" sx={{ fontSize: '1.25rem' }}>
                                        Sugestão: Investir em títulos indexados à Selic pode render até{' '}
                                        <Box component="span" sx={{ 
                                            color: '#4CAF50', 
                                            fontWeight: 'bold',
                                            fontSize: '1.3rem',
                                            px: 0.5
                                        }}>
                                            {(taxas.selicAnual - taxas.poupancaAnual).toFixed(2)}%
                                        </Box> a mais do que a poupança tradicional.
                                    </Typography>
                                </Box>
                            </CardContent>
                        </ModernCard>
                    </Box>
                </Container>
            </Box>
        </div>
    );
}

export default PaginaPrincipalUsuario;