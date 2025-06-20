import Formulario from '../components/PaginaFazerLogin/Formulario';
import Header from '../components/Header';
import { styled } from '@mui/system';

const MainContainer = styled('div')({
    display: 'flex',
    flexDirection: 'column',
    minHeight: '100vh',
});

const Conteudo = styled('div')({
    display: 'flex',
    flex: 1,
});

const MetadeEsquerda = styled('div')({
    flex: 1,
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
});

const MetadeDireita = styled('div')({
    flex: 1,
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#f5f5f5', 
});

const Imagem = styled('img')({
    maxWidth: '100%',
    maxHeight: '100%',
    objectFit: 'contain',
});

function PaginaFazerLogin() {
    return (
        <MainContainer>
            <Header/>
            <Conteudo>
                <MetadeEsquerda>
                    <Formulario />
                </MetadeEsquerda>
                <MetadeDireita>
                    <Imagem src="../../public/login-economizando.png" alt="Login illustration" />
                </MetadeDireita>
            </Conteudo>
        </MainContainer>
    );
}

export default PaginaFazerLogin;