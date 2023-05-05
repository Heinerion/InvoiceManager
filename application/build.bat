@ECHO OFF

REM --continuous kann während der Entwicklung ergänzt werden
./gradlew clean build --warning-mode all --configure-on-demand --daemon --parallel
