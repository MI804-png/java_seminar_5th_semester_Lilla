# PowerShell script to convert DOCUMENTATION.md to PDF
# Uses Microsoft Print to PDF or Chrome/Edge for conversion

param(
    [string]$InputFile = "DOCUMENTATION.md",
    [string]$OutputFile = "Vehicle-Registration-Documentation.pdf"
)

Write-Host "======================================"
Write-Host "Markdown to PDF Converter"
Write-Host "======================================"
Write-Host ""

# Check if input file exists
if (-Not (Test-Path $InputFile)) {
    Write-Host "ERROR: $InputFile not found!" -ForegroundColor Red
    exit 1
}

Write-Host "Input file: $InputFile" -ForegroundColor Green
Write-Host "Output file: $OutputFile" -ForegroundColor Green
Write-Host ""

# Try multiple methods to convert to PDF

# Method 1: Using Chrome/Edge headless (recommended)
Write-Host "Method 1: Trying Chrome/Edge headless..." -ForegroundColor Cyan

$chromePaths = @(
    "$env:ProgramFiles\Google\Chrome\Application\chrome.exe",
    "$env:ProgramFiles (x86)\Google\Chrome\Application\chrome.exe",
    "$env:LOCALAPPDATA\Google\Chrome\Application\chrome.exe"
)

$edgePaths = @(
    "$env:ProgramFiles\Microsoft\Edge\Application\msedge.exe",
    "$env:ProgramFiles (x86)\Microsoft\Edge\Application\msedge.exe"
)

$browser = $null
foreach ($path in $chromePaths) {
    if (Test-Path $path) {
        $browser = $path
        $browserName = "Chrome"
        break
    }
}

if (-not $browser) {
    foreach ($path in $edgePaths) {
        if (Test-Path $path) {
            $browser = $path
            $browserName = "Edge"
            break
        }
    }
}

if ($browser) {
    Write-Host "Found $browserName at: $browser" -ForegroundColor Green
    
    # Convert Markdown to HTML first
    Write-Host "Converting Markdown to HTML..." -ForegroundColor Cyan
    
    $htmlContent = @"
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Vehicle Registration Management System - Documentation</title>
    <style>
        body {
            font-family: 'Segoe UI', Arial, sans-serif;
            line-height: 1.6;
            max-width: 900px;
            margin: 40px auto;
            padding: 20px;
            color: #333;
        }
        h1 {
            color: #2c3e50;
            border-bottom: 3px solid #3498db;
            padding-bottom: 10px;
        }
        h2 {
            color: #34495e;
            border-bottom: 2px solid #95a5a6;
            padding-bottom: 5px;
            margin-top: 30px;
        }
        h3 {
            color: #7f8c8d;
            margin-top: 20px;
        }
        code {
            background-color: #f4f4f4;
            padding: 2px 6px;
            border-radius: 3px;
            font-family: 'Courier New', monospace;
        }
        pre {
            background-color: #2c3e50;
            color: #ecf0f1;
            padding: 15px;
            border-radius: 5px;
            overflow-x: auto;
        }
        pre code {
            background-color: transparent;
            color: #ecf0f1;
        }
        table {
            border-collapse: collapse;
            width: 100%;
            margin: 20px 0;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 12px;
            text-align: left;
        }
        th {
            background-color: #3498db;
            color: white;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
        a {
            color: #3498db;
            text-decoration: none;
        }
        a:hover {
            text-decoration: underline;
        }
        .check {
            color: #27ae60;
            font-weight: bold;
        }
        blockquote {
            border-left: 4px solid #3498db;
            padding-left: 20px;
            margin-left: 0;
            font-style: italic;
            color: #555;
        }
        hr {
            border: none;
            border-top: 2px solid #ecf0f1;
            margin: 30px 0;
        }
        @media print {
            body {
                margin: 0;
                padding: 20px;
            }
            h1, h2, h3 {
                page-break-after: avoid;
            }
            pre, table {
                page-break-inside: avoid;
            }
        }
    </style>
</head>
<body>
"@
    
    # Read markdown and do basic conversion
    $markdown = Get-Content $InputFile -Raw
    
    # Convert markdown to HTML (basic)
    $html = $markdown `
        -replace '(?m)^# (.+)$', '<h1>$1</h1>' `
        -replace '(?m)^## (.+)$', '<h2>$1</h2>' `
        -replace '(?m)^### (.+)$', '<h3>$1</h3>' `
        -replace '(?m)^#### (.+)$', '<h4>$1</h4>' `
        -replace '(?m)^\*\*(.+?)\*\*', '<strong>$1</strong>' `
        -replace '(?m)^- (.+)$', '<li>$1</li>' `
        -replace '(?m)^✅', '<span class="check">✅</span>' `
        -replace '(?m)^```(\w+)?$(.+?)^```$', '<pre><code>$2</code></pre>' `
        -replace '`([^`]+)`', '<code>$1</code>' `
        -replace '\[([^\]]+)\]\(([^\)]+)\)', '<a href="$2">$1</a>' `
        -replace '(?m)^---$', '<hr>' `
        -replace '(?m)^\n', '<p>'
    
    $htmlContent += $html
    $htmlContent += @"
</body>
</html>
"@
    
    $tempHtml = [System.IO.Path]::GetTempFileName() + ".html"
    $htmlContent | Out-File -FilePath $tempHtml -Encoding UTF8
    
    Write-Host "Temporary HTML: $tempHtml" -ForegroundColor Yellow
    Write-Host "Converting HTML to PDF using $browserName..." -ForegroundColor Cyan
    
    $fullOutputPath = Join-Path (Get-Location) $OutputFile
    
    & $browser --headless --disable-gpu --print-to-pdf="$fullOutputPath" $tempHtml
    
    Start-Sleep -Seconds 2
    
    if (Test-Path $OutputFile) {
        $size = ((Get-Item $OutputFile).Length / 1KB).ToString('F2')
        Write-Host ""
        Write-Host "SUCCESS! PDF created: $OutputFile ($size KB)" -ForegroundColor Green
        Write-Host ""
        Write-Host "Opening PDF..." -ForegroundColor Cyan
        Start-Process $OutputFile
        
        Remove-Item $tempHtml -ErrorAction SilentlyContinue
        exit 0
    } else {
        Write-Host "ERROR: PDF creation failed" -ForegroundColor Red
    }
    
    Remove-Item $tempHtml -ErrorAction SilentlyContinue
}

# Method 2: Manual instructions
Write-Host ""
Write-Host "MANUAL PDF GENERATION:" -ForegroundColor Yellow
Write-Host "======================================" -ForegroundColor Yellow
Write-Host ""
Write-Host "Option 1 - Using Microsoft Word:" -ForegroundColor Cyan
Write-Host "1. Open DOCUMENTATION.md in VS Code"
Write-Host "2. Press Ctrl+Shift+P"
Write-Host "3. Type 'Markdown: Print to PDF'"
Write-Host "4. Save as Vehicle-Registration-Documentation.pdf"
Write-Host ""
Write-Host "Option 2 - Using Browser:" -ForegroundColor Cyan
Write-Host "1. Open DOCUMENTATION.md in Chrome/Edge"
Write-Host "2. Press Ctrl+P (Print)"
Write-Host "3. Select 'Microsoft Print to PDF'"
Write-Host "4. Click 'Print' and save"
Write-Host ""
Write-Host "Option 3 - Using Online Converter:" -ForegroundColor Cyan
Write-Host "1. Go to: https://www.markdowntopdf.com/"
Write-Host "2. Upload DOCUMENTATION.md"
Write-Host "3. Download generated PDF"
Write-Host ""
Write-Host "Option 4 - Install Pandoc:" -ForegroundColor Cyan
Write-Host "1. Download: https://pandoc.org/installing.html"
Write-Host "2. Install Pandoc"
Write-Host "3. Run: pandoc DOCUMENTATION.md -o Documentation.pdf"
Write-Host ""
