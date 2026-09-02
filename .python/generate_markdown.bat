@ECHO OFF
CHCP 65001 >NUL
WHERE python >NUL 2>NUL
IF ERRORLEVEL 1 (
    py "%~dp0generate_markdown.py"
) ELSE (
    python "%~dp0generate_markdown.py"
)
EXIT /B %ERRORLEVEL%
