FROM tomee:9-jre11-alpine-plume

# Remove the default webapps
RUN rm -rf /usr/local/tomee/webapps/*

# Copy your WAR file into the webapps directory
COPY target/dev-sync.war /usr/local/tomee/webapps/ROOT.war

# Copy your persistence.xml to the correct location
# COPY src/main/resources/META-INF/persistence.xml /usr/local/tomee/conf/persistence.xml

# Expose the port that your app runs on the container
EXPOSE 8080

# Start TomEE
CMD ["catalina.sh", "run"]
