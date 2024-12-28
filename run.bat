@echo off
cd /d %~dp0
powershell Start-Process java -ArgumentList '-jar target/untitled-1.0-SNAPSHOT.jar' -Verb RunAs
pause 