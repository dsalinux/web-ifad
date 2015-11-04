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
import br.edu.ifnmg.ifad.entity.Turma;
import br.edu.ifnmg.ifad.util.StringHelper;
import br.edu.ifnmg.ifad.util.exception.BusinessException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * Cadastros de Alunos
 * @author Danilo Souza Almeida
 */
@ManagedBean
@SessionScoped
public class GerenciarAlunoBean extends GenericCrudBean<Senha>{

    private Integer quantidadeSenhas;
    private Boolean quantidadeTotal;
    private Turma turma;
    private int qtSenhas = 0;
    
    @Override
    public void newRegistre(ActionEvent actionEvent) {
        super.newRegistre(actionEvent);
        gerarSenha();
    }

    public void criarSenhas(){
        if(turma == null){
            addMessage(getSeverityWarn(), "Primeiro Informe a turma!");
            return;
        }
        
        try {
            doInTransaction(new PersistenceActionWithoutResult() {

                @Override
                public void execute(Session s) throws BusinessException {
                    if(quantidadeTotal){
                        qtSenhas = quantidadeSenhas;
                    } else {
                        Criteria criteria = s.createCriteria(Senha.class);
                        criteria.setProjection(Projections.count("id"));
                        Integer quantidade = (Integer) criteria.uniqueResult();
                        qtSenhas=quantidadeSenhas-quantidade;
                    }
                    if(qtSenhas <= 0){
                        addMessage(getSeverityWarn(),"Quantidade de senhas é inválida.");
                        return;
                    }
                    for(int i = 0; i< qtSenhas;i++){
                        boolean senhaRepetida = true;
                        while(senhaRepetida){
                            String senha = StringHelper.getRandomPassword(12).toUpperCase();
                            Criteria c = s.createCriteria(Senha.class);
                            c.add(Restrictions.eq("senha", senha));
                            if(c.uniqueResult() == null){
                                Senha sen = new Senha();
                                sen.setSenha(senha);
                                sen.setTurma(turma);
                                sen.setSession(s);
                                sen.save();
                                senhaRepetida = false;
                                addMessage("Salvando senha: "+senha);
                            } else {
                                senhaRepetida = true;
                            }
                        }
                    }
                }
            });
        } catch (BusinessException ex) {
            Logger.getLogger(GerenciarAlunoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void gerarSenha(){
       getEntity().setSenha(StringHelper.getRandomPassword(12));
    }
    
    public void desbloquear(Senha senha){
        try {
            setEntity(senha);
            if(getEntity().getDataFinalizacaoResposta() == null){
                getEntity().setDataFinalizacaoResposta(new Date());
            } else {
                getEntity().setDataFinalizacaoResposta(null);
            }
            save();
        } catch (Exception ex) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao tentar bloquear/desbloquear senha!");
            Logger.getLogger(GerenciarAlunoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<Turma> getTurmas() {
        try {
            return doInTransaction(new PersistenceAction<List<Turma>>() {
                @Override
                public List<Turma> execute(Session s) throws BusinessException {
                    return s.createCriteria(Turma.class).list();
                }
            });
        } catch (BusinessException ex) {
            addMessage(getSeverityWarn(), ex.getMessage());
            Logger.getLogger(GerenciarAlunoBean.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<Turma>();
        }
    }

    public Integer getQuantidadeSenhas() {
        return quantidadeSenhas;
    }
    public void setQuantidadeSenhas(Integer quantidadeSenhas) {
        this.quantidadeSenhas = quantidadeSenhas;
    }

    public Boolean getQuantidadeTotal() {
        return quantidadeTotal;
    }

    public void setQuantidadeTotal(Boolean quantidadeTotal) {
        this.quantidadeTotal = quantidadeTotal;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

}
