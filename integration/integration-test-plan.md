# Plano de testes de integração

## Escopo e estratégia

Os testes Layer 1 percorrem a aplicação Spring MVC completa, dos endpoints HTTP aos serviços, repositórios JPA e banco SQLite real em arquivo. O perfil `integration` usa `target/integration-tests.db`, separado do banco local da aplicação. Os dados das tabelas são limpos antes de cada teste.

SQLite é embutido neste projeto; não há dependência de banco remoto ou serviço que exija Testcontainers. Os testes não substituem controladores, serviços ou repositórios por mocks e não exigem Docker.

## Cobertura existente e lacunas

Antes desta alteração, havia testes unitários de serviços e um teste de contexto/OpenAPI, mas não testes do fluxo HTTP com persistência real ou de colaboração entre módulos.

## Componentes e cenários

| Componente | Cenários de integração e critérios |
|---|---|
| Lista de compras | Criar, atualizar, alternar conclusão, consultar agrupamento e excluir; verificar contagens e dados retornados pela API após persistência. |
| Validação e erros | Rejeitar nome em branco com erro de campo; retornar 404 para item inexistente. |
| Residência/geofence | Persistir e consultar residência; calcular posições dentro e fora do raio; retornar 404 sem residência. |
| Notificações | Combinar itens pendentes, geofence e soneca; testar ausência de itens, notificação fora de casa, supressão dentro de casa, pausa indefinida e reativação. |
| Configurações/diagnóstico | Criar configurações padrão, atualizar e reler preferências; refletir residência e permissões no diagnóstico. |
| Saúde | Validar a resposta de saúde da API e sua identificação do banco. |

## Configuração e execução

- Java 21 e Maven Wrapper do projeto.
- JUnit 5, Spring Boot Test/MockMvc e SQLite JDBC já declarados no projeto.
- Perfil de teste: `src/test/resources/application-integration.yaml`.
- Classes terminam em `L1Test` e usam `@Tag("Layer1")`.
- Não são necessários novos módulos, dependências ou containers.
- Execução: `powershell -ExecutionPolicy Bypass -File integration\run-layer1-tests.ps1` no Windows ou `bash integration/run-layer1-tests.sh` em sistemas Unix-like.

## Critérios de validação

O Maven deve descobrir os testes sem configuração manual, executar todos os métodos `Layer1` e terminar com sucesso. Os fluxos devem afirmar status HTTP, valores de negócio, erros, e leitura após escrita pelo endpoint real.
