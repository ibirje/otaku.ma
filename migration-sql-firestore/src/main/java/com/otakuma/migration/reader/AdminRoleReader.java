package com.otakuma.migration.reader;

import com.otakuma.migration.model.AdminRole;
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
public class AdminRoleReader {

    private final DataSource dataSource;

    public JdbcPagingItemReader<AdminRole> reader() throws Exception {
        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("adminRoleID", Order.ASCENDING);

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("SELECT *");
        queryProvider.setFromClause("FROM admin_role");
        queryProvider.setSortKeys(sortKeys);

        JdbcPagingItemReader<AdminRole> reader = new JdbcPagingItemReaderBuilder<AdminRole>()
                .name("adminRoleReader")
                .dataSource(dataSource)
                .pageSize(100)
                .queryProvider(queryProvider)
                .rowMapper(new BeanPropertyRowMapper<>(AdminRole.class))
                .build();
        
        reader.afterPropertiesSet();
        return reader;
    }
}