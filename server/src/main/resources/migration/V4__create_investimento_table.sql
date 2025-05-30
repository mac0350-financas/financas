-- V4__create_investimento_table.sql
CREATE TABLE investimentos (
    id SERIAL PRIMARY KEY,
    usuario_id INT NOT NULL REFERENCES usuarios(id),
    valor_investido DOUBLE PRECISION NOT NULL,
    taxa DOUBLE PRECISION NOT NULL,
    data_inicial VARCHAR(255) NOT NULL,
    data_final VARCHAR(255) NOT NULL
);