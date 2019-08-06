package com.manaco.org.operator;

import com.manaco.org.model.Item;
import org.bson.types.Decimal128;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.math.BigDecimal;
import java.util.Arrays;

@SpringBootApplication
@EntityScan(basePackageClasses = Item.class)
@ComponentScan("com.manaco.org")
@EnableMongoRepositories("com.manaco.org.repositories")
@EnableRabbit
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Inject a CustomConversions bean to overwrite the default mapping
     * of BigDecimal.
     *
     * @return a new instance of CustomConversons
     */
    @Bean
    CustomConversions customConverions() {
        Converter<Decimal128, BigDecimal> decimal128ToBigDecimal = new Converter<Decimal128, BigDecimal>() {
            @Override
            public BigDecimal convert(Decimal128 s) {
                return s==null ? null : s.bigDecimalValue();
            }
        };

        Converter<BigDecimal, Decimal128> bigDecimalToDecimal128 = new Converter<BigDecimal, Decimal128>() {
            @Override
            public Decimal128 convert(BigDecimal s) {
                return s==null ? null : new Decimal128(s);
            }
        };

        return new CustomConversions(Arrays.asList(decimal128ToBigDecimal, bigDecimalToDecimal128));
    }
}
