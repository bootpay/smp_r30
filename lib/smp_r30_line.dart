
import 'smp_r30_line_data.dart';

class SmpR30Line {
  static final int ALIGN_LEFT = 0;
  static final int ALIGN_CENTER = 1;
  static final int ALIGN_RIGHT = 2;
  static final int QR_ERROR_LEVEL_L = 48;
  static final int QR_ERROR_LEVEL_M = 49;
  static final int QR_ERROR_LEVEL_Q = 50;
  static final int QR_ERROR_LEVEL_H = 51;
  static final int BC_UPC_A = 65;
  static final int BC_UPC_E = 66;
  static final int BC_EAN_13 = 67;
  static final int BC_EAN_8 = 68;
  static final int BC_CODE39 = 69;
  static final int BC_ITF = 70;
  static final int BC_CODABAR = 71;
  static final int BC_CODE93 = 72;
  static final int BC_CODE128 = 73;
  static final int HRI_NONE = 0;
  static final int HRI_ABOVE = 1;
  static final int HRI_BELOW = 2;
  static final int HRI_ABOVE_AND_BELOW = 3;

  static SmpR30LineData reset() {
    return SmpR30LineData("reset");
  }

  static SmpR30LineData formatString(bool width, bool height, bool bold, bool underline) {
    return SmpR30LineData("formatString", arguments: {
      'width': width.toString(),
      'height': height.toString(),
      'bold': bold.toString(),
      'underline': underline.toString()
    });
  }

  static SmpR30LineData sendText(String text) {
    return SmpR30LineData("sendText", arguments: {'text': text});
  }

  static SmpR30LineData sendTextLine(String text) {
    return SmpR30LineData("sendTextLine", arguments: {'text': text});
  }

  static SmpR30LineData paperCut() {
    return SmpR30LineData("paperCut");
  }

  static SmpR30LineData feedLine(int line) {
    return SmpR30LineData("paperCut", arguments: {'line': line.toString()});
  }

  static SmpR30LineData printBarcode(int type, int width, int height, int hri, String data) {
    return SmpR30LineData("printBarcode", arguments: {
      'type': type.toString(),
      'width': width.toString(),
      'height': height.toString(),
      'hri': hri.toString(),
      'data': data
    });
  }

  static SmpR30LineData setAlignment(int type) {
    return SmpR30LineData("setAlignment", arguments: {'type': type.toString()});
  }
}
