import React from 'react';
import { TextField, Typography } from '@mui/material';
import { styled } from '@mui/system';

const Campo = styled(TextField)({
    height: '40px',
    width: '100%',
    backgroundColor: '#D9D9D9',
    borderRadius: '16px',
    '& .MuiInputBase-root': {
        height: '40px',
        borderRadius: '16px',
    },
    // Remove a borda do campo de input
    '& .MuiOutlinedInput-notchedOutline': {
        border: 'none',
    },
});

const Texto = styled(Typography)({
    fontSize: '16px',
    fontFamily: 'Kantumruy Pro, sans-serif',
    fontWeight: 'bold',
});

function CampoDeInformacao({ text, name, type, value, onChange, ...props }) {
    return (
        <div>
            <Texto>{text}</Texto>
            <Campo
                variant="outlined"
                name={name}
                type={type}
                value={value}
                onChange={onChange}
                required
                fullWidth
            />
        </div>
    );
}

export default CampoDeInformacao;
