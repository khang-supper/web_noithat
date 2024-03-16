package project.spring.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import project.spring.model.Category;

public final class CategoryRepository {
    private static CategoryRepository _instance = null;
    private JdbcTemplate db;

    public CategoryRepository() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(
                "jdbc:mysql://localhost:3306/project_sem4?verifyServerCertificate=false&useSSL=false&requireSSL=false");
        dataSource.setUsername("sem4");
        dataSource.setPassword("sem4");
        db = new JdbcTemplate(dataSource);
    }

    public static CategoryRepository Instance() {
        if (_instance == null) {
            _instance = new CategoryRepository();
        }
        return _instance;
    }

    class CategoryRowMapper implements RowMapper<Category> {
        @Override
        public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
            Category item = new Category();
            item.setId(rs.getInt(Category.ID));
            item.setName(rs.getString(Category.NAME));
            item.setImage(rs.getString(Category.IMAGE));
            item.setIsDelete(rs.getBoolean(Category.ISDELETE));
            return item;
        }
    }

    public List<Category> findAll() {
        return db.query("select * fron categories", new CategoryRowMapper());
    }

    public Category findById(int id) {
        return db.queryForObject("select * from categories where Id=?", new CategoryRowMapper(), new Object[] { id });
    }

    public int deleteById(int id) {
        return db.update("delete from categories where Id=?", new Object[] { id });
    }

    public int insert(Category newCategory) {
        return db.update("insert into categories (Name, Image, IsDelete)" + "value(?,?,?)",
                new Object[] { newCategory.getName(), newCategory.getImage(), newCategory.getIsDelete() });
    }

    public int update(Category upCategory) {
        return db.update("update categories" + " set Name = ?, set Image = ?, set IsDelete = ?" + "where Id = ?",
                new Object[] { upCategory.getName(), upCategory.getImage(), upCategory.getIsDelete(),
                        upCategory.getId() });
    }
}