// ignore: unused_import
import 'package:intl/intl.dart' as intl;
import 'app_localizations.dart';

// ignore_for_file: type=lint

/// The translations for Arabic (`ar`).
class AppLocalizationsAr extends AppLocalizations {
  AppLocalizationsAr([String locale = 'ar']) : super(locale);

  @override
  String get appTitle => 'السائق — الحالة';

  @override
  String get statusHealthy => 'جميع الأنظمة تعمل بشكل سليم';

  @override
  String get statusUnhealthy => 'الخدمة غير متاحة';

  @override
  String get requestIdLabel => 'معرّف الطلب';

  @override
  String get checkedAtLabel => 'تم الفحص في';

  @override
  String get buildLabel => 'الإصدار';

  @override
  String get themeLight => 'فاتح';

  @override
  String get themeDark => 'داكن';
}
