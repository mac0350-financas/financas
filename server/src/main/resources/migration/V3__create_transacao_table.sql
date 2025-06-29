CREATE TABLE transacoes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    data VARCHAR(255) NOT NULL,
    valor DOUBLE PRECISION NOT NULL,
    tipo INT NOT NULL,
    categoria VARCHAR(255) NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    usuario_id INT NOT NULL,
    FOREIGN KEY (tipo) REFERENCES tipos_transacao(id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);
