call runcrud.bat
if "%ERRORLEVEL%" == "0" goto :browser
echo.
echo Crud Application started
goto fail

:browser 
call "C:\Program Files (x86)\Mozilla Firefox\firefox.exe" "http://localhost:8080/crud/v1/task/getTasks"
if "%ERRORLEVEL%" == "0" goto :end
goto fail

:fail
echo.
echo There were errors

:end
echo Script done.