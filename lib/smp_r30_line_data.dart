
class SmpR30LineData {
  String command;
  Map<String, String>? arguments;
  SmpR30LineData(this.command, {this.arguments});
  SmpR30LineData.fromJson(Map<String, dynamic> json)
      : command = json['command'],
        arguments = json['arguments'];

  Map<String, dynamic> toJson() =>
      {
        'command': command,
        'arguments': arguments,
      };
}
