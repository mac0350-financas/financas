import { Routes, Route } from 'react-router-dom';
import PaginaLogin from './pages/PaginaLogin';

function App() {
  return (
    <Routes>
      <Route path="/login" element={<PaginaLogin />} />
    </Routes>
  );
}

export default App;
