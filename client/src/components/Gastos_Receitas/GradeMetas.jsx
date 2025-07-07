 import { Box, Typography } from '@mui/material';
 import { cores } from '../../themes/temas';
 
 function GradeMetas() {
    return (
        <Box sx={{ width: '100%', mt: 12 }}>
            <Typography variant="h4" fontWeight="bold" mb={4} color={cores.fundoEscuro}>
                        Metas de gastos
            </Typography>
        </Box>
    );
 }

 export default GradeMetas;