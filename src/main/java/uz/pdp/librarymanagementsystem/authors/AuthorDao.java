package uz.pdp.librarymanagementsystem.authors;

import uz.pdp.librarymanagementsystem.db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuthorDao {


    public static List<Author> getAllAuthors() {


        try (Connection connection = DbConnection.getConnection();) {
            List<Author> authorList = new ArrayList<>();

            String sql = "select * from author";
           PreparedStatement preparedStatement = connection.prepareStatement(sql);
           ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String fullName = resultSet.getString("fullName");

                Author author = Author.builder()
                        .id(id)
                        .fullName(fullName)
                        .build();

                authorList.add(author);
            }

            return authorList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
