import * as SecureStore from 'expo-secure-store';

const DEVICE_ID_KEY = 'smart-list.device-id';

function createDeviceId(): string {
  const randomPart = Math.random().toString(36).slice(2, 10);
  return `smart-list-${Date.now().toString(36)}-${randomPart}`;
}

export async function getDeviceId(): Promise<string> {
  const existingId = await SecureStore.getItemAsync(DEVICE_ID_KEY);
  if (existingId) {
    return existingId;
  }

  const newId = createDeviceId();
  await SecureStore.setItemAsync(DEVICE_ID_KEY, newId);
  return newId;
}
