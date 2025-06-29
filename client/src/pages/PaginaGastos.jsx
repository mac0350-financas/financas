import HeaderComMenu from '../components/HeaderComMenu';
import BotaoInserirTransacao from '../components/Gastos_Receitas/BotaoInserirTransacao';

function PaginaGastos() {
    return (
        <div>
            <HeaderComMenu />
            <BotaoInserirTransacao texto=" Novo Gasto" tipo="gasto" />
        </div>
    );
}

export default PaginaGastos; 