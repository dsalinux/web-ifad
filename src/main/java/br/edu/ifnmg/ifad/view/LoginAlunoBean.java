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

package br.edu.ifnmg.ifad.view;

import br.edu.ifnmg.ifad.entity.Senha;
import br.edu.ifnmg.ifad.util.exception.BusinessException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Danilo Souza Almeida
 */
@ManagedBean
@SessionScoped
public class LoginAlunoBean extends AbstractManager {
    
    private String senha;
    private Senha alunoLogado;
    
//    @ManagedProperty(value = "#{configuracaoBean}")
//    private ConfiguracaoBean configuracaoBean;
    
    public void logar(){
        try {
            doInTransaction(new PersistenceActionWithoutResult() {
                
                @Override
                public void execute(Session s) throws BusinessException {
                    Criteria criteria = s.createCriteria(Senha.class);
                    criteria.add(Restrictions.eq("senha", senha));
                    alunoLogado = (Senha) criteria.uniqueResult();
                    if(alunoLogado != null && alunoLogado.getDataFinalizacaoResposta() != null){
                        addMessage(getSeverityWarn(), "Seu questionário já foi enviado! Se necessitar solicite o desbloqueio.");
                        return;
                    }
                    if(!isLogged()){
                        addMessage(getSeverityWarn(), "Senha incorreta!");
                    } else {
//                            try {
//                                doInTransaction(new PersistenceActionWithoutResult() {
//
//                                    @Override
//                                    public void execute(Session s) throws BusinessException {
//                                        List<Questao> questoes = new ArrayList<Questao>();
//                                        Questionario questionario = configuracaoBean.getEntity().getQuestionario();
//                                        Criteria criteriaQuestao = s.createCriteria(Questao.class);
//                                        criteriaQuestao.add(Restrictions.eq("questionario", questionario));
//                                        questoes = criteriaQuestao.list();
//                                        Aluno aluno = getAlunoLogado();
//                                        s.refresh(aluno);
//                                        Criteria criteriaProfessor = s.createCriteria(Professor.class);
//                                        criteriaProfessor.createAlias("disciplinaList", "d");
//                                        criteriaProfessor.add(Restrictions.eq("d.turma", aluno.getTurma()));
//                                        criteriaProfessor.addOrder(Order.asc("nome"));
//                                        List<Professor> professores = criteriaProfessor.list();
//                                        Logger.getLogger(QuestionarioBean.class.getName()).log(Level.WARNING, "Listou {0} Professores()!", professores.size());
//                                        for (Professor professor : professores) {
//                                            for (Questao questao : questoes) {
//                                                AlunoHasQuestao alunoHasQuestao = new AlunoHasQuestao(new AlunoHasQuestaoPK(questao.getId(), aluno.getId(), professor.getId()));
//                                                if(!aluno.getAlunoHasQuestaoList().contains(alunoHasQuestao)){
//                                                    alunoHasQuestao.setAluno(aluno);
//                                                    alunoHasQuestao.setProfessor(professor);
//                                                    alunoHasQuestao.setQuestao(questao);
//                                                    alunoHasQuestao.setSession(s);
//                                                    alunoHasQuestao.save();
//                                                }
//                                            }
//                                        }
//                                    }
//                                });
//                            } catch (BusinessException ex) {
//                                addMessage(getSeverityWarn(), ex.getMessage());
//                                Logger.getLogger(QuestionarioBean.class.getName()).log(Level.SEVERE, null, ex);
//                            }
                    }
                }
            });
        } catch (BusinessException ex) {
            Logger.getLogger(LoginAlunoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void redirecionarNaoLogado(){
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("login-aluno.jsf");
        } catch (IOException ex) {
            addMessage(getSeverityError(), "Erro ao tentar acessar a tela de Login!");
            Logger.getLogger(LoginAlunoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void redirecionarQuestionario(){
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("questionario.jsf");
        } catch (IOException ex) {
            addMessage(getSeverityError(), "Erro ao tentar lhe direcionar para o Questionário!");
            Logger.getLogger(LoginAlunoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void logout(){
        try {
            alunoLogado = null;
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            FacesContext.getCurrentInstance().getExternalContext().redirect("login-aluno.jsf");
        } catch (IOException ex) {
            addMessage(getSeverityError(), "Erro ao voltar ao login!");
            Logger.getLogger(LoginAlunoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Getters and Setters
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Senha getAlunoLogado() {
        return alunoLogado;
    }

    public void setAlunoLogado(Senha alunoLogado) {
        this.alunoLogado = alunoLogado;
    }
    public boolean isLogged(){
        if(alunoLogado != null && alunoLogado.getId() != null && alunoLogado.getDataFinalizacaoResposta() == null){
            return true;
        }
        return false;
    }
        
//    public ConfiguracaoBean getConfiguracaoBean() {
//        return configuracaoBean;
//    }
//    public void setConfiguracaoBean(ConfiguracaoBean configuracaoBean) {
//        this.configuracaoBean = configuracaoBean;
//    }
}
