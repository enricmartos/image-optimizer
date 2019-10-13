FROM jboss/wildfly
USER jboss
RUN /opt/jboss/wildfly/bin/add-user.sh admin Jboss@admin01 --silent
# JBoss ports
COPY build/libs/image-optimizer.war /opt/jboss/wildfly/standalone/deployments/
# Add server.props to the container in /opt/conf new dir
WORKDIR /opt
USER root
RUN mkdir conf
ENV CONF_DIR=/opt/conf
WORKDIR $CONF_DIR
COPY devresources/server.properties $CONF_DIR
# create /tmp subFolder to store and zip docsConverter imgs
WORKDIR /tmp
USER root
RUN mkdir imgs
# JBoss ports
EXPOSE 8080 9990 8009
# install imagemagick on the distro of wildfly container (CentOS)
#RUN yum update -y && yum install -y ImageMagick
RUN yum install -y ImageMagick
#Program to prevent Postscript delegate failed error when converting docs (only on CentOS)
RUN yum install -y ghostscript
# Run
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0", "-c", "standalone.xml"]
