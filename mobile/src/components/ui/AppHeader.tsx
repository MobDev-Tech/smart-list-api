import { StyleSheet, Text, View } from 'react-native';

import { colors, spacing, typography } from '../../constants/design-tokens';

type AppHeaderProps = {
  title: string;
  subtitle?: string;
};

export function AppHeader({ title, subtitle }: AppHeaderProps) {
  return (
    <View style={styles.container}>
      <View style={styles.titleRow}>
        <View style={styles.brandMark}>
          <Text style={styles.brandMarkText}>✓</Text>
        </View>
        <Text style={styles.title}>{title}</Text>
      </View>
      {subtitle ? <Text style={styles.subtitle}>{subtitle}</Text> : null}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    gap: spacing.xs,
    marginBottom: spacing.md,
  },
  titleRow: {
    alignItems: 'center',
    flexDirection: 'row',
    gap: spacing.sm,
  },
  brandMark: {
    alignItems: 'center',
    backgroundColor: colors.primary,
    borderRadius: 999,
    height: 32,
    justifyContent: 'center',
    width: 32,
  },
  brandMarkText: {
    color: colors.onPrimary,
    fontSize: 18,
    fontWeight: '700',
  },
  title: {
    color: colors.onSurface,
    fontSize: typography.headlineLarge,
    fontWeight: '700',
    letterSpacing: -0.6,
  },
  subtitle: {
    color: colors.onSurfaceVariant,
    fontSize: typography.bodyMedium,
  },
});
