#!/bin/bash

# 4 cron:
#@reboot  sleep 2 && cd /home/bcache/bin/bCache && ./run01.sh >> /dev/null 2>&1 &

PATH_PROG=.
PATH_LOG=$PATH_PROG/log
#if [[ ! -d "$PATH_LOG" ]]; then
#  echo "Create log dir. on location: $PATH_LOG;"
#  mkdir log
#fi

if [ -f "$PATH_PROG/../src/main/resources/logging.properties" ]; then
  echo "File exists: $PATH_PROG/../src/main/resources/logging.properties"
  LOG_CONF=$PATH_PROG/../src/main/resources/logging.properties
fi
if [ -f "$PATH_PROG/properties/logging.properties" ]; then
  echo "File exists: $PATH_PROG/properties/logging.properties"
  LOG_CONF=$PATH_PROG/properties/logging.properties
fi

VMparam="-server -Xms264m -Xmx986m"
VMparam="-Dfile.encoding=UTF-8 $VMparam"
#if [[ ! -v LOG_CONF ]]; then
#  echo "LOG_CONF is not set."
#else ..
if [ -z "$LOG_CONF" ]; then
  echo "LOG_CONF is set to the empty string!"
else
  echo "LOG_CONF > value: $LOG_CONF"
  VMparam="-Djava.util.logging.config.file=$LOG_CONF $VMparam"
fi
# Java 8
#VMparam="-verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintHeapAtGC -Xloggc:$PATH_LOG/gc-prog-01.log -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=11 -XX:GCLogFileSize=22M $VMparam"
# Java 11
#VMparam="-verbose:gc -XX:+PrintGCDetails -Xloggc:$PATH_LOG/gc-prog-01.log $VMparam"
#VMparam="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=6106 $VMparam"

# RMI
VMparam="-Djava.rmi.server.logCalls=false $VMparam"
VMparam="-Djava.rmi.client.logCalls=false $VMparam"
VMparam="-Djava.rmi.server.disableHttp=true $VMparam"
#VMparam="-Djava.rmi.server.codebase=file:///home/bcache/bin/bCache/dbModel-1.7.0.jar $VMparam"
#VMparam="-Djava.rmi.server.useCodebaseOnly=false $VMparam"
# JMX
#VMparam="-Dcom.sun.management.jmxremote.port=13001 $VMparam"
#VMparam="-Dcom.sun.management.jmxremote.rmi.port=13001 $VMparam"
#VMparam="-Dcom.sun.management.jmxremote.local.only=true $VMparam"
#VMparam="-Djava.rmi.server.hostname=127.0.0.1 $VMparam"
#VMparam="-Dcom.sun.management.jmxremote.ssl=false $VMparam"
#VMparam="-Dcom.sun.management.jmxremote.authenticate=false $VMparam"

#VMparam="-Dapp.path.config=/home/bcache/bin/bCache/properties $VMparam"

echo "VMparam: $VMparam"
#java $VMparam -jar $PATH_PROG/bCacheServer.jar -v
nohup java $VMparam -jar $PATH_PROG/bCacheServer.jar -v &

echo "That_s it."

exit 0
