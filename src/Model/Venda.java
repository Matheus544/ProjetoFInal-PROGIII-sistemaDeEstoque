package Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Venda {

    private int id;                       // gerado pelo banco ao salvar
    private final LocalDateTime dataHora;
    private final List<ItemVenda> itens;

    public Venda(List<ItemVenda> itens, LocalDateTime dataHora) {
        this.itens = new ArrayList<>(itens);
        this.dataHora = dataHora;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public LocalDateTime getDataHora() { return dataHora; }
    public List<ItemVenda> getItens() { return itens; }

    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO.setScale(2);
        for (ItemVenda item : itens) {
            total = total.add(item.getSubtotal());
        }
        return total;
    }
}
