# LEDBack cover

This android application for Samsung galaxy S10 is a demonstration of how to control the [LEDBack cover](https://www.samsung.com/hk_en/mobile-accessories/led-cover-for-galaxy-s10/EF-KG973CBEGWW/) by a third-Party application.
Samsung does not provide any official SDK to interact with the case, however some features can be accessible using [Java Reflection](https://www.oracle.com/technical-resources/articles/java/javareflection.html). Most of the functions controlling the case are located in the Framework.jar.
I reverse engineer the applications related to the case to understand how it works and how to use the Samsung functions to make things work.

## Demonstration

I created a simple application to show how we can use the functions available. In this application, the user can select any application installed, and associate one of the default icons available, each time a notification of this application is receive, the icon will be displayed for X seconds. 

## Install the application

Download and install the apk available in the release section. You will not be able to compile the project by yourself, because this project is using a custom `android.jar` to use the Samsung framework.jar classes. Learn more about [hidden-api](https://github.com/anggrayudi/android-hidden-api/tree/master/sample).

## Discussion

You can found the XDA thread related to this application [here](https://forum.xda-developers.com/galaxy-s10/themes/app-ledback-cover-notification-t4177837).

## Notes

I will not work anymore on this project because I do not think it is possible to have more control on this case, for me it is impossible to create a method to control each led individually. Sadly, The case has not been made for this purpose.