# Download ActiveMQ JARs
$serverLib = "HAPAVehicleRentalServer26937\lib"
$urls = @(
    "https://repo1.maven.org/maven2/org/apache/activemq/activemq-broker/5.18.3/activemq-broker-5.18.3.jar",
    "https://repo1.maven.org/maven2/org/apache/activemq/activemq-client/5.18.3/activemq-client-5.18.3.jar",
    "https://repo1.maven.org/maven2/org/apache/geronimo/specs/geronimo-jms_1.1_spec/1.1.1/geronimo-jms_1.1_spec-1.1.1.jar",
    "https://repo1.maven.org/maven2/org/slf4j/slf4j-api/1.7.36/slf4j-api-1.7.36.jar"
)

Write-Host "Downloading ActiveMQ JARs..." -ForegroundColor Green
New-Item -ItemType Directory -Force -Path $serverLib | Out-Null

foreach ($url in $urls) {
    $filename = Split-Path $url -Leaf
    $filepath = Join-Path $serverLib $filename
    Write-Host "Downloading $filename..." -ForegroundColor Yellow
    try {
        Invoke-WebRequest -Uri $url -OutFile $filepath -UseBasicParsing
        Write-Host "Downloaded $filename" -ForegroundColor Green
    } catch {
        Write-Host "Failed to download $filename" -ForegroundColor Red
    }
}

Write-Host "Download complete! JARs saved to $serverLib" -ForegroundColor Green