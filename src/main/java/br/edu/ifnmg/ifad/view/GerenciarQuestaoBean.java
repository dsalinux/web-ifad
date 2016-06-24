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

import br.edu.ifnmg.ifad.entity.Questao;
import br.edu.ifnmg.ifad.entity.Questionario;
import br.edu.ifnmg.ifad.util.exception.BusinessException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.hibernate.Session;

/**
 * Cadastros de Alunos
 * @author Danilo Souza Almeida
 */
@ManagedBean
@SessionScoped
public class GerenciarQuestaoBean extends GenericCrudBean<Questao>{

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
}
