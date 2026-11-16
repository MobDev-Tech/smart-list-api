# Smart List — Plano de construção do front

Status: fundação técnica criada; estratégia de fidelidade Stitch definida; implementação em andamento
Atualizado em: 2026-11-12
Backend: `smart-list-api`  
Referência visual: `stitch_lista_inteligente_android_ui/`

## 1. Objetivo

Construir o aplicativo mobile da Smart List em React Native, reconstruindo o mais fielmente possível os protótipos Stitch — com todos os elementos presentes nos HTMLs — e conectando as telas à API Spring Boot existente.

O fluxo principal será:

1. Usuário abre o aplicativo.
2. Visualiza ou cadastra itens de compra.
3. Configura a residência e as permissões.
4. O aplicativo acompanha a localização do dispositivo.
5. Quando o dispositivo sai do raio residencial e existem itens pendentes, o aplicativo exibe um lembrete.
6. O usuário pode abrir a lista, concluir itens ou adiar os lembretes.

## 2. Direção de design — primeiro bloco concluído

Esta seção formaliza a direção visual e a estratégia de reprodução antes da próxima rodada de implementação. A pasta Stitch passa a ser a fonte visual primária: o objetivo é reconstruir as telas no React Native com máxima fidelidade, preservando todos os elementos, estados e relações de layout dos HTMLs. Adaptações serão feitas somente quando forem necessárias para navegação, acessibilidade ou funcionamento nativo.

### Estratégia visual ajustada: fidelidade Stitch

Os HTMLs e seus `screen.png` deixam de ser apenas inspiração e passam a funcionar como especificação visual de referência.

Contrato de fidelidade:

- nenhum elemento visual existente no HTML deve ser removido sem uma decisão registrada;
- textos, labels, ícones, cards, banners, seletores, modais e estados devem ser inventariados e reproduzidos;
- cores, tipografia, pesos, espaçamentos, raios, bordas e hierarquia devem começar pelos valores do HTML;
- o `screen.png` será usado para comparação visual após cada tela reconstruída;
- a API será conectada depois que a estrutura visual e os estados estáticos estiverem equivalentes;
- o React Native poderá substituir a implementação técnica, mas não deve alterar a intenção visual.

O que pode ser adaptado sem quebrar a fidelidade: `View` no lugar de `div`, `Pressable` no lugar de `button`, `TextInput` no lugar de `input`, Expo Router no lugar de `href`, permissões nativas no lugar de JavaScript de demonstração e componentes SVG/nativos no lugar de ilustrações impossíveis de executar diretamente.

Ordem de reconstrução das sete referências:

1. `configura_o_inicial` — onboarding;
2. `lista_de_compras` — lista principal;
3. `adicionar_ou_editar_item` — formulário de item;
4. `configurar_resid_ncia` — residência e geofence;
5. `configura_es` — configurações;
6. `modal_de_soneca` — modal de soneca;
7. `notifica_o_android` — prévia da notificação.

### Propósito

A interface deve permitir que a pessoa:

- registre uma compra em poucos segundos, com uma mão;
- entenda imediatamente o que ainda está pendente;
- saiba se o lembrete residencial está pronto, pendente ou bloqueado;
- configure a residência sem precisar interpretar termos técnicos de geolocalização;
- receba um lembrete contextual antes de sair para fazer compras.

O produto é principalmente **funcional**, com uma camada de cuidado: reduzir a carga mental da rotina e evitar que a pessoa precise lembrar da lista no momento errado.

### Direção estética nomeada

**Utilitarismo Nativo Botânico** (*Modern Native Utility + Organic Natural*).

É uma combinação controlada de dois tons:

1. **Industrial/utilitário**, para hierarquia direta, ações óbvias, leitura rápida e ergonomia de uma mão.
2. **Orgânico/natural**, para usar o verde botânico, o contexto de casa e o crescimento do raio como sinais de confiança e cuidado.

A inspiração conceitual vem de instrumentos nativos de rotina — claros, previsíveis e táteis — combinados com a ideia de uma casa viva que protege a memória das compras. Não haverá decoração ornamental sem função.

### DFII — Design Feasibility & Impact Index

| Dimensão | Nota | Justificativa |
|---|---:|---|
| Impacto estético | 4/5 | A combinação entre lista utilitária e raio residencial cria uma assinatura visual reconhecível. |
| Adequação ao contexto | 5/5 | Clareza, confiança e baixa carga cognitiva são adequadas para uso rápido fora de casa. |
| Viabilidade de implementação | 4/5 | Pode ser aplicado com React Native, `StyleSheet`, tokens e animações nativas simples. |
| Segurança de performance | 4/5 | A proposta prioriza superfícies tonais, ícones e uma animação focal, sem efeitos pesados. |
| Risco de consistência | 2/5 | As regras de cor, tipografia, raio e âncora reduzem variações entre telas. |
| **DFII** | **15** | **(4 + 5 + 4 + 4) − 2 = 15 — excelente; executar com disciplina.** |

### Âncora de diferenciação

**O “raio vivo da casa”.**

Se o logotipo fosse removido de uma captura de tela, a Smart List deveria ser reconhecida pelo encontro entre:

- um marcador de casa/check em verde botânico;
- um anel de geofence em azul cobalto;
- o estado textual curto “Pronto para sair” ou “Configure sua casa”;
- a lista de compras organizada ao redor desse estado.

Essa âncora será usada no banner da lista, na tela de residência, no onboarding e na notificação. O anel só terá movimento quando representar um estado real de monitoramento; ele não será um ornamento permanente.

### Sistema tipográfico

Como a prioridade desta rota é a fidelidade visual, a família, os pesos e as métricas tipográficas usadas nos HTMLs Stitch serão mantidos na primeira implementação. O HTML usa `Inter`; ela deverá ser carregada localmente no aplicativo, sem depender do CDN do protótipo.

- **Família de referência: Inter** — manter a mesma aparência dos protótipos.
- **Pesos** — reproduzir os pesos declarados no HTML; não trocar a fonte por uma alternativa visualmente diferente durante a migração.
- **Fallback** — usar uma fonte local de fallback somente durante carregamento ou em caso de falha, sem transformar o fallback na identidade final.

Escala estrutural:

- Display: 32px / 40px;
- Título de tela: 26px / 32px;
- Título de seção: 22px / 28px;
- Corpo principal: 16px / 24px;
- Corpo auxiliar: 14px / 20px;
- Label e metadados: 12px / 16px.

As fontes serão carregadas como assets do aplicativo na próxima etapa visual, com fallback controlado apenas para evitar texto invisível durante o carregamento.

### História de cor

A paleta é deliberadamente concentrada em um tom dominante, um acento funcional e neutros de superfície:

- **Canvas lilás-neutro `#FAF8FF`** — reduz o brilho de uma tela totalmente branca;
- **Superfície `#FFFFFF`** — reserva para cards, folhas e campos interativos;
- **Verde botânico `#006B2C`** — ação primária, conclusão e sensação de progresso;
- **Azul cobalto `#0051D5`** — exclusivamente para geofence, localização e telemetria residencial;
- **Âmbar `#F59E0B`** — somente permissões pendentes e alertas contextuais;
- **Tinta `#131B2E`** e neutros derivados — leitura e estrutura.

Não serão usados gradientes roxo-branco, cores igualmente distribuídas ou cor decorativa sem semântica. Os valores devem existir nos tokens compartilhados, nunca espalhados pelos componentes.

### Composição e profundidade

- Layout mobile de coluna única, com margem lateral de 16px e alvos de toque de pelo menos 48dp.
- A composição deve seguir primeiro a posição e a proporção dos elementos nos `screen.png`; melhorias de layout só entram depois de comprovar a equivalência visual.
- O banner de geofence, o anel da residência, o contador, a ação principal e o FAB devem manter o papel visual que já possuem nos protótipos.
- Espaço negativo, cards, bordas e sombras serão reproduzidos a partir dos valores do HTML, sem substituir a composição por um layout genérico de componentes.
- O comportamento responsivo deve preservar a composição original em telas Android comuns, permitindo apenas ajustes de safe area e conteúdo que precise rolar.

### Filosofia de movimento

Movimento será raro e informativo:

- uma entrada curta de opacidade e deslocamento para o conteúdo principal;
- pulsação lenta e discreta do anel azul somente quando o monitoramento estiver ativo;
- feedback de pressionar com escala/ripple nativo em botões e chips;
- transições de estado para item concluído, usando mudança de cor e risco no texto;
- respeito a `reduce motion`, removendo pulsação e deslocamentos quando necessário.

Não haverá animações decorativas contínuas, carrosséis automáticos ou efeitos que atrasem a ação principal.

### Differentiation Callout

> A Smart List evita uma tela genérica de lista reconstruindo integralmente o vocabulário visual dos protótipos — incluindo o “raio vivo da casa”, os cards, os estados e os modais — em vez de reduzir o design a uma lista padrão de componentes.

## 3. Estado atual

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

## 4. Stack planejada

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

## 5. Estrutura esperada do aplicativo

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

## 6. Rotas e telas

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

## 7. Contrato obrigatório do cliente HTTP

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

## 8. Decisões que precisam ser resolvidas

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

## 9. Design system a implementar

Fonte de referência: os HTMLs e `screen.png` de `stitch_lista_inteligente_android_ui/`, apoiados por `smart_grocery_geofence/DESIGN.md` e pela direção de **Utilitarismo Nativo Botânico** definida na seção 2.

Tokens principais:

```text
Fundo:       #FAF8FF
Texto:       #131B2E
Verde:       #006B2C
Azul:        #0051D5
Âmbar:       #F59E0B
Erro:        #BA1A1A
Fonte:       Inter local — reproduzir pesos e métricas do Stitch
Escala:      12px, 14px, 16px, 18px, 22px, 26px, 32px
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

## 10. Backlog de implementação

### Fase 0 — Preparação

- [ ] Criar projeto Expo com TypeScript.
- [ ] Definir se o app ficará em `mobile/` ou em um repositório separado.
- [ ] Configurar Expo Router.
- [ ] Configurar lint, formatter e TypeScript strict.
- [ ] Criar arquivo `.env.example` do mobile.
- [ ] Documentar como executar o app e o backend juntos.

### Fase 1 — Fundação visual

- [ ] Criar tokens de cores, tipografia, espaçamento e raios a partir dos HTMLs Stitch.
- [ ] Criar componentes base sem perder a estrutura visual dos protótipos.
- [ ] Configurar safe area e tema claro.
- [ ] Implementar tabs inferiores conforme a referência visual.
- [ ] Reproduzir header, FAB, cards, banners, chips e modais.
- [ ] Comparar cada tela com o `screen.png` correspondente.
- [ ] Corrigir divergências visuais antes de iniciar a integração da API.

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

- [x] Implementar onboarding real.
- [x] Persistir conclusão do onboarding.
- [ ] Criar fluxo de permissões negadas.
- [ ] Criar estados sem residência.
- [ ] Criar estados sem itens.
- [ ] Revisar acessibilidade.
- [ ] Testar diferentes tamanhos de tela.
- [ ] Validar Android físico.

## 11. Critérios de aceite do MVP

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

## 12. Testes necessários

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

## 13. Definição de pronto por tarefa

Uma tarefa só será considerada pronta quando:

- [ ] A tela ou funcionalidade estiver implementada.
- [ ] O estado estiver conectado à fonte correta.
- [ ] O loading estiver tratado.
- [ ] O erro estiver tratado.
- [ ] O comportamento vazio estiver tratado.
- [ ] O layout estiver alinhado ao protótipo.
- [ ] O teste correspondente estiver criado quando aplicável.
- [ ] A tarefa estiver marcada neste documento.

## 14. Registro de decisões

| Data | Decisão | Motivo |
|---|---|---|
| 2026-11-09 | Usar React Native com Expo e TypeScript | Reduzir complexidade inicial e acelerar o MVP mobile |
| 2026-11-09 | Usar os protótipos Stitch como referência visual | Já existem telas e tokens detalhados |
| 2026-11-09 | Não reutilizar os HTMLs diretamente como aplicativo final | Eles não possuem integração, persistência ou estrutura React Native |
| 2026-11-12 | Adotar o Utilitarismo Nativo Botânico | Dar à Smart List uma assinatura visual própria, mantendo clareza nativa e usando o raio residencial como âncora |
| 2026-11-12 | Tornar os HTMLs Stitch a fonte visual primária | Reproduzir todos os elementos e reduzir diferenças entre o front final e os protótipos antes de conectar a API |

## 15. Progresso da implementação

### Primeiro bloco — direção de design

- [x] Definir o propósito funcional e emocional da interface.
- [x] Nomear a direção estética e limitar os tons dominantes.
- [x] Calcular o DFII com resultado mínimo 8.
- [x] Definir a âncora visual memorável.
- [x] Definir a estratégia tipográfica com fidelidade ao Stitch e carregamento local da fonte.
- [x] Definir paleta, composição, profundidade e ritmo espacial.
- [x] Definir filosofia de movimento e regra de acessibilidade para reduzir movimento.
- [x] Registrar o Differentiation Callout.

- [x] Criar o esqueleto do aplicativo Expo/TypeScript em `mobile/`.
- [x] Configurar navegação inicial entre onboarding, lista e configurações.
- [x] Implementar os tokens visuais básicos.
- [x] Criar o cliente HTTP com `X-Device-ID`.

### Tela concluída — onboarding Stitch

- [x] Comparar o HTML e o `screen.png` de `configura_o_inicial`.
- [x] Inventariar hero, halo, SVG, ícones, cards, privacidade, CTAs, textos e microinterações.
- [x] Reconstruir a tela em React Native com `react-native-svg`.
- [x] Preservar configurar residência, pular onboarding e persistir a conclusão.
- [x] Carregar Inter localmente para manter a fidelidade tipográfica.
- [x] Validar lint, TypeScript, Expo Doctor e bundling web.
- [ ] Capturar e comparar a tela renderizada em Android Emulator ou dispositivo físico.

Validação executada:

- `npm run lint` — aprovado.
- `npm run typecheck` — aprovado.
- `npx expo-doctor` — 20/20 verificações aprovadas.
- `npx expo start --no-dev --offline` — Metro iniciou usando `src/app` como raiz do Expo Router.
- `npx expo start --web --no-dev` — bundle web concluído; captura visual automática bloqueada porque não há navegador disponível no ambiente.

### Tela em reconstrução — lista de compras Stitch

- [x] Comparar o HTML e o `screen.png` de `lista_de_compras`.
- [x] Inventariar header, monitoramento, pausa, itens pendentes, comprados recentes, FAB, bottom sheet e tabs.
- [x] Reconstruir o estado visual preenchido em React Native com dados estáticos de demonstração.
- [x] Preservar navegação para configurações e adicionar produto.
- [x] Implementar interações locais de marcar item, expandir comprados, limpar, pausar e abrir o bottom sheet.
- [x] Adicionar ícones SVG e navegação inferior com ícones.
- [x] Validar lint, TypeScript e Expo Doctor.
- [ ] Capturar e comparar a tela renderizada em Android Emulator ou dispositivo físico.
- [ ] Conectar a lista aos endpoints reais da API.

## 16. Próxima tarefa

- [ ] Capturar a tela de onboarding em Android e comparar com o `screen.png`.
- [ ] Capturar a lista de compras em Android e comparar com o `screen.png`.
- [ ] Auditar o HTML e o `screen.png` de `adicionar_ou_editar_item`.
- [ ] Reconstruir adicionar/editar item antes de iniciar a integração da API.
