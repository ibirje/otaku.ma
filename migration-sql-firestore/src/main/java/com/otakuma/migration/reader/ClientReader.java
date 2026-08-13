package com.otakuma.migration.reader;

import com.otakuma.migration.model.Client;
import com.otakuma.migration.model.ClientAddress;
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
public class ClientReader {
    private final DataSource dataSource;

    public JdbcPagingItemReader<Client> reader() throws Exception {
        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("c.clientID", Order.ASCENDING);

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("SELECT c.*");
        queryProvider.setFromClause("FROM client c");
        queryProvider.setSortKeys(sortKeys);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        JdbcPagingItemReader<Client> reader = new JdbcPagingItemReaderBuilder<Client>()
                .name("clientReader")
                .dataSource(dataSource)
                .pageSize(100)
                .queryProvider(queryProvider)
                .rowMapper((rs, rowNum) -> {
                    Client client = new Client();
                    client.setClientID(rs.getLong("clientID"));
                    client.setNom(rs.getString("nom"));
                    client.setPrenom(rs.getString("prenom"));
                    client.setEmail(rs.getString("email"));
                    client.setPseudo(rs.getString("pseudo"));
                    client.setTelephone1(rs.getString("telephone1"));
                    client.setTelephone2(rs.getString("telephone2"));
                    client.setDateNaissance(rs.getDate("dateNaissance"));
                    client.setNotes(rs.getString("notes"));
                    client.setIsActive(rs.getBoolean("isActive"));
                    client.setEtat(rs.getString("etat"));
                    client.setPanierCount(rs.getInt("panierCount"));
                    client.setExtra(rs.getString("extra"));
                    client.setDateCreation(rs.getTimestamp("dateCreation"));
                    client.setToken(rs.getString("token"));

                    // Fetch and set addresses
                    List<ClientAddress> addresses = jdbcTemplate.query(
                        "SELECT * FROM client_adresse WHERE clientID = ?",
                        new DataClassRowMapper<>(ClientAddress.class),
                        client.getClientID()
                    );
                    client.setAddresses(addresses);
                    
                    return client;
                })
                .build();
        
        reader.afterPropertiesSet();
        return reader;
    }
}