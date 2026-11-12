import { getDeviceId } from './device-id';

const API_URL = (process.env.EXPO_PUBLIC_API_URL ?? 'http://10.0.2.2:8080').replace(/\/$/, '');

export type ApiErrorPayload = {
  status?: number;
  message?: string;
  errors?: Record<string, string>;
};

export class ApiError extends Error {
  constructor(
    message: string,
    public readonly status?: number,
    public readonly details?: Record<string, string>,
  ) {
    super(message);
    this.name = 'ApiError';
  }
}

type RequestOptions = Omit<RequestInit, 'body'> & {
  body?: unknown;
};

export async function apiRequest<T>(path: string, options: RequestOptions = {}): Promise<T> {
  const deviceId = await getDeviceId();
  const headers = new Headers(options.headers);
  headers.set('Accept', 'application/json');
  headers.set('X-Device-ID', deviceId);

  let body: BodyInit | undefined;
  if (options.body !== undefined) {
    headers.set('Content-Type', 'application/json');
    body = JSON.stringify(options.body);
  }

  const response = await fetch(`${API_URL}${path}`, {
    ...options,
    headers,
    body,
  });

  const contentType = response.headers.get('content-type') ?? '';
  const payload = contentType.includes('application/json')
    ? ((await response.json()) as ApiErrorPayload | T)
    : undefined;

  if (!response.ok) {
    const errorPayload = payload as ApiErrorPayload | undefined;
    throw new ApiError(
      errorPayload?.message ?? `Não foi possível concluir a operação (${response.status}).`,
      response.status,
      errorPayload?.errors,
    );
  }

  return payload as T;
}

export const api = {
  get: <T>(path: string) => apiRequest<T>(path),
  post: <T>(path: string, body: unknown) => apiRequest<T>(path, { method: 'POST', body }),
  put: <T>(path: string, body: unknown) => apiRequest<T>(path, { method: 'PUT', body }),
  patch: <T>(path: string, body?: unknown) => apiRequest<T>(path, { method: 'PATCH', body }),
  delete: <T>(path: string) => apiRequest<T>(path, { method: 'DELETE' }),
};

export { API_URL };
