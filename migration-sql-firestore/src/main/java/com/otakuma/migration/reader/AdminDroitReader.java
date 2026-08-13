package com.otakuma.migration.reader;

import com.otakuma.migration.model.AdminDroit;
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
public class AdminDroitReader {

    private final DataSource dataSource;

    public JdbcPagingItemReader<AdminDroit> reader() throws Exception {
        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("adminDroitID", Order.ASCENDING);

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("SELECT *");
        queryProvider.setFromClause("FROM admin_droit");
        queryProvider.setSortKeys(sortKeys);

        JdbcPagingItemReader<AdminDroit> reader = new JdbcPagingItemReaderBuilder<AdminDroit>()
                .name("adminDroitReader")
                .dataSource(dataSource)
                .pageSize(100)
                .queryProvider(queryProvider)
                .rowMapper(new BeanPropertyRowMapper<>(AdminDroit.class))
                .build();
        
        reader.afterPropertiesSet();
        return reader;
    }
}