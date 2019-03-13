#!/bin/bash

# Get the new database host from the first parameter
database=$1
#echo $database

# TODO: switch dir of conf
# nginxpath=nginx.conf # For testing
nginxpath=/etc/nginx/nginx.conf

# Resources:    
# https://www.gnu.org/software/sed/manual/html_node/Regular-Expressions.html
# http://www.grymoire.com/Unix/Sed.html#uh-19

# match any string between server and :, replace it with the new database
# -r for regex
sed -ri "s|server .+:|server $database:|" $nginxpath



# Reload after swapping database
/usr/sbin/nginx -s reload