import { Redirect } from 'expo-router';
import { ActivityIndicator, StyleSheet, View } from 'react-native';
import { useEffect, useState } from 'react';

import { colors } from '../constants/design-tokens';
import { hasCompletedOnboarding } from '../lib/storage';

export default function IndexRoute() {
  const [destination, setDestination] = useState<'/onboarding' | '/(tabs)/list' | null>(null);

  useEffect(() => {
    hasCompletedOnboarding().then((completed) => {
      setDestination(completed ? '/(tabs)/list' : '/onboarding');
    });
  }, []);

  if (!destination) {
    return (
      <View style={styles.loading}>
        <ActivityIndicator color={colors.primary} />
      </View>
    );
  }

  return <Redirect href={destination} />;
}

const styles = StyleSheet.create({
  loading: {
    alignItems: 'center',
    backgroundColor: colors.surface,
    flex: 1,
    justifyContent: 'center',
  },
});
