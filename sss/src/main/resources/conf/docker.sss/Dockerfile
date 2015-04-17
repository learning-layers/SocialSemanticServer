FROM java:8-jre

MAINTAINER Dieter Theiler "dtheiler@tugraz.at"

ADD sss.app/ /sss.app

EXPOSE 8390

COPY sss.sss.docker.entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]