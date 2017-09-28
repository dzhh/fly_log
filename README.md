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
http://127.0.0.1:9002/index.html

clientip字段(text类型)需要聚合，需要执行
PUT logstash-apacheaccesslog*/_mapping/logs/
{
  "properties": {
    "clientip": { 
      "type":  "text",
      "norms": false,
      "fielddata": true
    }
  }
}

创建searchlogs索引logs类型，用于记录搜索历史
POST searchlogs/logs/_mapping  
{
  "logs": {
    "properties": {
      "message": {
        "type": "text",
        "norms": false,
        "fields": {
          "keyword": {
            "type": "keyword"
          }
        }
      },
      "timestamp": {
        "type": "text",
        "norms": false,
        "fields": {
          "keyword": {
            "type": "keyword"
          }
        }
      },
      "clientip": {
        "type": "ip"
      },
      "city": {
        "type": "text",
        "norms": false,
        "fields": {
          "keyword": {
            "type": "keyword"
          }
        }
      },
      "usetime": {
        "type": "short"
      }
    },
    "createDate": {
      "type": "date"
    }
  }
}
因为需要聚合message字段(text类型)，需要执行
PUT searchlogs/_mapping/logs/
{
  "properties": {
    "message": { 
      "type":  "text",
      "norms": false,
      "fielddata": true
    }
  }
}


