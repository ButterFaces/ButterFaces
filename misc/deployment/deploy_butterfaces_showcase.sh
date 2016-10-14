#!/usr/bin/env bash

if [ $# -ne 1 ]; then
        echo "usage: $0 <VERSION> "
        echo "example:"
        echo "usage: $0 2.1.3"
        exit 1
else
   VERSION=$1
   echo "[INFO] script-call: $0"
fi

echo ''
echo '[BEGIN] Download artifact'
echo '----------------------------------------'
curl -O --fail http://repo1.maven.org/maven2/de/larmic/butterfaces/showcase/${VERSION}/showcase-${VERSION}.war
if [ ! -f showcase-${VERSION}.war ]; then
    echo "[ERROR] Download artifact failed"
    exit 1
else
    echo '------------------------------------------'
    echo '[SUCCESS] Download artifact completed'
    echo ''
fi

echo '[BEGIN] Stopping wildfly server'
echo '----------------------------------------'
/etc/init.d/jboss-as-new stop
echo '------------------------------------------'
echo '[SUCCESS] Wildfly server stopped'
echo ''

echo '[BEGIN] Copy artifact'
echo '----------------------------------------'
mv showcase-${VERSION}.war showcase.war
echo '------------------------------------------'
echo '[SUCCESS] Copy artifact'
echo ''

echo '[BEGIN] Starting wildfly server'
echo '----------------------------------------'
/etc/init.d/jboss-as-new start
echo '------------------------------------------'
echo '[SUCCESS] Wildfly server started'