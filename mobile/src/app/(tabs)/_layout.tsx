import { Tabs } from 'expo-router';
import Svg, { Circle, Path } from 'react-native-svg';

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
      <Tabs.Screen
        name="list"
        options={{
          title: 'Lista',
          tabBarIcon: ({ color }) => <ChecklistTabIcon color={color} />,
        }}
      />
      <Tabs.Screen
        name="settings"
        options={{
          title: 'Configurações',
          tabBarIcon: ({ color }) => <SettingsTabIcon color={color} />,
        }}
      />
    </Tabs>
  );
}

function ChecklistTabIcon({ color }: { color: unknown }) {
  const iconColor = typeof color === 'string' ? color : colors.onSurfaceVariant;

  return (
    <Svg height="24" viewBox="0 0 24 24" width="24">
      <Path d="M4 7h3M4 12h3M4 17h3M10 7h10M10 12h10M10 17h10" stroke={iconColor} strokeLinecap="round" strokeWidth="1.8" />
      <Path d="m4.5 6.8 1.2 1.2L8 5.8" fill="none" stroke={iconColor} strokeLinecap="round" strokeWidth="1.6" />
    </Svg>
  );
}

function SettingsTabIcon({ color }: { color: unknown }) {
  const iconColor = typeof color === 'string' ? color : colors.onSurfaceVariant;

  return (
    <Svg height="24" viewBox="0 0 24 24" width="24">
      <Path d="m19.4 13.5 1.6 1.2-1.8 3.1-1.9-.8a7.5 7.5 0 0 1-2.1 1.2L15 20.3h-3.6l-.3-2.1a7.5 7.5 0 0 1-2.1-1.2l-1.9.8-1.8-3.1 1.6-1.2a7.7 7.7 0 0 1 0-2.4L5.3 9.9l1.8-3.1 1.9.8a7.5 7.5 0 0 1 2.1-1.2l.3-2.1H15l.3 2.1a7.5 7.5 0 0 1 2.1 1.2l1.9-.8 1.8 3.1-1.6 1.2a7.7 7.7 0 0 1-.1 2.4Z" fill="none" stroke={iconColor} strokeLinejoin="round" strokeWidth="1.8" />
      <Circle cx="13.2" cy="12.3" fill="none" r="2.8" stroke={iconColor} strokeWidth="1.8" />
    </Svg>
  );
}
