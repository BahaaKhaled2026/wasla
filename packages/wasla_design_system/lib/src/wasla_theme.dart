import 'package:flutter/material.dart';
import 'generated/wasla_tokens.dart';

/// Builds Flutter ThemeData from the generated design tokens (rule 10.2).
/// Never construct colors/spacing manually in app code - always go through
/// WaslaTokens (generated) or WaslaTheme (this file) so all four client
/// surfaces stay visually consistent with admin_web/control_plane_web.
class WaslaTheme {
  WaslaTheme._();

  static ThemeData light() => _base(Brightness.light);
  static ThemeData dark() => _base(Brightness.dark);

  static ThemeData _base(Brightness brightness) {
    final isDark = brightness == Brightness.dark;

    final surfacePrimary = isDark
        ? const Color(0xFF111114)
        : WaslaTokens.surfacePrimary;
    final contentPrimary = isDark
        ? const Color(0xFFF5F5F6)
        : WaslaTokens.contentPrimary;

    return ThemeData(
      brightness: brightness,
      useMaterial3: true,
      scaffoldBackgroundColor: surfacePrimary,
      colorScheme: ColorScheme.fromSeed(
        seedColor: WaslaTokens.brandPrimary,
        brightness: brightness,
      ),
      textTheme: Typography.material2021().black.apply(
            bodyColor: contentPrimary,
            displayColor: contentPrimary,
          ),
    );
  }
}