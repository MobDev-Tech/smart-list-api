import { router } from 'expo-router';
import { Pressable, StyleSheet, Text, View } from 'react-native';

import { AppHeader } from '../../components/ui/AppHeader';
import { Screen } from '../../components/ui/Screen';
import { colors, radii, spacing, typography } from '../../constants/design-tokens';

export default function SettingsScreen() {
  return (
    <Screen>
      <AppHeader title="Configurações" subtitle="Preferências e monitoramento da Smart List." />

      <Text style={styles.sectionLabel}>LEMBRETES E MONITORAMENTO</Text>
      <View style={styles.card}>
        <SettingRow title="Lembretes ao sair de casa" description="Ative quando a residência estiver configurada." value="Em breve" />
        <SettingRow title="Vibração ao notificar" description="Padrão tátil para chamar sua atenção." value="Em breve" />
      </View>

      <Text style={styles.sectionLabel}>CONFIGURAÇÃO</Text>
      <View style={styles.card}>
        <Pressable onPress={() => router.push('/residence')} style={styles.actionRow}>
          <View style={styles.actionCopy}>
            <Text style={styles.actionTitle}>Configuração da residência</Text>
            <Text style={styles.actionDescription}>Defina o local e o raio do monitoramento.</Text>
          </View>
          <Text style={styles.chevron}>›</Text>
        </Pressable>
      </View>

      <View style={styles.diagnosticCard}>
        <Text style={styles.diagnosticTitle}>Diagnóstico do sistema</Text>
        <Text style={styles.diagnosticDescription}>
          A integração com as configurações e permissões será conectada na próxima etapa.
        </Text>
      </View>
    </Screen>
  );
}

function SettingRow({ title, description, value }: { title: string; description: string; value: string }) {
  return (
    <View style={styles.settingRow}>
      <View style={styles.actionCopy}>
        <Text style={styles.actionTitle}>{title}</Text>
        <Text style={styles.actionDescription}>{description}</Text>
      </View>
      <Text style={styles.value}>{value}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  sectionLabel: {
    color: colors.onSurfaceVariant,
    fontSize: typography.labelMedium,
    fontWeight: '700',
    letterSpacing: 0.6,
    marginBottom: spacing.sm,
    marginTop: spacing.md,
  },
  card: {
    backgroundColor: colors.white,
    borderRadius: radii.lg,
    overflow: 'hidden',
  },
  settingRow: {
    alignItems: 'center',
    borderBottomColor: colors.surfaceContainer,
    borderBottomWidth: 1,
    flexDirection: 'row',
    gap: spacing.sm,
    minHeight: 78,
    padding: spacing.md,
  },
  actionRow: {
    alignItems: 'center',
    flexDirection: 'row',
    minHeight: 88,
    padding: spacing.md,
  },
  actionCopy: {
    flex: 1,
    gap: spacing.xs,
  },
  actionTitle: {
    color: colors.onSurface,
    fontSize: typography.bodyLarge,
    fontWeight: '700',
  },
  actionDescription: {
    color: colors.onSurfaceVariant,
    fontSize: typography.bodyMedium,
    lineHeight: 20,
  },
  value: {
    color: colors.onSurfaceVariant,
    fontSize: typography.labelMedium,
    fontWeight: '700',
  },
  chevron: {
    color: colors.onSurfaceVariant,
    fontSize: 30,
    fontWeight: '300',
  },
  diagnosticCard: {
    backgroundColor: colors.surfaceContainer,
    borderRadius: radii.lg,
    marginTop: spacing.lg,
    padding: spacing.md,
  },
  diagnosticTitle: {
    color: colors.onSurface,
    fontSize: typography.labelLarge,
    fontWeight: '700',
    marginBottom: spacing.xs,
  },
  diagnosticDescription: {
    color: colors.onSurfaceVariant,
    fontSize: typography.bodyMedium,
    lineHeight: 20,
  },
});
