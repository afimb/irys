#!/bin/bash

if test -z "$JAVA_HOME"; then
	if test -d /home/jdk1.6.0_03; then
	   export JAVA_HOME=/home/jdk1.6.0_03
	elif test -d /usr/lib/jvm/java-6-sun; then
	   export JAVA_HOME=/usr/lib/jvm/java-6-sun
	fi
fi

java -jar ${project.build.finalName}.jar $*
