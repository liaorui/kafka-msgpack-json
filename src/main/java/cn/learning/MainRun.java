package cn.learning;

import java.io.BufferedInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainRun {

    public static void main(String[] args) throws Exception {
        // System.out.println(Arrays.deepToString(args));
        String protoDescFile = null;
        if (args.length > 0) {
            protoDescFile = args[0];
        }

        Parser parser = new MsgPackParser();
        if (protoDescFile != null) {
            byte[] protoDescBuf = Files.readAllBytes(Paths.get(protoDescFile));
            // System.out.println(protoDescFile + ", " + protoDescBuf.length);
            parser = new ProtobufParser(protoDescBuf);
        }

        BufferedInputStream bis = new BufferedInputStream(System.in);
        byte[] buffer = new byte[4096];
        int length;
        while ((length = bis.read(buffer)) != -1) {
            if (length <= 1) {
                continue;
            }
            byte[] data = new byte[length - 1];
            System.arraycopy(buffer, 0, data, 0, length - 1);
            try {
                // System.out.println(data.length);
                System.out.println(parser.parse(data));
            } catch (Exception e) {
                System.out.println(data.length);
                System.err.println(e.getMessage());
            }
        }
    }
}
