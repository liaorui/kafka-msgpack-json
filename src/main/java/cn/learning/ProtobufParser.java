package cn.learning;

import com.github.os72.protobuf.dynamic.DynamicSchema;
import com.google.protobuf.Descriptors;
import com.google.protobuf.DynamicMessage;
import com.googlecode.protobuf.format.JsonFormat;

import java.util.Set;

/**
 * @author liaorui
 */
public class ProtobufParser implements Parser {

    private JsonFormat jsonFormat = new JsonFormat();
    private Descriptors.Descriptor descriptor;

    public ProtobufParser(byte[] schemaDescBuf) {
        try {
            DynamicSchema schema = DynamicSchema.parseFrom(schemaDescBuf);
            Set<String> set = schema.getMessageTypes();
            descriptor = schema.getMessageDescriptor(set.iterator().next());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public String parse(byte[] data) throws Exception {
        DynamicMessage message = DynamicMessage.parseFrom(descriptor, data);
        return jsonFormat.printToString(message);
    }
}
