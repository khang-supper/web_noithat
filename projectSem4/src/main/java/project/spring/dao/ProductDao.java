package project.spring.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
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
        String sql ="SELECT * FROM PRODUCT";
       List<Product>listproduct= jdbcTemplate.query(sql, 
                    BeanPropertyRowMapper.newInstance(Product.class));
        return listproduct;
    }
   public void save(Product product) {
    SimpleJdbcInsert insertActor = new SimpleJdbcInsert(jdbcTemplate);
    insertActor.withTableName("product").usingColumns("id","name", "code", "description");
    BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(product);
     
    insertActor.execute(param);    
}

    public Product get (int id ){
        return null;

    }
    public void update (Product product){

    }
    public void delate(int id){

    }
}
