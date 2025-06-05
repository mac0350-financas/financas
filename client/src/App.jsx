import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import LandingPage from './pages/LandingPage';
import PaginaConfiguracoesConta from './pages/PaginaConfiguracoesConta';
import PaginaCriarConta from './pages/PaginaCriarConta';
import PaginaFazerLogin from './pages/PaginaFazerLogin';
import PaginaGastos from './pages/PaginaGastos';
import PaginaInvestimentos from './pages/PaginaInvestimentos';
import PaginaPrincipalUsuario from './pages/PaginaPrincipalUsuario';
import PaginaReceitas from './pages/PaginaReceitas';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LandingPage />} />
        <Route path="/configuracoes-conta" element={<PaginaConfiguracoesConta />} />
        <Route path="/criar-conta" element={<PaginaCriarConta />} />
        <Route path="/fazer-login" element={<PaginaFazerLogin />} />
        <Route path="/gastos" element={<PaginaGastos />} />
        <Route path="/investimentos" element={<PaginaInvestimentos />} />
        <Route path="/principal-usuario" element={<PaginaPrincipalUsuario />} />
        <Route path="/receitas" element={<PaginaReceitas />} />
      </Routes>
    </Router>
  );
}

export default App;
