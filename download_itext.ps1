# Download iText PDF library
$clientLib = "HapaVehicleRentalClient26937\lib"
$url = "https://repo1.maven.org/maven2/com/itextpdf/itextpdf/5.5.13.1/itextpdf-5.5.13.1.jar"

Write-Host "Downloading iText PDF library..." -ForegroundColor Green
New-Item -ItemType Directory -Force -Path $clientLib | Out-Null

$filename = "itextpdf-5.5.13.1.jar"
$filepath = Join-Path $clientLib $filename
Write-Host "Downloading $filename..." -ForegroundColor Yellow
try {
    Invoke-WebRequest -Uri $url -OutFile $filepath -UseBasicParsing
    Write-Host "Downloaded $filename" -ForegroundColor Green
} catch {
    Write-Host "Failed to download $filename" -ForegroundColor Red
}

Write-Host "Download complete! JAR saved to $clientLib" -ForegroundColor Green