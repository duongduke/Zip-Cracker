@echo off
cd /d %~dp0
powershell Start-Process mvn -ArgumentList 'clean compile exec:java' -Verb RunAs
pause 