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

import br.edu.ifnmg.ifad.entity.Configuracao;
import br.edu.ifnmg.ifad.entity.Questionario;
import br.edu.ifnmg.ifad.util.exception.BusinessException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import org.hibernate.Session;

/**
 *
 * @author Danilo Souza Almeida
 */
@ManagedBean
@ApplicationScoped
public class ConfiguracaoBean extends AbstractManager{
    
    private Configuracao entity;

    @PostConstruct
    public void init(){
        buscar();
    }
    
    public void salvar(){
        try {
            doInTransaction(new PersistenceActionWithoutResult() {
                @Override
                public void execute(Session s) throws BusinessException {
                    entity.setSession(s);
                    entity.save();
                    addMessage("Alterado com sucesso!");
                }
            });
        } catch (BusinessException ex) {
            Logger.getLogger(ConfiguracaoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void buscar(){
        try {
            entity = doInTransaction(new PersistenceAction<Configuracao>() {
                
                @Override
                public Configuracao execute(Session s) throws BusinessException {
                    Configuracao configuracao = (Configuracao) s.get(Configuracao.class, 1);
                    return configuracao;
                }
            });
            if(entity == null){
                entity = new Configuracao(1);
                salvar();
            }
        } catch (BusinessException ex) {
            Logger.getLogger(ConfiguracaoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<Questionario> getQuestionarios() {
        try {
            return doInTransaction(new PersistenceAction<List<Questionario>>() {
                @Override
                public List<Questionario> execute(Session s) throws BusinessException {
                    return s.createCriteria(Questionario.class).list();
                }
            });
        } catch (BusinessException ex) {
            addMessage(getSeverityWarn(), ex.getMessage());
            Logger.getLogger(GerenciarAlunoBean.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<Questionario>();
        }
    }
    
    public boolean isAtivo(){
        Calendar dataAtual = Calendar.getInstance();
        Calendar dataInicio = Calendar.getInstance();
        dataInicio.setTime(entity.getDataInicio());
        Calendar dataFim = Calendar.getInstance();
        dataFim.setTime(entity.getDataFim());
        return entity.getBloqueado() == false && dataAtual.after(dataInicio) && dataAtual.before(dataFim);
    }
    
    public Configuracao getEntity() {
        return entity;
    }
    public void setEntity(Configuracao entity) {
        this.entity = entity;
    }
    
}
