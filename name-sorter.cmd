@echo off
rem Runs the name sorter on Windows. Build first with: mvn package
rem Usage: name-sorter .\unsorted-names-list.txt

set "JAR=%~dp0target\name-sorter.jar"

if not exist "%JAR%" (
  echo Jar not found at %JAR%. Run 'mvn package' first. 1>&2
  exit /b 1
)

java -jar "%JAR%" %*
