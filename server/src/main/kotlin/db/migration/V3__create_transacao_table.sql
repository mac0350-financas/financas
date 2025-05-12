-- V3__create_transacao_table.sql
CREATE TABLE transacoes (
    id SERIAL PRIMARY KEY,
    data DATE NOT NULL,
    valor DOUBLE PRECISION NOT NULL,
    tipo INT NOT NULL REFERENCES tipos_transacao(id),
    categoria VARCHAR(50) NOT NULL,
    usuario_id INT NOT NULL REFERENCES usuarios(id)
);