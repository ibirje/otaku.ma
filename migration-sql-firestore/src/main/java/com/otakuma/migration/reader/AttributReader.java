package com.otakuma.migration.reader;

import com.otakuma.migration.model.Attribut;
import com.otakuma.migration.model.OptionAttribut;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AttributReader {
    private final DataSource dataSource;

    public JdbcPagingItemReader<Attribut> reader() throws Exception {
        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("a.attributID", Order.ASCENDING);

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("SELECT a.*");
        queryProvider.setFromClause("FROM attribut a");
        queryProvider.setSortKeys(sortKeys);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        JdbcPagingItemReader<Attribut> reader = new JdbcPagingItemReaderBuilder<Attribut>()
                .name("attributReader")
                .dataSource(dataSource)
                .pageSize(100)
                .queryProvider(queryProvider)
                .rowMapper((rs, rowNum) -> {
                    Attribut attribut = new Attribut();
                    attribut.setAttributID(rs.getLong("attributID"));
                    attribut.setProduitID(rs.getLong("produitID"));
                    attribut.setCode(rs.getString("code"));
                    attribut.setNom(rs.getString("nom"));
                    attribut.setType(rs.getString("type"));
                    attribut.setDescription(rs.getString("description"));

                    // Fetch and set options
                    List<OptionAttribut> options = jdbcTemplate.query(
                        "SELECT * FROM optionattribut WHERE attributID = ? ORDER BY ordre",
                        new DataClassRowMapper<>(OptionAttribut.class),
                        attribut.getAttributID()
                    );
                    attribut.setOptions(options);

                    return attribut;
                })
                .build();
        
        reader.afterPropertiesSet();
        return reader;
    }
}