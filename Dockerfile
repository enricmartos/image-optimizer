FROM gradle:jdk8 as builder
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build -x test

FROM jboss/wildfly
USER jboss
RUN /opt/jboss/wildfly/bin/add-user.sh admin Jboss@admin01 --silent
USER root
RUN yum install -y ImageMagick
#Program to prevent Postscript delegate failed error when converting docs (only on CentOS)
RUN yum install -y ghostscript
RUN yum install -y unoconv
#COPY build/libs/image-optimizer.war /opt/jboss/wildfly/standalone/deployments/
# Add server.props to the container in /opt/conf new dir
WORKDIR /opt
RUN mkdir conf
ENV CONF_DIR=/opt/conf
WORKDIR $CONF_DIR
COPY devresources/server.properties $CONF_DIR
# JBoss ports
# EXPOSE 8080 9990 8009
# install imagemagick on the distro of wildfly container (CentOS)
#RUN yum update -y && yum install -y ImageMagick
#copy artifacts from one stage to the next
COPY --from=builder  /home/gradle/src/build/libs/image-optimizer.war /opt/jboss/wildfly/standalone/deployments/
# Run
#CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0", "-c", "standalone.xml"]
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-c", "standalone.xml", "-Djboss.http.port=$PORT", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
