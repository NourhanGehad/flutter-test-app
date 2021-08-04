package com.example.piano_test_app

import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

import com.example.piano_test_app.Synth

/** PianoPlugin */
class PianoPlugin: FlutterPlugin, MethodCallHandler {

/////////////////////// Part 1 /////////////////////////////

    private lateinit var channel : MethodChannel
    private lateinit var synth: Synth

/////////////////////// Part 2 /////////////////////////////

    //override fun onAttachedToEngine (*override from FlutterPlugin)
    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.flutterPluginBinding) {
        
        //Generate a channel from MethodChannel constructer to communicate with Flutter.
        //Note that the channel name “piano” is the same as step //1 in piano.dart.
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "piano")
        
        //Set the channel’s MethodCallHandler to be the current class instance 
        //(with implementation under override fun onMethodCall)
        channel.setMethodCallHandler(this)

        //Initiate a Singleton object, i.e., synth, of Synth class (See Synth class in Synth.kt)
        Factory.setup(this, flutterPluginBinding.binaryMessenger)
    }

/////////////////////// Part 3 /////////////////////////////

    //override fun onMethodCall (*override from MethodCallHandler)
    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result){

       
        if (call.method == "getPlatformVersion") { //if (call.method == “getPlatformVersion”)
            result.success(android.os.Build.VERSION.RELEASE)
        } else if (call.method == "onKeyDown") { //if (call.method == “onKeyDown”), call keyDown method from synth
            try {
                val arguments: ArrayList<Int> = call.arguments as ArrayList<Int>
                val numKeysDown: Int = synth.keyDown(arguments.get(0) as Int)
                result.success(numKeysDown)
            } catch (ex: Exception) {
            result.error("1", ex.message, ex.getStackTrace())
            }
        } else if (call.method == "onKeyUp") { //if (call.method == “onKeyUp”)), call keyUp method from synth
            try {
                val arguments: ArrayList<Int> = call.arguments as ArrayList<Int>
                val numKeysUp: Int = synth.keyUp(arguments.get(0) as Int)
                result.success(numKeysUp)
            } catch (ex: Exception) {
                result.error("1", ex.message, ex.getStackTrace())
            }
        } else {
            result.notImplemented()
        }
    }

/////////////////////// Part 4/////////////////////////////

    //override fun onDetachedFromEngine (*override from FlutterPlugin)
    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.flutterPluginBinding) {
        //Unregister the the MethodCallHandler to null
        channel.setMethodCallHandler(null)
    }

/////////////////////// Part 5/////////////////////////////

    //companion object Factory
    //“companion object” is to create a Singleton object in the class.
    private companion object Factory {
        fun  setup(plugin: PianoPlugin, binaryMessenger: BinaryMessenger) {
            //Define the initialization of the Synth class (which is a Runnable).
            plugin.synth = Synth()
            plugin.synth.start()
        }
    }


}