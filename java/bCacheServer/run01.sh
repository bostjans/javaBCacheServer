#!/bin/sh

# This ..
#

PROG_DIR=.
#PROG_DIR=target
PATH_LOG=$PATH_PROG/log

LOG_CONF=$PATH_PROG/properties/logging.properties

VMparam="-server -Xms264m -Xmx986m"
VMparam="-Djava.util.logging.config.file=$LOG_CONF $VMparam"
#VMparam="-Dapp.path.config=/home/lenko12/bin/lenkoTrader/properties $VMparam"

java $VMparam -jar $PATH_PROG/bCacheServer-0.1.0.jar -v

echo That_s it.

exit 0
