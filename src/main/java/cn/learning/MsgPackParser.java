package cn.learning;

import org.msgpack.MessagePack;

/**
 * @author liaorui
 */
public class MsgPackParser implements Parser {

    @Override
    public String parse(byte[] data) throws Exception {
        MessagePack msgpack = new MessagePack();
        return msgpack.read(data).toString();
    }
}
