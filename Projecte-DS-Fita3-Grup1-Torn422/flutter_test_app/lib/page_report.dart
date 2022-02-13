// ignore_for_file: must_call_super

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/painting.dart';
import 'package:intl/intl.dart';

class PageReport extends StatefulWidget {
  const PageReport({Key? key}) : super(key: key);

  @override
  _PageReportState createState() => _PageReportState();
}

class _PageReportState extends State<PageReport> {
  DateTime? sundayLastWeek;
  DateTime? mondayLastWeek;
  DateTimeRange? dates;
  DateTime? today;
  DateTime? yesterday;
  DateTime? mondayThisWeek;

  String? dropdownValuePeriod;
  String? dropdownValueContent;
  String? dropdownValueFormat;

  @override
  initState() {
    /* DATES VARS */
    today = DateTime.now();
    yesterday = today!.subtract(const Duration(days: 1));
    mondayThisWeek = DateTime(today!.year, today!.month, today!.day - today!.weekday + 1);
    sundayLastWeek = mondayThisWeek!.subtract(const Duration(days: 1));
    mondayLastWeek = mondayThisWeek!.subtract(const Duration(days: 7));

    dates = DateTimeRange(start: mondayLastWeek!, end: sundayLastWeek!);

    dropdownValuePeriod = 'Last week';
    dropdownValueContent = 'Brief';
    dropdownValueFormat = 'Web page';
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Report'),
      ),
      body: Container(
          padding: const EdgeInsets.all(50.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: <Widget>[
              Row(
                //mainAxisAlignment: MainAxisAlignment.center,
                children: <Widget>[
                  const SizedBox(
                    width: 100,
                    child: Text(
                      'Period',
                      style: TextStyle(fontWeight: FontWeight.bold),
                    ),
                  ),
                  const SizedBox(
                    width: 20.0,
                  ),
                  DropdownButton(
                    value: dropdownValuePeriod,
                    icon: const Icon(Icons.arrow_drop_down),
                    onChanged: (String? newValue) {
                      setState(() {
                        dropdownValuePeriod = newValue!;
                        dates = _changeFromTo();
                      });
                    },
                    items: <String>[
                      'Last week',
                      'This week',
                      'Yesterday',
                      'Today',
                      'Other'
                    ].map<DropdownMenuItem<String>>((String value) {
                      return DropdownMenuItem<String>(
                        value: value,
                        child: Text(value),
                      );
                    }).toList(),
                  )
                ],
              ),
              Row(
                  //mainAxisAlignment: MainAxisAlignment.center,
                  children: <Widget>[
                    const SizedBox(
                      width: 100,
                      child: Text(
                        "From",
                        style: TextStyle(fontWeight: FontWeight.bold),
                      ),
                    ),
                    const SizedBox(
                      width: 20.0,
                    ),
                    Text(DateFormat('d/M/y').format(dates!.start)),
                    IconButton(
                      icon: const Icon(
                        Icons.calendar_today,
                        color: Colors.blue,
                      ),
                      onPressed: () {
                        _pickFromDate();
                      },
                    ),
                  ]),
              Row(
                  //mainAxisAlignment: MainAxisAlignment.center,
                  children: <Widget>[
                    const SizedBox(
                      width: 100,
                      child: Text(
                        "To",
                        style: TextStyle(fontWeight: FontWeight.bold),
                      ),
                    ),
                    const SizedBox(
                      width: 20.0,
                    ),
                    Text(DateFormat('d/M/y').format(dates!.end)),
                    IconButton(
                      icon: const Icon(Icons.calendar_today, color: Colors.blue),
                      onPressed: () {
                        _pickToDate();
                      },
                    ),
                  ]),
              Row(
                //mainAxisAlignment: MainAxisAlignment.center,
                children: <Widget>[
                  const SizedBox(
                    width: 100,
                    child: Text(
                      'Content',
                      style: TextStyle(fontWeight: FontWeight.bold),
                    ),
                  ),
                  const SizedBox(
                    width: 20.0,
                  ),
                  DropdownButton(
                    value: dropdownValueContent,
                    icon: const Icon(Icons.arrow_drop_down),
                    onChanged: (String? newValue) {
                      setState(() {
                        dropdownValueContent = newValue!;
                      });
                    },
                    items: <String>['Brief', 'Detailed', 'Statistic']
                        .map<DropdownMenuItem<String>>((String value) {
                      return DropdownMenuItem<String>(
                        value: value,
                        child: Text(value),
                      );
                    }).toList(),
                  )
                ],
              ),
              Row(
                //mainAxisAlignment: MainAxisAlignment.center,
                children: <Widget>[
                  const SizedBox(
                    width: 100,
                    child: Text(
                      'Format',
                      style: TextStyle(fontWeight: FontWeight.bold),
                    ),
                  ),
                  const SizedBox(
                    width: 20.0,
                  ),
                  DropdownButton(
                    value: dropdownValueFormat,
                    icon: const Icon(Icons.arrow_drop_down),
                    onChanged: (String? newValue) {
                      setState(() {
                        dropdownValueFormat = newValue!;
                      });
                    },
                    items: <String>['Web page', 'PDF', 'Text']
                        .map<DropdownMenuItem<String>>((String value) {
                      return DropdownMenuItem<String>(
                        value: value,
                        child: Text(value),
                      );
                    }).toList(),
                  )
                ],
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: <Widget>[
                  TextButton(
                    child: const Text(
                      "Generate",
                      style: TextStyle(fontWeight: FontWeight.bold),
                    ),
                    style: TextButton.styleFrom(
                        textStyle: const TextStyle(fontSize: 20)),
                    onPressed: () {},
                  )
                ],
              )
            ],
          )),
    );
  }

  void _pickFromDate() async {
    DateTime? newStart = await showDatePicker(
        context: context,
        initialDate: today!,
        firstDate: DateTime(today!.year - 5),
        lastDate: DateTime(today!.year + 5));
    DateTime end = dates!.end;
    if (end.difference(newStart!) >= const Duration(days: 0)) {
      debugPrint("CAMBIO FROM DATE");
      dates = DateTimeRange(start: newStart, end: end);

      setState(() {
        dropdownValuePeriod = 'Other';
      });
    } else {
      _showAlertDates();
    }
  }

  void _pickToDate() async {
    DateTime? newEnd = await showDatePicker(
        context: context,
        initialDate: today!,
        firstDate: DateTime(today!.year - 5),
        lastDate: DateTime(today!.year + 5));
    DateTime start = dates!.start;
    if (start.difference(newEnd!) <= const Duration(days: 0)) {
      debugPrint("CAMBIO TO DATE");
      dates = DateTimeRange(start: start, end: newEnd);

      setState(() {
        dropdownValuePeriod = 'Other';
      });
    } else {
      _showAlertDates();
    }
  }

  Future<void> _showAlertDates() async {
    return showDialog(
        context: context,
        builder: (BuildContext context) => AlertDialog(
              title: const Text(
                "Range dates",
                style: TextStyle(fontWeight: FontWeight.bold),
              ),
              content: const Text(
                  "The From date is after the To date.\n\nPlease, select a new date."),
              actions: <Widget>[
                TextButton(
                  onPressed: () => Navigator.pop(context, "OK"),
                  child: const Text("OK"),
                )
              ],
            ));
  }

  DateTimeRange? _changeFromTo() {
    
    DateTimeRange? newDates = dates;

    switch (dropdownValuePeriod) {
      case 'Last week':
        newDates = DateTimeRange(start: mondayLastWeek!, end: sundayLastWeek!);
        break;
      case 'This week':
        newDates = DateTimeRange(start: mondayThisWeek!, end: today!);
        break;
      case 'Yesterday':
        newDates = DateTimeRange(start: yesterday!, end: yesterday!);
        break;
      case 'Today':
        newDates = DateTimeRange(start: today!, end: today!);
        break;
      default:
        newDates = DateTimeRange(start: today!, end: today!);
        break;
    }
    
    return newDates;
  }
}
