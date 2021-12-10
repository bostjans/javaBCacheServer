#!/bin/sh

# This ..

PATH_PROG=.
#PATH_PROG=target
PATH_LOG=$PATH_PROG/log

LOG_CONF=$PATH_PROG/properties/logging.properties

VMparam="-server -Xms264m -Xmx986m"
VMparam="-Djava.util.logging.config.file=$LOG_CONF $VMparam"
#VMparam="-Dapp.path.config=/home/bcache/bin/bCache/properties $VMparam"

java $VMparam -jar $PATH_PROG/bCacheServer-0.2.3.jar -v

echo That_s it.

exit 0
