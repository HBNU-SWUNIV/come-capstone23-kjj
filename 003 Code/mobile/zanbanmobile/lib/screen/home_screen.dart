import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:webview_flutter/webview_flutter.dart';

final uri = Uri.parse('https://m.naver.com');

class HomeScreen extends StatelessWidget {
  WebViewController controller = WebViewController()
    ..setJavaScriptMode(JavaScriptMode.unrestricted)
    ..loadRequest(uri);

  HomeScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () {
        var future = controller.canGoBack();
        future.then((canGoBack) => {
              if (canGoBack)
                {controller.goBack()}
              else
                {
                  showDialog(
                    context: context,
                    builder: (context) => AlertDialog(
                      title: Text('앱 종료'),
                      content: Text('정말로 종료하시겠어요? 😥'),
                      actions: [
                        TextButton(
                          onPressed: () {
                            SystemNavigator.pop();
                          },
                          child: Text('예'),
                        ),
                        TextButton(
                          onPressed: () {
                            Navigator.of(context).pop();
                          },
                          child: Text('아니오'),
                        ),
                      ],
                    ),
                  ),
                }
            });
        return Future.value(false);
      },
      child: SafeArea(
        child: Scaffold(
          appBar: AppBar(
            backgroundColor: Color.fromARGB(255, 247, 203, 146),
            title: Text('잔반제로'),
            centerTitle: true,
          ),
          body: WebViewWidget(controller: controller),
        ),
      ),
    );
  }
}
