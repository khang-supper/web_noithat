package project.spring.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import project.spring.model.Product;

@Service
public final class ProductRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    class ProductRowMapper implements RowMapper<Product> {
        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            Product item = new Product();
            item.setId(rs.getInt(Product.ID));
            item.setName(rs.getString(Product.NAME));
            item.setCode(rs.getString(Product.CODE));
            item.setDescription(rs.getString(Product.DESCRIPTION));
            item.setContent(rs.getString(Product.CONTENT));
            item.setPrice(rs.getDouble(Product.PRICE));
            item.setStock(rs.getInt(Product.STOCK));
            item.setIsDelete(rs.getBoolean(Product.ISDELETE));
            item.setAccountId(rs.getInt(Product.ACCOUNTID));
            item.setPath(rs.getString(Product.PATH));
            item.setCategoryId(rs.getInt(Product.CATEGORYID));
            return item;
        }
    }

    public List<Product> findAll() {
        return jdbcTemplate.query("select * from products order by id asc", new ProductRowMapper());
    }

    @SuppressWarnings("deprecation")
    public List<Product> findAllByPage(int page, int pageSize) {
        int start = page * pageSize;
        return jdbcTemplate.query("SELECT * FROM products LIMIT ?, ? ", new Object[] { start, pageSize }, new ProductRowMapper());
    }

    public int getTotalPages(int pageSize) {
        int totalCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM products", Integer.class);
        return (int) Math.ceil((double) totalCount / pageSize);
    }

    public Product findById(int id) {
        return jdbcTemplate.queryForObject("select * from products where Id=?", new ProductRowMapper(), new Object[] { id });
    }

    public int deleteById(int id) {
        return jdbcTemplate.update("update products set IsDelete = true where Id = ?", new Object[] { id });
    }

    public int insert(Product newProduct) {
        return jdbcTemplate.update("insert into products (Name, Code,Description, Content, Price,Stock , Size,IsDelete,isProposal,Material ,AccountId,Path, CategoryId) values (?,?,?,?,?, ?, ?, ?, ?, ?, ?, ?,?)",
                new Object[] { newProduct.getName(), newProduct.getCode(),newProduct.getDescription(), newProduct.getContent(), newProduct.getPrice(),newProduct.getStock(),newProduct.getSize(),  false, true,newProduct.getMaterial(), newProduct.getAccountId(),newProduct.getPath(), newProduct.getCategoryId() });
    }

    public int update(Product upPro) {
        return jdbcTemplate.update("update products set Name = ?, Code = ?, Description = ? where Id = ?",
                new Object[] { upPro.getName(), upPro.getCode(), upPro.getDescription(), upPro.getId() });
    }
}
