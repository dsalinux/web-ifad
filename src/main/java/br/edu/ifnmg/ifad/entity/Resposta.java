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
public class Resposta extends EntityManageable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private RespostaId id;

    @Size(max = 1000)
    @Column(name = "resposta")
    private String resposta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "data_criacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataResposta;
    @ManyToOne
    @JoinColumn(name = "questao_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Questao questao;
    @ManyToOne
    @JoinColumn(name = "professor_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Professor professor;
    @ManyToOne
    @JoinColumn(name = "senha_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Senha senha;

    public Resposta() {
    }

    public Resposta(RespostaId id) {
        this.id = id;
    }

    public Resposta(RespostaId id, Date dataResposta) {
        this.id = id;
        this.dataResposta = dataResposta;
    }

    public Resposta(int questaoId, int professorId, int senhaId) {
        this(new RespostaId(questaoId, professorId, senhaId));
    }

    public RespostaId getId() {
        return id;
    }

    public void setId(RespostaId id) {
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
        if (questao != null) {
            if (id == null) {
                id = new RespostaId();
            }
            id.setQuestao(questao.getId());
        }
        this.questao = questao;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        if (id == null) {
            id = new RespostaId();
        }
        id.setProfessor(professor.getId());
        this.professor = professor;
    }

    public Senha getSenha() {
        return senha;
    }

    public void setSenha(Senha senha) {
        if (id == null) {
            id = new RespostaId();
        }
        id.setSenha(senha.getId());
        this.senha = senha;
    }

    @Override
    public String toString() {
        return "br.edu.ifnmg.ifad.entity.AlunoHasQuestao[ id=" + id + " ]";
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.id != null ? this.id.hashCode() : 0);
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
        final Resposta other = (Resposta) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public void save() throws BusinessException {
        valid();
        Resposta alunoHasQuestao = this;
        if (dataResposta == null) {
            dataResposta = new Date();
        }
        getSession().merge(alunoHasQuestao);
    }

    @Override
    public void remove() throws BusinessException {
    }

    @Override
    public void valid() throws BusinessException {
        if (professor == null || questao == null || senha == null) {
            throw new BusinessException("Não foi possível identificar a referências dos seguintes objetos: professor, questão e senha de avaliaçao.");
        }
    }

}
