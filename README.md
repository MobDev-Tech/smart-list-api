# 🛒 Smart List API

Uma API para um app mobile de lista de compras inteligente, desenvolvido em React Native. A ideia é simples: ajudar você a lembrar do que precisa comprar. Saiu de casa? O app poderá avisar sobre os itens que estão esperando na sua lista. 🏠📍

## ✨ A ideia

A API dará suporte ao aplicativo mobile e à organização das listas e dos itens de compra. A experiência planejada inclui um lembrete na hora certa — quando você sair de casa — para não deixar nada para trás. 🧺

> 🚧 **Projeto em construção:** as funcionalidades de listas, configurações, localização e notificações estão sendo desenvolvidas.

## 🧰 Tecnologias

- ☕ Java 21
- 🌱 Spring Boot 4.1.1
- 🌐 Spring Web MVC
- 💾 Spring Data JPA e SQLite JDBC
- ✅ Bean Validation
- 📦 Maven (com Maven Wrapper)

## 📋 Antes de começar

- JDK 21 instalado

Com o Maven Wrapper incluído, você não precisa instalar o Maven separadamente. 🙌

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