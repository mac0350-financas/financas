import { Button } from '@mui/material';
import { styled } from '@mui/material/styles';
import { useNavigate } from 'react-router-dom';

const TransparentButton = styled(Button)({
    backgroundColor: 'transparent',
    color: '#2F404A',
    textTransform: 'none',
    fontFamily: 'Kantumruy Pro, sans-serif',
    fontWeight: 'bold',
    fontSize: '20px'
});

function HeaderBotaoPaginas({ texto, rota }) {
    const navigate = useNavigate();

    const handleClick = () => {
        navigate(rota);
    };

    return (
        <TransparentButton variant="text" onClick={handleClick}>
            {texto}
        </TransparentButton>
    );
}

export default HeaderBotaoPaginas;