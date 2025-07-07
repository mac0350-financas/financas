-- V5__create_meta_table.sql
CREATE TABLE metas (
    id SERIAL PRIMARY KEY,
    usuario_id INT NOT NULL REFERENCES usuarios(id),
    categoria VARCHAR(255) NOT NULL,
    valor_limite DOUBLE PRECISION NOT NULL,
    valor_atual DOUBLE PRECISION NOT NULL,
    data_inicial VARCHAR(255) NOT NULL,
    data_final VARCHAR(255) NOT NULL,
    tipo INT NOT NULL,
    FOREIGN KEY (tipo) REFERENCES tipos_transacao(id)
);