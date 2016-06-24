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
import javax.persistence.ManyToMany;
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
@Table(name = "professor")
public class Professor extends EntityManageable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "nome")
    private String nome;
    
    
    @ManyToMany(mappedBy = "professorList")
    private List<Turma> turmaList;
    
    @JoinColumn(name = "foto_id", referencedColumnName = "id")
    @ManyToOne
    private Foto foto;
    @Size(min = 1, max = 20)
    @Column(name = "matricula_siape")
    private String matriculaSiape;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "campus_lotacao")
    private String campusLotacao;
    @Size(min = 1, max = 45)
    @Column(name = "classe_nivel_atual")
    private String classeNivelAtual;
    @Column(name = "data_ultima_progressao")
    @Temporal(TemporalType.DATE)
    private Date dataUltimaProgressao;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "cargo_funcao")
    private String cargoFuncao;
    @Size(max = 45)
    @Column(name = "codigo_nivel")
    private String codigoNivel;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "professor")
    private List<Resposta> alunoHasQuestaoList;

    public Professor() {
    }

    public Professor(Integer id) {
        this.id = id;
    }

    public Professor(Integer id, String nome) {
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

    public String getMatriculaSiape() {
        return matriculaSiape;
    }

    public void setMatriculaSiape(String matriculaSiape) {
        this.matriculaSiape = matriculaSiape;
    }

    public String getCampusLotacao() {
        return campusLotacao;
    }

    public void setCampusLotacao(String campusLotacao) {
        this.campusLotacao = campusLotacao;
    }

    public String getClasseNivelAtual() {
        return classeNivelAtual;
    }

    public void setClasseNivelAtual(String classeNivelAtual) {
        this.classeNivelAtual = classeNivelAtual;
    }

    public Date getDataUltimaProgressao() {
        return dataUltimaProgressao;
    }

    public void setDataUltimaProgressao(Date dataUltimaProgressao) {
        this.dataUltimaProgressao = dataUltimaProgressao;
    }

    public String getCargoFuncao() {
        return cargoFuncao;
    }

    public void setCargoFuncao(String cargoFuncao) {
        this.cargoFuncao = cargoFuncao;
    }

    public String getCodigoNivel() {
        return codigoNivel;
    }

    public void setCodigoNivel(String codigoNivel) {
        this.codigoNivel = codigoNivel;
    }

    public List<Resposta> getAlunoHasQuestaoList() {
        return alunoHasQuestaoList;
    }

    public void setAlunoHasQuestaoList(List<Resposta> alunoHasQuestaoList) {
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
        if (!(object instanceof Professor)) {
            return false;
        }
        Professor other = (Professor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }


    @Override
    public void save() throws BusinessException {
        valid();
        Professor professor = this;
        professor = (Professor) getSession().merge(professor);
        id = professor.id;
    }

    @Override
    public void remove() throws BusinessException {
        Professor professor = (Professor) getSession().get(Professor.class, id);
        getSession().delete(professor);
    }

    @Override
    public void valid() throws BusinessException {
        if(Assert.isStringNullOrEmpty(nome)){
            throw new BusinessException("Por favor informe o Nome do Professor!");
        }
        if(Assert.isStringNullOrEmpty(campusLotacao)){
            throw new BusinessException("Por favor informe o Campus Lotação!");
        }
        if(Assert.isStringNullOrEmpty(cargoFuncao)){
            throw new BusinessException("Por favor informe o Cargo / Função!");
        }
//        if(Assert.isStringNullOrEmpty(classeNivelAtual)){
//            throw new BusinessException("Por favor informe o Classe / Nível Atual!");
//        }
//        if(Assert.isStringNullOrEmpty(matriculaSiape)){
//            throw new BusinessException("Por favor informe a Matrícula do Siape!");
//        }
    }

    public List<Turma> getTurmaList() {
        return turmaList;
    }

    public void setTurmaList(List<Turma> turmaList) {
        this.turmaList = turmaList;
    }

    public Foto getFoto() {
        return foto;
    }

    public void setFoto(Foto foto) {
        this.foto = foto;
    }
}
