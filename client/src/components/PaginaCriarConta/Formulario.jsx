import React, { useState } from 'react';
import { Box, Alert } from '@mui/material';
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
        if (!formData.nome.trim()) {
            setError('Nome é obrigatório');
            return false;
        }
        if (!formData.email.trim()) {
            setError('Email é obrigatório');
            return false;
        }
        if (!formData.senha.trim()) {
            setError('Senha é obrigatória');
            return false;
        }
        if (formData.senha !== formData.confirmarSenha) {
            setError('As senhas não coincidem');
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
            const response = await fetch('http://localhost:8080/formulario-cadastro', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify({
                    nome: formData.nome,
                    email: formData.email,
                    senha: formData.senha
                }),
            });

            if (response.ok) {
                console.log('Formulário enviado com sucesso');
                setSuccess(true);
                setFormData({ nome: '', email: '', senha: '', confirmarSenha: '' });

                setTimeout(() => {
                     window.location.href = '/fazer-login';
                }, 2000);
            } 
            else {
                const data = await response.json();
                throw new Error(data.message || 'Erro ao criar conta');
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
        <FormularioBox component="form" onSubmit={handleSubmit}>
            {error && <Alert severity="error" sx={{ marginBottom: '16px' }}>{error}</Alert>}
            {success && <Alert severity="success" sx={{ marginBottom: '16px' }}>Conta criada com sucesso!</Alert>}

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
            <BotaoEnviarFormulario disabled={loading}>
                {loading ? 'CRIANDO CONTA...' : 'VOU SER FINATURE'}
            </BotaoEnviarFormulario>
        </FormularioBox>
    );
}

export default Formulario;