package objects;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Categoria {
    
    @Id
    private Integer cod;
    private String descricao;
    private String ativo;

    public Categoria() {
    }

    public Categoria(Integer cod, String descricao, String ativo) {
        this.cod = cod;
        this.descricao = descricao;
        this.ativo = ativo;
    }

    public Integer getCod() { return cod; }
    public void setCod(Integer cod) { this.cod = cod; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getAtivo() { return ativo; }
    public void setAtivo(String ativo) { this.ativo = ativo; }

    @Override
    public String toString() {
        return "Categoria [Código=" + cod + ", Descrição=" + descricao + ", Ativo=" + ativo + "]";
    }
}