package com.otakuma.migration.reader;

import com.otakuma.migration.model.Product;
import com.otakuma.migration.model.Variation;
import com.otakuma.migration.model.Categorie;
import com.otakuma.migration.model.Theme;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class ProductReader {
    private static final Logger logger = LoggerFactory.getLogger(ProductReader.class);
    private final DataSource dataSource;
    private final AtomicInteger productCount = new AtomicInteger(0);

    public JdbcPagingItemReader<Product> reader() throws Exception {
        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("p.produitID", Order.ASCENDING);

        // Using subqueries for optimization
        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause(
            "SELECT p.*, " +
            "c.categorieID as cat_id, c.code as cat_code, c.nom as cat_nom, " +
            "t.themeID as theme_id, t.code as theme_code, t.nom as theme_nom"
        );
        queryProvider.setFromClause(
            "FROM produit p " +
            "LEFT JOIN categorie c ON p.categorieID = c.categorieID " +
            "LEFT JOIN theme t ON p.themeID = t.themeID"
        );
        queryProvider.setSortKeys(sortKeys);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        // First, let's count total products
        Integer totalProducts = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM produit", Integer.class);
        logger.info("Total products in database: {}", totalProducts);

        JdbcPagingItemReader<Product> reader = new JdbcPagingItemReaderBuilder<Product>()
                .name("productReader")
                .dataSource(dataSource)
                .pageSize(20)
                .queryProvider(queryProvider)
                .rowMapper((ResultSet rs, int rowNum) -> {
                    Product product = new Product();
                    
                    // Map product fields
                    product.setProduitID(rs.getLong("produitID"));
                    product.setCode(rs.getString("code"));
                    product.setNom(rs.getString("nom"));
                    product.setKeywords(rs.getString("keywords"));
                    product.setDescription(rs.getString("description"));
                    product.setShortDescription(rs.getString("shortDescription"));
                    product.setPrixUnite(rs.getBigDecimal("prixUnite"));
                    product.setPrixPromo(rs.getBigDecimal("prixPromo"));
                    product.setDateDebutPromo(rs.getString("dateDebutPromo"));
                    product.setDateFinPromo(rs.getString("dateFinPromo"));
                    product.setQte(rs.getInt("QTE"));
                    product.setPendingQte(rs.getInt("pendingQte"));
                    product.setLockedQte(rs.getInt("lockedQte"));
                    product.setThumbnail(rs.getString("thumbnail"));
                    product.setImage1(rs.getString("image1"));
                    product.setImage2(rs.getString("image2"));
                    product.setImage3(rs.getString("image3"));
                    product.setCategorieId(rs.getInt("categorieID"));
                    product.setThemeId(rs.getInt("themeID"));
                    product.setIsActive(rs.getBoolean("isActive"));
                    product.setHasVariations(rs.getBoolean("hasVariations"));
                    product.setExtra1(rs.getString("extra1"));
                    product.setExtra2(rs.getString("extra2"));
                    product.setExtra3(rs.getString("extra3"));

                    // Map category fields
                    Categorie categorie = new Categorie();
                    categorie.setCategorieID(rs.getLong("cat_id"));
                    categorie.setCode(rs.getString("cat_code"));
                    categorie.setNom(rs.getString("cat_nom"));
                    product.setCategorie(categorie);

                    // Map theme fields
                    Theme theme = new Theme();
                    theme.setThemeID(rs.getLong("theme_id"));
                    theme.setCode(rs.getString("theme_code"));
                    theme.setNom(rs.getString("theme_nom"));
                    product.setTheme(theme);

                    // Fetch variations in a separate query to avoid memory issues
                    List<Variation> variations = jdbcTemplate.query(
                        "SELECT * FROM variation WHERE produitID = ? ORDER BY variationID LIMIT 100",
                        new DataClassRowMapper<>(Variation.class),
                        product.getProduitID()
                    );
                    product.setVariations(variations);

                    int count = productCount.incrementAndGet();
                    if (count % 20 == 0) {
                        logger.info("Processed {} products out of {}", count, totalProducts);
                    }

                    return product;
                })
                .build();
        
        reader.afterPropertiesSet();
        return reader;
    }
}