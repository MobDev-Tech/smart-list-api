# Resumo dos testes de integração

## Testes adicionados

- `src/test/java/com/smartlist/smartlistapi/integration/SmartListApiFlowsL1Test.java` — 8 testes Layer 1 usando MockMvc, serviços reais, JPA e H2 em memória:
  - fluxo de criação, atualização, conclusão, consulta e exclusão de itens;
  - validação de entrada e resposta 404 para item inexistente;
  - persistência da residência e avaliação de geofence dentro e fora do raio;
  - ausência de residência durante consulta de geofence;
  - colaboração entre lista, residência, geofence e soneca no acionamento de notificações;
  - preferências do usuário e diagnóstico, antes e depois de configurar residência;
  - endpoint de saúde.

## Ganhos e configuração

Os cenários cobrem requisições HTTP até o banco H2 em memória, incluindo leitura após escrita e integração entre módulos. O perfil `integration` cria um banco isolado automaticamente e limpa os dados antes de cada teste; não é necessário ter PostgreSQL disponível.

## Problemas encontrados e corrigidos

- O identificador de `ResidenceEntity` era gerado sem tipo inteiro no esquema SQLite, impedindo o JPA de reler as residências. `@JdbcTypeCode(SqlTypes.INTEGER)` agora define o tipo correto.
- A ausência esperada de residência durante o diagnóstico marcava a transação como rollback-only. `ResidenceService.getResidence()` agora exclui `ResourceNotFoundException` do rollback, permitindo que o diagnóstico retorne normalmente quando a residência ainda não foi configurada.

## Execução final

- Testes de serviço e contexto: aprovados localmente.
- A configuração do perfil `integration` usa H2 para evitar dependência de serviço ou credenciais externas.

## Scripts para repetir

- Windows: `powershell -ExecutionPolicy Bypass -File integration\run-layer1-tests.ps1`
- Unix-like: `bash integration/run-layer1-tests.sh`

Os scripts executam `verify` filtrado por `Layer1` e exibem um resumo de aprovação ou falha. O H2 em memória usa `create-drop` e não afeta o banco PostgreSQL da aplicação.
