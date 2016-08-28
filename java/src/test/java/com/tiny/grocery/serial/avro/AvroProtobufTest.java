package com.tiny.grocery.serial.avro;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.tiny.grocery.serial.protobuf.AddressBookProtos;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.protobuf.ProtobufDatumReader;
import org.apache.avro.protobuf.ProtobufDatumWriter;
import org.junit.Before;
import org.junit.Test;

public class AvroProtobufTest {

    private File protoFile;

    private File avroFile;

    @Before
    public void init() throws Exception {
        protoFile = new File("src/test/resources/addressBook.pbd");
        avroFile = new File("src/test/resources/pbd2avsc.avsc");
    }

    @Test
    public void readFromProtobuf() throws Exception {
        AddressBookProtos.AddressBook addrBook = AddressBookProtos.AddressBook.parseFrom(new FileInputStream(protoFile));
        ProtobufDatumWriter<AddressBookProtos.AddressBook> writer = new ProtobufDatumWriter<AddressBookProtos.AddressBook>(AddressBookProtos.AddressBook.class);
        FileOutputStream out = new FileOutputStream(avroFile);
        BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null);
        writer.write(addrBook, encoder);
        encoder.flush();
    }

    @SuppressWarnings("resource")
    @Test
    public void deserialize() throws IOException {
        FileInputStream in = new FileInputStream(avroFile);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        AddressBookProtos.AddressBook addrBook = new ProtobufDatumReader<AddressBookProtos.AddressBook>(AddressBookProtos.AddressBook.class)
                .read(null, DecoderFactory.get().binaryDecoder(out.toByteArray(), null));
        System.out.println(addrBook.toString());
    }
}
