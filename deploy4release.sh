#!/bin/sh

command_str="ant "
file_name="daqWeb.war"
para_str="-Ddeploy=product -DwarFileName=$file_name"
cur_dir=`pwd`
work_dir="/root/daqWeb"
dest_file="$cur_dir/$file_name"


if [ -f $dest_file ]; then
    echo "daqWeb.war already exists.overwrite file? Y/n"
    read ow
    if [ "$ow" == "Y" -o "$ow" == "y" -o "$ow" == "" ]; then
        rm -f $dest_file
    else
        exit 1
    fi
fi


cd "$work_dir"
echo 'making warfile...'
if /bin/sh $command_str$para_str; then
    echo 'making warfile success!'
    if [ "$work_dir/$file_name" != $dest_file ]; then
        mv "$work_dir/$file_name" $dest_file
    fi
    exit 0;
else
    echo 'making warfile failure!'
    exit 1;
fi

