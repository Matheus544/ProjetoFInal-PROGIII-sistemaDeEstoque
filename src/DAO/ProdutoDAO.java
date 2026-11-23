package DAO;

import Model.Produto;
import Util.Config;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProdutoDAO {

    // O nome da coluna de preço vem de Config (constante do programa, nunca do usuário).
    private static final String SELECT =
            "SELECT id, codBarra, nome, " + Config.COLUNA_PRECO_VENDA + " AS preco_venda, quantidade FROM produto ";

    /**
     * Procura primeiro pelo código de barras; se não achar e o texto for um número
     * curto (até 9 dígitos), procura pelo ID. Devolve null se não existir.
     */
    public Produto buscarPorCodigoOuId(String entrada) throws SQLException {
        Produto p = buscar(SELECT + "WHERE codBarra = ? ORDER BY id LIMIT 1", entrada);
        if (p == null && entrada.matches("\\d{1,9}")) {   // 9 dígitos cabem em int; código de barras (13) não
            p = buscar(SELECT + "WHERE id = ?", Integer.parseInt(entrada));
        }
        return p;
    }

    private Produto buscar(String sql, Object parametro) throws SQLException {
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setObject(1, parametro);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Produto(
                            rs.getInt("id"),
                            rs.getString("codBarra"),
                            rs.getString("nome"),
                            rs.getBigDecimal("preco_venda"),
                            rs.getInt("quantidade"));
                }
                return null;
            }
        }
    }
}
