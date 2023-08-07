#!/usr/bin/env bash
# -----------------------------------------------------------------------------
# Auth: wenfengSAT@163.com
# date: 2020-12-15 17:58
# Desc:
# -----------------------------------------------------------------------------
# chkconfig:   2345 80 20

cygwin=false
darwin=false
os400=false
case "`uname`" in
CYGWIN*) cygwin=true;;
Darwin*) darwin=true;;
OS400*) os400=true;;
esac

# default heap for server
export DEFAULT_JAVA_OPTS="-XX:+UseG1GC -Xms1g -Xmx1g -Xmn512m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=256m "


PROJECT_NAME=${PROJECT_NAME}
APP_NAME=${APP_NAME}


APPBIN="${BASH_SOURCE-$0}"
APPBIN="$(dirname "${APPBIN}")"
APP_BIN_DIR="$(cd "${APPBIN}"; pwd)"

APP_HOME="${APP_BIN_DIR}/.."

JAR_FILE=${APP_HOME}/*${APP_NAME}*.jar
APP_CONF=${APP_HOME}/conf/

LOG_PATH=${LOG_PATH}
LOG_LEVEL=${LOG_LEVEL:-INFO}


#===========================================================================================
# JMX Configuration
#===========================================================================================
# See the following page for extensive details on setting
# up the JVM to accept JMX remote management:
# http://java.sun.com/javase/6/docs/technotes/guides/management/agent.html
# by default we allow local JMX connections

JAVA_JMX_OPTS=""

if [ "x$JMXLOCALONLY" = "x" ]
then
    JMXLOCALONLY=false
fi

if [ "x$JMXDISABLE" = "x" ] || [ "$JMXDISABLE" = 'false' ]
then
  echo "$APP_NAME JMX enabled by default" >&2
  if [ "x$JMXPORT" = "x" ]
  then
    # for some reason these two options are necessary on jdk6 on Ubuntu
    #   accord to the docs they are not necessary, but otw jconsole cannot
    #   do a local attach
    JAVA_JMX_OPTS="-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.local.only=$JMXLOCALONLY "
  else
    if [ "x$JMXAUTH" = "x" ]
    then
      JMXAUTH=false
    fi
    if [ "x$JMXSSL" = "x" ]
    then
      JMXSSL=false
    fi
    echo "$APP_NAME remote JMX Port set to $JMXPORT" >&2
    echo "$APP_NAME remote JMX authenticate set to $JMXAUTH" >&2
    echo "$APP_NAME remote JMX ssl set to $JMXSSL" >&2

    if [ "x$JMXHOSTNAME" = "x" ]
    then
      JAVA_JMX_OPTS="-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=$JMXPORT \
                     -Dcom.sun.management.jmxremote.authenticate=$JMXAUTH -Dcom.sun.management.jmxremote.ssl=$JMXSSL "
    else
      echo "$APP_NAME remote JMX Hostname set to $JMXHOSTNAME" >&2
      JAVA_JMX_OPTS="-Dcom.sun.management.jmxremote -Djava.rmi.server.hostname=$JMXHOSTNAME -Dcom.sun.management.jmxremote.port=$JMXPORT \
                     -Dcom.sun.management.jmxremote.authenticate=$JMXAUTH -Dcom.sun.management.jmxremote.ssl=$JMXSSL "
    fi
  fi
else
    echo "JMX disabled by user request" >&2
    JAVA_JMX_OPTS=""
fi


#===========================================================================================
# JVM Configuration
#===========================================================================================
# 配置 JVM参数
if [ "x$JAVA_OPTS" = "x" ]
then
    JAVA_OPTS="${DEFAULT_JAVA_OPTS}"
fi


JAVA_OPT="${JAVA_OPT} ${JAVA_OPTS}"
JAVA_OPT="${JAVA_OPT} -XX:-OmitStackTraceInFastThrow -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${LOG_PATH}/java_heapdump.hprof"
JAVA_OPT="${JAVA_OPT} -XX:-UseLargePages"

JAVA_MAJOR_VERSION=$($JAVA -version 2>&1 | sed -E -n 's/.* version "([0-9]*).*$/\1/p')
if [[ "$JAVA_MAJOR_VERSION" -ge "9" ]]; then
  JAVA_OPT="${JAVA_OPT} -Xlog:gc*:file=${LOG_PATH}/gc.log:time,tags:filecount=10,filesize=102400"
else
  JAVA_OPT="${JAVA_OPT}"
#   JAVA_OPT="${JAVA_OPT} -Djava.ext.dirs=${JAVA_HOME}/jre/lib/ext:${JAVA_HOME}/lib/ext"
#   JAVA_OPT="${JAVA_OPT} -Xloggc:${LOG_PATH}/gc.log -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=100M"
fi
JAVA_OPT="${JAVA_OPT} ${JAVA_JMX_OPTS}"

JAVA_OPT="${JAVA_OPT} -Dfile.encoding=UTF-8"
JAVA_OPT="${JAVA_OPT} -Dproject.name=${PROJECT_NAME}"
JAVA_OPT="${JAVA_OPT} -Dapp.name=${APP_NAME}"
[[ "x${LOG_PATH}" != "x" ]] && JAVA_OPT="${JAVA_OPT} -Dlog.path=${LOG_PATH}"
JAVA_OPT="${JAVA_OPT} -Dlog.level=${LOG_LEVEL}"
#JAVA_OPT="${JAVA_OPT} -Dspring.config.location=${APP_CONF}"

JAVA_OPT="${JAVA_OPT} -jar ${JAR_FILE}"
#JAVA_OPT="${JAVA_OPT} --logging.config=${APP_HOME}/conf/logback.xml"


if [ "x${LOG_PATH}" != "x" ];then
  mkdir -p ${LOG_PATH}
fi



java -server -Djava.security.egd=file:/dev/./urandom ${JAVA_OPT}
