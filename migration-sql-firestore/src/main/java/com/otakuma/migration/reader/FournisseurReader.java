package com.otakuma.migration.reader;

import com.otakuma.migration.model.Fournisseur;
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
public class FournisseurReader {
    private final DataSource dataSource;

    public JdbcPagingItemReader<Fournisseur> reader() throws Exception {
        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("f.fournisseurID", Order.ASCENDING);

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("SELECT f.*");
        queryProvider.setFromClause("FROM fournisseur f");
        queryProvider.setSortKeys(sortKeys);

        JdbcPagingItemReader<Fournisseur> reader = new JdbcPagingItemReaderBuilder<Fournisseur>()
                .name("fournisseurReader")
                .dataSource(dataSource)
                .pageSize(100)
                .queryProvider(queryProvider)
                .rowMapper((rs, rowNum) -> {
                    Fournisseur fournisseur = new Fournisseur();
                    fournisseur.setFournisseurID(rs.getLong("fournisseurID"));
                    fournisseur.setCode(rs.getString("code"));
                    fournisseur.setTitre(rs.getString("titre"));
                    fournisseur.setNom(rs.getString("nom"));
                    fournisseur.setCategorie1(rs.getLong("categorie1"));
                    fournisseur.setCategorie2(rs.getLong("categorie2"));
                    fournisseur.setCategorie3(rs.getLong("categorie3"));
                    fournisseur.setService(rs.getString("service"));
                    fournisseur.setDescription(rs.getString("description"));
                    fournisseur.setTelephone(rs.getString("telephone"));
                    fournisseur.setWatsapp(rs.getString("watsapp"));
                    fournisseur.setAdresse(rs.getString("adresse"));
                    fournisseur.setSite(rs.getString("site"));
                    fournisseur.setHasLivraison(rs.getBoolean("hasLivraison"));
                    fournisseur.setEmail(rs.getString("email"));
                    fournisseur.setPrix(rs.getBigDecimal("prix"));
                    fournisseur.setMOQ(rs.getInt("MOQ"));
                    fournisseur.setMaxOQ(rs.getInt("maxOQ"));
                    return fournisseur;
                })
                .build();
        
        reader.afterPropertiesSet();
        return reader;
    }
}