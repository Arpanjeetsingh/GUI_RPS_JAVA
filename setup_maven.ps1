# Downloads Maven 3.9.6 locally and runs the game.
# Requires Java 17 or higher to be installed.
# Run with: powershell -ExecutionPolicy Bypass -File setup_maven.ps1

$MavenVersion = "3.9.6"
$MavenDir     = "$PSScriptRoot\maven"
$MavenZip     = "$PSScriptRoot\maven.zip"
$MavenHome    = "$MavenDir\apache-maven-$MavenVersion"
$MavenExe     = "$MavenHome\bin\mvn.cmd"

# Auto-detect Java 17+ - check JAVA_HOME first, then PATH
if ($env:JAVA_HOME -and (Test-Path "$env:JAVA_HOME\bin\java.exe")) {
    $javaExe = "$env:JAVA_HOME\bin\java.exe"
} else {
    $javaExe = (Get-Command java -ErrorAction SilentlyContinue).Source
}

if (-not $javaExe) {
    Write-Host "ERROR: Java not found. Install Java 17 or higher and try again."
    exit 1
}

$version = & $javaExe -version 2>&1 | Select-String "version" | Out-String
Write-Host "Using Java: $($version.Trim())"

# Download Maven if not already present
if (-not (Test-Path $MavenExe)) {
    Write-Host "Downloading Maven $MavenVersion (one-time setup)..."
    $url = "https://archive.apache.org/dist/maven/maven-3/$MavenVersion/binaries/apache-maven-$MavenVersion-bin.zip"
    Invoke-WebRequest -Uri $url -OutFile $MavenZip -UseBasicParsing
    if (-not (Test-Path $MavenZip)) {
        Write-Host "ERROR: Download failed. Check your internet connection."
        exit 1
    }
    Write-Host "Extracting Maven..."
    Expand-Archive -Path $MavenZip -DestinationPath $MavenDir -Force
    Remove-Item $MavenZip
    Write-Host "Maven ready."
}

# Point Maven to the detected Java
$env:JAVA_HOME = Split-Path (Split-Path $javaExe)
Set-Location $PSScriptRoot
Write-Host "Starting game (JavaFX downloads automatically on first run)..."
& $MavenExe javafx:run
