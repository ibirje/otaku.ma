package com.otakuma.migration.reader;

import com.otakuma.migration.model.Admin;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AdminReader {

    private final DataSource dataSource;

    public JdbcPagingItemReader<Admin> reader() throws Exception {
        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("adminID", Order.ASCENDING);

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("SELECT *");
        queryProvider.setFromClause("FROM admin");
        queryProvider.setSortKeys(sortKeys);

        JdbcPagingItemReader<Admin> reader = new JdbcPagingItemReaderBuilder<Admin>()
                .name("adminReader")
                .dataSource(dataSource)
                .pageSize(100)
                .queryProvider(queryProvider)
                .rowMapper(new BeanPropertyRowMapper<>(Admin.class))
                .build();
        
        reader.afterPropertiesSet();
        return reader;
    }
}