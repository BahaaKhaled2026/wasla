import 'package:flutter/material.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:wasla_design_system/wasla_design_system.dart';
import 'l10n/app_localizations.dart';

void main() {
  runApp(const FamilySchoolApp());
}

class FamilySchoolApp extends StatefulWidget {
  const FamilySchoolApp({super.key});

  @override
  State<FamilySchoolApp> createState() => _FamilySchoolAppState();
}

class _FamilySchoolAppState extends State<FamilySchoolApp> {
  ThemeMode _themeMode = ThemeMode.light;
  Locale _locale = const Locale('en');

  void _toggleTheme() {
    setState(() {
      _themeMode = _themeMode == ThemeMode.light ? ThemeMode.dark : ThemeMode.light;
    });
  }

  void _toggleLocale() {
    setState(() {
      _locale = _locale.languageCode == 'en' ? const Locale('ar') : const Locale('en');
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Wasla Family & School',
      theme: WaslaTheme.light(),
      darkTheme: WaslaTheme.dark(),
      themeMode: _themeMode,
      locale: _locale,
      localizationsDelegates: AppLocalizations.localizationsDelegates,
      supportedLocales: AppLocalizations.supportedLocales,
      home: Builder(
        builder: (context) {
          final t = AppLocalizations.of(context)!;
          return StatusScreen(
            title: t.appTitle,
            healthyLabel: t.statusHealthy,
            unhealthyLabel: t.statusUnhealthy,
            requestIdLabel: t.requestIdLabel,
            checkedAtLabel: t.checkedAtLabel,
            buildLabel: t.buildLabel,
            actions: [
              TextButton(
                onPressed: _toggleLocale,
                child: Text(_locale.languageCode == 'en' ? 'AR' : 'EN'),
              ),
              IconButton(
                onPressed: _toggleTheme,
                icon: Icon(_themeMode == ThemeMode.light ? Icons.dark_mode : Icons.light_mode),
                tooltip: _themeMode == ThemeMode.light ? t.themeDark : t.themeLight,
              ),
            ],
          );
        },
      ),
    );
  }
}