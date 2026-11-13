# 🛒 Smart List API

Uma API para um app mobile de lista de compras inteligente, desenvolvido em React Native. A ideia é simples: ajudar você a lembrar do que precisa comprar. Saiu de casa? O app poderá avisar sobre os itens que estão esperando na sua lista. 🏠📍

## ✨ A ideia

A API dará suporte ao aplicativo mobile e à organização das listas e dos itens de compra. A experiência planejada inclui um lembrete na hora certa — quando você sair de casa — para não deixar nada para trás. 🧺

> 🚧 **Projeto em construção:** as funcionalidades de listas, configurações, localização e notificações estão sendo desenvolvidas.

## 🧰 Tecnologias

- ☕ Java 21
- 🌱 Spring Boot 4.1.1
- 🌐 Spring Web MVC
- 💾 Spring Data JPA e PostgreSQL
- ✅ Bean Validation
- 📦 Maven (com Maven Wrapper)

## 📋 Antes de começar

- JDK 21 instalado
- Docker e Docker Compose (opcional, para executar a API e o PostgreSQL em containers)

Com o Maven Wrapper incluído, você não precisa instalar o Maven separadamente. 🙌

## ⚙️ Configuração do `.env`

O arquivo `.env.example` contém um modelo das variáveis de ambiente. Copie-o para `.env` na raiz do projeto e preencha as senhas antes de iniciar os serviços:

```powershell
Copy-Item .env.example .env
```

No Linux ou macOS:

```bash
cp .env.example .env
```

Configure principalmente:

| Variável | Uso |
|---|---|
| `POSTGRES_DB` | Nome do banco criado pelo container PostgreSQL. |
| `POSTGRES_USER` e `POSTGRES_PASSWORD` | Usuário e senha do PostgreSQL. Defina uma senha antes de iniciar os containers. |
| `PGADMIN_EMAIL` e `PGADMIN_PASSWORD` | Credenciais para entrar no pgAdmin. |
| `DB_URL` | URL JDBC usada pela aplicação quando executada fora do Docker. Ao usar o PostgreSQL do Compose, use `jdbc:postgresql://localhost:5433/<POSTGRES_DB>`. |

O `.env` é opcional para executar a aplicação diretamente, pois o código o carrega quando está presente e a configuração possui valores padrão. Para conectar a aplicação local ao PostgreSQL do Docker, configure `DB_URL`, `DB_USERNAME` e `DB_PASSWORD` de acordo com o banco. Dentro do Docker Compose, a API conecta-se ao serviço `postgres` pela rede interna; não use `localhost` na URL JDBC do container.

Não compartilhe nem versione o `.env`: ele pode conter credenciais. O arquivo está listado no `.gitignore`.

## ▶️ Rodando o projeto

Abra um terminal na pasta do projeto e execute:

```powershell
.\mvnw.cmd spring-boot:run
```

No Linux ou macOS, use:

```bash
./mvnw spring-boot:run
```

A aplicação está configurada com o nome `smart-list-api` e inicia na porta `8080`.

## 🐳 Docker e Docker Compose

O [Dockerfile](./Dockerfile) constrói a aplicação em duas etapas: compila o projeto com Maven e Java 21 e, em seguida, cria uma imagem de execução menor com Java 21. Os testes são ignorados durante a criação da imagem.

O [docker-compose.yml](./docker-compose.yml) inicia três serviços:

- **API**: disponível em `http://localhost:8080`.
- **PostgreSQL**: porta `5433` na máquina host, encaminhada para a porta `5432` do container. Os dados ficam no volume persistente `postgres_data`.
- **pgAdmin**: interface de administração em `http://localhost:5050`, usando `PGADMIN_EMAIL` e `PGADMIN_PASSWORD` do `.env`.

Depois de criar e preencher o `.env`, construa a imagem da API e inicie os serviços:

```powershell
docker compose up --build -d
```

Para acompanhar os logs:

```powershell
docker compose logs -f
```

Para parar e remover os containers e a rede, mantendo os dados do PostgreSQL:

```powershell
docker compose down
```

Para apagar também os dados persistidos do banco, use `docker compose down -v`. Essa ação remove o volume `postgres_data` e os dados não poderão ser recuperados.

## 📖 Documentação da API

Com a aplicação em execução, acesse a interface interativa do Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

O documento OpenAPI em JSON está disponível em:

```text
http://localhost:8080/v3/api-docs
```

O Swagger documenta os endpoints implementados, seus parâmetros, corpos de requisição, respostas e os esquemas dos DTOs.

## ☁️ Deploy

A API está publicada no Render: [https://smart-list-api.onrender.com](https://smart-list-api.onrender.com).
Você também pode acessar a documentação interativa do Swagger em [https://smart-list-api.onrender.com/swagger-ui.html](https://smart-list-api.onrender.com/swagger-ui.html).

## 🧪 Executando os testes

No Windows:

```powershell
.\mvnw.cmd test
```

No Linux ou macOS:

```bash
./mvnw test
```

## 🗂️ Onde encontrar as coisas

```text
src/
├── main/
│   ├── java/com/smartlist/smartlistapi/  # ☕ Código da aplicação
│   └── resources/                       # ⚙️ Configurações e recursos
└── test/
    └── java/com/smartlist/smartlistapi/ # 🧪 Testes
```

## 🚀 O que vem por aí

- 📝 Definir os recursos e contratos da API para listas e itens.
- 💾 Implementar a persistência e a validação dos dados.
- 📱 Conectar o app mobile à API.
- 📍 Definir como localização e lembretes ao sair de casa vão funcionar.