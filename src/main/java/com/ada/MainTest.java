package com.ada;

import com.ada.model.ClienteRecord;

import java.util.UUID;

public class MainTest {

    public static void main(String[] args) {
        ClienteRecord clienteRecord = new ClienteRecord(UUID.randomUUID(), "Vini", "1234");
        System.out.println(clienteRecord);
        System.out.println(clienteRecord.nomeMaiusculo());
    }
}
