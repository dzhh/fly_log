# osp_log
日志中心 基于ES

搭建：
Ubuntu 16.04
https://www.elastic.co/downloads/elasticsearch
1. 启动ES bin/elasticsearch
2. 启动kibana bin/kibana
3. 启动tomcat bin/startup.sh
4. 启动logstash bin/logstash -f tomcat.conf

启动spring 工程，访问
http://127.0.0.1:8080/index.html