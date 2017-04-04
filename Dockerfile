FROM java:8

MAINTAINER Marcos Tenrero <m.tenrero@alumnos.urjc.es>

WORKDIR /tmp

EXPOSE 4444

ADD target/Iservice-1.0-SNAPSHOT.jar /tmp/app.jar
ADD za.store /tmp/za.store
RUN bash -c 'touch /tmp/app.jar'

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /tmp/app.jar" ]
