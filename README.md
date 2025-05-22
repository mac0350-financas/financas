# Finature
Finature é o seu sistema de gerenciamento de finanças pessoais e investimentos. Permite o acompanhamento de transações financeiras, definição de metas de gastos e cálculo de rendimentos com base na taxa do CDI.

A aplicação segue a arquitetura MVC, com backend desenvolvido em Kotlin utilizando o framework Ktor, e frontend em JavaScript com React + Vite.

## Dependências

- Versão do NPM: 9.2.0
- Versão do Vite: 6.2.3
- Versão do Ktor: 3.1.1
- Versão do Kotlin: 2.1.20

## Acessando o front-end

1. Acesse um terminal;
2. Acesse o diretório do projeto com `cd`;
3. Acesse o diretório `client`;
4. Inicie o servidor com `npm run dev`;

O front-end estará rodando em [localhost:5173](http://localhost:5173)

## Acessando o back-end

1. Acesse outro terminal;
2. Acesse o diretório do projeto com `cd`;
3. Acesse o diretório `server`;
4. Inicie o servidor com `./gradlew run`;

O back-end estará rodando em [localhost:8080](http://localhost:8080)

## Executando os testes

Ainda em fase de implementação

## Estrutura do projeto

finature/
├── server/                          # Backend (Ktor + Kotlin)
│   ├── config/                      # Configurações (segurança, logs, serialização, etc)
│   ├── models/                      # Modelos de dados (Usuario, Transacao, Meta, etc)
│   ├── services/                    # Lógica de negócio e integração com banco
│   ├── schemas/                     # Schemas para persistência (Exposed, etc)
│   ├── Application.kt               # Arquivo principal do servidor
│   ├── Routing.kt                   # Definições de rotas
│   ├── Security.kt                  # Configuração de sessões e autenticação
│   ├── Monitoring.kt                # Monitoramento com CallLogging
│   └── Serialization.kt             # Configuração de serialização JSON
│
├── client/                          # Frontend (React + Vite)
│   ├── src/
│   │   ├── components/              # Componentes reutilizáveis
│   │   ├── pages/                   # Páginas principais da aplicação
│   │   ├── App.jsx                  # Componente raiz
│   │   └── main.jsx                 # Ponto de entrada da aplicação
│   ├── App.css                      # Estilo principal
│   └── index.css                    # Estilo base do projeto
│
├── gradle/                           # Configurações do Gradle (libs.versions.toml, etc)
│
└── README.md                        

## Diagrama UML

![Diagrama UML](docs/uml/diagrama.jpeg)
