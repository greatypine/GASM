#!/bin/sh

command_str='ant '
file_name="GASM.war"
para_str="-Ddeploy=develop -DwarFileName=$file_name"
cur_dir=`pwd`
work_dir="/root/GASM"
dest_file="$cur_dir/$file_name"

cd /root/GASM
if [ -f $dest_file ]; then
    echo "GASM.war already exists.overwrite file? Y/n"
    read ow
    if [ "$ow" == "Y" -o "$ow" == "y" -o "$ow" == "" ]; then
        rm -f $dest_file
    else
        exit 1
    fi
fi

echo 'updating source...'
git pull origin master

echo 'making warfile...'
if  $command_str$para_str; then
    if [ "$work_dir/$file_name" != $dest_file ]; then
        mv "$work_dir/$file_name" $dest_file
    fi
    counter=`ps -ef | grep tomcat7gasm|grep -vE 'grep|tail'|wc -l`
    if [ $counter -eq 1 ];then
        echo 'stoping tomcat...'
        /usr/local/tomcat7gasm/bin/catalina.sh stop
        sleep 5
    fi
  
    echo 'deploying...'
    rm -rf /usr/local/tomcat7gasm/webapps/GASM* &&
    cp $dest_file /usr/local/tomcat7gasm/webapps/
    echo 'starting tomcat...'
    /usr/local/tomcat7gasm/bin/catalina.sh start
    sleep 5
    counter=`ps -ef | grep tomcat7gasm|grep -vE 'grep|tail'|wc -l`
    if [ $counter -eq 1 ];then
        echo 'starting tomcat success...'
    else
        echo 'starting tomcat failure'
        exit 1
    fi
else
    echo 'making warfile failure!'
    exit 1;
fi

