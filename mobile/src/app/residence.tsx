import { router } from 'expo-router';
import { useState } from 'react';
import { Pressable, StyleSheet, Text, View } from 'react-native';

import { AppHeader } from '../components/ui/AppHeader';
import { PrimaryButton } from '../components/ui/PrimaryButton';
import { Screen } from '../components/ui/Screen';
import { StatusBanner } from '../components/ui/StatusBanner';
import { colors, radii, spacing, typography } from '../constants/design-tokens';

const radiusOptions = [100, 150, 200, 300] as const;

export default function ResidenceScreen() {
  const [radius, setRadius] = useState<number>(150);

  return (
    <Screen>
      <AppHeader title="Configuração da residência" subtitle="Escolha onde o monitoramento deve começar." />
      <StatusBanner
        title="Residência ainda não configurada"
        description="A localização real será conectada na etapa de permissões."
        tone="warning"
      />

      <View style={styles.mapPlaceholder}>
        <Text style={styles.homeIcon}>⌂</Text>
        <View style={styles.radiusRing} />
        <Text style={styles.mapLabel}>Prévia do perímetro</Text>
      </View>

      <View style={styles.card}>
        <Text style={styles.cardLabel}>RAIO DE MONITORAMENTO</Text>
        <Text style={styles.radiusValue}>{radius} metros</Text>
        <Text style={styles.description}>
          O raio será usado para avaliar quando o dispositivo se afastar da residência.
        </Text>
        <View style={styles.options}>
          {radiusOptions.map((option) => (
            <Pressable
              accessibilityRole="button"
              key={option}
              onPress={() => setRadius(option)}
              style={[styles.option, option === radius && styles.optionSelected]}
            >
              <Text style={[styles.optionText, option === radius && styles.optionTextSelected]}>{option}m</Text>
            </Pressable>
          ))}
        </View>
      </View>

      <PrimaryButton label="Salvar configuração" onPress={() => router.replace('/(tabs)/list')} />
      <Pressable onPress={() => router.back()} style={styles.cancel}>
        <Text style={styles.cancelText}>Voltar</Text>
      </Pressable>
    </Screen>
  );
}

const styles = StyleSheet.create({
  mapPlaceholder: {
    alignItems: 'center',
    backgroundColor: colors.surfaceContainer,
    borderRadius: radii.lg,
    height: 210,
    justifyContent: 'center',
    marginTop: spacing.md,
    overflow: 'hidden',
    position: 'relative',
  },
  radiusRing: {
    borderColor: colors.secondary,
    borderRadius: 140,
    borderWidth: 2,
    height: 170,
    opacity: 0.35,
    position: 'absolute',
    width: 170,
  },
  homeIcon: {
    color: colors.primary,
    fontSize: 48,
    fontWeight: '700',
    zIndex: 1,
  },
  mapLabel: {
    bottom: spacing.md,
    color: colors.onSurfaceVariant,
    fontSize: typography.labelMedium,
    position: 'absolute',
  },
  card: {
    backgroundColor: colors.white,
    borderRadius: radii.lg,
    marginVertical: spacing.md,
    padding: spacing.md,
  },
  cardLabel: {
    color: colors.onSurfaceVariant,
    fontSize: typography.labelMedium,
    fontWeight: '700',
    letterSpacing: 0.6,
  },
  radiusValue: {
    color: colors.primary,
    fontSize: typography.headlineMedium,
    fontWeight: '700',
    marginTop: spacing.sm,
  },
  description: {
    color: colors.onSurfaceVariant,
    fontSize: typography.bodyMedium,
    lineHeight: 20,
    marginTop: spacing.sm,
  },
  options: {
    flexDirection: 'row',
    gap: spacing.sm,
    marginTop: spacing.md,
  },
  option: {
    alignItems: 'center',
    backgroundColor: colors.surfaceContainer,
    borderRadius: radii.full,
    flex: 1,
    minHeight: 42,
    justifyContent: 'center',
  },
  optionSelected: {
    backgroundColor: colors.secondary,
  },
  optionText: {
    color: colors.onSurfaceVariant,
    fontSize: typography.labelMedium,
    fontWeight: '700',
  },
  optionTextSelected: {
    color: colors.onPrimary,
  },
  cancel: {
    alignItems: 'center',
    padding: spacing.md,
  },
  cancelText: {
    color: colors.onSurfaceVariant,
    fontSize: typography.labelLarge,
    fontWeight: '600',
  },
});
