package br.com.zup.serverest.factory;

import com.github.javafaker.Faker;

import java.util.UUID;

public class SimulationDataFactory {

    private final Faker faker;

    public SimulationDataFactory() {
        faker = new Faker();
    }

    public String generateId() {
        return UUID.randomUUID().toString();
    }

    public String genereteName() {
        return faker.name().firstName();
    }

    public String generetePassword() {
        return UUID.randomUUID().toString();
    }

    public String genereteEmail() {
        return faker.internet().emailAddress();
    }

    public String genereteNameProduct() {
        return faker.commerce().productName();
    }

    public Integer generetePriceProduct() {
        return faker.number().randomDigitNotZero();
    }

    public String genereteDescriptionProduct() {
        return faker.lorem().sentence();
    }
}
