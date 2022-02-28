import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:smp_r30/smp_r30.dart';
import 'package:smp_r30/smp_r30_line.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
    printTest();
  }

  connect() {
    SmpR30.connect("172.30.1.50");
  }

  printTest() {
    // connect();
    var arguments = [
      SmpR30Line.reset(),
      SmpR30Line.sendTextLine("test11"),
      SmpR30Line.setAlignment(10),
      SmpR30Line.sendTextLine("test2"),
      SmpR30Line.sendTextLine("test2"),
      SmpR30Line.sendTextLine("test2"),
      SmpR30Line.sendTextLine("test2"),
      SmpR30Line.sendTextLine("test2"),
      SmpR30Line.sendTextLine("test2"),
      SmpR30Line.sendTextLine("test2"),
      SmpR30Line.sendTextLine("test2"),
      SmpR30Line.sendTextLine("test2"),
      SmpR30Line.sendTextLine("test2"),
      SmpR30Line.sendTextLine("test2"),
      SmpR30Line.sendTextLine("test2"),
      SmpR30Line.sendTextLine("test2"),
      SmpR30Line.paperCut()
    ];

    SmpR30.printPaper(arguments);
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          children: [
            ListTile(
              title: Text('프린트 연결'),
              onTap: () => connect(),
            ),
            ListTile(
              title: Text('테스트 출력'),
              onTap: () => printTest(),
            ),
            Center(
              child: Text('Running on: $_platformVersion\n'),
            ),
          ],
        ),
      ),
    );
  }
}
