package DAO;

import Model.ItemVenda;
import Model.Venda;
import Util.Config;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class VendaDAO {

    private static final String SQL_VENDA =
            "INSERT INTO venda (data_hora, total) VALUES (?, ?)";

    private static final String SQL_ITEM =
            "INSERT INTO item_venda (id_venda, id_produto, nome_produto, quantidade, valor_unitario, subtotal) "
            + "VALUES (?, ?, ?, ?, ?, ?)";

    // baixa atômica: só desconta se ainda houver estoque suficiente
    private static final String SQL_ESTOQUE =
            "UPDATE produto SET quantidade = quantidade - ? WHERE id = ? AND quantidade >= ?";

    /**
     * Grava a venda, os itens e (se Config.CONTROLAR_ESTOQUE) a baixa do estoque
     * em UMA transação: ou grava tudo, ou não grava nada.
     * Ao terminar, o id gerado fica em venda.getId().
     */
    public void salvar(Venda venda) throws SQLException {
        try (Connection con = Conexao.getConexao()) {
            con.setAutoCommit(false);
            try {
                int idVenda = inserirVenda(con, venda);

                // ordem fixa por produto: evita travamento entre duas vendas simultâneas
                List<ItemVenda> itens = new ArrayList<>(venda.getItens());
                itens.sort(Comparator.comparingInt(i -> i.getProduto().getId()));

                try (PreparedStatement psItem = con.prepareStatement(SQL_ITEM);
                     PreparedStatement psEstoque = con.prepareStatement(SQL_ESTOQUE)) {

                    for (ItemVenda item : itens) {
                        if (Config.CONTROLAR_ESTOQUE) {
                            psEstoque.setInt(1, item.getQuantidade());
                            psEstoque.setInt(2, item.getProduto().getId());
                            psEstoque.setInt(3, item.getQuantidade());
                            if (psEstoque.executeUpdate() == 0) {
                                throw new SQLException("Estoque insuficiente para \""
                                        + item.getProduto().getNome() + "\".");
                            }
                        }

                        psItem.setInt(1, idVenda);
                        psItem.setInt(2, item.getProduto().getId());
                        psItem.setString(3, item.getProduto().getNome());
                        psItem.setInt(4, item.getQuantidade());
                        psItem.setBigDecimal(5, item.getValorUnitario());
                        psItem.setBigDecimal(6, item.getSubtotal());
                        psItem.executeUpdate();
                    }
                }

                con.commit();
                venda.setId(idVenda);

            } catch (SQLException | RuntimeException e) {
                con.rollback();   // desfaz tudo
                throw e;
            }
        }
    }

    private int inserirVenda(Connection con, Venda venda) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(SQL_VENDA, Statement.RETURN_GENERATED_KEYS)) {
            ps.setObject(1, venda.getDataHora());   // LocalDateTime: sem conversão de fuso
            ps.setBigDecimal(2, venda.getTotal());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }
}
