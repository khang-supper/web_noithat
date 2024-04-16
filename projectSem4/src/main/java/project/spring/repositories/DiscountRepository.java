package project.spring.repositories;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import project.spring.model.Discount;
@Service
public class DiscountRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    class DiscountRowMapper implements RowMapper<Discount> {
        @Override
        public Discount mapRow(ResultSet rs, int rowNum) throws SQLException {
            Discount item = new Discount();
            item.setId(rs.getInt(Discount.ID));
            item.setName(rs.getString(Discount.NAME));
            item.setStartDate(rs.getDate(Discount.STARTDATE));
            item.setEndDate(rs.getDate(Discount.ENDDATE));
            item.setAccountId(rs.getInt(Discount.ACCOUNTID));
          
            
            return item;
        }
    }

    public List<Discount> findAll() {
        return jdbcTemplate.query("select * from discounts ", new DiscountRowMapper());
    }


    @SuppressWarnings("deprecation")
    public List<Discount> findAllByPage(int page, int pageSize) {
        int start = page * pageSize;
        return jdbcTemplate.query("SELECT * FROM discounts LIMIT ?, ? ", new Object[] { start, pageSize }, new DiscountRowMapper());
    }


    public int getTotalPages(int pageSize) {
        int totalCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM discounts", Integer.class);
        return (int) Math.ceil((double) totalCount / pageSize);
    }

    public Discount findById(int id) {
        return jdbcTemplate.queryForObject("select * from discounts where Id=?", new DiscountRowMapper(), new Object[] { id });
    }

    public int insert(Discount newDiscount) {
    return jdbcTemplate.update("INSERT INTO discounts (name, startDate, endDate, accountId) VALUES (?,?, ?, ?)",
            new Object[] { newDiscount.getName(), newDiscount.getStartDate(), newDiscount.getEndDate(), newDiscount.getAccountId()});
}

public int update(Discount upPro) {
    return jdbcTemplate.update("UPDATE discounts SET name = ?, startDate = ?, endDate = ?, accountId = ?, WHERE Id = ?",
            new Object[] { upPro.getName(), upPro.getStartDate(), upPro.getEndDate(), upPro.getAccountId(), upPro.getId() });
}

public int deleteById(int id) {
    return jdbcTemplate.update("UPDATE discounts SET name = 1  WHERE Id = ?", new Object[] { id });
}

}
