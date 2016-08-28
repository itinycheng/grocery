package com.tiny.grocery.serial.protobuf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.junit.Before;
import org.junit.Test;

public class ProtoBufTest {
	
	private File file;
	
	@Before
	public void init() throws Exception {
		file = new File("src/test/resources/addressBook.pbd");
	}

	@Test
	public void serialize() throws Exception {
		AddressBookProtos.Person person = AddressBookProtos.Person.newBuilder()
			.setId(1)
			.setName("tiny")
			.setEmail("tiny@amc.com")
			.addPhone(AddressBookProtos.Person.PhoneNumber.newBuilder()
					.setNumber("123456")
					.setType(AddressBookProtos.Person.PhoneType.HOME))
			.build();
		
		AddressBookProtos.AddressBook.newBuilder().addPerson(person)
			.build()
			.writeTo(new FileOutputStream(file));
	}
	
	@Test
	public void deserialize() throws Exception {
		AddressBookProtos.AddressBook addressBook = AddressBookProtos.AddressBook.parseFrom(new FileInputStream(file));
		System.out.println(addressBook.toString());
			
	}
	
}
