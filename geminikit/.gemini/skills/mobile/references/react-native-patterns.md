# React Native (Expo) Patterns Reference

Canonical patterns for Expo Router, React Navigation, and React Native development.

## Expo Router File Structure
```
app/
├── _layout.tsx          # Root layout — providers, theme, auth gate
├── index.tsx            # Home screen /
├── (tabs)/              # Tab group
│   ├── _layout.tsx      # Tab bar configuration
│   ├── index.tsx        # / (Home tab)
│   ├── explore.tsx      # /explore
│   └── profile.tsx      # /profile
├── (auth)/              # Auth group (no tab bar)
│   ├── login.tsx
│   └── register.tsx
└── modal.tsx            # /modal — presented as modal on iOS
```

## Root Layout with Auth Gate
```tsx
// app/_layout.tsx
import { Stack } from "expo-router";
import { useSession } from "@/hooks/useSession";
import { useEffect } from "react";
import { router } from "expo-router";

export default function RootLayout() {
  const { session, isLoading } = useSession();

  useEffect(() => {
    if (!isLoading) {
      if (!session) {
        router.replace("/(auth)/login");
      }
    }
  }, [session, isLoading]);

  return (
    <Stack screenOptions={{ headerShown: false }}>
      <Stack.Screen name="(tabs)" options={{ headerShown: false }} />
      <Stack.Screen name="(auth)" options={{ headerShown: false }} />
      <Stack.Screen name="modal" options={{ presentation: "modal" }} />
    </Stack>
  );
}
```

## Screen Component Pattern
```tsx
// app/(tabs)/profile.tsx
import { View, Text, FlatList, Pressable, StyleSheet } from "react-native";
import { useSafeAreaInsets } from "react-native-safe-area-context";
import { useRouter } from "expo-router";
import { useProfile } from "@/hooks/useProfile";

export default function ProfileScreen() {
  const insets = useSafeAreaInsets();
  const router = useRouter();
  const { profile, isLoading, error, refetch } = useProfile();

  if (isLoading) return <ProfileSkeleton />;
  if (error) return <ErrorView message={error.message} onRetry={refetch} />;
  if (!profile) return <EmptyView message="Profile not found" />;

  return (
    <View style={[styles.container, { paddingTop: insets.top }]}>
      <Text style={styles.name} accessibilityRole="header">
        {profile.name}
      </Text>
      <FlatList
        data={profile.posts}
        keyExtractor={(item) => item.id}
        renderItem={({ item }) => (
          <Pressable
            style={styles.postItem}
            onPress={() => router.push(`/post/${item.id}`)}
            accessible
            accessibilityRole="button"
            accessibilityLabel={`Open post: ${item.title}`}
          >
            <Text>{item.title}</Text>
          </Pressable>
        )}
        onRefresh={refetch}
        refreshing={isLoading}
        ListEmptyComponent={<Text>No posts yet</Text>}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: "#fff" },
  name: { fontSize: 24, fontWeight: "bold", padding: 16 },
  postItem: { padding: 12, borderBottomWidth: 1, borderColor: "#eee" },
});
```

## Custom Hook with API
```ts
// src/hooks/useProfile.ts
import { useState, useCallback, useEffect } from "react";
import { api } from "@/lib/api";

interface Profile {
  id: string;
  name: string;
  email: string;
  posts: { id: string; title: string }[];
}

export function useProfile(userId?: string) {
  const [profile, setProfile] = useState<Profile | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<Error | null>(null);

  const fetch = useCallback(async () => {
    setIsLoading(true);
    setError(null);
    try {
      const data = await api.get<Profile>(`/users/${userId ?? "me"}`);
      setProfile(data);
    } catch (err) {
      setError(err instanceof Error ? err : new Error("Failed to load profile"));
    } finally {
      setIsLoading(false);
    }
  }, [userId]);

  useEffect(() => { fetch(); }, [fetch]);

  return { profile, isLoading, error, refetch: fetch };
}
```

## Permissions Handling
```ts
// src/hooks/useCamera.ts
import { useCallback, useState } from "react";
import { Alert, Platform } from "react-native";
import * as ImagePicker from "expo-image-picker";

export function useCamera() {
  const [hasPermission, setHasPermission] = useState<boolean | null>(null);

  const requestPermission = useCallback(async (): Promise<boolean> => {
    const { status } = await ImagePicker.requestMediaLibraryPermissionsAsync();
    const granted = status === "granted";
    setHasPermission(granted);

    if (!granted) {
      Alert.alert(
        "Permission Required",
        "Please allow photo library access in Settings to pick images.",
        [{ text: "Cancel" }, { text: "Open Settings", onPress: openSettings }]
      );
    }
    return granted;
  }, []);

  const pickImage = useCallback(async (): Promise<string | null> => {
    const granted = hasPermission ?? await requestPermission();
    if (!granted) return null;

    const result = await ImagePicker.launchImageLibraryAsync({
      mediaTypes: ImagePicker.MediaTypeOptions.Images,
      quality: 0.8,
      aspect: [1, 1],
      allowsEditing: true,
    });

    if (result.canceled) return null;
    return result.assets[0].uri;
  }, [hasPermission, requestPermission]);

  return { pickImage, hasPermission };
}
```

## Navigation with Deep Links
```ts
// app.json — configure deep links
// "scheme": "myapp" → myapp://
// "intentFilters" for Android, "associatedDomains" for iOS Universal Links

// Programmatic navigation
import { router } from "expo-router";
router.push("/post/123");          // push on stack
router.replace("/home");           // replace current screen
router.back();                     // go back

// Navigation with params — type-safe with expo-router
// app/post/[id].tsx
import { useLocalSearchParams } from "expo-router";
const { id } = useLocalSearchParams<{ id: string }>();
```

## Platform-Specific Code
```ts
import { Platform } from "react-native";

// Method 1: Platform.select (inline)
const shadowStyle = Platform.select({
  ios: { shadowColor: "#000", shadowOffset: { width: 0, height: 2 }, shadowOpacity: 0.1, shadowRadius: 4 },
  android: { elevation: 3 },
});

// Method 2: Platform files (preferred for larger differences)
// camera.ios.ts — iOS implementation using AVFoundation bridge
// camera.android.ts — Android implementation using Camera2 bridge
// Import: import { Camera } from "./camera" → picks correct platform file
```

## Anti-Patterns

| Anti-pattern | Fix |
|---|---|
| `ScrollView` for long lists | Use `FlatList` with `keyExtractor` |
| State updates after unmount | Use cleanup function in `useEffect` or abort controller |
| Missing `keyExtractor` in lists | Always provide stable, unique key |
| Navigation before router ready | Wrap navigation in `useEffect` with mounted check |
| `console.log` left in production | Use `__DEV__` guard: `if (__DEV__) console.log(...)` |
| Inline arrow functions in FlatList renderItem | Extract to const outside component to avoid re-renders |
