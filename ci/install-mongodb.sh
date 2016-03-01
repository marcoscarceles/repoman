#! /bin/sh
sudo /etc/init.d/mongod stop
sudo yum update mongodb-org-server -y --enablerepo=mongodb
sudo /etc/init.d/mongod start
