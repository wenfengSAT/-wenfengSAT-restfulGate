#!/bin/bash
# -----------------------------------------------------------------------------
#   Author: wenfengSAT@163.com
#     date: 2018-8-21 10:39
#     Desc:
# -----------------------------------------------------------------------------

# chkconfig:   2345 80 20

# Source function library.
. /etc/rc.d/init.d/functions

PROJECT_NAME=resultful
APP_NAME=api
RUN_USER=${PROJECT_NAME}
PROG=${PROJECT_NAME}-${APP_NAME}
PID_DIR=/var/run/${PROJECT_NAME}
PID_FILE=${PID_DIR}/$PROG.pid

BASE_DIR=$(cd `dirname $0`; pwd)
APP_HOME=$(cd `dirname ${BASE_DIR}`; pwd)


APP_JAR=${APP_HOME}/main/*.jar
APP_CONF=${APP_HOME}/conf/
APP_LOG_DIR=/data/logs/${PROJECT_NAME}-${APP_NAME}

PORT=

[ -z "`grep $RUN_USER /etc/passwd`" ] && echo -e "error: user $RUN_USER does not exist"

[ -n "${APP_LOG_DIR}" ] && mkdir -pv ${APP_LOG_DIR} >/dev/null && chown -R ${RUN_USER}:${RUN_USER} ${APP_LOG_DIR} >/dev/null


#JVM参数
JVM_OPTS="-server -Xms1g -Xmx1g -Xmn512m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=160m"

JAVA_CMD="/data/jdk-11/bin/java -jar -Dfile.encoding=UTF-8"
[ -n "$JVM_OPTS" ] && JAVA_CMD="${JAVA_CMD} ${JVM_OPTS}"

[ -n "$APP_CONF" ] && JAVA_CMD="${JAVA_CMD} -Dspring.config.location=${APP_CONF}"

[ -n "$PORT" ] && JAVA_CMD="${JAVA_CMD} -Dserver.port=${PORT}"

JAVA_CMD="${JAVA_CMD} --logging.config=${APP_CONF}/logback.xml"


RETVAL=1
liv=


start() {

    if [ -n "$PID_DIR" ] && [ ! -e "$PID_DIR" ]; then
        mkdir -p "$PID_DIR" && chown $RUN_USER:$RUN_USER "$PID_DIR"
    fi
    if [ -n "$PID_FILE" ] && [ ! -e "$PID_FILE" ]; then
        touch "$PID_FILE" && chown $RUN_USER:$RUN_USER "$PID_FILE"
    fi

    echo -n "Starting $PROG "
    if [ $(whoami) == 'root' ]
    then
        daemon --user $RUN_USER --pidfile $PID_FILE "$JAVA_CMD $APP_JAR 2>&1 &" > /dev/null
    else
        daemon --pidfile $PID_FILE "$JAVA_CMD $APP_JAR 2>&1 &" > /dev/null
    fi
    RETVAL=$?

    if [ $RETVAL = 0 ]
    then
        RETVAL=1
        sleep 15
        pid=$(ps -ef | grep -v grep | grep java | grep $APP_JAR | awk '{print $2}')

        if [ -n "$pid" ]
        then
            echo $pid > "$PID_FILE";
            chown ${RUN_USER}:${RUN_USER} "$PID_FILE";
            RETVAL=0
        fi
    fi

    if [ $RETVAL -eq 0 ]
    then
        action $"pid: $pid" /bin/true
    else
        action $"" /bin/false
    fi
    return $RETVAL

}

stop() {
    [ -f "$PID_FILE" ] && local pid=$(cat $PID_FILE)
    killproc -p $PID_FILE
    RETVAL=$?

    if [ $RETVAL -eq 0 ]; then
        action "Stopping $PROG pid: $pid" /bin/true
        [ -f $PID_FILE  ] && rm -rf $PID_FILE
    elif [ $RETVAL -ne 0 ]; then
        action "Stopping $PROG" /bin/false
        RETVAL=1
    fi
    return $RETVAL
}


restart() {
    rh_status
    status=$(rh_status| tr -cd "[0-9]")
    [ -n "$status" ] && stop
    sleep 2
    start
}

rh_status() {
     status -p $PID_FILE $APP_NAME
}

rh_status_q() {
    rh_status >/dev/null 2>&1
}

case "$1" in
    start)
    (rh_status_q && rh_status ) && exit 0
    $1
    ;;
    stop)
    (rh_status_q || rh_status )|| exit 0
    $1
    ;;
    restart)
    $1
    ;;
    status)
    rh_status
    ;;
    *)
    echo $"Usage: $0 {start|stop|status|restart}"
    exit 2
    ;;
esac

