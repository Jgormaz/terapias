Set WshShell = CreateObject("WScript.Shell") 
WshShell.Run "cmd /c java -jar target\terapias-0.0.1-SNAPSHOT.jar", 0, False
