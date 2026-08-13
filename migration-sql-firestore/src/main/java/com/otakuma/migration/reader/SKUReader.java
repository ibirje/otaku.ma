package com.otakuma.migration.reader;

import com.otakuma.migration.model.SKU;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SKUReader {
    private final DataSource dataSource;

    public JdbcPagingItemReader<SKU> reader() throws Exception {
        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("s.skuID", Order.ASCENDING);

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("SELECT s.*");
        queryProvider.setFromClause("FROM sku s");
        queryProvider.setSortKeys(sortKeys);

        JdbcPagingItemReader<SKU> reader = new JdbcPagingItemReaderBuilder<SKU>()
                .name("skuReader")
                .dataSource(dataSource)
                .pageSize(100)
                .queryProvider(queryProvider)
                .rowMapper((rs, rowNum) -> {
                    SKU sku = new SKU();
                    sku.setSkuID(rs.getLong("skuID"));
                    sku.setVariationID(rs.getLong("variationID"));
                    sku.setAchatStockItemID(rs.getLong("achatStockItemID"));
                    sku.setDescription(rs.getString("description"));
                    sku.setQte(rs.getInt("QTE"));
                    sku.setIsActive(rs.getBoolean("isActive"));
                    sku.setCode(rs.getString("code"));
                    sku.setDateInsertion(rs.getTimestamp("dateInsertion"));
                    sku.setMotif(rs.getString("motif"));
                    sku.setAuthAdminID(rs.getLong("authAdminID"));
                    return sku;
                })
                .build();
        
        reader.afterPropertiesSet();
        return reader;
    }
}