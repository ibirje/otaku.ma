package com.otakuma.migration.reader;

import com.otakuma.migration.model.Categorie;
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
public class CategorieReader {

    private final DataSource dataSource;

    public JdbcPagingItemReader<Categorie> reader() throws Exception {
        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("categorieID", Order.ASCENDING);

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("SELECT *");
        queryProvider.setFromClause("FROM categorie");
        queryProvider.setSortKeys(sortKeys);

        JdbcPagingItemReader<Categorie> reader = new JdbcPagingItemReaderBuilder<Categorie>()
                .name("categorieReader")
                .dataSource(dataSource)
                .pageSize(100)
                .queryProvider(queryProvider)
                .rowMapper(new BeanPropertyRowMapper<>(Categorie.class))
                .build();
        
        reader.afterPropertiesSet();
        return reader;
    }
}