@ECHO OFF
ECHO ***Date and Time***
set datetimef=%date:~-4%_%date:~3,2%_%date:~0,2%__%time:~0,2%_%time:~3,2%_%time:~6,2%
echo %datetimef%
ECHO ***List Processes***
wmic /output:c:\data\process.html process list brief /format:htable
ECHO ***CPU Status***
wmic /output:c:\data\cpu.html cpu get Name, NumberOfCores, NumberOfEnabledCore, LoadPercentage, MaxClockSpeed, Status, CpuStatus /format:htable
ECHO ***wireless bandwidth***
wmic /output:c:\data\nic.html NIC get "Name","Speed", "NetConnectionStatus", "PNPDeviceID" /format:htable
ECHO ***Memory Usage***
wmic /output:c:\data\memory.html memorychip list full /format:htable
ECHO ***Battery Usage***
FOR /F "tokens=1* delims==" %%A IN ('WMIC /NameSpace:"\\root\WMI" Path BatteryStatus              Get Charging^,Critical^,Discharging /Format:list ^| FIND "=TRUE"') DO ECHO Battery is %%A
FOR /F "tokens=*  delims="  %%A IN ('WMIC /NameSpace:"\\root\WMI" Path BatteryStatus              Get PowerOnline^,RemainingCapacity  /Format:list ^| FIND "="')     DO SET  Battery.%%A
FOR /F "tokens=*  delims="  %%A IN ('WMIC /NameSpace:"\\root\WMI" Path BatteryRuntime             Get EstimatedRuntime                /Format:list ^| FIND "="')     DO SET  Battery.%%A
FOR /F "tokens=*  delims="  %%A IN ('WMIC /NameSpace:"\\root\WMI" Path BatteryFullChargedCapacity Get FullChargedCapacity             /Format:list ^| FIND "="')     DO SET  Battery.%%A

:: Calculate runtime left and capacity
SET /A Battery.EstimatedRuntime  = ( %Battery.EstimatedRuntime% + 30 ) / 60
SET /A Battery.RemainingCapacity = ( %Battery.RemainingCapacity%00 + %Battery.FullChargedCapacity% / 2 ) / %Battery.FullChargedCapacity%

powercfg /batteryreport /xml
powercfg /srumutil /output c:/data/battstat.csv /csv
:: Display results
IF /I "%Battery.PowerOnline%"=="TRUE" (
	ECHO Now working on mains power
	ECHO Battery %Battery.RemainingCapacity%%% charged
) ELSE (
	ECHO Estimated remaining runtime %Battery.EstimatedRuntime% minutes
	ECHO Remaining capacity %Battery.RemainingCapacity%%%
)


GOTO:EOF

:: End localization
IF "%OS%"=="Windows_NT" ENDLOCAL