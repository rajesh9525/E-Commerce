package E_commers;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class SchemaFixer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        try {
            jdbcTemplate.execute("ALTER TABLE cart DROP COLUMN quantity");
            System.out.println("Successfully dropped stale 'quantity' column from 'cart' table.");
        } catch (Exception e) {
            System.out.println("Could not drop 'quantity' column from 'cart' table (it may have already been dropped): " + e.getMessage());
        }
    }
}
