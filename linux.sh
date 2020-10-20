@ECHO OFF
ECHO ***Date and Time***
set datetimef=%date:~-4%_%date:~3,2%_%date:~0,2%__%time:~0,2%_%time:~3,2%_%time:~6,2%
echo %datetimef%
ECHO ***List Processes and cpu utilization***
top > ~/data/process.txt
ECHO ***wireless bandwidth***
ip a > ~/data/network.txt
ECHO ***Memory Usage***
cat /proc/meminfo > ~/data/memory.htable
ECHO ***Battery Usage***
acpi --everything  > ~/data/battery.txt
echo $?
GOTO:EOF
:: End localization