# Smart List Mobile

Aplicativo mobile da Smart List, construído com Expo, React Native, TypeScript e Expo Router.

## Executar

```powershell
npm install
npm start
```

Comandos úteis:

```powershell
npm run android
npm run web
npm run lint
npm run typecheck
npx expo-doctor
```

## URL da API

Copie `.env.example` para `.env.local` e ajuste a URL conforme o ambiente:

```powershell
Copy-Item .env.example .env.local
```

Valores comuns:

- Android Emulator: `http://10.0.2.2:8080`.
- Dispositivo físico: `http://IP_DA_MAQUINA:8080`.
- Web: `http://localhost:8080`.

O cliente HTTP gera e persiste um `X-Device-ID` no dispositivo usando `expo-secure-store`.

## Estrutura

- `src/app/` — rotas do Expo Router.
- `src/components/` — componentes visuais reutilizáveis.
- `src/constants/` — tokens de design.
- `src/lib/` — cliente HTTP e persistência local.

As telas ainda estão na etapa inicial de fundação. A próxima integração é conectar a tela da lista aos endpoints reais do backend.
