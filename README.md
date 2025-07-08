# Finature
Finature é o seu sistema de gerenciamento de finanças pessoais e investimentos. Permite o acompanhamento de transações financeiras, definição de metas de gastos e cálculo de rendimentos com base na taxa do CDI.

A aplicação segue a arquitetura MVC, com backend desenvolvido em Kotlin utilizando o framework Ktor, e frontend em JavaScript com React + Vite.

## Dependências

- Versão do NPM: 9.2.0
- Versão do Vite: 6.2.3
- Versão do Ktor: 3.1.1
- Versão do Kotlin: 2.1.20

Para instalar as dependências do NPM, faça o comando `npm install` no diretório `client`.
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

Para executar os testes unitários e de integração:

```bash
cd server
./gradlew test
```

Para gerar relatório de cobertura:

```bash
./gradlew jacocoTestReport
```

O relatório será gerado em `build/reports/jacoco/test/html/index.html`

## Estrutura do projeto

```text
finature/
├── server/                          # Backend (Ktor + Kotlin)
│   ├── src/main/kotlin/com/finature/
│   │   ├── db/                      # Camada de persistência
│   │   │   ├── dao/                 # Data Access Objects (Exposed)
│   │   │   ├── seed/                # Dados iniciais do sistema
│   │   │   └── tables/              # Definições das tabelas
│   │   ├── models/                  # Modelos de dados (DTOs)
│   │   ├── repositories/            # Repositórios para acesso a dados
│   │   ├── routes/                  # Definições de rotas da API
│   │   ├── services/                # Lógica de negócio
│   │   ├── sessions/                # Gerenciamento de sessões
│   │   ├── Application.kt           # Arquivo principal do servidor
│   │   └── *.kt                     # Configurações (HTTP, DB, etc)
│   ├── src/test/kotlin/             # Testes unitários e de integração
│   │   ├── com/finature/repositories/ # Testes dos repositórios
│   │   └── com/finature/services/   # Testes dos services
│   ├── config/                      # Configurações do projeto
│   └── resources/                   # Recursos da aplicação
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
├── gradle/                          # Configurações do Gradle (libs.versions.toml, etc)
│
└── README.md                        
```

## Funcionalidades implementadas

- **Gestão de usuários**: Cadastro e autenticação com sessões
- **Transações financeiras**: Registro de gastos e receitas por categoria
- **Metas financeiras**: Definição e acompanhamento de metas de gastos/receitas
- **Simulador de investimentos**: Comparação entre poupança e investimentos atrelados à SELIC
- **Relatórios**: Visualização de transações por período e categoria

## Tecnologias utilizadas

### Backend
- Kotlin com Ktor
- Exposed ORM para persistência
- H2 Database (testes)
- JUnit 5 para testes
- Mockk para mocks em testes

### Frontend
- React com Vite
- Material-UI para componentes

## Uso de IA no desenvolvimento

IAs estão sendo utilizadas ao longo do processo de desenvolvimento para consultas de sintaxe das linguagens e outras dúvidas "operacionais", como organização dos diretórios, entre outros. O planejamento, escolha das features e funcionalidade principais, bem como o layout do site, foram feitas sem uso de IA.
