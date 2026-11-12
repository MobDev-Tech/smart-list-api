import { router } from 'expo-router';
import { Pressable, StyleSheet, Text, View } from 'react-native';

import { AppHeader } from '../../components/ui/AppHeader';
import { PrimaryButton } from '../../components/ui/PrimaryButton';
import { Screen } from '../../components/ui/Screen';
import { StatusBanner } from '../../components/ui/StatusBanner';
import { colors, radii, spacing, typography } from '../../constants/design-tokens';

export default function ShoppingListScreen() {
  return (
    <Screen>
      <AppHeader title="Lista de compras" subtitle="Organize o que você precisa antes de sair." />

      <StatusBanner
        title="Monitoramento residencial pendente"
        description="Configure sua residência para ativar os lembretes de saída."
        tone="warning"
      />

      <View style={styles.sectionHeader}>
        <Text style={styles.sectionTitle}>Sua lista</Text>
        <Text style={styles.counter}>0 pendentes</Text>
      </View>

      <View style={styles.emptyCard}>
        <Text style={styles.emptyIcon}>＋</Text>
        <Text style={styles.emptyTitle}>Sua lista começa aqui</Text>
        <Text style={styles.emptyDescription}>
          Adicione produtos e mantenha tudo organizado para a próxima ida ao mercado.
        </Text>
        <PrimaryButton label="Adicionar primeiro item" onPress={() => router.push('/item/new')} />
      </View>

      <Pressable onPress={() => router.push('/residence')} style={styles.secondaryAction}>
        <Text style={styles.secondaryActionText}>Configurar residência</Text>
      </Pressable>
    </Screen>
  );
}

const styles = StyleSheet.create({
  sectionHeader: {
    alignItems: 'center',
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginTop: spacing.lg,
    marginBottom: spacing.sm,
  },
  sectionTitle: {
    color: colors.onSurface,
    fontSize: typography.headlineMedium,
    fontWeight: '700',
  },
  counter: {
    color: colors.onSurfaceVariant,
    fontSize: typography.bodyMedium,
  },
  emptyCard: {
    alignItems: 'center',
    backgroundColor: colors.white,
    borderRadius: radii.xl,
    gap: spacing.sm,
    padding: spacing.lg,
  },
  emptyIcon: {
    color: colors.primary,
    fontSize: 42,
    fontWeight: '300',
  },
  emptyTitle: {
    color: colors.onSurface,
    fontSize: typography.headlineSmall,
    fontWeight: '700',
  },
  emptyDescription: {
    color: colors.onSurfaceVariant,
    fontSize: typography.bodyMedium,
    lineHeight: 20,
    marginBottom: spacing.sm,
    textAlign: 'center',
  },
  secondaryAction: {
    alignItems: 'center',
    borderColor: colors.primary,
    borderRadius: radii.md,
    borderWidth: 1,
    justifyContent: 'center',
    marginTop: spacing.md,
    minHeight: 50,
  },
  secondaryActionText: {
    color: colors.primary,
    fontSize: typography.labelLarge,
    fontWeight: '700',
  },
});
