package com.otakuma.migration.reader;

import com.otakuma.migration.model.AchatStock;
import com.otakuma.migration.model.AchatStockItem;
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
public class AchatStockReader {
    private final DataSource dataSource;

    public JdbcPagingItemReader<AchatStock> reader() throws Exception {
        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("a.achatStockID", Order.ASCENDING);

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("SELECT a.*");
        queryProvider.setFromClause("FROM achatstock a");
        queryProvider.setSortKeys(sortKeys);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        JdbcPagingItemReader<AchatStock> reader = new JdbcPagingItemReaderBuilder<AchatStock>()
                .name("achatStockReader")
                .dataSource(dataSource)
                .pageSize(100)
                .queryProvider(queryProvider)
                .rowMapper((rs, rowNum) -> {
                    AchatStock achatStock = new AchatStock();
                    achatStock.setAchatStockID(rs.getLong("achatStockID"));
                    achatStock.setCode(rs.getString("code"));
                    achatStock.setFournisseurID(rs.getLong("fournisseurID"));
                    achatStock.setDateInsertion(rs.getTimestamp("dateInsertion"));
                    achatStock.setType(rs.getString("type"));
                    achatStock.setDateCommande(rs.getTimestamp("dateCommande"));
                    achatStock.setDateLivraison(rs.getTimestamp("dateLivraison"));
                    achatStock.setQte(rs.getInt("qte"));
                    achatStock.setPrixTotal(rs.getBigDecimal("prixTotal"));
                    achatStock.setFraisSupplementaires(rs.getBigDecimal("fraisSupplementaires"));
                    achatStock.setDescription(rs.getString("description"));
                    achatStock.setAuthAdminID(rs.getLong("authAdminID"));
                    achatStock.setAssocie(rs.getBoolean("associe"));

                    // Fetch and set items
                    List<AchatStockItem> items = jdbcTemplate.query(
                        "SELECT * FROM achatstockitem WHERE achatStockID = ?",
                        new DataClassRowMapper<>(AchatStockItem.class),
                        achatStock.getAchatStockID()
                    );
                    achatStock.setItems(items);

                    return achatStock;
                })
                .build();
        
        reader.afterPropertiesSet();
        return reader;
    }
}