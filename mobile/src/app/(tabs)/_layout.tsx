import { Tabs } from 'expo-router';

import { colors, typography } from '../../constants/design-tokens';

export default function TabsLayout() {
  return (
    <Tabs
      screenOptions={{
        headerShown: false,
        tabBarActiveTintColor: colors.primary,
        tabBarInactiveTintColor: colors.onSurfaceVariant,
        tabBarLabelStyle: {
          fontSize: typography.labelMedium,
          fontWeight: '600',
        },
        tabBarStyle: {
          backgroundColor: colors.surface,
          borderTopColor: colors.surfaceContainer,
          height: 70,
          paddingBottom: 10,
          paddingTop: 8,
        },
      }}
    >
      <Tabs.Screen name="list" options={{ title: 'Lista' }} />
      <Tabs.Screen name="settings" options={{ title: 'Configurações' }} />
    </Tabs>
  );
}
