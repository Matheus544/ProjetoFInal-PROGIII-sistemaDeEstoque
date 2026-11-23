/**package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TesteConexao {
    public static void main(String[] args) throws Exception {
        try (Connection con = Conexao.getConexao();
             Statement st = con.createStatement()) {

            try (ResultSet rs = st.executeQuery("SELECT DATABASE(), @@hostname, @@port, VERSION()")) {
                rs.next();
                System.out.println("Banco:   " + rs.getString(1));
                System.out.println("Host:    " + rs.getString(2));
                System.out.println("Porta:   " + rs.getInt(3));
                System.out.println("Versão:  " + rs.getString(4));
            }

            System.out.println("--- colunas de produto vistas pelo Java ---");
            try (ResultSet rs = st.executeQuery("SHOW COLUMNS FROM produto")) {
                while (rs.next()) {
                    System.out.println("[" + rs.getString("Field") + "]");
                }
            }
        }
    }
} */