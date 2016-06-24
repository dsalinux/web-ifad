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

import br.edu.ifnmg.ifad.entity.Turma;
import br.edu.ifnmg.ifad.util.HibernateUtil;
import br.edu.ifnmg.ifad.util.ReportUtil;
import br.edu.ifnmg.ifad.util.exception.BusinessException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.hibernate.Session;
import org.hibernate.engine.SessionFactoryImplementor;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 * Cadastros de Alunos
 * @author Danilo Souza Almeida
 */
@ManagedBean
@SessionScoped
public class EmissaoReportsBean extends AbstractManager {
    
    private Turma turma = null;
    
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
    public StreamedContent getListaFile() {
        try {
            if(turma == null){
                addMessage(getSeverityWarn(),"Por favor selecione a turma!");
                return null;
            }
            InputStream inputStream = getClass().getResourceAsStream("/br/edu/ifnmg/ifad/report/lista_ata_assinatura.jrxml");
            HashMap<String, Object> map  = new HashMap<String, Object>();
            map.put("COD_TURMA", turma.getId());
            SessionFactoryImplementor factoryImplementor = (SessionFactoryImplementor) HibernateUtil.getSessionFactory();
            map.put("REPORT_CONNECTION",factoryImplementor.getConnectionProvider().getConnection());
            StringBuilder nome = new StringBuilder("lista_ata_");
            nome.append(turma.getNome().replaceAll(" ", "_"));
            nome.append(".pdf");
            StreamedContent streamedContent = new DefaultStreamedContent(new ByteArrayInputStream(ReportUtil.reportToPDF(null, inputStream, map)), "application/pdf", nome.toString());
            return streamedContent;
        } catch (Exception ex) {
            addMessage(getSeverityError(),"Erro ao emitir arquivo com senhas! Detalhes: "+ex.getMessage());
            Logger.getLogger(EmissaoReportsBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    public StreamedContent getSenhasFile() {
        try {
//            if(turma == null){
//                addMessage(getSeverityWarn(),"Por favor selecione a turma!");
//                return null;
//            }
            InputStream inputStream = getClass().getResourceAsStream("/br/edu/ifnmg/ifad/report/lista_cpf_senha.jrxml");
            HashMap<String, Object> map  = new HashMap<String, Object>();
            StringBuilder nome = new StringBuilder("senhas_");
            if(turma != null){
                map.put("COD_TURMA", turma.getId());
                nome.append(turma.getNome().replaceAll(" ", "_"));
            }
            SessionFactoryImplementor factoryImplementor = (SessionFactoryImplementor) HibernateUtil.getSessionFactory();
            map.put("REPORT_CONNECTION",factoryImplementor.getConnectionProvider().getConnection());
            nome.append(".pdf");
            StreamedContent streamedContent = new DefaultStreamedContent(new ByteArrayInputStream(ReportUtil.reportToPDF(null, inputStream, map)), "application/pdf", nome.toString());
            return streamedContent;
        } catch (Exception ex) {
            addMessage(getSeverityError(),"Erro ao emitir arquivo com senhas! Detalhes: "+ex.getMessage());
            Logger.getLogger(EmissaoReportsBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    public StreamedContent getFichaAvaliacaoDocenteFile() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/br/edu/ifnmg/ifad/report/ficha_avaliacao_docente.jrxml");
            HashMap<String, Object> map  = new HashMap<String, Object>();
            InputStream isSubReport = getClass().getResourceAsStream("/br/edu/ifnmg/ifad/report/media_respostas_avaliadas_subreport.jrxml");
            map.put("SUBREPORT_DIR", ReportUtil.compileReport(isSubReport));
            SessionFactoryImplementor factoryImplementor = (SessionFactoryImplementor) HibernateUtil.getSessionFactory();
            map.put("REPORT_CONNECTION",factoryImplementor.getConnectionProvider().getConnection());
            StringBuilder nome = new StringBuilder("ficha_avaliacao_docente_");
            nome.append(new SimpleDateFormat("yyyy_MM_dd").format(new Date()));
            nome.append(".pdf");
            StreamedContent streamedContent = new DefaultStreamedContent(new ByteArrayInputStream(ReportUtil.reportToPDF(null, inputStream, map)), "application/pdf", nome.toString());
            return streamedContent;
        } catch (Exception ex) {
            addMessage(getSeverityError(),"Erro ao emitir arquivo com senhas! Detalhes: "+ex.getMessage());
            Logger.getLogger(EmissaoReportsBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Turma getTurma() {
        return turma;
    }
    public void setTurma(Turma turma) {
        this.turma = turma;
    }
    
}
