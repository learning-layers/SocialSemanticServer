FROM java:8-jre

MAINTAINER Dieter Theiler "dtheiler@tugraz.at"

ADD https://github.com/learning-layers/SocialSemanticServer/releases/download/v9.0.0-alpha/sss.package.zip /

RUN apt-get update
RUN apt-get install -y unzip mysql-client vim sed

EXPOSE 8390

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]