#mvn clean package
ssh isador@isador.ru:4022 "sudo docker compose -f /home/isador/seabattle/compose.yaml stop"
ssh isador@isador.ru:4022 "rm -rf /home/isador/seabattle/app"
scp -r .\target\quarkus-app isador@isador.ru:4022:~/seabattle/app
ssh isador@isador.ru:4022 "sudo docker image rm -f seabattle-seabattle"
ssh isador@isador.ru:4022 "sudo docker compose -f /home/isador/seabattle/compose.yaml build --no-cache"
ssh isador@isador.ru:4022 "sudo docker compose -f /home/isador/seabattle/compose.yaml up -d"
