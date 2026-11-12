import { router } from 'expo-router';
import { useState } from 'react';
import { Pressable, StyleSheet, Text, TextInput, View } from 'react-native';

import { AppHeader } from '../../components/ui/AppHeader';
import { PrimaryButton } from '../../components/ui/PrimaryButton';
import { Screen } from '../../components/ui/Screen';
import { colors, radii, spacing, typography } from '../../constants/design-tokens';

export default function NewItemScreen() {
  const [name, setName] = useState('');

  return (
    <Screen>
      <AppHeader title="Adicionar item" subtitle="A integração com a API entra na próxima etapa." />
      <View style={styles.card}>
        <Text style={styles.label}>Nome do produto</Text>
        <TextInput
          accessibilityLabel="Nome do produto"
          onChangeText={setName}
          placeholder="Ex.: Arroz integral"
          placeholderTextColor={colors.outline}
          style={styles.input}
          value={name}
        />
        <Text style={styles.label}>Quantidade</Text>
        <TextInput
          accessibilityLabel="Quantidade"
          placeholder="Ex.: 2 kg"
          placeholderTextColor={colors.outline}
          style={styles.input}
        />
      </View>
      <PrimaryButton disabled={!name.trim()} label="Salvar item" onPress={() => router.back()} />
      <Pressable onPress={() => router.back()} style={styles.cancel}>
        <Text style={styles.cancelText}>Cancelar</Text>
      </Pressable>
    </Screen>
  );
}

const styles = StyleSheet.create({
  card: {
    backgroundColor: colors.white,
    borderRadius: radii.lg,
    gap: spacing.sm,
    padding: spacing.md,
  },
  label: {
    color: colors.onSurface,
    fontSize: typography.labelLarge,
    fontWeight: '700',
    marginTop: spacing.sm,
  },
  input: {
    backgroundColor: colors.surfaceContainerLow,
    borderRadius: radii.md,
    color: colors.onSurface,
    fontSize: typography.bodyLarge,
    minHeight: 52,
    paddingHorizontal: spacing.md,
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
