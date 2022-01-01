import 'package:flutter/material.dart';

String printDuration(int duration) {
  Duration toDuration = Duration(seconds: duration);
  
  String twoDigits(int n) => n.toString().padLeft(2, "0");
  String twoDigitMinutes = twoDigits(toDuration.inMinutes.remainder(60));
  String twoDigitSeconds = twoDigits(toDuration.inSeconds.remainder(60));
  return "${twoDigits(toDuration.inHours)}:$twoDigitMinutes:$twoDigitSeconds";
}