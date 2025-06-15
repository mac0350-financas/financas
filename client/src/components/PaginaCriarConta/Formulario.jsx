import React, { useState } from 'react';
import { Box } from '@mui/material';
import { styled } from '@mui/system';
import CampoDeInformacao from './CampoDeInformacao';
import BotaoEnviarFormulario from './BotaoEnviarFormulario';

const FormularioBox = styled(Box)({
    display: 'flex',
    flexDirection: 'column',
    gap: '16px',
    maxWidth: '50%',
    margin: '0 auto',
});

function Formulario() {
    const [formData, setFormData] = useState({
        nome: '',
        email: '',
        senha: '',
        confirmarSenha: ''
    });

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        // Add form submission logic here
        console.log(formData);
    };

    return (
        <FormularioBox component="form" onSubmit={handleSubmit}>
            <CampoDeInformacao
                text="Como gostaria de ser chamado?"
                name="nome"
                type="text"
                value={formData.nome}
                onChange={handleChange}
            />
            <CampoDeInformacao
                text="Qual é o seu e-mail?"
                name="email"
                type="email"
                value={formData.email}
                onChange={handleChange}
            />
            <CampoDeInformacao
                text="Crie uma senha"
                name="senha"
                type="password"
                value={formData.senha}
                onChange={handleChange}
            />
            <CampoDeInformacao
                text="Confirme sua senha"
                name="confirmarSenha"
                type="password"
                value={formData.confirmarSenha}
                onChange={handleChange}
            />
            <BotaoEnviarFormulario>
                VOU SER FINATURE
            </BotaoEnviarFormulario>
        </FormularioBox>
    );
}

export default Formulario;