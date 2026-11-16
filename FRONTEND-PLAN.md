# Smart List — Plano de construção do front

Status: planejamento  
Atualizado em: 2026-11-09  
Backend: `smart-list-api`  
Referência visual: `stitch_lista_inteligente_android_ui/`

## 1. Objetivo

Construir o aplicativo mobile da Smart List em React Native, reproduzindo o visual dos protótipos Stitch e conectando as telas à API Spring Boot existente.

O fluxo principal será:

1. Usuário abre o aplicativo.
2. Visualiza ou cadastra itens de compra.
3. Configura a residência e as permissões.
4. O aplicativo acompanha a localização do dispositivo.
5. Quando o dispositivo sai do raio residencial e existem itens pendentes, o aplicativo exibe um lembrete.
6. O usuário pode abrir a lista, concluir itens ou adiar os lembretes.

## 2. Estado atual

### Backend

- Spring Boot 4.1.1.
- Java 21.
- API em `http://localhost:8080`.
- Persistência em PostgreSQL.
- Testes de integração com H2.
- Identificação atual por header `X-Device-ID`.

### Protótipos

A pasta `stitch_lista_inteligente_android_ui/` contém referências visuais em HTML, JavaScript inline e PNGs. Ela não é um projeto React Native e não possui integração real com a API.

Referências principais:

- `lista_de_compras/` — tela principal.
- `adicionar_ou_editar_item/` — formulário de item.
- `configurar_resid_ncia/` — residência e geofence.
- `configura_es/` — preferências e diagnóstico.
- `configura_o_inicial/` — onboarding.
- `modal_de_soneca/` — pausa de lembretes.
- `notifica_o_android/` — conceito de notificação Android.
- `smart_grocery_geofence/DESIGN.md` — tokens e regras visuais.

## 3. Stack planejada

| Área | Escolha |
|---|---|
| Framework | React Native com Expo |
| Linguagem | TypeScript |
| Navegação | Expo Router |
| Estado local | Zustand |
| Estado remoto | TanStack Query |
| Formulários | React Hook Form |
| Validação | Zod |
| Persistência local | Expo SecureStore ou AsyncStorage |
| Localização | Expo Location inicialmente |
| Notificações | Expo Notifications inicialmente |
| Estilos | NativeWind ou StyleSheet com tokens compartilhados |
| Testes | Jest + React Native Testing Library |

Decisão inicial: usar Expo + TypeScript + Expo Router. A escolha reduz a complexidade inicial e permite validar o MVP rapidamente em Android.

## 4. Estrutura esperada do aplicativo

```text
mobile/
├── app/
│   ├── _layout.tsx
│   ├── index.tsx
│   ├── onboarding.tsx
│   ├── residence.tsx
│   ├── item/
│   │   ├── new.tsx
│   │   └── [id].tsx
│   ├── notification-preview.tsx
│   └── (tabs)/
│       ├── _layout.tsx
│       ├── list.tsx
│       └── settings.tsx
├── components/
│   ├── ui/
│   ├── shopping/
│   ├── geofence/
│   ├── settings/
│   └── notifications/
├── features/
│   ├── shopping-list/
│   ├── residence/
│   ├── settings/
│   └── notifications/
├── hooks/
├── lib/
│   ├── api.ts
│   ├── api-errors.ts
│   ├── device-id.ts
│   └── query-client.ts
├── store/
├── constants/
│   ├── categories.ts
│   ├── design-tokens.ts
│   └── routes.ts
├── types/
├── assets/
└── app.json
```

Se decidirmos colocar o aplicativo diretamente na raiz, esta estrutura deverá ser adaptada sem misturar código TypeScript mobile com o código Java do backend.

## 5. Rotas e telas

### Onboarding

Rota: `/onboarding`

- Apresentar a proposta do aplicativo.
- Explicar os três passos principais.
- Permitir configurar residência agora.
- Permitir pular e configurar depois.
- Persistir localmente que o onboarding foi concluído.

Critério: ao reabrir o app, o onboarding não deve aparecer novamente depois de concluído.

### Lista de compras

Rota: `/(tabs)/list`

- Buscar itens agrupados da API.
- Exibir itens pendentes.
- Exibir itens concluídos recentemente.
- Alternar conclusão.
- Abrir edição.
- Excluir item.
- Limpar itens concluídos.
- Exibir contador real.
- Exibir estado de carregamento, vazio, erro e atualização.
- Exibir status real do geofence.

Endpoints:

```text
GET    /api/v1/shopping-items
POST   /api/v1/shopping-items
PUT    /api/v1/shopping-items/{id}
PATCH  /api/v1/shopping-items/{id}/toggle
DELETE /api/v1/shopping-items/{id}
DELETE /api/v1/shopping-items/completed
```

### Adicionar/editar item

Rotas:

```text
/item/new
/item/:id
```

- Nome obrigatório.
- Quantidade opcional.
- Categoria obrigatória.
- Seleção de unidade visual.
- Feedback de sucesso e erro.
- Atualização otimista somente depois que a chamada da API estiver validada.

Categorias do front devem ser traduzidas para os enums do backend:

```text
MERCEARIA
LATICINIOS
OLEOS_CONDIMENTOS
PADARIA
HORTIFRUTI
LIMPEZA
HIGIENE_PESSOAL
FARMACIA
MERCADO
PAPELARIA
OUTROS
```

### Configuração da residência

Rota: `/residence`

- Solicitar permissão de localização.
- Obter localização atual.
- Permitir confirmar ou alterar endereço.
- Exibir latitude/longitude apenas quando necessário.
- Exibir raio selecionado.
- Salvar configuração.
- Mostrar diagnóstico.

Endpoint:

```text
POST /api/v1/residence
GET  /api/v1/residence
POST /api/v1/residence/check-geofence
```

### Configurações

Rota: `/(tabs)/settings`

- Ligar/desligar lembretes ao sair de casa.
- Ligar/desligar vibração.
- Exibir permissões de notificação e localização.
- Abrir residência.
- Limpar itens concluídos.
- Abrir soneca.
- Exibir diagnóstico.

Endpoints:

```text
GET /api/v1/settings
PUT /api/v1/settings
GET /api/v1/settings/diagnostic
```

### Soneca

Pode ser um modal ou uma rota modal do Expo Router.

Mapeamento visual para a API:

| Interface | Backend |
|---|---|
| 30 minutos | `MINUTES_30` |
| 1 hora | `HOUR_1` |
| Até amanhã | `TOMORROW_MORNING` |
| Manual | `INDEFINITE` |

Endpoints:

```text
POST   /api/v1/notifications/snooze
DELETE /api/v1/notifications/snooze
```

## 6. Contrato obrigatório do cliente HTTP

Toda chamada de negócio deve enviar:

```http
X-Device-ID: <id-persistido-do-dispositivo>
```

O cliente HTTP deverá centralizar:

- URL base por ambiente.
- Header `X-Device-ID`.
- `Content-Type: application/json`.
- Tratamento de erros da API.
- Timeout.
- Conversão de respostas.
- Logs somente em desenvolvimento.

Exemplo de ambientes:

```text
Android Emulator: http://10.0.2.2:8080
Dispositivo físico: http://IP_DA_MAQUINA:8080
Produção: URL definida em variável de ambiente
```

O `X-Device-ID` precisa ser criado uma única vez e persistido no dispositivo. Não deve mudar a cada inicialização do app.

## 7. Decisões que precisam ser resolvidas

### Raio do geofence

Os protótipos oferecem 100m, 150m, 200m e 300m, mas o backend atual sempre força 150m.

Opções:

- [ ] Ajustar o backend para aceitar e persistir `radiusMeters`.
- [ ] Manter 150m e remover as outras opções do front.

Decisão recomendada: permitir o raio configurável no backend, porque o design já prevê essa experiência.

### Privacidade da localização

O onboarding afirma que a localização nunca é enviada para servidores, mas a API recebe coordenadas da residência e localização atual.

Opções:

- [ ] Corrigir o texto do front para explicar o envio ao backend.
- [ ] Redesenhar a arquitetura para manter o cálculo local.

Decisão recomendada para o MVP: corrigir o texto e informar claramente o que é enviado e armazenado.

### Monitoramento em segundo plano

O backend atual apenas avalia o trigger quando recebe uma requisição. Ele não possui push notification nem serviço de localização.

Para o MVP:

- [ ] Implementar localização em segundo plano no aplicativo.
- [ ] Chamar `POST /api/v1/notifications/trigger-check` quando houver uma mudança relevante.
- [ ] Exibir notificação local quando `shouldNotify` for `true`.
- [ ] Evitar chamadas repetidas usando cooldown local.

Depois do MVP:

- [ ] Avaliar push notification.
- [ ] Avaliar serviço nativo Android dedicado.
- [ ] Implementar ações reais da notificação.

### Permissões e regras de negócio

O backend salva `remindOnLeave`, `notificationPermission`, `locationPermission` e `geofenceActive`, mas a decisão de notificação ainda não usa todos esses valores.

- [ ] Confirmar regra definitiva com o backend.
- [ ] Fazer o front refletir o estado real retornado pela API.
- [ ] Não exibir “Monitoramento Ativo” quando o diagnóstico indicar que o sistema não está pronto.

## 8. Design system a implementar

Fonte de referência: `smart_grocery_geofence/DESIGN.md`.

Tokens principais:

```text
Fundo:       #FAF8FF
Texto:       #131B2E
Verde:       #006B2C / validar contra #16A34A do documento
Azul:        #0051D5
Âmbar:       #825100
Erro:        #BA1A1A
Inter:       12px, 14px, 16px, 18px, 22px, 26px, 32px
Raio base:   8px, 12px, 16px, 24px
Espaçamento: 4px, 8px, 16px, 24px, 32px
Touch target mínimo: 48dp
```

Componentes reutilizáveis prioritários:

- `AppHeader`.
- `BottomTabBar`.
- `ShoppingItemCard`.
- `CategoryChip`.
- `StatusBanner`.
- `PrimaryButton`.
- `FloatingAddButton`.
- `BottomSheet`.
- `PermissionStatusBadge`.
- `RadiusSelector`.
- `DiagnosticCard`.
- `ToastFeedback`.

## 9. Backlog de implementação

### Fase 0 — Preparação

- [ ] Criar projeto Expo com TypeScript.
- [ ] Definir se o app ficará em `mobile/` ou em um repositório separado.
- [ ] Configurar Expo Router.
- [ ] Configurar lint, formatter e TypeScript strict.
- [ ] Criar arquivo `.env.example` do mobile.
- [ ] Documentar como executar o app e o backend juntos.

### Fase 1 — Fundação visual

- [ ] Criar tokens de cores, tipografia, espaçamento e raios.
- [ ] Criar componentes base.
- [ ] Configurar safe area e tema claro.
- [ ] Implementar tabs inferiores.
- [ ] Reproduzir header e FAB.

### Fase 2 — Lista de compras

- [ ] Criar tipos TypeScript dos DTOs.
- [ ] Implementar cliente HTTP.
- [ ] Persistir `X-Device-ID`.
- [ ] Buscar lista real.
- [ ] Criar item.
- [ ] Editar item.
- [ ] Alternar conclusão.
- [ ] Excluir item.
- [ ] Limpar concluídos.
- [ ] Implementar estados de carregamento, vazio e erro.

### Fase 3 — Configurações e soneca

- [ ] Buscar configurações.
- [ ] Atualizar configurações.
- [ ] Implementar diagnóstico.
- [ ] Implementar modal de soneca.
- [ ] Integrar pausa temporária.
- [ ] Integrar reativação.

### Fase 4 — Residência e localização

- [ ] Solicitar permissão de localização.
- [ ] Obter localização atual.
- [ ] Salvar residência.
- [ ] Consultar residência salva.
- [ ] Implementar raio compatível com o backend.
- [ ] Consultar geofence.
- [ ] Exibir estado dentro/fora do perímetro.

### Fase 5 — Notificação local

- [ ] Configurar `expo-notifications`.
- [ ] Criar canal de notificação Android.
- [ ] Criar conteúdo da notificação.
- [ ] Integrar `trigger-check`.
- [ ] Implementar cooldown para não notificar repetidamente.
- [ ] Implementar ação “Abrir lista”.
- [ ] Implementar ação “Adiar”.
- [ ] Definir comportamento de “Dispensar”.

### Fase 6 — Onboarding e acabamento

- [ ] Implementar onboarding real.
- [ ] Persistir conclusão do onboarding.
- [ ] Criar fluxo de permissões negadas.
- [ ] Criar estados sem residência.
- [ ] Criar estados sem itens.
- [ ] Revisar acessibilidade.
- [ ] Testar diferentes tamanhos de tela.
- [ ] Validar Android físico.

## 10. Critérios de aceite do MVP

- [ ] O app inicia sem erro em Android.
- [ ] O usuário consegue criar um item e vê-lo após recarregar a tela.
- [ ] O usuário consegue concluir, editar e excluir um item.
- [ ] O estado da lista vem da API, não de dados fixos.
- [ ] O `X-Device-ID` é persistido e enviado em todas as chamadas necessárias.
- [ ] O usuário consegue configurar a residência.
- [ ] O diagnóstico exibe corretamente as permissões e a residência.
- [ ] O usuário consegue pausar e reativar lembretes.
- [ ] O aplicativo consegue obter a localização com permissão explícita.
- [ ] Uma notificação local pode ser exibida quando o backend retornar `shouldNotify: true`.
- [ ] Erros de rede e validação possuem feedback compreensível.
- [ ] O layout permanece utilizável em telas Android comuns.

## 11. Testes necessários

### Unitários

- Mapeamento de categorias.
- Mapeamento de opções de soneca.
- Formatação de quantidade.
- Geração e persistência do device ID.
- Regra local de cooldown.

### Componentes

- Item pendente.
- Item concluído.
- Modal de adicionar item.
- Seletor de raio.
- Card de diagnóstico.
- Modal de soneca.

### Integração

- Carregamento da lista.
- Criação de item.
- Toggle de item.
- Configuração de residência.
- Atualização de settings.
- Aplicação e cancelamento da soneca.

### Manual

- Android Emulator.
- Android físico.
- Permissão de localização aceita.
- Permissão de localização negada.
- API desligada.
- Lista vazia.
- Muitos itens.
- Rotação ou diferentes dimensões de tela.

## 12. Definição de pronto por tarefa

Uma tarefa só será considerada pronta quando:

- [ ] A tela ou funcionalidade estiver implementada.
- [ ] O estado estiver conectado à fonte correta.
- [ ] O loading estiver tratado.
- [ ] O erro estiver tratado.
- [ ] O comportamento vazio estiver tratado.
- [ ] O layout estiver alinhado ao protótipo.
- [ ] O teste correspondente estiver criado quando aplicável.
- [ ] A tarefa estiver marcada neste documento.

## 13. Registro de decisões

| Data | Decisão | Motivo |
|---|---|---|
| 2026-11-09 | Usar React Native com Expo e TypeScript | Reduzir complexidade inicial e acelerar o MVP mobile |
| 2026-11-09 | Usar os protótipos Stitch como referência visual | Já existem telas e tokens detalhados |
| 2026-11-09 | Não reutilizar os HTMLs diretamente como aplicativo final | Eles não possuem integração, persistência ou estrutura React Native |

## 14. Progresso da implementação

- [x] Criar o esqueleto do aplicativo Expo/TypeScript em `mobile/`.
- [x] Configurar navegação inicial entre onboarding, lista e configurações.
- [x] Implementar os tokens visuais básicos.
- [x] Criar o cliente HTTP com `X-Device-ID`.

Validação executada:

- `npm run lint` — aprovado.
- `npm run typecheck` — aprovado.
- `npx expo-doctor` — 20/20 verificações aprovadas.
- `npx expo start --no-dev --offline` — Metro iniciou usando `src/app` como raiz do Expo Router.

## 15. Próxima tarefa

- [ ] Criar o esqueleto do aplicativo Expo/TypeScript.
- [ ] Configurar navegação entre onboarding, lista e configurações.
- [ ] Implementar os tokens visuais básicos.
- [ ] Criar o cliente HTTP com `X-Device-ID`.
