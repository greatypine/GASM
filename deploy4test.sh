#!/bin/sh

command_str='ant '
file_name="daqWeb.war"
para_str="-Ddeploy=develop -DwarFileName=$file_name"
cur_dir=`pwd`
work_dir="/root/daqWeb"
dest_file="$cur_dir/$file_name"

cd /root/daqWeb
if [ -f $dest_file ]; then
    echo "daqWeb.war already exists.overwrite file? Y/n"
    read ow
    if [ "$ow" == "Y" -o "$ow" == "y" -o "$ow" == "" ]; then
        rm -f $dest_file
    else
        exit 1
    fi
fi

echo 'updating source...'
svn up

echo 'making warfile...'
if /bin/sh $command_str$para_str; then
    if [ "$work_dir/$file_name" != $dest_file ]; then
        mv "$work_dir/$file_name" $dest_file
    fi
    counter=$(ps -C java --no-heading | wc -l)
    if [ $counter -eq 1 ];then
        echo 'stoping tomcat...'
        /usr/local/tomcat7/bin/catalina.sh stop;
    fi

    echo 'deploying...'
    rm -rf /usr/local/tomcat7/webapps/daqWeb* &&
    cp $dest_file /usr/local/tomcat7/webapps/;
    echo 'starting tomcat...'
    /usr/local/tomcat7/bin/catalina.sh start;
    sleep 5
    counter=$(ps -C java --no-heading|wc -l)
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

