/*
 * Copyright 2014 Danilo Souza Almeida.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.edu.ifnmg.ifad.entity;

import br.edu.ifnmg.ifad.util.Assert;
import br.edu.ifnmg.ifad.util.exception.BusinessException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Danilo Souza Almeida
 */
@Entity
@Table(name = "questao")
@NamedQueries({
    @NamedQuery(name = "Questao.findAll", query = "SELECT q FROM Questao q")})
public class Questao extends EntityManageable {
    
    public enum TipoQuestao {
        AVALIACAO1A5,
        TEXTO;
    }
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 400)
    @Column(name = "descricao")
    private String descricao;
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoQuestao tipo_ = TipoQuestao.AVALIACAO1A5;
    @Column(name = "opcoes")
    private String opcoes;
    @Size(max = 200)
    @Column(name = "dica")
    private String dica;
    @JoinColumn(name = "questionario_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Questionario questionario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "questao")
    private List<Respostas> alunoHasQuestaoList;

    public Questao() {
    }

    public Questao(Integer id) {
        this.id = id;
    }

    public Questao(Integer id, String descricao, TipoQuestao tipo_, String opcoes) {
        this.id = id;
        this.descricao = descricao;
        this.tipo_ = tipo_;
        this.opcoes = opcoes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoQuestao getTipo_() {
        return tipo_;
    }

    public void setTipo_(TipoQuestao tipo) {
        this.tipo_ = tipo;
    }

    public String getOpcoes() {
        return opcoes;
    }

    public void setOpcoes(String opcoes) {
        this.opcoes = opcoes;
    }

    public String getDica() {
        return dica;
    }

    public void setDica(String dica) {
        this.dica = dica;
    }

    public Questionario getQuestionario() {
        return questionario;
    }

    public void setQuestionario(Questionario questionario) {
        this.questionario = questionario;
    }

    public List<Respostas> getAlunoHasQuestaoList() {
        return alunoHasQuestaoList;
    }

    public void setAlunoHasQuestaoList(List<Respostas> alunoHasQuestaoList) {
        this.alunoHasQuestaoList = alunoHasQuestaoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Questao)) {
            return false;
        }
        Questao other = (Questao) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.ifnmg.ifad.entity.Questao[ id=" + id + " ]";
    }

    @Override
    public void save() throws BusinessException {
        Questao questao = this;
        questao = (Questao) getSession().merge(questao);
        id = questao.id;
    }

    @Override
    public void remove() throws BusinessException {
        Questao questao = (Questao) getSession().get(Questao.class, id);
        getSession().delete(questao);
    }

    @Override
    public void valid() throws BusinessException {
        if(Assert.isStringNullOrEmpty(descricao)){
            throw new BusinessException("Por favor informe a Descrição.");
        }
        if(Assert.isNull(tipo_)){
            throw new BusinessException("Por favor informe o Tipo.");
        }
    }
    
}
