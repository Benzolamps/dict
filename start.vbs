set ws = wscript.createObject("wscript.shell")
set indexProcess = ws.exec(".\jre\bin\javaw.exe index tmp/dict/")
if not indexProcess.stdOut.atEndOfStream Then
  parameter = indexProcess.stdOut.readAll
  ws.run("%comspec% /c .\mysql\bin\mysqld.exe --defaults-file=.\mysql\my.ini --port=3306"), 0, false
  ws.run(".\jre\bin\java.exe -jar dict.jar " + parameter), 1, true
  ws.run("%comspec% /c .\mysql\bin\mysqladmin.exe -P3306 -uroot -p123456 shutdown"), 0, true
  wscript.sleep 3000
  ws.run("taskkill /f /im mysqld.exe"), 0, true
  set fso = wscript.createObject("scripting.fileSystemObject")
  on error resume next
  set ib_logfile0 = fso.getFile(".\mysql\data\ib_logfile0")
  set ib_logfile1 = fso.getFile(".\mysql\data\ib_logfile1")
  ib_logfile0.attributes = 0
  ib_logfile0.delete
  ib_logfile1.attributes = 0
  ib_logfile1.delete
  if err then wscript.sleep 3000
end if