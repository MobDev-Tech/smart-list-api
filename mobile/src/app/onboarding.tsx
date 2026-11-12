import { router } from 'expo-router';
import { StyleSheet, Text, View } from 'react-native';

import { AppHeader } from '../components/ui/AppHeader';
import { PrimaryButton } from '../components/ui/PrimaryButton';
import { Screen } from '../components/ui/Screen';
import { colors, radii, spacing, typography } from '../constants/design-tokens';
import { completeOnboarding } from '../lib/storage';

export default function OnboardingScreen() {
  async function skipSetup() {
    await completeOnboarding();
    router.replace('/(tabs)/list');
  }

  return (
    <Screen>
      <View style={styles.hero}>
        <View style={styles.illustration}>
          <Text style={styles.illustrationText}>⌂</Text>
        </View>
        <AppHeader
          title="Nunca mais esqueça as compras"
          subtitle="A Smart List lembra você quando sair de casa com itens pendentes."
        />
      </View>

      <View style={styles.steps}>
        <Step number="1" title="Cadastre o que precisa" description="Adicione produtos rapidamente, com quantidade e categoria." />
        <Step number="2" title="Defina sua residência" description="Configure o local e o raio que serão usados no lembrete." />
        <Step number="3" title="Receba o lembrete certo" description="Ao sair de casa, confira a lista antes de seguir para o mercado." />
      </View>

      <View style={styles.privacyCard}>
        <Text style={styles.privacyTitle}>Privacidade em primeiro lugar</Text>
        <Text style={styles.privacyText}>
          A localização será usada somente conforme as permissões e configurações que você escolher.
        </Text>
      </View>

      <View style={styles.actions}>
        <PrimaryButton label="Configurar residência e permissões" onPress={() => router.push('/residence')} />
        <Text accessibilityRole="button" onPress={skipSetup} style={styles.skip}>
          Começar a usar a lista agora
        </Text>
      </View>
    </Screen>
  );
}

function Step({ number, title, description }: { number: string; title: string; description: string }) {
  return (
    <View style={styles.step}>
      <View style={styles.stepNumber}>
        <Text style={styles.stepNumberText}>{number}</Text>
      </View>
      <View style={styles.stepCopy}>
        <Text style={styles.stepTitle}>{title}</Text>
        <Text style={styles.stepDescription}>{description}</Text>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  hero: {
    alignItems: 'center',
    paddingVertical: spacing.md,
  },
  illustration: {
    alignItems: 'center',
    backgroundColor: colors.surfaceContainer,
    borderColor: colors.secondary,
    borderRadius: 100,
    borderWidth: 2,
    height: 128,
    justifyContent: 'center',
    marginBottom: spacing.lg,
    width: 128,
  },
  illustrationText: {
    color: colors.primary,
    fontSize: 64,
    fontWeight: '700',
  },
  steps: {
    gap: spacing.sm,
  },
  step: {
    alignItems: 'center',
    backgroundColor: colors.white,
    borderRadius: radii.lg,
    flexDirection: 'row',
    gap: spacing.md,
    padding: spacing.md,
  },
  stepNumber: {
    alignItems: 'center',
    backgroundColor: colors.surfaceContainer,
    borderRadius: radii.md,
    height: 44,
    justifyContent: 'center',
    width: 44,
  },
  stepNumberText: {
    color: colors.primary,
    fontSize: typography.headlineSmall,
    fontWeight: '700',
  },
  stepCopy: {
    flex: 1,
    gap: spacing.xs,
  },
  stepTitle: {
    color: colors.onSurface,
    fontSize: typography.bodyLarge,
    fontWeight: '700',
  },
  stepDescription: {
    color: colors.onSurfaceVariant,
    fontSize: typography.bodyMedium,
    lineHeight: 20,
  },
  privacyCard: {
    backgroundColor: colors.surfaceContainer,
    borderRadius: radii.lg,
    marginTop: spacing.md,
    padding: spacing.md,
  },
  privacyTitle: {
    color: colors.onSurface,
    fontSize: typography.labelLarge,
    fontWeight: '700',
    marginBottom: spacing.xs,
  },
  privacyText: {
    color: colors.onSurfaceVariant,
    fontSize: typography.bodyMedium,
    lineHeight: 20,
  },
  actions: {
    gap: spacing.md,
    marginTop: spacing.lg,
  },
  skip: {
    color: colors.onSurface,
    fontSize: typography.labelLarge,
    fontWeight: '600',
    padding: spacing.sm,
    textAlign: 'center',
  },
});
