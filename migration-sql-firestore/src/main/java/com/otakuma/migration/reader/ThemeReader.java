package com.otakuma.migration.reader;

import com.otakuma.migration.model.Theme;
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
public class ThemeReader {

    private final DataSource dataSource;

    public JdbcPagingItemReader<Theme> reader() throws Exception {
        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("themeID", Order.ASCENDING);

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("SELECT *");
        queryProvider.setFromClause("FROM theme");
        queryProvider.setSortKeys(sortKeys);

        JdbcPagingItemReader<Theme> reader = new JdbcPagingItemReaderBuilder<Theme>()
                .name("themeReader")
                .dataSource(dataSource)
                .pageSize(100)
                .queryProvider(queryProvider)
                .rowMapper(new BeanPropertyRowMapper<>(Theme.class))
                .build();
        
        reader.afterPropertiesSet();
        return reader;
    }
}