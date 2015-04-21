android-quake-aws-util
===============

The util app for AndroidQuakeNG

to encrypt AWS account IDs for safe storing in the apk

* Specify AWS account details in
```
src/main/resources/nz/org/geonet/quake/aws.properties
```
* Generate encrypted IDs
```
./gradlew clean run
```
* Copy generated encrypted ids from console to AndroidQuakeNG




