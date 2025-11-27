# Deployment script for rivendell.nje.hu
# This script uploads the WAR file to the Tomcat server

param(
    [string]$Password = ""
)

$username = "student208"
$serverHost = "rivendell.nje.hu"
$port = 22
$warFile = "c:\java_seminar\java_seminar\vehicle-registration-app\target\ihutsc-se.war"
$remoteTempPath = "/home/student208/ihutsc-se.war"
$remoteTomcatPath = "/opt/tomcat/webapps/ihutsc-se.war"

Write-Host "======================================"
Write-Host "Deploying to Tomcat Server"
Write-Host "======================================"
Write-Host "Server: $serverHost"
Write-Host "User: $username"
Write-Host "WAR File: $warFile"
Write-Host ""

# Check if WAR file exists
if (-Not (Test-Path $warFile)) {
    Write-Host "ERROR: WAR file not found at $warFile" -ForegroundColor Red
    Write-Host "Run: mvnw.cmd clean package -DskipTests" -ForegroundColor Yellow
    exit 1
}

$warSize = ((Get-Item $warFile).Length / 1MB).ToString('F2')
Write-Host "WAR file found ($warSize MB)" -ForegroundColor Green
Write-Host ""

# Try to find WinSCP
$winscpPaths = @(
    "C:\Program Files (x86)\WinSCP\WinSCP.com",
    "C:\Program Files\WinSCP\WinSCP.com",
    "$env:LOCALAPPDATA\Programs\WinSCP\WinSCP.com",
    "$env:ProgramFiles\WinSCP\WinSCP.com",
    "$env:ProgramFiles(x86)\WinSCP\WinSCP.com"
)

$winscpExe = $null
foreach ($path in $winscpPaths) {
    if (Test-Path $path) {
        $winscpExe = $path
        break
    }
}

# Try to find pscp (PuTTY)
$pscpPaths = @(
    "C:\Program Files\PuTTY\pscp.exe",
    "C:\Program Files (x86)\PuTTY\pscp.exe",
    "$env:LOCALAPPDATA\Programs\PuTTY\pscp.exe"
)

$pscpExe = $null
foreach ($path in $pscpPaths) {
    if (Test-Path $path) {
        $pscpExe = $path
        break
    }
}

if ($winscpExe) {
    Write-Host "Found WinSCP at: $winscpExe" -ForegroundColor Green
    Write-Host ""
    
    if ([string]::IsNullOrEmpty($Password)) {
        $securePassword = Read-Host "Enter password for $username@$serverHost" -AsSecureString
        $BSTR = [System.Runtime.InteropServices.Marshal]::SecureStringToBSTR($securePassword)
        $Password = [System.Runtime.InteropServices.Marshal]::PtrToStringAuto($BSTR)
    }
    
    Write-Host "Uploading WAR file using WinSCP..." -ForegroundColor Cyan
    
    $winscpScript = @"
open sftp://${username}:${Password}@${serverHost}:${port}/ -hostkey=*
put `"$warFile`" `"$remoteTempPath`"
call cp $remoteTempPath $remoteTomcatPath
ls /opt/tomcat/webapps/ihutsc-se*
exit
"@
    
    $scriptFile = [System.IO.Path]::GetTempFileName()
    $winscpScript | Out-File -FilePath $scriptFile -Encoding ASCII
    
    try {
        & $winscpExe /script=$scriptFile /log="$env:TEMP\winscp_deploy.log"
        $exitCode = $LASTEXITCODE
        
        if ($exitCode -eq 0) {
            Write-Host ""
            Write-Host "SUCCESS! Deployment completed." -ForegroundColor Green
            Write-Host ""
            Write-Host "Your application should be available at:" -ForegroundColor Cyan
            Write-Host "  http://rivendell.nje.hu:9443/ihutsc-se/" -ForegroundColor Yellow
            Write-Host ""
            Write-Host "Login credentials:" -ForegroundColor Cyan
            Write-Host "  admin / admin123"
            Write-Host "  user / user123"
            Write-Host ""
        } else {
            Write-Host ""
            Write-Host "ERROR: Upload failed with exit code $exitCode" -ForegroundColor Red
            Write-Host "Check log at: $env:TEMP\winscp_deploy.log" -ForegroundColor Yellow
            Write-Host ""
            throw "Deployment failed"
        }
    } finally {
        Remove-Item $scriptFile -ErrorAction SilentlyContinue
    }
    
} elseif ($pscpExe) {
    Write-Host "Found pscp (PuTTY) at: $pscpExe" -ForegroundColor Green
    Write-Host ""
    
    if ([string]::IsNullOrEmpty($Password)) {
        Write-Host "Note: You'll need to enter password twice (upload + move)" -ForegroundColor Yellow
    }
    
    Write-Host "Uploading WAR file using pscp..." -ForegroundColor Cyan
    
    $pscpArgs = "-P $port `"$warFile`" ${username}@${serverHost}:$remoteTempPath"
    if (-not [string]::IsNullOrEmpty($Password)) {
        $pscpArgs = "-pw `"$Password`" " + $pscpArgs
    }
    
    $pscpCmd = "& `"$pscpExe`" $pscpArgs"
    Invoke-Expression $pscpCmd
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "WAR uploaded to home directory. Moving to Tomcat..." -ForegroundColor Cyan
        Write-Host "You'll need to run manually:" -ForegroundColor Yellow
        Write-Host "  ssh $username@$serverHost" -ForegroundColor White
        Write-Host "  cp $remoteTempPath $remoteTomcatPath" -ForegroundColor White
    } else {
        Write-Host "ERROR: Upload failed" -ForegroundColor Red
        exit 1
    }
    
} else {
    Write-Host "WinSCP or PuTTY (pscp) not found." -ForegroundColor Yellow
    Write-Host ""
    Write-Host "MANUAL DEPLOYMENT INSTRUCTIONS:" -ForegroundColor Cyan
    Write-Host "================================"
    Write-Host ""
    Write-Host "Option 1 - Install and use WinSCP (Recommended):" -ForegroundColor Green
    Write-Host "1. Download from: https://winscp.net/eng/download.php"
    Write-Host "2. Install WinSCP"
    Write-Host "3. Run this script again"
    Write-Host ""
    Write-Host "Option 2 - Manual SCP upload:" -ForegroundColor Green
    Write-Host "Run and enter password when prompted:"
    Write-Host "  scp -P $port `"$warFile`" ${username}@${serverHost}:$remoteTempPath" -ForegroundColor White
    Write-Host ""
    Write-Host "Then SSH to move to Tomcat:"
    Write-Host "  ssh ${username}@${serverHost}" -ForegroundColor White
    Write-Host "  cp $remoteTempPath $remoteTomcatPath" -ForegroundColor White
    Write-Host "  ls -lh /opt/tomcat/webapps/ihutsc-se*" -ForegroundColor White
    Write-Host ""
    Write-Host "After upload, verify at:"
    Write-Host "  http://rivendell.nje.hu:9443/ihutsc-se/" -ForegroundColor Yellow
    Write-Host ""
}
