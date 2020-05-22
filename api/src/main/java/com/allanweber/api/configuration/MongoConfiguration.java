package com.allanweber.api.configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.NoOpDbRefResolver;

@Configuration
@RequiredArgsConstructor
public class MongoConfiguration extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.database:anyDb}")
    private String dataBaseName;

    @Value("${spring.data.mongodb.uri:}")
    private String mongoUriConnection;

    @Override
    protected String getDatabaseName() {
        return dataBaseName;
    }

    @Override
    public MongoClient mongoClient() {
        return MongoClients.create(mongoUriConnection);
    }

    @Override
    @Primary
    public MappingMongoConverter mappingMongoConverter() throws ClassNotFoundException {
        final MappingMongoConverter converter = new MappingMongoConverter(NoOpDbRefResolver.INSTANCE, mongoMappingContext());
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return converter;
    }

}
