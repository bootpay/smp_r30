package kr.co.bootpay.smp_r30;

import android.util.Log;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

import com.syncrown.smpwifi.SMPWifi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/** SmpR30Plugin */
public class SmpR30Plugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;

  private SMPWifi mSyncPrinter;
  private SMPWifi.OnConnectionListener mOnConnectionListener;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "smp_r30");
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else if(call.method.equals("connect")) {
      String ip = call.argument("ip");
      connect(ip, result);
    } else if(call.method.equals("isConnect")) {
      isConnect(result);
    } else if(call.method.equals("disConnect")) {
      disConnect(result);
    } else if(call.method.equals("printPaper")) {
      String arguments = call.argument("arguments");
      printPaper(arguments, result);

    } else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }


  private void initPlugins(@NonNull Result result) {
    if(mSyncPrinter != null) return;

    mSyncPrinter = new SMPWifi();
//    Log.d("bootpay", "연결시도");
//    Toast.makeText(mAct, "Connected", Toast.LENGTH_SHORT).show();
    mOnConnectionListener = new SMPWifi.OnConnectionListener() {
      @Override
      public void onConnectionSuccess() {
//        mBtnConnect.setText("Disconnect");
//        Toast.makeText(mAct, "Connected", Toast.LENGTH_SHORT).show();
        Log.d("bootpay", "연결성공");
      }

      @Override
      public void onConnectionFailed() {

        Log.d("bootpay", "연결실패");
      }

      @Override
      public void onConnectionLost() {
        Log.d("bootpay", "연결끊김");
//        mBtnConnect.setText("Connect");
//        Toast.makeText(mAct, "Disconnected", Toast.LENGTH_SHORT).show();
      }
    };
    mSyncPrinter.setOnConnectionoListener(mOnConnectionListener);
  }

  private void connect(String ip, @NonNull Result result) {
    initPlugins(result);
    if(mSyncPrinter.isConnected()) {
      Log.d("bootpay", "isConnected");
      mSyncPrinter.close();
    } else {
      mSyncPrinter = new SMPWifi();
      Log.d("bootpay", "mSyncPrinter");
      mSyncPrinter.open(ip);
    }
    result.success(ip);
  }

  private void disConnect(@NonNull Result result) {
    if(mSyncPrinter.isConnected()) {
      mSyncPrinter.close();
    }
  }

  private void isConnect( @NonNull Result result) {
    result.success(mSyncPrinter.isConnected());
  }

  private void printPaper(String arguments, @NonNull Result result) {

    try {
      if(mSyncPrinter == null || !mSyncPrinter.isConnected()) {
        Log.d("bootpay", "연결되지 않음");
        return;
      }
      JSONArray array = new JSONArray(arguments);
      for(int i=0; i < array.length(); i++) {
        JSONObject jsonObject = array.getJSONObject(i);
        doPrintJob(jsonObject);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

//  private void doPrintJobStart() {
//    mSyncPrinter.reset();
//  }

  private void doPrintJob(JSONObject jsonObject) throws JSONException {
    String command = jsonObject.getString("command");
    if("reset".equals(command)) {
      reset();
    } else if("formatString".equals(command)) {
      formatString(jsonToMap(jsonObject.getJSONObject("arguments")));
    } else if("sendText".equals(command)) {
      sendText(jsonToMap(jsonObject.getJSONObject("arguments")));
    } else if("sendTextLine".equals(command)) {
      sendTextLine(jsonToMap(jsonObject.getJSONObject("arguments")));
    } else if("paperCut".equals(command)) {
      paperCut();
    } else if("feedLine".equals(command)) {
      feedLine(jsonToMap(jsonObject.getJSONObject("arguments")));
    } else if("printBarcode".equals(command)) {
      printBarcode(jsonToMap(jsonObject.getJSONObject("arguments")));
    } else if("setAlignment".equals(command)) {
      setAlignment(jsonToMap(jsonObject.getJSONObject("arguments")));
    }
  }

  private Map<String, String> jsonToMap(JSONObject jsonObject) {
    Map<String, String> params = new HashMap<String, String>();
    try {
      Iterator<?> keys = jsonObject.keys();

      while (keys.hasNext()) {
        String key = (String) keys.next();
        String value = jsonObject.getString(key);
        params.put(key, value);
      }
    } catch (Exception xx) {
      xx.toString();
    }
    return params;
  }

  private void reset() {
    mSyncPrinter.reset();
  }

  private void formatString(Map<String, String> arguments) {
    mSyncPrinter.formatString(
            arguments.get("width").toLowerCase().equals("true"),
            arguments.get("height").toLowerCase().equals("true"),
            arguments.get("bold").toLowerCase().equals("true"),
            arguments.get("underline").toLowerCase().equals("true")
    );
  }

  private void sendText(Map<String, String> arguments) {
    Log.d("bootpay", arguments.get("text"));
    mSyncPrinter.sendText(
            arguments.get("text")
    );
  }

  private void sendTextLine(Map<String, String> arguments) {
    Log.d("bootpay", arguments.get("text"));
    mSyncPrinter.sendText(
            arguments.get("text") + "\n"
    );
  }

  private void paperCut() {
    mSyncPrinter.paperCut();
  }

  private void feedLine(Map<String, String> arguments) {
    mSyncPrinter.feedLine(
            Integer.parseInt(arguments.get("line"))
    );
  }

  private void printBarcode(Map<String, String> arguments) {
    mSyncPrinter.printBarcode(
            Integer.parseInt(arguments.get("type")),
            Integer.parseInt(arguments.get("width")),
            Integer.parseInt(arguments.get("height")),
            Integer.parseInt(arguments.get("hri")),
            arguments.get("data")
    );
  }

  private void setAlignment(Map<String, String> arguments) {
    mSyncPrinter.setAlignment(
            Integer.parseInt(arguments.get("type"))
    );
  }

//  public void printOrderDetail(String orderDetail, @NonNull Result result) {
//    Log.d("bootpay", "printOrderDetail");
//
//    if(mSyncPrinter == null || !mSyncPrinter.isConnected()) {
//      Log.d("bootpay", "연결되지 않음");
////      Toast.makeText(mAct, "Printer is not connected", Toast.LENGTH_SHORT).show();
//      return;
//    }
//    mSyncPrinter.reset();
//
////    mSyncPrinter.setAlignment(SMPWifi.ALIGN_CENTER);
////
////    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo139);
////    mSyncPrinter.printBitmap(bitmap);
////    mSyncPrinter.sendText("\n");
//
//    mSyncPrinter.sendText("#704, E&C dream-tower 8th. 45 Gasan Digital 1st road, GeumCheon, Seoul 08592 Korea\n");
//
//    mSyncPrinter.setAlignment(SMPWifi.ALIGN_LEFT);
//    mSyncPrinter.sendText("\n");
//    mSyncPrinter.sendText("Bill No : 3221\n");
//    mSyncPrinter.sendText("================================================\n");
//    mSyncPrinter.sendText("SMP-M240                                   $1.00\n");
//    mSyncPrinter.sendText("SMP-M240L                                  $1.00\n");
//    mSyncPrinter.sendText("SMP-M200                                   $1.00\n");
//    mSyncPrinter.sendText("SMP-M300                                   $1.00\n");
//    mSyncPrinter.sendText("SMS-1339                                   $1.00\n");
//    mSyncPrinter.sendText("------------------------------------------------\n");
//    mSyncPrinter.formatString(false, true, false, false);
//    mSyncPrinter.sendText("Total                                      $5.00\n");
//    mSyncPrinter.formatString(false, false, false, false);
//    mSyncPrinter.sendText("================================================\n");
//    mSyncPrinter.setAlignment(SMPWifi.ALIGN_CENTER);
//    mSyncPrinter.sendText("Homepage : www.syncrown.com\n");
//    mSyncPrinter.sendText("E-mail : info@syncrown.com\n");
//    mSyncPrinter.sendText("Tel : +82.2.6331.1000\n\n");
//    mSyncPrinter.printBarcode(SMPWifi.BC_CODE39, 2, 40, SMPWifi.HRI_BELOW, "20160512");
//
//    mSyncPrinter.feedLine(100);
//
//    mSyncPrinter.paperCut();
//    result.success("print");
////    result.success(false);
//  }
}
