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
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Danilo Souza Almeida
 */
@Entity
@Table(name = "senha")
public class Senha extends EntityManageable implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 16)
    @Column(name = "senha")
    private String senha;
    @JoinColumn(name = "turma_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Turma turma;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "senha")
    private List<Respostas> respostas;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_finalizacao_resposta")
    private Date dataFinalizacaoResposta;
    
    public Senha() {
    }

    public Senha(Integer id) {
        this.id = id;
    }

    public Senha(String senha) {
        this.senha = senha;
    }
    
    public Senha(Integer id, String senha) {
        this.id = id;
        this.senha = senha;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public List<Respostas> getRespostas() {
        return respostas;
    }

    public void setRespostas(List<Respostas> respostas) {
        this.respostas = respostas;
    }

    public Date getDataFinalizacaoResposta() {
        return dataFinalizacaoResposta;
    }

    public void setDataFinalizacaoResposta(Date dataFinalizacaoResposta) {
        this.dataFinalizacaoResposta = dataFinalizacaoResposta;
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
        if (!(object instanceof Senha)) {
            return false;
        }
        Senha other = (Senha) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.ifnmg.ifad.entity.Aluno[ id=" + id + " ]";
    }

    @Override
    public void save() throws BusinessException {
        valid();
        Senha aluno = this;
        aluno = (Senha) getSession().merge(aluno);
        id = aluno.id;
    }

    @Override
    public void remove() throws BusinessException {
        Senha aluno = (Senha) getSession().get(Senha.class, id);
        getSession().delete(aluno);
    }

    @Override
    public void valid() throws BusinessException {
        if(Assert.isStringNullOrEmpty(senha)){
            throw new BusinessException("Por favor informe o Senha!");
        }
        if(Assert.isNull(turma)){
            throw new BusinessException("Por favor informe a Turma!");
        }
    }
    
}
