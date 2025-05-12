-- V5__create_meta_table.sql
CREATE TABLE metas (
    id SERIAL PRIMARY KEY,
    usuario_id INT NOT NULL REFERENCES usuarios(id),
    categoria VARCHAR(255) NOT NULL,
    valor_limite DOUBLE PRECISION NOT NULL,
    periodo DOUBLE PRECISION NOT NULL
);