# Resumo dos testes de integração

## Testes adicionados

- `src/test/java/com/smartlist/smartlistapi/integration/SmartListApiFlowsL1Test.java` — 8 testes Layer 1 usando MockMvc, serviços reais, JPA e PostgreSQL:
  - fluxo de criação, atualização, conclusão, consulta e exclusão de itens;
  - validação de entrada e resposta 404 para item inexistente;
  - persistência da residência e avaliação de geofence dentro e fora do raio;
  - ausência de residência durante consulta de geofence;
  - colaboração entre lista, residência, geofence e soneca no acionamento de notificações;
  - preferências do usuário e diagnóstico, antes e depois de configurar residência;
  - endpoint de saúde.

## Ganhos e configuração

Os cenários cobrem requisições HTTP até o banco PostgreSQL real, incluindo leitura após escrita e integração entre módulos. O perfil `integration` usa o banco dedicado `smart_list_test` em `localhost:5432`; os dados são limpos antes de cada teste. O serviço do banco deve estar disponível e as credenciais podem ser configuradas com `TEST_DB_URL`, `TEST_DB_USERNAME` e `TEST_DB_PASSWORD`.

## Problemas encontrados e corrigidos

- O identificador de `ResidenceEntity` era gerado sem tipo inteiro no esquema SQLite, impedindo o JPA de reler as residências. `@JdbcTypeCode(SqlTypes.INTEGER)` agora define o tipo correto.
- A ausência esperada de residência durante o diagnóstico marcava a transação como rollback-only. `ResidenceService.getResidence()` agora exclui `ResourceNotFoundException` do rollback, permitindo que o diagnóstico retorne normalmente quando a residência ainda não foi configurada.

## Execução final

- Testes de serviço e contexto: aprovados localmente.
- A execução limpa dos testes Layer1 encontrou 8 erros de conexão: a instância PostgreSQL na porta 5432 rejeitou a senha configurada para `postgres`.
- Para executar Layer1, crie o banco `smart_list_test` e configure as variáveis `TEST_DB_URL`, `TEST_DB_USERNAME` e `TEST_DB_PASSWORD` para a instância local.

## Scripts para repetir

- Windows: `powershell -ExecutionPolicy Bypass -File integration\run-layer1-tests.ps1`
- Unix-like: `bash integration/run-layer1-tests.sh`

Os scripts executam `verify` filtrado por `Layer1` e exibem um resumo de aprovação ou falha. O banco PostgreSQL `smart_list_test` deve estar acessível localmente; o perfil usa `create-drop`, portanto não deve apontar para um banco com dados da aplicação.
