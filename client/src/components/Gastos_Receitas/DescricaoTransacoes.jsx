import React, { useEffect, useState } from 'react';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Typography } from '@mui/material';
import { styled } from '@mui/system';
import IconButton from '@mui/material/IconButton';
import DeleteIcon from '@mui/icons-material/Delete';


const StyledTableHead = styled(TableHead)(({ theme }) => ({
  position: 'sticky',
  top: 0,
  zIndex: 1,
  '& .MuiTableCell-head': {
    backgroundColor: '#fafafa',
    color: '#424242',
    fontWeight: 600,
    fontSize: '16px',
    padding: '20px 16px',
    borderBottom: '2px solid #e0e0e0',
    textTransform: 'uppercase',
    letterSpacing: '0.5px',
  }
}));

const StyledTableBody = styled(TableBody)(({ theme }) => ({
    '& .MuiTableRow-root': {
        borderBottom: 'none',
        transition: 'background-color 0.2s ease',
    },
    '& .MuiTableRow-root:hover': {
        backgroundColor: '#f8f9fa',
    },
    '& .MuiTableCell-root': {
        borderBottom: '1px solid #f0f0f0',
        fontSize: '18px',
        padding: '16px',
        color: '#616161',
    }
}));

const StyledTableContainer = styled(TableContainer)(({ theme }) => ({
    boxShadow: 'none',
    border: '1px solid #e0e0e0',
    borderRadius: '16px',
    maxWidth: '100vw',
    width: '100vw',
    maxHeight: '400px',
    overflowY: 'auto',
}));

function DescricaoTransacoes({ onSuccess, tipo, mes, ano }) {
    const [linhas, setLinhas] = useState([]);
    const [loading, setLoading] = useState(false);

    const fetchLinhas = async () => {
        if (!mes || !ano || !tipo) return;

    setLoading(true);
    try {
        const tipoNumero = tipo === 'gastos' ? -1 : 1;
        const response = await fetch(`/api/transacoes/lista?tipo=${tipoNumero}&mes=${mes}&ano=${ano}`);
        const data = await response.json(); 
        setLinhas(Array.isArray(data.lista) ? data.lista : []);
    } catch (error) {
        console.error('Erro ao buscar descrições:', error);
        setLinhas([]);
    } finally {
        setLoading(false);
    }
  };

    useEffect(() => {
        fetchLinhas();
    }, [tipo, mes, ano]);

    const handleRemover = async (id) => {
        try {
          const resposta = await fetch(`/api/transacoes/${id}`, {
            method: 'DELETE',
          });
      
          if (resposta.ok) {
            setLinhas(linhas.filter((linha) => linha.id !== id));
            if (onSuccess) {
              onSuccess(); // Chama a função de callback se fornecida
            }
          } else {
            console.error("Erro ao remover transação");
          }
        } catch (e) {
          console.error("Erro na requisição de exclusão:", e);
        }
      };
      

    return (
    <StyledTableContainer component={Paper}>
        {loading ? (
        <Typography sx={{ p: 2 }}>Carregando...</Typography>
        ) : (
        <Table aria-label="tabela de transações">
            <StyledTableHead>
            <TableRow>
                <TableCell align="center"></TableCell>
                <TableCell align="left">Data</TableCell>
                <TableCell align="left">Descrição</TableCell>
                <TableCell align="left">Categoria</TableCell>
                <TableCell align="left">Valor</TableCell>
            </TableRow>
            </StyledTableHead>
            <StyledTableBody>
            {linhas.map((linha, index) => (
                <TableRow key={linha.id} sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
                    <TableCell align="center">
                        <IconButton color="error" onClick={() => handleRemover(linha.id)}>
                            <DeleteIcon />
                        </IconButton>
                    </TableCell>
                <TableCell align="left">{linha.data}</TableCell>
                <TableCell align="left">{linha.descricao}</TableCell>
                <TableCell align="left">{linha.categoria}</TableCell>
                <TableCell align="left">R$ {linha.valor.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}</TableCell>
                </TableRow>
            ))}
            </StyledTableBody>
        </Table>
        )}
    </StyledTableContainer>
    );
}

export default DescricaoTransacoes;
