FROM library/postgres:bullseye

COPY init-scripts/1_schema.sql /docker-entrypoint-initdb.d/
COPY init-scripts/2_data.sql /docker-entrypoint-initdb.d/

ENV POSTGRES_USER app
ENV POSTGRES_PASSWORD P@ssw0rd
ENV POSTGRES_DB seabattle
ENV TZ 'GMT+7'
