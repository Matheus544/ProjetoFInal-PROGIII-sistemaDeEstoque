package DAO;

import Model.Produto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ProdutoDAO {

    public void inserir(Produto p) throws SQLException {
        String sql = "INSERT INTO produto "
                + "(codBarra, nome, preco, valor, fornecedor, marca, quantidade) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getCodBarra());
            ps.setString(2, p.getNome());
            ps.setBigDecimal(3, p.getPreco());
            ps.setBigDecimal(4, p.getValor());
            ps.setString(5, p.getFornecedor());
            ps.setString(6, p.getMarca());
            ps.setInt(7, p.getQuantidade());

            ps.executeUpdate();

            // recupera o ID gerado pelo AUTO_INCREMENT
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<Produto> listar() throws SQLException {
        String sql = "SELECT id, codBarra, nome, preco, valor, fornecedor, marca, quantidade "
                   + "FROM produto ORDER BY nome";
        List<Produto> lista = new ArrayList<>();

        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Produto p = new Produto(
                        rs.getString("codBarra"),
                        rs.getString("nome"),
                        rs.getBigDecimal("preco"),
                        rs.getBigDecimal("valor"),
                        rs.getString("fornecedor"),
                        rs.getString("marca"),
                        rs.getInt("quantidade"));
                p.setId(rs.getInt("id"));
                lista.add(p);
            }
        }
        return lista;
    }

    
    public boolean atualizar(Produto p) throws SQLException {
        String sql = "UPDATE produto SET codBarra = ?, nome = ?, preco = ?, valor = ?, "
                + "fornecedor = ?, marca = ?, quantidade = ? WHERE id = ?";

        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getCodBarra());
            ps.setString(2, p.getNome());
            ps.setBigDecimal(3, p.getPreco());
            ps.setBigDecimal(4, p.getValor());
            ps.setString(5, p.getFornecedor());
            ps.setString(6, p.getMarca());
            ps.setInt(7, p.getQuantidade());
            ps.setInt(8, p.getId());

            return ps.executeUpdate() > 0; 
        }
    }

    /** Exclui o produto pelo id. */
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM produto WHERE id = ?";

        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

}
