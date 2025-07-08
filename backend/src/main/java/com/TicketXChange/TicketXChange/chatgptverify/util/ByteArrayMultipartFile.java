package com.TicketXChange.TicketXChange.chatgptverify.util;


import org.springframework.core.io.ByteArrayResource;

public class ByteArrayMultipartFile extends ByteArrayResource {

    private final String name;

    public ByteArrayMultipartFile(byte[] byteArray, String name) {
        super(byteArray);
        this.name = name;
    }

    @Override
    public String getFilename() {
        return this.name;
    }
}
