@echo off
echo Downloading ActiveMQ JARs...
echo.

set SERVER_LIB=HAPAVehicleRentalServer26937\lib
set CLIENT_LIB=HapaVehicleRentalClient26937\lib

echo Creating directories if they don't exist...
if not exist "%SERVER_LIB%" mkdir "%SERVER_LIB%"
if not exist "%CLIENT_LIB%" mkdir "%CLIENT_LIB%"

echo.
echo Please download the following JAR files manually from Maven Central:
echo https://mvnrepository.com/
echo.
echo Required JARs:
echo 1. activemq-client-5.18.3.jar
echo 2. activemq-broker-5.18.3.jar
echo 3. activemq-kahadb-store-5.18.3.jar
echo 4. activemq-protobuf-1.1.jar
echo 5. geronimo-j2ee-management_1.1_spec-1.0.1.jar
echo 6. geronimo-jms_1.1_spec-1.1.1.jar
echo 7. slf4j-api-1.7.36.jar
echo.
echo Copy these JARs to both:
echo - %SERVER_LIB%
echo - %CLIENT_LIB%
echo.
echo Alternative: Use the embedded broker (no external JARs needed)
echo Just add activemq-broker-5.18.3.jar to the server lib folder
echo.
pause