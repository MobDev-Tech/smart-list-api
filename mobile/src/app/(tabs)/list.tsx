import { Inter_400Regular, Inter_600SemiBold, Inter_700Bold, useFonts } from '@expo-google-fonts/inter';
import { router } from 'expo-router';
import { useState } from 'react';
import {
  ActivityIndicator,
  Modal,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
  View,
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import Svg, { Circle, Path } from 'react-native-svg';

import { colors, radii, spacing } from '../../constants/design-tokens';

type Category = 'Mercearia' | 'Hortifrúti' | 'Laticínios' | 'Padaria' | 'Limpeza' | 'Higiene Pessoal';

type GroceryItem = {
  id: string;
  title: string;
  meta: string;
  category: Category;
};

const categories: Category[] = [
  'Mercearia',
  'Hortifrúti',
  'Laticínios',
  'Padaria',
  'Limpeza',
  'Higiene Pessoal',
];

const initialPending: GroceryItem[] = [
  { id: 'coffee', title: 'Café moído tradicional', meta: '2 pacotes (500g)', category: 'Mercearia' },
  { id: 'milk', title: 'Leite desnatado', meta: '4 caixas (1L)', category: 'Laticínios' },
  { id: 'oil', title: 'Azeite de oliva extra virgem', meta: '1 garrafa (500ml)', category: 'Mercearia' },
  { id: 'bread', title: 'Pão de forma integral', meta: '1 pct (450g)', category: 'Padaria' },
];

const initialCompleted: GroceryItem[] = [
  { id: 'detergent', title: 'Detergente neutro', meta: '', category: 'Limpeza' },
  { id: 'apple', title: 'Maçã Fuji', meta: '1 kg', category: 'Hortifrúti' },
  { id: 'soap', title: 'Sabonete líquido', meta: '', category: 'Higiene Pessoal' },
];

export default function ShoppingListScreen() {
  const [fontsLoaded] = useFonts({ Inter_400Regular, Inter_600SemiBold, Inter_700Bold });
  const [pending, setPending] = useState<GroceryItem[]>(initialPending);
  const [completed, setCompleted] = useState<GroceryItem[]>(initialCompleted);
  const [completedExpanded, setCompletedExpanded] = useState(true);
  const [paused, setPaused] = useState(false);
  const [sheetOpen, setSheetOpen] = useState(false);
  const [name, setName] = useState('');
  const [quantity, setQuantity] = useState('');
  const [category, setCategory] = useState<Category>('Mercearia');

  if (!fontsLoaded) {
    return (
      <View style={styles.loading}>
        <ActivityIndicator color={colors.primary} />
      </View>
    );
  }

  function togglePending(item: GroceryItem) {
    setPending((current) => current.filter((entry) => entry.id !== item.id));
    setCompleted((current) => [...current, item]);
  }

  function toggleCompleted(item: GroceryItem) {
    setCompleted((current) => current.filter((entry) => entry.id !== item.id));
    setPending((current) => [...current, item]);
  }

  function saveQuickItem() {
    const trimmedName = name.trim();
    if (!trimmedName) return;

    setPending((current) => [
      {
        id: `quick-${Date.now()}`,
        title: trimmedName,
        meta: quantity.trim(),
        category,
      },
      ...current,
    ]);
    setName('');
    setQuantity('');
    setCategory('Mercearia');
    setSheetOpen(false);
  }

  return (
    <SafeAreaView edges={['top']} style={styles.safeArea}>
      <ScrollView contentContainerStyle={styles.content} showsVerticalScrollIndicator={false}>
        <ListHeader onSettings={() => router.push('/(tabs)/settings')} />

        <View style={styles.summaryRow}>
          <Text style={styles.pendingCount}>{pending.length} itens pendentes</Text>
          <Pressable
            accessibilityLabel={paused ? 'Reativar lembretes' : 'Pausar lembretes'}
            accessibilityRole="button"
            onPress={() => setPaused((current) => !current)}
            style={[styles.pauseButton, paused && styles.pauseButtonActive]}
          >
            <SnoozeIcon color={paused ? colors.tertiary : colors.onSurfaceVariant} />
            <Text style={[styles.pauseText, paused && styles.pauseTextActive]}>
              {paused ? 'Pausado (1h)' : 'Pausar'}
            </Text>
          </Pressable>
        </View>

        <View style={styles.pendingList}>
          {pending.map((item) => (
            <GroceryCard key={item.id} item={item} onToggle={() => togglePending(item)} />
          ))}
          {pending.length === 0 ? (
            <View style={styles.emptyState}>
              <Text style={styles.emptyTitle}>Tudo comprado</Text>
              <Text style={styles.emptyText}>Adicione um produto quando sua próxima lista começar.</Text>
            </View>
          ) : null}
        </View>

        <View style={styles.completedHeader}>
          <Pressable
            accessibilityLabel={completedExpanded ? 'Recolher comprados recentemente' : 'Expandir comprados recentemente'}
            accessibilityRole="button"
            onPress={() => setCompletedExpanded((current) => !current)}
            style={styles.completedTitleButton}
          >
            <ChevronIcon expanded={completedExpanded} />
            <Text style={styles.completedTitle}>Comprados recentemente ({completed.length})</Text>
          </Pressable>
          <Pressable accessibilityRole="button" onPress={() => setCompleted([])} style={styles.clearButton}>
            <Text style={styles.clearText}>Limpar</Text>
          </Pressable>
        </View>

        {completedExpanded ? (
          <View style={styles.completedList}>
            {completed.map((item) => (
              <GroceryCard key={item.id} completed item={item} onToggle={() => toggleCompleted(item)} />
            ))}
            {completed.length === 0 ? <Text style={styles.noCompleted}>Nenhum item comprado recentemente.</Text> : null}
          </View>
        ) : null}
      </ScrollView>

      <Pressable
        accessibilityLabel="Adicionar produto à lista"
        accessibilityRole="button"
        onPress={() => setSheetOpen(true)}
        style={({ pressed }) => [styles.fab, pressed && styles.fabPressed]}
      >
        <PlusIcon />
      </Pressable>

      <QuickAddSheet
        category={category}
        name={name}
        onCategoryChange={setCategory}
        onClose={() => setSheetOpen(false)}
        onNameChange={setName}
        onQuantityChange={setQuantity}
        onSave={saveQuickItem}
        quantity={quantity}
        visible={sheetOpen}
      />
    </SafeAreaView>
  );
}

function ListHeader({ onSettings }: { onSettings: () => void }) {
  return (
    <View style={styles.header}>
      <View style={styles.headerCopy}>
        <Text style={styles.time}>09:41</Text>
        <Text style={styles.screenTitle}>Lista De Compras</Text>
        <View style={styles.monitoringRow}>
          <View style={styles.monitoringDot} />
          <Text style={styles.monitoringText}>MONITORAMENTO RESIDENCIAL ATIVO</Text>
        </View>
      </View>
      <View style={styles.headerActions}>
        <Pressable accessibilityLabel="Abrir configurações" accessibilityRole="button" onPress={onSettings} style={styles.iconButton}>
          <GearIcon color={colors.onSurfaceVariant} />
        </Pressable>
        <View style={styles.avatar}>
          <UserIcon />
        </View>
      </View>
    </View>
  );
}

function GroceryCard({ completed = false, item, onToggle }: { completed?: boolean; item: GroceryItem; onToggle: () => void }) {
  return (
    <View style={[styles.groceryCard, completed && styles.completedCard]}>
      <Pressable
        accessibilityLabel={completed ? `Marcar ${item.title} como pendente` : `Marcar ${item.title} como comprado`}
        accessibilityRole="checkbox"
        accessibilityState={{ checked: completed }}
        onPress={onToggle}
        style={[styles.checkbox, completed && styles.checkboxChecked]}
      >
        {completed ? <CheckIcon /> : null}
      </Pressable>
      <View style={styles.itemCopy}>
        <Text style={[styles.itemTitle, completed && styles.completedText]}>{item.title}</Text>
        <View style={styles.itemMetaRow}>
          {item.meta ? <Text style={[styles.itemMeta, completed && styles.completedMeta]}>{item.meta}</Text> : null}
          {item.meta ? <Text style={[styles.metaDivider, completed && styles.completedMeta]}>•</Text> : null}
          <Text style={[styles.categoryChip, completed && styles.completedChip]}>{item.category}</Text>
        </View>
      </View>
      {!completed ? (
        <Pressable accessibilityLabel={`Mais opções para ${item.title}`} accessibilityRole="button" style={styles.moreButton}>
          <MoreIcon />
        </Pressable>
      ) : null}
    </View>
  );
}

function QuickAddSheet({
  category,
  name,
  onCategoryChange,
  onClose,
  onNameChange,
  onQuantityChange,
  onSave,
  quantity,
  visible,
}: {
  category: Category;
  name: string;
  onCategoryChange: (value: Category) => void;
  onClose: () => void;
  onNameChange: (value: string) => void;
  onQuantityChange: (value: string) => void;
  onSave: () => void;
  quantity: string;
  visible: boolean;
}) {
  return (
    <Modal animationType="slide" onRequestClose={onClose} transparent visible={visible}>
      <View style={styles.modalRoot}>
        <Pressable accessibilityLabel="Fechar adicionar produto" onPress={onClose} style={styles.modalBackdrop} />
        <View style={styles.sheet}>
          <View style={styles.sheetHandle} />
          <View style={styles.sheetHeader}>
            <Text style={styles.sheetTitle}>Adicionar Produto</Text>
            <Pressable accessibilityLabel="Fechar" accessibilityRole="button" onPress={onClose} style={styles.closeButton}>
              <Text style={styles.closeText}>×</Text>
            </Pressable>
          </View>
          <Text style={styles.fieldLabel}>Nome do item</Text>
          <TextInput
            accessibilityLabel="Nome do item"
            onChangeText={onNameChange}
            placeholder="Ex: Iogurte Grego, Ovos..."
            placeholderTextColor={colors.outline}
            style={styles.input}
            value={name}
          />
          <Text style={styles.fieldLabel}>Quantidade</Text>
          <TextInput
            accessibilityLabel="Quantidade"
            onChangeText={onQuantityChange}
            placeholder="Ex: 2 un, 500g"
            placeholderTextColor={colors.outline}
            style={styles.input}
            value={quantity}
          />
          <Text style={styles.fieldLabel}>Categoria</Text>
          <View style={styles.categoryOptions}>
            {categories.map((option) => (
              <Pressable
                accessibilityRole="button"
                key={option}
                onPress={() => onCategoryChange(option)}
                style={[styles.categoryOption, option === category && styles.categoryOptionSelected]}
              >
                <Text style={[styles.categoryOptionText, option === category && styles.categoryOptionTextSelected]}>{option}</Text>
              </Pressable>
            ))}
          </View>
          <Pressable accessibilityRole="button" disabled={!name.trim()} onPress={onSave} style={[styles.saveButton, !name.trim() && styles.saveButtonDisabled]}>
            <Text style={styles.saveButtonText}>Salvar na Lista</Text>
          </Pressable>
        </View>
      </View>
    </Modal>
  );
}

function GearIcon({ color }: { color: string }) {
  return (
    <Svg height="24" viewBox="0 0 24 24" width="24">
      <Path d="m19.4 13.5 1.6 1.2-1.8 3.1-1.9-.8a7.5 7.5 0 0 1-2.1 1.2L15 20.3h-3.6l-.3-2.1a7.5 7.5 0 0 1-2.1-1.2l-1.9.8-1.8-3.1 1.6-1.2a7.7 7.7 0 0 1 0-2.4L5.3 9.9l1.8-3.1 1.9.8a7.5 7.5 0 0 1 2.1-1.2l.3-2.1H15l.3 2.1a7.5 7.5 0 0 1 2.1 1.2l1.9-.8 1.8 3.1-1.6 1.2a7.7 7.7 0 0 1-.1 2.4Z" fill="none" stroke={color} strokeLinejoin="round" strokeWidth="1.8" />
      <Circle cx="13.2" cy="12.3" fill="none" r="2.8" stroke={color} strokeWidth="1.8" />
    </Svg>
  );
}

function UserIcon() {
  return (
    <Svg height="24" viewBox="0 0 24 24" width="24">
      <Circle cx="12" cy="8" fill="none" r="3" stroke={colors.onPrimary} strokeWidth="1.8" />
      <Path d="M5.5 19c.8-3.1 3-4.7 6.5-4.7s5.7 1.6 6.5 4.7" fill="none" stroke={colors.onPrimary} strokeLinecap="round" strokeWidth="1.8" />
    </Svg>
  );
}

function SnoozeIcon({ color }: { color: string }) {
  return (
    <Svg height="18" viewBox="0 0 24 24" width="18">
      <Circle cx="11" cy="13" fill="none" r="6" stroke={color} strokeWidth="1.7" />
      <Path d="M11 10v3l2 1M7 4h4M8 2h2M17 8h4l-4 4h4" fill="none" stroke={color} strokeLinecap="round" strokeLinejoin="round" strokeWidth="1.5" />
    </Svg>
  );
}

function CheckIcon() {
  return (
    <Svg height="18" viewBox="0 0 24 24" width="18">
      <Path d="m5 12 4 4L19 6" fill="none" stroke={colors.onPrimary} strokeLinecap="round" strokeLinejoin="round" strokeWidth="2.5" />
    </Svg>
  );
}

function MoreIcon() {
  return (
    <Svg height="24" viewBox="0 0 24 24" width="24">
      <Circle cx="12" cy="5" fill={colors.onSurfaceVariant} r="1.5" />
      <Circle cx="12" cy="12" fill={colors.onSurfaceVariant} r="1.5" />
      <Circle cx="12" cy="19" fill={colors.onSurfaceVariant} r="1.5" />
    </Svg>
  );
}

function ChevronIcon({ expanded }: { expanded: boolean }) {
  return (
    <Svg height="18" viewBox="0 0 24 24" width="18">
      <Path d={expanded ? 'm7 14 5-5 5 5' : 'm7 10 5 5 5-5'} fill="none" stroke={colors.onSurfaceVariant} strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />
    </Svg>
  );
}

function PlusIcon() {
  return (
    <Svg height="28" viewBox="0 0 24 24" width="28">
      <Path d="M12 5v14M5 12h14" stroke={colors.onPrimary} strokeLinecap="round" strokeWidth="2.2" />
    </Svg>
  );
}

const styles = StyleSheet.create({
  loading: { alignItems: 'center', backgroundColor: colors.surface, flex: 1, justifyContent: 'center' },
  safeArea: { backgroundColor: colors.surface, flex: 1 },
  content: { paddingBottom: 116, paddingHorizontal: spacing.md },
  header: { alignItems: 'center', flexDirection: 'row', justifyContent: 'space-between', minHeight: 112 },
  headerCopy: { flex: 1 },
  time: { color: colors.onSurface, fontFamily: 'Inter_600SemiBold', fontSize: 12, lineHeight: 16 },
  screenTitle: { color: colors.onSurface, fontFamily: 'Inter_700Bold', fontSize: 24, letterSpacing: -0.5, lineHeight: 30, marginTop: 2 },
  monitoringRow: { alignItems: 'center', flexDirection: 'row', gap: 6, marginTop: 3 },
  monitoringDot: { backgroundColor: colors.primary, borderRadius: 5, height: 8, width: 8 },
  monitoringText: { color: colors.primary, fontFamily: 'Inter_600SemiBold', fontSize: 11, letterSpacing: 0.25 },
  headerActions: { alignItems: 'center', flexDirection: 'row', gap: 12, marginLeft: spacing.sm },
  iconButton: { alignItems: 'center', height: 40, justifyContent: 'center', width: 40 },
  avatar: { alignItems: 'center', backgroundColor: colors.primary, borderRadius: 20, height: 40, justifyContent: 'center', width: 40 },
  summaryRow: { alignItems: 'center', flexDirection: 'row', justifyContent: 'space-between', marginBottom: spacing.sm },
  pendingCount: { color: colors.onSurface, fontFamily: 'Inter_700Bold', fontSize: 20, lineHeight: 26 },
  pauseButton: { alignItems: 'center', backgroundColor: colors.surfaceContainer, borderRadius: radii.full, flexDirection: 'row', gap: 6, minHeight: 40, paddingHorizontal: 14 },
  pauseButtonActive: { backgroundColor: '#FFEDC7' },
  pauseText: { color: colors.onSurfaceVariant, fontFamily: 'Inter_600SemiBold', fontSize: 13 },
  pauseTextActive: { color: colors.tertiary },
  pendingList: { gap: spacing.sm },
  groceryCard: { alignItems: 'center', backgroundColor: colors.surfaceContainerLowest, borderRadius: radii.md, elevation: 1, flexDirection: 'row', gap: spacing.md, minHeight: 72, paddingHorizontal: 14, paddingVertical: 12, shadowColor: colors.onSurface, shadowOffset: { height: 1, width: 0 }, shadowOpacity: 0.05, shadowRadius: 3 },
  completedCard: { backgroundColor: colors.surfaceContainerLow, elevation: 0, opacity: 0.72, shadowOpacity: 0 },
  checkbox: { alignItems: 'center', backgroundColor: colors.surfaceContainerHighest, borderRadius: 8, height: 24, justifyContent: 'center', width: 24 },
  checkboxChecked: { backgroundColor: '#65A982' },
  itemCopy: { flex: 1, minWidth: 0 },
  itemTitle: { color: colors.onSurface, fontFamily: 'Inter_600SemiBold', fontSize: 16, lineHeight: 21 },
  completedText: { color: colors.onSurfaceVariant, textDecorationLine: 'line-through' },
  itemMetaRow: { alignItems: 'center', flexDirection: 'row', flexWrap: 'wrap', gap: 6, marginTop: 3 },
  itemMeta: { color: colors.onSurfaceVariant, fontFamily: 'Inter_400Regular', fontSize: 12, lineHeight: 16 },
  completedMeta: { color: colors.onSurfaceVariant },
  metaDivider: { color: colors.outline, fontSize: 12 },
  categoryChip: { backgroundColor: '#E8EBFF', borderRadius: 9, color: colors.onSurfaceVariant, fontFamily: 'Inter_600SemiBold', fontSize: 11, lineHeight: 18, overflow: 'hidden', paddingHorizontal: 9 },
  completedChip: { backgroundColor: 'transparent', paddingHorizontal: 0 },
  moreButton: { alignItems: 'center', height: 40, justifyContent: 'center', width: 24 },
  completedHeader: { alignItems: 'center', flexDirection: 'row', justifyContent: 'space-between', marginTop: 30, minHeight: 30 },
  completedTitleButton: { alignItems: 'center', flexDirection: 'row', gap: 6 },
  completedTitle: { color: colors.onSurfaceVariant, fontFamily: 'Inter_600SemiBold', fontSize: 14 },
  clearButton: { minHeight: 40, justifyContent: 'center', paddingHorizontal: 4 },
  clearText: { color: colors.onSurfaceVariant, fontFamily: 'Inter_600SemiBold', fontSize: 12 },
  completedList: { gap: spacing.sm, marginTop: spacing.sm },
  noCompleted: { color: colors.onSurfaceVariant, fontFamily: 'Inter_400Regular', fontSize: 13, paddingVertical: spacing.md },
  emptyState: { alignItems: 'center', backgroundColor: colors.surfaceContainerLow, borderRadius: radii.md, marginTop: spacing.sm, padding: spacing.lg },
  emptyTitle: { color: colors.onSurface, fontFamily: 'Inter_700Bold', fontSize: 18 },
  emptyText: { color: colors.onSurfaceVariant, fontFamily: 'Inter_400Regular', fontSize: 14, lineHeight: 20, marginTop: spacing.xs, textAlign: 'center' },
  fab: { alignItems: 'center', backgroundColor: colors.primary, borderRadius: 28, bottom: 24, elevation: 5, height: 56, justifyContent: 'center', position: 'absolute', right: 16, shadowColor: colors.primary, shadowOffset: { height: 6, width: 0 }, shadowOpacity: 0.3, shadowRadius: 8, width: 56 },
  fabPressed: { transform: [{ scale: 0.96 }] },
  modalRoot: { flex: 1, justifyContent: 'flex-end' },
  modalBackdrop: { backgroundColor: 'rgba(19, 27, 46, 0.42)', bottom: 0, left: 0, position: 'absolute', right: 0, top: 0 },
  sheet: { backgroundColor: colors.surfaceContainerLowest, borderTopLeftRadius: radii.xl, borderTopRightRadius: radii.xl, elevation: 8, padding: spacing.lg, paddingBottom: 32 },
  sheetHandle: { alignSelf: 'center', backgroundColor: colors.outlineVariant, borderRadius: 3, height: 4, marginBottom: spacing.lg, width: 36 },
  sheetHeader: { alignItems: 'center', flexDirection: 'row', justifyContent: 'space-between', marginBottom: spacing.md },
  sheetTitle: { color: colors.onSurface, fontFamily: 'Inter_700Bold', fontSize: 22 },
  closeButton: { alignItems: 'center', height: 40, justifyContent: 'center', width: 40 },
  closeText: { color: colors.onSurfaceVariant, fontFamily: 'Inter_400Regular', fontSize: 28, lineHeight: 30 },
  fieldLabel: { color: colors.onSurface, fontFamily: 'Inter_600SemiBold', fontSize: 13, marginBottom: 6, marginTop: spacing.sm },
  input: { backgroundColor: colors.surfaceContainerLow, borderColor: colors.outlineVariant, borderRadius: radii.md, borderWidth: 1, color: colors.onSurface, fontFamily: 'Inter_400Regular', fontSize: 15, height: 48, paddingHorizontal: spacing.md },
  categoryOptions: { flexDirection: 'row', flexWrap: 'wrap', gap: spacing.sm },
  categoryOption: { backgroundColor: colors.surfaceContainerLow, borderRadius: radii.full, minHeight: 36, justifyContent: 'center', paddingHorizontal: 12 },
  categoryOptionSelected: { backgroundColor: colors.primary },
  categoryOptionText: { color: colors.onSurfaceVariant, fontFamily: 'Inter_600SemiBold', fontSize: 12 },
  categoryOptionTextSelected: { color: colors.onPrimary },
  saveButton: { alignItems: 'center', backgroundColor: colors.primary, borderRadius: radii.md, height: 50, justifyContent: 'center', marginTop: spacing.lg },
  saveButtonDisabled: { opacity: 0.45 },
  saveButtonText: { color: colors.onPrimary, fontFamily: 'Inter_600SemiBold', fontSize: 14 },
});
