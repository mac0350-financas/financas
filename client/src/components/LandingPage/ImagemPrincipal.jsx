import React from 'react';
import { styled } from '@mui/system';

const Imagem = styled('img')({
  maxWidth: '100%',
  maxHeight: '100%',
  width: '85%',
  height: 'auto',
  objectFit: 'contain',
  borderRadius: '16px',
  filter: 'drop-shadow(0px 0px 20px rgba(255, 255, 255, 0.3))',
});

const ImagemPrincipal = ({ src, alt = "Finature App" }) => (
  <Imagem src={src} alt={alt} />
);

export default ImagemPrincipal;
