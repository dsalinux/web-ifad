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

import br.edu.ifnmg.ifad.util.exception.BusinessException;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "respostas")
public class Respostas extends EntityManageable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    @Size(max = 1000)
    @Column(name = "resposta")
    private String resposta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "data_criacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataResposta;
    @JoinColumn(name = "questao_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Questao questao;
    @JoinColumn(name = "professor_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Professor professor;
    @JoinColumn(name = "senha_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Senha senha;

    public Respostas() {
    }

    public Respostas(Integer id) {
        this.id = id;
    }

    public Respostas(Integer id, Date dataResposta) {
        this.id = id;
        this.dataResposta = dataResposta;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public Date getDataResposta() {
        return dataResposta;
    }

    public void setDataResposta(Date dataResposta) {
        this.dataResposta = dataResposta;
    }

    public Questao getQuestao() {
        return questao;
    }

    public void setQuestao(Questao questao) {
        this.questao = questao;
    }
    
    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Senha getSenha() {
        return senha;
    }

    public void setSenha(Senha senha) {
        this.senha = senha;
    }
    
    @Override
    public String toString() {
        return "br.edu.ifnmg.ifad.entity.AlunoHasQuestao[ id=" + id + " ]";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 23 * hash + (this.questao != null ? this.questao.hashCode() : 0);
        hash = 23 * hash + (this.professor != null ? this.professor.hashCode() : 0);
        hash = 23 * hash + (this.senha != null ? this.senha.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Respostas other = (Respostas) obj;
        if (this.questao != other.questao && (this.questao == null || !this.questao.equals(other.questao))) {
            return false;
        }
        if (this.professor != other.professor && (this.professor == null || !this.professor.equals(other.professor))) {
            return false;
        }
        if (this.senha != other.senha && (this.senha == null || !this.senha.equals(other.senha))) {
            return false;
        }
        if(other.id != null && other.id.equals(id)){
            return true;
        }
        return true;
    }

    

    @Override
    public void save() throws BusinessException {
        valid();
        Respostas alunoHasQuestao = this;
        if(dataResposta == null){
            dataResposta = new Date();
        }
        getSession().merge(alunoHasQuestao);
    }

    @Override
    public void remove() throws BusinessException {
    }

    @Override
    public void valid() throws BusinessException {
        if(professor == null || questao == null || senha == null){
            throw new BusinessException("Não foi possível identificar a referências dos seguintes objetos: professor, questão e senha de avaliaçao.");
        }
    }
    
}
