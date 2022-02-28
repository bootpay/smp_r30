import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:smp_r30/smp_r30.dart';

void main() {
  const MethodChannel channel = MethodChannel('smp_r30');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await SmpR30.platformVersion, '42');
  });
}
