import * as SecureStore from 'expo-secure-store';

const ONBOARDING_COMPLETED_KEY = 'smart-list.onboarding-completed';

export async function hasCompletedOnboarding(): Promise<boolean> {
  return (await SecureStore.getItemAsync(ONBOARDING_COMPLETED_KEY)) === 'true';
}

export async function completeOnboarding(): Promise<void> {
  await SecureStore.setItemAsync(ONBOARDING_COMPLETED_KEY, 'true');
}
