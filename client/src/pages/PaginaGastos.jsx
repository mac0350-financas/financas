import HeaderComMenu from '../components/HeaderComMenu';
import BotaoInserirTransacao from '../components/Gastos_Receitas/BotaoInserirTransacao';
import DescricaoTransacoes from '../components/Gastos_Receitas/DescricaoTransacoes';
import SelecaoMes from '../components/Gastos_Receitas/SelecaoMes';
import SelecaoAno from '../components/Gastos_Receitas/SelecaoAno';
import TotalTransacoes from '../components/Gastos_Receitas/TotalTransacoes';
import GraficoTransacoes from '../components/Gastos_Receitas/GraficoTransacoes';
import GradeMetas from '../components/Gastos_Receitas/GradeMetas';
import { Typography, Box } from '@mui/material';
import { cores, espacamento } from '../themes/temas';
import { useState } from 'react';

function PaginaGastos() {
    const [mesSelecionado, setMesSelecionado] = useState(
        new Date().toLocaleString('pt-BR', { month: 'long' }).charAt(0).toUpperCase() + 
        new Date().toLocaleString('pt-BR', { month: 'long' }).slice(1)
    );
    const [indiceMes, setIndiceMes] = useState(new Date().getMonth() + 1);
    const [anoSelecionado, setAnoSelecionado] = useState(new Date().getFullYear());

    const handleSelecionarMes = (mes, indice) => {
        setMesSelecionado(mes);
        setIndiceMes(indice);
        console.log('Mês selecionado:', mes, 'Índice:', indice);
    };

    const handleSelecionarAno = (ano) => {
        setAnoSelecionado(ano);
        console.log('Ano selecionado:', ano);
    };

    const [reloadTrigger, setReloadTrigger] = useState(0);
    const handleTransacaoEnviada = () => {
        setReloadTrigger(prev => prev + 1); // incrementa para forçar reload nos filhos
    };

    const [reloadTriggerMetas, setReloadTriggerMetas] = useState(0);
    const handleMetaEnviada = () => {
        setReloadTriggerMetas(prev => prev + 1); // incrementa para forçar reload nos filhos
    };

    const [reloadTriggerDescricao, setReloadTriggerDescricao] = useState(0);
    const handleDescricaoAlterada = () => {
        setReloadTriggerDescricao(prev => prev + 1); // incrementa para forçar reload nos filhos
    }


    return (
        <div>
            <HeaderComMenu />
            <Box sx={{ p: espacamento.paddingInterno }}>

                <Box sx={{ display: 'flex', flexDirection: 'column', gap: 4 }}>

                    {/* Título separado no topo */}
                    <Typography variant="h3" fontWeight="bold" color={cores.fundoEscuro}>
                        Gastos
                    </Typography>

                    {/* Conteúdo principal: gráfico e controles lado a lado, alinhados no topo */}
                    <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', gap: 4, flexWrap: 'wrap' }}>
                        {/* Gráfico */}
                        <Box sx={{ flex: 1, minWidth: '300px' }}>
                            <GraficoTransacoes key={reloadTrigger + reloadTriggerDescricao} tipo="gastos" mes={indiceMes.toString()} ano={anoSelecionado} />
                        </Box>

                        {/* Controles: seletores em cima, total e botão abaixo */}
                        <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3, minWidth: '250px' }}>
                            <Box sx={{ display: 'flex', gap: 2 }}>
                                <SelecaoMes aoSelecionarMes={handleSelecionarMes} />
                                <SelecaoAno aoSelecionarAno={handleSelecionarAno} />
                            </Box>

                            <TotalTransacoes key={reloadTrigger + reloadTriggerDescricao} tipo="gastos" mes={indiceMes.toString()} ano={anoSelecionado} />
                            <Box sx={{ marginTop: 9 }}>
                                <BotaoInserirTransacao onSuccess={handleTransacaoEnviada} texto="+ Novo Gasto" tipo="gasto" />
                            </Box>
                        </Box>
                    </Box>
                </Box>

                <Box sx={{ width: '100%', mt: 12 }}>
                    {/* Texto alinhado à esquerda */}
                    <Typography variant="h4" fontWeight="bold" mb={4} color={cores.fundoEscuro}>
                                Descrição dos gastos
                    </Typography>

                    {/* Componente centralizado */}
                    <Box sx={{ display: 'flex', justifyContent: 'center' }}>
                        <DescricaoTransacoes onSuccess={handleDescricaoAlterada} key={reloadTrigger + reloadTriggerDescricao} tipo="gastos" mes={indiceMes.toString()} ano={anoSelecionado} />
                    </Box>
                </Box>
                <Box sx={{ width: '100%', mt: 12 }}>
                    <Typography variant="h4" fontWeight="bold" mb={4} color={cores.fundoEscuro}>
                                Metas de gastos
                    </Typography>
                    <GradeMetas key={reloadTriggerMetas + reloadTrigger + reloadTriggerDescricao} onSuccess={handleMetaEnviada} tipo="gastos" />
                </Box>
                
            </Box>
        </div>
    );
}

export default PaginaGastos;