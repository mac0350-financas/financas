// LandingPage.jsx  (trecho relevante)

import React from 'react';
import HeaderEscura from '../components/HeaderEscura';
import TituloSecao from '../components/LandingPage/TituloSecao';
import SecaoSobre from '../components/LandingPage/SecaoSobre';
import ImagemPrincipal from '../components/LandingPage/ImagemPrincipal';
import {
  MainContainer,
  Conteudo,
  MetadeEsquerda,
  MetadeDireita,
  MetadeCima,
  MetadeBaixo
} from '../components/LandingPage/LayoutPrincipal';

function LandingPage() {
  return (
    <MainContainer>
      <HeaderEscura/>
      <Conteudo>
        <MetadeEsquerda>
          <MetadeCima>
            <TituloSecao />
          </MetadeCima>
          <MetadeBaixo>
            <SecaoSobre />
          </MetadeBaixo>
        </MetadeEsquerda>
        <MetadeDireita>
          <ImagemPrincipal src="../../public/landing-branca.png" />
        </MetadeDireita>
      </Conteudo>
    </MainContainer>
  );
}

export default LandingPage;