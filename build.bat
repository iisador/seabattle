@REM call mvn package -Pnative -Dquarkus.native.container-build=true -Dquarkus.native.builder-image=quay.io/quarkus/ubi-quarkus-mandrel-builder-image:jdk-22
@REM call mvn package -Dnative -Dquarkus.native.container-build=true -Dquarkus.container-image.build=true
@REM call docker build -f Dockerfile.native -t seabattle .
@REM call docker save seabattle -o seabattle.tar
@REM timeout /t 1
@REM call scp .\target\seabattle-0.0.1-runner isador@192.168.1.58:~/seabattle/seabattle-0.0.1-runner
@REM call scp .\db\init-scripts\1_schema.sql isador@192.168.1.58:~/seabattle/init-scripts/1_schema.sql
@REM call scp .\db\init-scripts\2_data.sql isador@192.168.1.58:~/seabattle/init-scripts/2_data.sql
@REM call ssh isador@192.168.1.58 "sudo docker build -f ~/seabattle/Dockerfile.native -t seabattle ~/seabattle"
@REM call ssh isador@192.168.1.58 "sudo docker load -i ~/seabattle/seabattle.tar"

call mvn clean package
call ssh isador@192.168.1.58 "sudo docker compose -f /home/isador/seabattle/compose.yaml stop"
call ssh isador@192.168.1.58 "rm -rf /home/isador/seabattle/app"
call scp -r .\target\quarkus-app isador@192.168.1.58:~/seabattle/app
call ssh isador@192.168.1.58 "sudo docker image rm -f seabattle-seabattle"
call ssh isador@192.168.1.58 "sudo docker compose -f /home/isador/seabattle/compose.yaml build --no-cache"
call ssh isador@192.168.1.58 "sudo docker compose -f /home/isador/seabattle/compose.yaml up -d"
