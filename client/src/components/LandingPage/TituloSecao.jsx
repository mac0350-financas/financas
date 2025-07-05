import React from 'react';
import { Typography } from '@mui/material';
import { titulo } from '../../themes/temas';

const TituloSecao = () => (
  <Typography
    variant="h4"
    align="left"
    sx={{
      color: titulo.cor,
      fontSize: titulo.tamanho,
      fontFamily: titulo.fontePadrao,
      fontWeight: titulo.pesoNegrito,
      mb: 4,
    }}
  >
    Obtenha já o controle de toda a sua vida financeira. <br/>Seu eu do futuro agradece! 🚀
  </Typography>
);

export default TituloSecao;
