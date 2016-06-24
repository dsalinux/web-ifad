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

import br.edu.ifnmg.ifad.entity.Professor;
import br.edu.ifnmg.ifad.entity.Turma;
import br.edu.ifnmg.ifad.util.exception.BusinessException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;

/**
 * Cadastros de Alunos
 * @author Danilo Souza Almeida
 */
@ManagedBean
@SessionScoped
public class GerenciarTurmaBean extends GenericCrudBean<Turma>{
    
    private List<Professor> professores = new ArrayList<Professor>();
    private final int TEMPO_ATUALIZAR  = 20000;
    private long ultimaAtualizacao = Calendar.getInstance().getTimeInMillis()-TEMPO_ATUALIZAR;
    private List<Professor> professoresSelecionados = new ArrayList<Professor>();

    @Override
    public void save() throws BusinessException, Exception {
//        doInTransaction(new PersistenceActionWithoutResult() {
//            @Override
//            public void execute(Session s) throws BusinessException {
//                getEntity().setProfessorList(null);
//                getEntity().setSession(s);
//                getEntity().save();
//                getEntity().setProfessorList(professoresSelecionados);
//                getEntity().save();
//            }
//        });
        getEntity().setProfessorList(professoresSelecionados);
        super.save();
        professoresSelecionados = new ArrayList<Professor>();
    }
    
    
    
    @Override
    public void newRegistre(ActionEvent actionEvent) {
        super.newRegistre(actionEvent); //To change body of generated methods, choose Tools | Templates.
        professoresSelecionados = new ArrayList<Professor>();
    }
    
    @Override
    public void edit(Turma entity) {
        professoresSelecionados = new ArrayList<Professor>();
        try {
            super.setEntity(entity);
            doInTransaction(new PersistenceActionWithoutResult() {
                @Override
                public void execute(Session s) throws BusinessException {
                    s.refresh(getEntity());
                    Hibernate.initialize(getEntity().getProfessorList());
                    professoresSelecionados.addAll(getEntity().getProfessorList());
                }
            });
            changeStateToEdit();
        } catch (BusinessException ex) {
            addMessage(getSeverityWarn(), ex.getMessage());
        }
    }
    
    public List<Professor> getProfessores() {
        long tempoAtual = Calendar.getInstance().getTimeInMillis();
        if(tempoAtual-ultimaAtualizacao>TEMPO_ATUALIZAR){
            try {
                professores = doInTransaction(new PersistenceAction<List<Professor>>() {
                    @Override
                    public List<Professor> execute(Session s) throws BusinessException {
                        Criteria c = s.createCriteria(Professor.class);
                        return c.list();
                    }
                });
            } catch (BusinessException ex) {
                addMessage(getSeverityError(), ex.getMessage());
            }
            ultimaAtualizacao = tempoAtual;
        }
        return professores;
    }

    public void setProfessores(List<Professor> professores) {
        this.professores = professores;
    }

    public List<Professor> getProfessoresSelecionados() {
        return professoresSelecionados;
    }

    public void setProfessoresSelecionados(List<Professor> professoresSelecionados) {
        this.professoresSelecionados = professoresSelecionados;
    }
    
}
