import { styled } from '@mui/system';
import { cores } from '../../themes/temas';

export const MainContainer = styled('div')({
  display: 'flex',
  flexDirection: 'column',
  minHeight: '100vh',
  width: '100%',
  margin: 0,
  padding: 0,
  backgroundColor: cores.fundoEscuro,
  position: 'absolute',
  top: 0,
  left: 0,
  right: 0,
  bottom: 0,
});

export const Conteudo = styled('div')({
  display: 'flex',
  flex: '0 0 auto',
  minHeight: '100vh',
  backgroundColor: cores.fundoEscuro
});

export const MetadeEsquerda = styled('div')({
  flex: 1,
  display: 'flex',
  height: '90vh',
  flexDirection: 'column',
  alignItems: 'center',
  justifyContent: 'center',
  marginLeft: '50px',
});

export const MetadeDireita = styled('div')({
  flex: 1,
  display: 'flex',
  height: '90vh',
  alignItems: 'center',
  justifyContent: 'center',
  marginRight: '40px',
});

export const MetadeCima = styled('div')({
  flex: 2,
  display: 'flex',
  alignItems: 'top',
  marginTop: '90px',
  justifyContent: 'center',
  width: '100%',
});

export const MetadeBaixo = styled('div')({
  flex: 3,
  display: 'flex',
  alignItems: 'top',
  justifyContent: 'center',
  marginTop: '20px',
  marginLeft: '80px',
  marginRight: '12%',
  width: '100%',
});

export const SecaoInferior = styled('div')({
  backgroundColor: cores.fundoLandingSecao,
  display: 'flex',
  flexDirection: 'column',
});
