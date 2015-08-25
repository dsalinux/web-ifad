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
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Danilo Souza Almeida
 */
@Entity
@Table(name = "turma")
public class Turma extends EntityManageable {
    
    private static final long serialVersionUID = 1L;
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "nome")
    private String nome;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "curso")
    private String curso;
    @OneToMany(mappedBy = "turma")
    private List<Senha> alunoList;
    
    @JoinTable(name = "turma_has_professor", joinColumns = {
        @JoinColumn(name = "turma_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "professor_id", referencedColumnName = "id")})
    @ManyToMany
    private List<Professor> professorList;

    public Turma() {
    }

    public Turma(Integer id) {
        this.id = id;
    }

    public Turma(Integer id, String nome, String curso) {
        this.id = id;
        this.nome = nome;
        this.curso = curso;
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

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public List<Senha> getAlunoList() {
        return alunoList;
    }

    public void setAlunoList(List<Senha> alunoList) {
        this.alunoList = alunoList;
    }

    public List<Professor> getProfessorList() {
        return professorList;
    }

    public void setProfessorList(List<Professor> professorList) {
        this.professorList = professorList;
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
        if (!(object instanceof Turma)) {
            return false;
        }
        Turma other = (Turma) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }


    @Override
    public void save() throws BusinessException {
        Turma turma  = this;
        turma = (Turma) getSession().merge(turma);
        id = turma.id;
    }

    @Override
    public void remove() throws BusinessException {
        Turma turma = (Turma) getSession().get(Turma.class, id);
        if(!Assert.isNull(turma)){
            getSession().delete(turma);
        }
    }

    @Override
    public void valid() throws BusinessException {
        if(Assert.isStringNullOrEmpty(nome)){
            throw new BusinessException("Por favor informe o nome da turma!");
        }
        if(Assert.isStringNullOrEmpty(curso)){
            throw new BusinessException("Por favor informe o nome do curso!");
        }
    }
    
}
