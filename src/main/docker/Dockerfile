FROM kpms/openjdk8-python3-yasha:latest
LABEL maintainer="Dries De Proost <driesdeproost@gmail.com>"

VOLUME /tmp

ARG YT_DOWNLOAD_WAR_NAME
ADD ${YT_DOWNLOAD_WAR_NAME} app.war


#RUN apk add --update \
#    openjdk8
#    python-dev \
#    py-pip \
#    build-base \
#  && pip install --upgrade youtube-dl \

ENV JAVA_OPTS=""

CMD ["java", "-jar", "/app.war"]