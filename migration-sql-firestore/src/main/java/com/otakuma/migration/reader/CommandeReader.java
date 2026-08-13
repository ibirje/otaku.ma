package com.otakuma.migration.reader;

import com.otakuma.migration.model.Commande;
import com.otakuma.migration.model.CommandeItem;
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
public class CommandeReader {
    private final DataSource dataSource;

    public JdbcPagingItemReader<Commande> reader() throws Exception {
        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("c.commandeID", Order.ASCENDING);

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("SELECT c.*");
        queryProvider.setFromClause("FROM commande c");
        queryProvider.setSortKeys(sortKeys);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        JdbcPagingItemReader<Commande> reader = new JdbcPagingItemReaderBuilder<Commande>()
                .name("commandeReader")
                .dataSource(dataSource)
                .pageSize(100)
                .queryProvider(queryProvider)
                .rowMapper((rs, rowNum) -> {
                    Commande commande = new Commande();
                    commande.setCommandeID(rs.getLong("commandeID"));
                    commande.setClientID(rs.getLong("clientID"));
                    commande.setItemCount(rs.getInt("itemCount"));
                    commande.setCode(rs.getString("code"));
                    commande.setEtat(rs.getString("etat"));
                    commande.setPrixPieces(rs.getBigDecimal("prixPieces"));
                    commande.setPrixLivraison(rs.getBigDecimal("prixLivraison"));
                    commande.setCoutLivraison(rs.getBigDecimal("coutLivraison"));
                    commande.setDateCommande(rs.getTimestamp("dateCommande"));
                    commande.setDateAccepte(rs.getTimestamp("dateAccepte"));
                    commande.setDatePrepare(rs.getTimestamp("datePrepare"));
                    commande.setDateEnvoi(rs.getTimestamp("dateEnvoi"));
                    commande.setDateFin(rs.getTimestamp("dateFin"));
                    commande.setPaye(rs.getBoolean("paye"));
                    commande.setNotes(rs.getString("notes"));
                    commande.setPrenom(rs.getString("prenom"));
                    commande.setNom(rs.getString("nom"));
                    commande.setAdresse1(rs.getString("adresse1"));
                    commande.setAdresse2(rs.getString("adresse2"));
                    commande.setVille(rs.getString("ville"));
                    commande.setTelephone1(rs.getString("telephone1"));
                    commande.setCodePostal(rs.getString("codePostal"));
                    commande.setLivraison(rs.getString("livraison"));

                    // Fetch and set items
                    List<CommandeItem> items = jdbcTemplate.query(
                        "SELECT * FROM commandeitem WHERE commandeID = ?",
                        new DataClassRowMapper<>(CommandeItem.class),
                        commande.getCommandeID()
                    );
                    commande.setItems(items);

                    return commande;
                })
                .build();
        
        reader.afterPropertiesSet();
        return reader;
    }
}