import 'package:flutter/material.dart';
import 'dart:math' as math;

class SemiCircleWidget extends StatelessWidget {
  final double? diameter;
  final double? sweepAngle;
  final Color? color;

  const SemiCircleWidget({
    Key? key,
    this.diameter = 200,
    @required this.sweepAngle,
    @required this.color,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return CustomPaint(
      painter: MyPainter(sweepAngle, color),
      size: Size(diameter!, diameter!),
    );
  }
}


class MyPainter extends CustomPainter {
  MyPainter(this.sweepAngle, this.color);
  final double? sweepAngle;
  final Color? color;
  
  @override
  void paint(Canvas canvas, Size size) {
    final Paint paint = Paint()
      ..strokeWidth = 30.0
      ..style = PaintingStyle.stroke
      ..color = color!;
    
    double degToRad(double deg) => deg * (math.pi / 180.0);

    final path = Path()
      ..arcTo(
        Rect.fromCenter(
          center: Offset(size.height / 2, size.width / 2), 
          width: size.width, 
          height: size.height,
          ),
          degToRad(270),
          degToRad(sweepAngle!),
          false);
    canvas.drawPath(path, paint);
  }

  @override
  bool shouldRepaint(CustomPainter oldDelegate) => false;
}