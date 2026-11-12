import { StyleSheet, Text, View } from 'react-native';

import { colors, radii, spacing, typography } from '../../constants/design-tokens';

type StatusBannerProps = {
  title: string;
  description: string;
  tone?: 'info' | 'warning';
};

export function StatusBanner({ title, description, tone = 'info' }: StatusBannerProps) {
  const isWarning = tone === 'warning';

  return (
    <View style={[styles.container, isWarning ? styles.warning : styles.info]}>
      <View style={[styles.dot, isWarning ? styles.warningDot : styles.infoDot]} />
      <View style={styles.copy}>
        <Text style={styles.title}>{title}</Text>
        <Text style={styles.description}>{description}</Text>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    alignItems: 'flex-start',
    borderRadius: radii.lg,
    flexDirection: 'row',
    gap: spacing.sm,
    padding: spacing.md,
  },
  info: {
    backgroundColor: '#EFF6FF',
  },
  warning: {
    backgroundColor: colors.warningSurface,
  },
  dot: {
    borderRadius: 999,
    height: 10,
    marginTop: 4,
    width: 10,
  },
  infoDot: {
    backgroundColor: colors.secondary,
  },
  warningDot: {
    backgroundColor: '#F59E0B',
  },
  copy: {
    flex: 1,
    gap: spacing.xs,
  },
  title: {
    color: colors.onSurface,
    fontSize: typography.labelLarge,
    fontWeight: '700',
  },
  description: {
    color: colors.onSurfaceVariant,
    fontSize: typography.bodyMedium,
    lineHeight: 20,
  },
});
