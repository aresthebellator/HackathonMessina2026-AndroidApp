# Flutter Patterns Reference

Canonical patterns for Flutter 3.x widget development and Dart best practices.

## Project Structure
```
lib/
├── main.dart                    # App entry point
├── app.dart                     # MaterialApp / CupertinoApp config
├── core/
│   ├── router/app_router.dart   # go_router configuration
│   ├── theme/app_theme.dart     # ThemeData
│   └── di/service_locator.dart  # GetIt dependency injection
├── features/
│   └── profile/
│       ├── data/
│       │   ├── profile_repository.dart
│       │   └── profile_service.dart
│       ├── domain/
│       │   └── profile_model.dart
│       └── presentation/
│           ├── profile_screen.dart
│           ├── profile_controller.dart (Riverpod)
│           └── widgets/
│               └── profile_card.dart
└── shared/
    ├── widgets/
    │   ├── loading_widget.dart
    │   └── error_widget.dart
    └── extensions/
```

## Stateless Widget Pattern
```dart
// lib/features/profile/presentation/widgets/profile_card.dart
import 'package:flutter/material.dart';
import '../../domain/profile_model.dart';

class ProfileCard extends StatelessWidget {
  const ProfileCard({
    super.key,
    required this.profile,
    this.onTap,
    this.elevation = 1.0,
  });

  final Profile profile;
  final VoidCallback? onTap;
  final double elevation;

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    return Card(
      elevation: elevation,
      child: InkWell(
        onTap: onTap,
        borderRadius: BorderRadius.circular(12),
        child: Padding(
          padding: const EdgeInsets.all(16),
          child: Row(
            children: [
              CircleAvatar(
                radius: 28,
                backgroundImage: profile.avatarUrl != null
                    ? NetworkImage(profile.avatarUrl!)
                    : null,
                child: profile.avatarUrl == null
                    ? Text(profile.name[0].toUpperCase())
                    : null,
              ),
              const SizedBox(width: 12),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(profile.name, style: theme.textTheme.titleMedium),
                    Text(
                      profile.email,
                      style: theme.textTheme.bodySmall?.copyWith(
                        color: theme.colorScheme.onSurfaceVariant,
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
```

## Riverpod State Management
```dart
// lib/features/profile/presentation/profile_controller.dart
import 'package:riverpod_annotation/riverpod_annotation.dart';
import '../data/profile_repository.dart';
import '../domain/profile_model.dart';

part 'profile_controller.g.dart';

@riverpod
class ProfileController extends _$ProfileController {
  @override
  Future<Profile> build(String userId) async {
    final repo = ref.read(profileRepositoryProvider);
    return repo.getProfile(userId);
  }

  Future<void> updateName(String name) async {
    state = const AsyncLoading();
    state = await AsyncValue.guard(() async {
      final repo = ref.read(profileRepositoryProvider);
      return repo.updateProfile(userId: state.value!.id, name: name);
    });
  }
}

// Profile screen using Riverpod
class ProfileScreen extends ConsumerWidget {
  const ProfileScreen({super.key, required this.userId});
  final String userId;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final profileAsync = ref.watch(profileControllerProvider(userId));

    return Scaffold(
      appBar: AppBar(title: const Text('Profile')),
      body: profileAsync.when(
        loading: () => const Center(child: CircularProgressIndicator()),
        error: (err, stack) => Center(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Text('Error: $err'),
              ElevatedButton(
                onPressed: () => ref.invalidate(profileControllerProvider(userId)),
                child: const Text('Retry'),
              ),
            ],
          ),
        ),
        data: (profile) => ProfileCard(profile: profile),
      ),
    );
  }
}
```

## go_router Navigation
```dart
// lib/core/router/app_router.dart
import 'package:go_router/go_router.dart';
import 'package:riverpod_annotation/riverpod_annotation.dart';

part 'app_router.g.dart';

@riverpod
GoRouter appRouter(AppRouterRef ref) {
  final authState = ref.watch(authStateProvider);

  return GoRouter(
    initialLocation: '/',
    debugLogDiagnostics: true,
    redirect: (context, state) {
      final isLoggedIn = authState.value != null;
      final isAuthRoute = state.matchedLocation.startsWith('/auth');

      if (!isLoggedIn && !isAuthRoute) return '/auth/login';
      if (isLoggedIn && isAuthRoute) return '/';
      return null;
    },
    routes: [
      GoRoute(
        path: '/',
        builder: (context, state) => const HomeScreen(),
      ),
      GoRoute(
        path: '/profile/:userId',
        builder: (context, state) => ProfileScreen(
          userId: state.pathParameters['userId']!,
        ),
      ),
      ShellRoute(
        builder: (context, state, child) => MainShell(child: child),
        routes: [
          GoRoute(path: '/feed', builder: (_, __) => const FeedScreen()),
          GoRoute(path: '/explore', builder: (_, __) => const ExploreScreen()),
        ],
      ),
      GoRoute(
        path: '/auth/login',
        builder: (context, state) => const LoginScreen(),
      ),
    ],
  );
}
```

## Platform-Specific Code
```dart
import 'dart:io' show Platform;
import 'package:flutter/foundation.dart' show kIsWeb;

// Platform check
if (Platform.isIOS) {
  // iOS-specific code
} else if (Platform.isAndroid) {
  // Android-specific code
}

// Widget-level platform adaptation
Widget buildSaveButton() {
  if (Platform.isIOS) {
    return CupertinoButton(
      onPressed: _save,
      child: const Text('Save'),
    );
  }
  return ElevatedButton(
    onPressed: _save,
    child: const Text('Save'),
  );
}
```

## Error Handling and Async
```dart
// Repository pattern with proper error handling
class ProfileRepository {
  final ApiClient _client;
  ProfileRepository(this._client);

  Future<Profile> getProfile(String userId) async {
    try {
      final response = await _client.get('/users/$userId');
      if (response.statusCode == 404) throw NotFoundException('User not found');
      if (response.statusCode != 200) throw ApiException('Failed to fetch profile', response.statusCode);
      return Profile.fromJson(response.data as Map<String, dynamic>);
    } on DioException catch (e) {
      throw NetworkException(e.message ?? 'Network error');
    }
  }
}

// Custom exception types
class NotFoundException implements Exception {
  NotFoundException(this.message);
  final String message;
  @override String toString() => 'NotFoundException: $message';
}
```

## Anti-Patterns

| Anti-pattern | Fix |
|---|---|
| `setState` in large widget trees | Use Riverpod or Bloc for state management |
| `BuildContext` across async gaps | Check `mounted` before using context after `await` |
| `Column` inside `SingleChildScrollView` without shrinkWrap | Use `ListView` instead |
| Missing `const` constructors | Add `const` to widgets with no runtime dependencies |
| `print()` in production | Use `debugPrint()` or logging package |
| Blocking main isolate with heavy computation | Use `compute()` or isolates for CPU-intensive work |
