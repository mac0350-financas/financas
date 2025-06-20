import { Button } from '@mui/material';
import { styled } from '@mui/system';
import { useNavigate } from 'react-router-dom';

const StyledButton = styled(Button)({
    width: 50,
    height: 50,
    borderRadius: '50%',
    minWidth: 50,
    padding: 0,
    fontWeight: 'bold',
    backgroundColor: 'white',
    borderColor: '#2F404A',
    borderWidth: 1,
    color: '#2F404A',
});

function HeaderBotaoConta({ rota }) {
    const navigate = useNavigate();

    const handleClick = () => {
        navigate(rota);
    };

    return <StyledButton variant="outlined" onClick={handleClick}>👤</StyledButton>;
}

export default HeaderBotaoConta; 
