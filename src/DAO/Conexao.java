package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Mesma conexão do projeto de estoque (mesmo banco).
 * Copie a SENHA do Conexao.java do projeto de estoque.
 */
public class Conexao {

    private static final String URL =
        "jdbc:mysql://localhost:3306/estoque_db_prog?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String SENHA = "Iod72261!";

    public static Connection getConexao() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
