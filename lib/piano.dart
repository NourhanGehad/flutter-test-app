//This will be the Flutter API interface,
//which defines all the callable functions for clients to call

import 'dart:async';
import 'package:flutter/services.dart';

class Piano {

  //First, it create a MethodChannel instance _channel 
  //which registers the channel name as piano.
  static const MethodChannel _channel = 
      const MethodChannel("piano");

  //Defined the callable functions and Getter for your client to call: 
  //platformVersion (getter), onKeyDown (function), onKeyUp (function).
  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<int?> onKeyDown(int key) async {
    final int? numNotesOn = await _channel.invokeMethod('onKeyDown', [key]);
    return numNotesOn;
  }

  static Future<int?> onKeyUp(int key) async {
    final int? numNotesOn = await _channel.invokeMethod('onKeyUp', [key]);
    return numNotesOn;
  }

  //Explaining the callable functions and getter:
  //PS: These functions invokeMethod( <plugin-method-name>, [args]) 
  //    with the method name identified in the pianoPlugin.kt
  //PS: Simply return the client with what was returned from the invokeMethod.

  //Conclusion: 
  //So this API simply provides an interface for your client to call
  //android native functions, written in Kotlin

  //Your client requests methods call from API,(i.e. in piano.dart),
  //and API goes ahead to forward the methods call to MethodChannel factory,(i.e. from pianoPlugin.kt), 
  //and returns the client with what is returned from the MethodChannel. This is all the API interface does.
}