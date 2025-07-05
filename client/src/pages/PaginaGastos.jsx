import HeaderComMenu from '../components/HeaderComMenu';
import BotaoInserirTransacao from '../components/Gastos_Receitas/BotaoInserirTransacao';
import DescricaoTransacoes from '../components/Gastos_Receitas/DescricaoTransacoes';
import SelecaoMes from '../components/Gastos_Receitas/SelecaoMes';
import SelecaoAno from '../components/Gastos_Receitas/SelecaoAno';
import TotalTransacoes from '../components/Gastos_Receitas/TotalTransacoes';
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


    return (
        <div>
            <HeaderComMenu />
            <BotaoInserirTransacao texto=" Novo Gasto" tipo="gasto" />
            <SelecaoMes aoSelecionarMes={handleSelecionarMes} />
            <SelecaoAno aoSelecionarAno={handleSelecionarAno} />
            <TotalTransacoes tipo={"gastos"} mes={indiceMes.toString()} ano={anoSelecionado} />
            <DescricaoTransacoes tipo={"gastos"} mes={indiceMes.toString()} ano={anoSelecionado} />
        </div>
    );
}

export default PaginaGastos;