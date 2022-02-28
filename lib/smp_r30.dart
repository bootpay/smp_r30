
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:smp_r30/smp_r30_line_data.dart';
import 'dart:convert';

class SmpR30 {
  static const MethodChannel _channel = MethodChannel('smp_r30');

  static Future<void> connect(String ip) async {
    await _channel.invokeMethod('connect', <String, dynamic>{
      'ip': ip
    });
  }

  static Future<bool> isConnect() async {
    return await _channel.invokeMethod('isConnect');
  }

  static Future<void> disConnect() async {
    return await _channel.invokeMethod('disConnect');
  }

  static Future<void> printPaper(List<SmpR30LineData> arguments) async {
    // print(json.encode(arguments));
    await _channel.invokeMethod('printPaper', <String, dynamic>{
      'arguments': json.encode(arguments)
    });
  }
}
