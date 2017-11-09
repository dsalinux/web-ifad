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

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author danilo
 */
@Entity
@Table(name = "tipos_respostas_item")
@NamedQueries({
    @NamedQuery(name = "TiposRespostasItem.findAll", query = "SELECT t FROM TiposRespostasItem t")})
public class TiposRespostasItem implements Serializable {

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
    @JoinColumn(name = "tipo_respostas_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TiposRespostas tiposRespostas;

    public TiposRespostasItem() {
    }

    public TiposRespostasItem(Integer id) {
        this.id = id;
    }

    public TiposRespostasItem(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
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

    public TiposRespostas getTiposRespostas() {
        return tiposRespostas;
    }

    public void setTiposRespostas(TiposRespostas tiposRespostas) {
        this.tiposRespostas = tiposRespostas;
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
        if (!(object instanceof TiposRespostasItem)) {
            return false;
        }
        TiposRespostasItem other = (TiposRespostasItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.ifnmg.ifad.entity.TiposRespostasItem[ id=" + id + " ]";
    }
    
}
