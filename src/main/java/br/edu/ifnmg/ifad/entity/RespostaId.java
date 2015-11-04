package br.edu.ifnmg.ifad.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RespostaId implements Serializable {
    
    public RespostaId(){
        
    }

    public RespostaId(int questao, int professor, int senha) {
        this.questao = questao;
        this.professor = professor;
        this.senha = senha;
    }
    
    @Column(name = "questao_id")
    private int questao;
    @Column(name = "professor_id")
    private int professor;
    @Column(name = "senha_id")
    private int senha;

    public int getQuestao() {
        return questao;
    }

    public void setQuestao(int questao) {
        this.questao = questao;
    }

    public int getProfessor() {
        return professor;
    }

    public void setProfessor(int professor) {
        this.professor = professor;
    }

    public int getSenha() {
        return senha;
    }

    public void setSenha(int senha) {
        this.senha = senha;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.questao;
        hash = 89 * hash + this.professor;
        hash = 89 * hash + this.senha;
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
        final RespostaId other = (RespostaId) obj;
        if (this.questao != other.questao) {
            return false;
        }
        if (this.professor != other.professor) {
            return false;
        }
        if (this.senha != other.senha) {
            return false;
        }
        return true;
    }
    
    
    
}
