import React from 'react';
import { Box, Divider } from '@mui/material';
import { cores } from '../themes/temas';

const Header = () => {
  return (
    <Box sx={{ backgroundColor: cores.fundoBranco, pt: 2, px: 3 }}>
      <Box sx={{ display: 'flex', alignItems: 'center', height: 40 }}>
        <img
          src="/logo-finature.png"
          alt="Finature logo"
          style={{ height: 45}}
        />
      </Box>
      <Divider sx={{mt: 2.5}} />
    </Box>
  );
};

export default Header;