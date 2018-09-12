package com.tiny.grocery.serial.avro;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.reflect.ReflectDatumWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * allow/not null
 */
public class ReflectAvroTest {
    public static void main(String[] args) throws IOException {
        EventPackage ep = new EventPackage();
        ReflectDatumWriter<EventPackage> serializer = new ReflectDatumWriter<>(EventPackage.class);
        // ReflectDatumWriter<EventPackage> serializer = new ReflectDatumWriter<>(EventPackage.class, ReflectData.AllowNull.get());
        FileOutputStream out = new FileOutputStream(Paths.get("D:/te.txt").toFile());
        BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null);
        serializer.write(ep, encoder);
    }

    static class EventPackage {
        private Long id = 0L;
        private String name;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
