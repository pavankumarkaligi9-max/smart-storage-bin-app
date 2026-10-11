@echo off
echo ========================================================
echo     Smart Storage Bin - Complete Project Launcher
echo ========================================================
echo [1] Launching Interactive Web Simulation on port 8085...
cd /d "%~dp0SmartStorageBinWeb"
start http://localhost:8085
python -m http.server 8085 --bind 0.0.0.0
pause
