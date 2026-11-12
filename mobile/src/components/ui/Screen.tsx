import { PropsWithChildren } from 'react';
import { ScrollView, StyleSheet, View, ViewProps } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';

import { colors, spacing } from '../../constants/design-tokens';

type ScreenProps = PropsWithChildren<ViewProps> & {
  scroll?: boolean;
};

export function Screen({ children, scroll = true, style, ...props }: ScreenProps) {
  const content = scroll ? (
    <ScrollView contentContainerStyle={styles.scrollContent} showsVerticalScrollIndicator={false}>
      {children}
    </ScrollView>
  ) : (
    <View style={styles.content}>{children}</View>
  );

  return (
    <SafeAreaView style={[styles.safeArea, style]} {...props}>
      {content}
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
    backgroundColor: colors.surface,
  },
  content: {
    flex: 1,
    padding: spacing.md,
  },
  scrollContent: {
    padding: spacing.md,
    paddingBottom: spacing.xl,
  },
});
