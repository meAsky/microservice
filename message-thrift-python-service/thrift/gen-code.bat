#生成py代码
thrift --gen py -out ../ message.thrift

#生成java代码
thrift --gen java -out ../../message-thrift-service-api/src/main/java message.thrift

# 查看端口是否被占用
netstat -aon|findstr "9090"