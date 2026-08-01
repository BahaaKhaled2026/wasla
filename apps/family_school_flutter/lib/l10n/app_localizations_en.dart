// ignore: unused_import
import 'package:intl/intl.dart' as intl;
import 'app_localizations.dart';

// ignore_for_file: type=lint

/// The translations for English (`en`).
class AppLocalizationsEn extends AppLocalizations {
  AppLocalizationsEn([String locale = 'en']) : super(locale);

  @override
  String get appTitle => 'Family & School — Status';

  @override
  String get statusHealthy => 'All systems healthy';

  @override
  String get statusUnhealthy => 'Service unavailable';

  @override
  String get requestIdLabel => 'Request ID';

  @override
  String get checkedAtLabel => 'Checked at';

  @override
  String get buildLabel => 'Build';

  @override
  String get themeLight => 'Light';

  @override
  String get themeDark => 'Dark';
}
