/*
 * Copyright 2017 danilo.
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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author danilo
 */
@Entity
@Table(name = "tipos_respostas")
@NamedQueries({
    @NamedQuery(name = "TiposRespostas.findAll", query = "SELECT t FROM TiposRespostas t")})
public class TiposRespostas extends EntityManageable implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nome")
    private String nome;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "tipo_resposta")
    private String tipoResposta;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tiposRespostas")
    private List<TiposRespostasItem> tiposRespostasItemList;

    public TiposRespostas() {
    }

    public TiposRespostas(Integer id) {
        this.id = id;
    }

    public TiposRespostas(Integer id, String nome, String tipoResposta) {
        this.id = id;
        this.nome = nome;
        this.tipoResposta = tipoResposta;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipoResposta() {
        return tipoResposta;
    }

    public void setTipoResposta(String tipoResposta) {
        this.tipoResposta = tipoResposta;
    }

    public List<TiposRespostasItem> getTiposRespostasItemList() {
        return tiposRespostasItemList;
    }

    public void setTiposRespostasItemList(List<TiposRespostasItem> tiposRespostasItemList) {
        this.tiposRespostasItemList = tiposRespostasItemList;
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
        if (!(object instanceof TiposRespostas)) {
            return false;
        }
        TiposRespostas other = (TiposRespostas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.ifnmg.ifad.entity.TiposRespostas[ id=" + id + " ]";
    }

@Override
    public void save() throws BusinessException {
        TiposRespostas tipo = this;
        tipo = (TiposRespostas) getSession().merge(tipo);
        id = tipo.id;
    }

    @Override
    public void remove() throws BusinessException {
        TiposRespostas tipo = (TiposRespostas) getSession().get(TiposRespostas.class, id);
        if(tipo.tiposRespostasItemList != null){
            for (TiposRespostasItem tiposRespostasItem : tipo.tiposRespostasItemList) {
                getSession().delete(tiposRespostasItem);
            }
        }
        getSession().delete(tipo);
    }

    @Override
    public void valid() throws BusinessException {
        if(Assert.isStringNullOrEmpty(nome)){
            throw new BusinessException("Por favor informe a Descrição.");
        }
        if(Assert.isStringNullOrEmpty(tipoResposta)){
            throw new BusinessException("Por favor informe o Tipo.");
        }
    }

}
