FROM quay.io/wildfly/wildfly:35.0.1.Final-jdk21

COPY playground-ear/target/playground.ear /opt/jboss/wildfly/standalone/deployments/