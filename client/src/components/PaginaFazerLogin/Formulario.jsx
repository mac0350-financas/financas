import CampoDeInformacao from '../PaginaCriarConta/CampoDeInformacao';
import BotaoEnviarFormulario from '../PaginaCriarConta/BotaoEnviarFormulario';

import React, { useState } from 'react';
import { Box, Alert, Typography } from '@mui/material';
import { styled } from '@mui/system';

const FormularioBox = styled(Box)({
    display: 'flex',
    flexDirection: 'column',
    gap: '24px',
    width: '524px',
    maxWidth: '50vw',
    backgroundColor: 'rgba(256, 256, 256, 0.5)',
    padding: '24px',
    borderRadius: '16px',
    borderTop: '4px solid #ccc',
    borderBottom: '4px solid #ccc',
});

const TituloTexto = styled(Typography)({
    fontSize: '24px',
    fontFamily: 'Kantumruy Pro, sans-serif',
    color: '#8899A6',
    fontWeight: 'bold',
    textAlign: 'center',
    marginBottom: '24px',
});

const Container = styled('div')({
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    minHeight: '100vh',
    width: '100%',
});

function Formulario() {

    const [formData, setFormData] = useState({
        email: '',
        senha: '',
    });

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState(false);

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const validateForm = () => {
        if (!formData.email.trim()) {
            setError('Email é obrigatório');
            return false;
        }
        if (!formData.senha.trim()) {
            setError('Senha é obrigatória');
            return false;
        }
        return true;
    };

    const handleSubmit = async (e) => { 
        e.preventDefault();
        setError('');
        setSuccess(false);

        if (!validateForm()) return;

        setLoading(true);

        try {
            const response = await fetch('http://localhost:8080/formulario-login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify({
                    email: formData.email,
                    senha: formData.senha
                }),
            });

            if (response.ok) {
                console.log('Formulário enviado com sucesso');
                setSuccess(true);
                setFormData({ email: '', senha: '' });

                setTimeout(() => {
                     window.location.href = '/principal-usuario';
                }, 1500);
            } 
            else {
                const data = await response.json();
                throw new Error(data.message || 'Erro ao fazer login');
            }

        } 
        
        catch (error) {
            console.error('Erro:', error);
            setError(error.message || 'Erro de conexão. Tente novamente.');
        } 
        
        finally {
            setLoading(false);
        }
    };

    return (
        <Container>
            <TituloTexto>Entre na sua conta e economize agora! 😍</TituloTexto>
            {error && <Alert severity="error" sx={{ marginBottom: '16px', width: '1024px' }}>{error}</Alert>}
            {success && <Alert severity="success" sx={{ marginBottom: '16px', width: '1024px' }}>Conta criada com sucesso!</Alert>}
            <FormularioBox component="form" onSubmit={handleSubmit}>
                <CampoDeInformacao
                    text="Insira seu email"
                    name="email"
                    type="email"
                    value={formData.email}
                    onChange={handleChange}
                />
                <CampoDeInformacao
                    text="Insira sua senha"
                    name="senha"
                    type="password"
                    value={formData.senha}
                    onChange={handleChange}
                />
                <BotaoEnviarFormulario disabled={loading}>
                    {loading ? 'FAZENDO LOGIN...' : 'ACESSAR CONTA'}
                </BotaoEnviarFormulario>
            </FormularioBox>
        </Container>
    );

}

export default Formulario;