import React from 'react';
import { styled } from '@mui/system';
import { Divider } from '@mui/material';
import { cores } from '../themes/temas';
import HeaderBotaoConta from './HeaderBotaoConta';
import HeaderBotaoPaginas from './HeaderBotaoPaginas';

const HeaderContainer = styled('div')({
    backgroundColor: cores.fundoPrimario,
    paddingTop: 16, 
    paddingLeft: 24,
    paddingRight: 24,
});

const LogoContainer = styled('div')({
    display: 'flex',
    alignItems: 'center',
    height: 40,
});

const StyledDivider = styled(Divider)({
    marginTop: 20, 
});

const MainContainer = styled('div')({
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    height: 40,
});

const ContainerBotoes = styled('div')({
    display: 'flex',
    alignItems: 'center',
    gap: 16,
    flex: 1,
    marginLeft: 32,
});

const QuatroBotoes = styled('div')({
    display: 'flex',
    justifyContent: 'space-evenly',
    flex: 1,
});

function HeaderComMenu() {
    return (
        <HeaderContainer>
            <MainContainer>
                <LogoContainer>
                    <img
                        src="/logo-finature.png"
                        alt="Finature logo"
                        style={{ height: 45, objectFit: 'contain' }}
                    />
                </LogoContainer>
                <ContainerBotoes>
                    <QuatroBotoes>
                        <HeaderBotaoPaginas texto="RESUMO" rota="/principal-usuario" />
                        <HeaderBotaoPaginas texto="GASTOS" rota="/gastos" />
                        <HeaderBotaoPaginas texto="RECEITAS" rota="/receitas" />
                        <HeaderBotaoPaginas texto="INVESTIMENTOS" rota="/investimentos" />
                    </QuatroBotoes>
                    <HeaderBotaoConta rota="/configuracoes-conta"/>
                </ContainerBotoes>
            </MainContainer>
            <StyledDivider />
        </HeaderContainer>
    );
}

export default HeaderComMenu;