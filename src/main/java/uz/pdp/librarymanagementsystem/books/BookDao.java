package uz.pdp.librarymanagementsystem.books;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import uz.pdp.librarymanagementsystem.authors.Author;
import uz.pdp.librarymanagementsystem.category.Category;
import uz.pdp.librarymanagementsystem.db.DbConnection;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BookDao {


    static Author author1 = new Author(1L, "Tohir Malik");
    static Author author2 = new Author(2L, "Author2");
    static Author author3 = new Author(3L, "Author3");

    static Category category1 = new Category(1L, "cat1");
    static Category category2 = new Category(2L, "cat2");
//    static List<Book> bookList = Arrays.asList(
//            new Book(1L,
//                    "Book1",
//                    "fasfasfsdf",
//                    author1,
//                    category1,
//                    "4134-32423",
//                    1998,
//                    "images/shaytanat.png")
    //   );

    public static List<Book> getAllBooks(int size, int page) {
        try {
            ArrayList<Book> bookList = new ArrayList<>();

//          1. CONNECTION OCHAMIZa
            Connection connection = DbConnection.getConnection();

//        2. GET PREPARED STATEMENT

            String sql = "select book.id,\n" +
                    "       book.title,\n" +
                    "       book.\"imgUrl\",\n" +
                    "       json_agg(\n" +
                    "               json_build_object(\n" +
                    "                       'id', a.id,\n" +
                    "                       'fullName', a.fullname)) as authors,\n" +
                    "    json_build_object('id', c.id, 'name', c.name) as category\n" +
                    "--        c.id                                     as categoryId,\n" +
                    "--        c.name                                   as categoryName\n" +
                    "from book\n" +
                    "         join book_authors ba on book.id = ba.book_id\n" +
                    "         join author a on a.id = ba.author_id\n" +
                    "         join category c on c.id = book.category_id\n" +
                    "group by book.id, c.id, c.name, book.title\n" +
                    "limit ? offset ? * (? - 1)";


            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, size);
            preparedStatement.setInt(2, page);
            preparedStatement.setInt(3, page);


//            3. GET RESULTSET

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                long bookId = resultSet.getLong("id");
                String title = resultSet.getString("title");
                Array array = resultSet.getArray("authors");
                Object categoryObj = resultSet.getObject("category");
                String imgUrl = resultSet.getString("imgUrl");
                Type listType = new TypeToken<Set<Author>>() {
                }.getType();
                Set<Author> list = new Gson().fromJson(array.toString(), listType);

                Category category = new Gson().fromJson(categoryObj.toString(), Category.class);


                Book book = Book.builder()
                        .id(bookId)
                        .title(title)
                        .authors(list)
                        .category(category)
                        .imgUrl(imgUrl)
                        .build();
                bookList.add(book);
            }
            return bookList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean addNewBook(Book book) {

        try {

            Connection connection = DbConnection.getConnection();

            String insertBook = "insert into book (title, description, category_id, \"imgUrl\") VALUES " +
                    "(?, ?, ?, ?)";
            // TODO: 03/08/22 add isbn, year

            PreparedStatement preparedStatement = connection.prepareStatement(insertBook);

            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getDescription());
            preparedStatement.setLong(3, book.getCategoryId());
            preparedStatement.setString(4, book.getImgUrl());


            String insertBooksAuthors = "insert into book_authors VALUES ((select currval('books_id_seq')), ?)";
            PreparedStatement preparedStatement2 = connection.prepareStatement(insertBooksAuthors);

            int executeUpdate1 = preparedStatement.executeUpdate();
            int executeUpdate2 = 0;
            for (Long authorId : book.getAuthorsIds()) {
                preparedStatement2.setLong(1, authorId);
                executeUpdate2 = preparedStatement2.executeUpdate();
            }
            return executeUpdate1 == 1 && executeUpdate2 == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
