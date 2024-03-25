package project.spring.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import project.spring.model.Product;

@Repository
@Transactional

public class ProductDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ProductDao (JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    public List<Product> list(){
        String sql ="SELECT * FROM PRODUCTS";
       List<Product>listproduct= jdbcTemplate.query(sql, 
                    BeanPropertyRowMapper.newInstance(Product.class));
        return listproduct;
    }
   public void save(Product products) {
    SimpleJdbcInsert insertActor = new SimpleJdbcInsert(jdbcTemplate);
    insertActor.withTableName("products").usingColumns("id","name", "code", "description", "content", "price", "stock","isDelete");
    BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(products);
     
    insertActor.execute(param);    
}

    public Product get(int id) {
        String sql = "SELECT * FROM PRODUCTS WHERE id = ?";
        return jdbcTemplate.query(sql, productExtractor, id);
    }

    public void update(Product products) {
        String sql = "UPDATE PRODUCTS SET name = ?, code = ?, description = ?,content=? WHERE id = ?";
        jdbcTemplate.update(sql, products.getName(), products.getCode(), products.getDescription(), products.getId());
    }

    public void delete(int id) {
        String sql = "DELETE FROM PRODUCTS WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // ResultSetExtractor để lấy một đối tượng Product từ ResultSet
    private ResultSetExtractor<Product> productExtractor = rs -> {
        if (rs.next()) {
            Product products = new Product();
            products.setId(rs.getInt("id"));
            products.setName(rs.getString("name"));
            products.setCode(rs.getString("code"));
            products.setDescription(rs.getString("description"));
            products.setContent(rs.getString("content"));
            return products;
        }
        return null;
    };
}
