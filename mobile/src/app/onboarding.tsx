import { Inter_400Regular, Inter_600SemiBold, Inter_700Bold, useFonts } from '@expo-google-fonts/inter';
import { router } from 'expo-router';
import { useEffect, useMemo } from 'react';
import {
  ActivityIndicator,
  Animated,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  View,
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import Svg, { Circle, G, Path, Rect } from 'react-native-svg';

import { colors, radii, spacing } from '../constants/design-tokens';
import { completeOnboarding } from '../lib/storage';

const copy = {
  title: 'Nunca mais esqueça as compras ao sair de casa',
  description:
    'O Lista Inteligente monitora sua lista e avisa automaticamente assim que você se afasta da sua residência com produtos pendentes.',
  steps: [
    {
      label: 'Passo 1',
      title: 'Cadastre o que precisa',
      description: 'Anote produtos rapidamente com ou sem quantidades, organizados pelo seu ritmo.',
      icon: 'checklist' as const,
      tone: 'primary' as const,
    },
    {
      label: 'Passo 2',
      title: 'Defina sua residência',
      description: 'Configure a sua casa e a distância ideal (100m a 300m) para ser avisado sem falsos alarmes.',
      icon: 'location' as const,
      tone: 'primary' as const,
    },
    {
      label: 'Passo 3',
      title: 'Lembretes inteligentes',
      description: 'O app avisa na hora exata em que você sai de casa para que sua viagem não seja em vão.',
      icon: 'notification' as const,
      tone: 'secondary' as const,
    },
  ],
};

export default function OnboardingScreen() {
  const [fontsLoaded] = useFonts({
    Inter_400Regular,
    Inter_600SemiBold,
    Inter_700Bold,
  });

  async function skipSetup() {
    await completeOnboarding();
    router.replace('/(tabs)/list');
  }

  if (!fontsLoaded) {
    return (
      <View style={styles.loading}>
        <ActivityIndicator color={colors.primary} />
      </View>
    );
  }

  return (
    <SafeAreaView edges={['top', 'bottom']} style={styles.safeArea}>
      <ScrollView contentContainerStyle={styles.content} showsVerticalScrollIndicator={false}>
        <GeofenceHero />

        <View style={styles.introduction}>
          <Text style={styles.title}>{copy.title}</Text>
          <Text style={styles.description}>{copy.description}</Text>
        </View>

        <View style={styles.steps}>
          {copy.steps.map((step) => (
            <StepCard key={step.label} {...step} />
          ))}
        </View>

        <PrivacyCard />

        <View style={styles.actions}>
          <Pressable
            accessibilityLabel="Configurar residência e permissões"
            accessibilityRole="button"
            onPress={() => router.push({ pathname: '/residence', params: { from: 'onboarding' } })}
            style={({ pressed }) => [styles.primaryButton, pressed && styles.pressed]}
          >
            <NearMeIcon />
            <Text style={styles.primaryButtonText}>Configurar Residência e Permissões</Text>
          </Pressable>

          <Pressable
            accessibilityLabel="Começar a usar a lista agora, configurar depois"
            accessibilityRole="button"
            onPress={skipSetup}
            style={({ pressed }) => [styles.secondaryButton, pressed && styles.pressed]}
          >
            <Text style={styles.secondaryButtonText}>
              Começar a usar a lista agora (configurar depois)
            </Text>
          </Pressable>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
}

function GeofenceHero() {
  const pulse = useMemo(() => new Animated.Value(0), []);

  useEffect(() => {
    const animation = Animated.loop(
      Animated.sequence([
        Animated.timing(pulse, { duration: 1500, toValue: 1, useNativeDriver: true }),
        Animated.timing(pulse, { duration: 1500, toValue: 0, useNativeDriver: true }),
      ]),
    );
    animation.start();
    return () => animation.stop();
  }, [pulse]);

  const pulseStyle = {
    opacity: pulse.interpolate({ inputRange: [0, 1], outputRange: [0.2, 0.02] }),
    transform: [{ scale: pulse.interpolate({ inputRange: [0, 1], outputRange: [0.94, 1.08] }) }],
  };

  return (
    <View style={styles.hero}>
      <View style={styles.halo}>
        <Animated.View style={[styles.haloPulse, pulseStyle]} />
        <View style={styles.haloBackdrop} />
        <View style={styles.illustration}>
          <Svg height="120" viewBox="0 0 160 160" width="120">
            <Circle cx="80" cy="80" r="68" stroke={colors.outlineVariant} strokeDasharray="4 4" strokeWidth="1.5" opacity={0.6} />
            <Circle cx="80" cy="80" r="50" stroke={colors.primaryContainer} strokeWidth="2" opacity={0.3} />
            <Circle cx="80" cy="80" fill={colors.primaryContainer} opacity={0.15} r="32" />
            <Path d="M40 120 C60 110 70 95 80 80 C90 65 100 50 120 40" fill="none" stroke={colors.secondary} strokeDasharray="3 5" strokeLinecap="round" strokeWidth="2.5" opacity={0.35} />
            <G transform="translate(34 68)">
              <Rect fill={colors.surfaceContainerHigh} height="24" rx="4" width="28" x="2" y="14" />
              <Path d="M0 15L16 3L32 15Z" fill={colors.primary} />
              <Rect fill={colors.onSurfaceVariant} height="14" rx="2" width="10" x="11" y="24" />
              <Circle cx="16" cy="11" fill={colors.surfaceContainerLowest} r="3" />
            </G>
            <G transform="translate(94 64)">
              <Rect fill={colors.surfaceContainer} height="30" rx="6" width="28" x="4" y="14" />
              <Path d="M11 14V9C11 5.13 14.13 2 18 2C21.87 2 25 5.13 25 9V14" fill="none" stroke={colors.primary} strokeLinecap="round" strokeWidth="3" />
              <Circle cx="18" cy="27" fill={colors.primaryContainer} r="7" />
              <Path d="M16 27L17.5 28.5L20.5 25.5" fill="none" stroke={colors.onPrimary} strokeLinecap="round" strokeLinejoin="round" strokeWidth="1.5" />
            </G>
            <G transform="translate(71 38)">
              <Path d="M9 0C4.03 0 0 4.03 0 9C0 15.75 9 26 9 26C9 26 18 15.75 18 9C18 4.03 13.97 0 9 0Z" fill={colors.primary} />
              <Circle cx="9" cy="9" fill={colors.surfaceContainerLowest} r="4" />
              <Circle cx="9" cy="9" fill={colors.primary} r="2" />
            </G>
          </Svg>
          <View style={styles.autoBadge}>
            <View style={styles.autoDot} />
            <Text style={styles.autoText}>AUTO</Text>
          </View>
        </View>
      </View>
    </View>
  );
}

type StepCardProps = (typeof copy.steps)[number];

function StepCard({ label, title, description, icon, tone }: StepCardProps) {
  return (
    <View style={styles.stepCard}>
      <View style={styles.stepIconBox}>
        <StepIcon icon={icon} tone={tone} />
      </View>
      <View style={styles.stepCopy}>
        <Text style={[styles.stepLabel, tone === 'secondary' && styles.secondaryStepLabel]}>{label}</Text>
        <Text style={styles.stepTitle}>{title}</Text>
        <Text style={styles.stepDescription}>{description}</Text>
      </View>
    </View>
  );
}

function StepIcon({ icon, tone }: Pick<StepCardProps, 'icon' | 'tone'>) {
  const iconColor = tone === 'secondary' ? colors.secondary : colors.primary;

  if (icon === 'notification') {
    return (
      <Svg height="26" viewBox="0 0 26 26" width="26">
        <Path d="M6 17.5V11a7 7 0 0114 0v6.5l2 2H4l2-2Z" fill={iconColor} />
        <Path d="M10 22h6" stroke={iconColor} strokeLinecap="round" strokeWidth="2" />
        <Path d="M2 9a9 9 0 010 6M24 9a9 9 0 000 6" fill="none" stroke={iconColor} strokeWidth="2" />
      </Svg>
    );
  }

  if (icon === 'location') {
    return (
      <Svg height="26" viewBox="0 0 26 26" width="26">
        <Path d="M13 23s7-7.3 7-13a7 7 0 10-14 0c0 5.7 7 13 7 13Z" fill="none" stroke={iconColor} strokeWidth="2" />
        <Circle cx="13" cy="10" fill={iconColor} r="2.5" />
        <Path d="M3 21h5M18 21h5" stroke={iconColor} strokeLinecap="round" strokeWidth="2" opacity={0.45} />
      </Svg>
    );
  }

  return (
    <Svg height="26" viewBox="0 0 26 26" width="26">
      <Path d="M4 7h3M4 13h3M4 19h3" stroke={iconColor} strokeLinecap="round" strokeWidth="2" />
      <Path d="M10 7h12M10 13h12M10 19h12" stroke={iconColor} strokeLinecap="round" strokeWidth="2" />
      <Path d="M4.5 6.8l1.2 1.2L8 5.8" fill="none" stroke={iconColor} strokeLinecap="round" strokeWidth="1.6" />
    </Svg>
  );
}

function PrivacyCard() {
  return (
    <View style={styles.privacyCard}>
      <View style={styles.privacyIcon}>
        <Svg height="18" viewBox="0 0 24 24" width="18">
          <Path d="M12 3l7 3v5c0 4.8-3 8.2-7 10-4-1.8-7-5.2-7-10V6l7-3Z" fill={colors.onSurfaceVariant} />
          <Path d="m8.5 12 2.2 2.2 4.8-5" fill="none" stroke={colors.surfaceContainerHighest} strokeLinecap="round" strokeLinejoin="round" strokeWidth="1.8" />
        </Svg>
      </View>
      <View style={styles.privacyCopy}>
        <Text style={styles.privacyTitle}>Sua privacidade em primeiro lugar</Text>
        <Text style={styles.privacyText}>
          A localização é usada apenas no seu aparelho para identificar a saída de casa e nunca é enviada para servidores externos. Você pode usar a lista normalmente mesmo sem ativar a localização.
        </Text>
      </View>
    </View>
  );
}

function NearMeIcon() {
  return (
    <Svg height="20" viewBox="0 0 24 24" width="20">
      <Path d="m4 4 16 7-7 2-2 7L4 4Z" fill="none" stroke={colors.onPrimary} strokeLinejoin="round" strokeWidth="2" />
    </Svg>
  );
}

const styles = StyleSheet.create({
  loading: { alignItems: 'center', backgroundColor: colors.surface, flex: 1, justifyContent: 'center' },
  safeArea: { backgroundColor: colors.surface, flex: 1 },
  content: { paddingBottom: spacing.xl, paddingHorizontal: spacing.md },
  hero: { alignItems: 'center', paddingBottom: spacing.lg, paddingTop: spacing.md },
  halo: { alignItems: 'center', height: 176, justifyContent: 'center', position: 'relative', width: 176 },
  haloPulse: { backgroundColor: colors.primaryContainer, borderRadius: 88, height: 176, position: 'absolute', width: 176 },
  haloBackdrop: { backgroundColor: colors.surfaceContainerHighest, borderRadius: 80, height: 160, opacity: 0.62, position: 'absolute', width: 160 },
  illustration: { alignItems: 'center', backgroundColor: colors.surfaceContainerLowest, borderRadius: 72, elevation: 2, height: 144, justifyContent: 'center', shadowColor: colors.onSurface, shadowOffset: { height: 3, width: 0 }, shadowOpacity: 0.12, shadowRadius: 5, width: 144 },
  autoBadge: { alignItems: 'center', backgroundColor: colors.primary, borderRadius: 10, bottom: 9, flexDirection: 'row', gap: 4, paddingHorizontal: 8, paddingVertical: 3, position: 'absolute', right: 8 },
  autoDot: { backgroundColor: '#62DF7D', borderRadius: 3, height: 6, width: 6 },
  autoText: { color: colors.onPrimary, fontFamily: 'Inter_700Bold', fontSize: 10, letterSpacing: 0.7, lineHeight: 12 },
  introduction: { alignItems: 'center', marginBottom: spacing.lg },
  title: { color: colors.onSurface, fontFamily: 'Inter_700Bold', fontSize: 26, letterSpacing: -0.52, lineHeight: 32, maxWidth: 350, textAlign: 'center' },
  description: { color: colors.onSurfaceVariant, fontFamily: 'Inter_400Regular', fontSize: 14, lineHeight: 22, marginTop: spacing.xs, maxWidth: 350, textAlign: 'center' },
  steps: { gap: spacing.sm, marginBottom: spacing.lg },
  stepCard: { alignItems: 'flex-start', backgroundColor: colors.surfaceContainerLowest, borderRadius: radii.md, elevation: 1, flexDirection: 'row', gap: spacing.md, padding: spacing.md, shadowColor: colors.onSurface, shadowOffset: { height: 2, width: 0 }, shadowOpacity: 0.06, shadowRadius: 5 },
  stepIconBox: { alignItems: 'center', backgroundColor: colors.surfaceContainer, borderRadius: radii.md, flexShrink: 0, height: 48, justifyContent: 'center', width: 48 },
  stepCopy: { flex: 1, minWidth: 0 },
  stepLabel: { color: colors.primary, fontFamily: 'Inter_700Bold', fontSize: 11, letterSpacing: 0.65, lineHeight: 14, marginBottom: 4, textTransform: 'uppercase' },
  secondaryStepLabel: { color: colors.secondary },
  stepTitle: { color: colors.onSurface, fontFamily: 'Inter_600SemiBold', fontSize: 18, lineHeight: 24 },
  stepDescription: { color: colors.onSurfaceVariant, fontFamily: 'Inter_400Regular', fontSize: 14, lineHeight: 20, marginTop: 1 },
  privacyCard: { alignItems: 'flex-start', backgroundColor: colors.surfaceContainerLow, borderRadius: radii.md, flexDirection: 'row', gap: spacing.sm, marginBottom: spacing.xl, padding: spacing.md },
  privacyIcon: { alignItems: 'center', backgroundColor: colors.surfaceContainerHighest, borderRadius: 16, flexShrink: 0, height: 32, justifyContent: 'center', marginTop: 2, width: 32 },
  privacyCopy: { flex: 1, minWidth: 0 },
  privacyTitle: { color: colors.onSurface, fontFamily: 'Inter_600SemiBold', fontSize: 12, lineHeight: 16, marginBottom: 2 },
  privacyText: { color: colors.onSurfaceVariant, fontFamily: 'Inter_400Regular', fontSize: 12, lineHeight: 20 },
  actions: { gap: spacing.sm },
  primaryButton: { alignItems: 'center', backgroundColor: colors.primary, borderRadius: radii.md, elevation: 2, flexDirection: 'row', gap: spacing.sm, height: 48, justifyContent: 'center', paddingHorizontal: spacing.md, shadowColor: colors.onSurface, shadowOffset: { height: 2, width: 0 }, shadowOpacity: 0.16, shadowRadius: 4 },
  primaryButtonText: { color: colors.onPrimary, fontFamily: 'Inter_600SemiBold', fontSize: 14, lineHeight: 20 },
  secondaryButton: { alignItems: 'center', backgroundColor: colors.surfaceContainerLow, borderRadius: radii.md, height: 48, justifyContent: 'center', paddingHorizontal: spacing.sm },
  secondaryButtonText: { color: colors.onSurface, fontFamily: 'Inter_600SemiBold', fontSize: 14, lineHeight: 20, textAlign: 'center' },
  pressed: { opacity: 0.9, transform: [{ scale: 0.985 }] },
});
