
## kafka console consumer tools
可以消费msgpack和protobuf格式的数据，以json格式打印出来

### 安装
```
mvn clean package -Dmaven.test.skip=true
cp target/msgpack2json-1.0-SNAPSHOT.jar ${KAFKA_HOME}/libs
cp msgpack2json.sh ${KAFKA_HOME}/bin
```

### 消费msgpack数据
```
${KAFKA_HOME}/bin/msgpack2json.sh --bootstrap-server localhost:9092 --topic test
```

### 消费protobuf数据
```
${KAFKA_HOME}/bin/msgpack2json.sh --bootstrap-server localhost:9092 --topic test --proto log_message.proto

```