package br.edu.ifnmg.ifad.view;

import br.edu.ifnmg.ifad.entity.Professor;
import br.edu.ifnmg.ifad.entity.Turma;
import br.edu.ifnmg.ifad.entity.vo.ClasseVO;
import br.edu.ifnmg.ifad.entity.vo.ProfessorVO;
import br.edu.ifnmg.ifad.util.exception.BusinessException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.primefaces.event.FileUploadEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@ManagedBean
@SessionScoped
public class ImportarXmlRoz extends AbstractManager {

    DocumentBuilderFactory dbFactory;
    DocumentBuilder builder;
    Document document;

    public ImportarXmlRoz() {
        try {
            dbFactory = DocumentBuilderFactory.newInstance();
            builder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            addMessage(getSeverityError(), "Erro ao iniciar a configuração!\n" + ex.getMessage());
        }
    }

    public void iniciarImportacao(FileUploadEvent event) {
        try {
            InputStream f = event.getFile().getInputstream();
            if (f == null) {
                addMessage(getSeverityError(), "Erro ao selecionar um arquivo!");
                return;
            }

            document = builder.parse(f);
            document.getDocumentElement().normalize();
        } catch (SAXException ex) {
            addMessage(getSeverityError(), "Arquivo inválido!\n" + ex.getMessage());
            Logger.getLogger(ImportarXmlRoz.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            addMessage(getSeverityError(), "Arquivo inválido!\n" + ex.getMessage());
            Logger.getLogger(ImportarXmlRoz.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void salvar() {
        if(getClasseComProfessores().isEmpty()){
            addMessage(getSeverityWarn(), "Nenhum dado para Importar!");
            return;
        }
        try {
            doInTransaction(new PersistenceActionWithoutResult() {
                @Override
                public void execute(Session s) throws BusinessException {
                    List<ClasseVO> classes = getClasseComProfessores();
                    for (ClasseVO classe : classes) {
                        List<Professor> professores = new ArrayList<Professor>();
                        for (ProfessorVO p : classe.getTeachers()) {
                            Criteria c = s.createCriteria(Professor.class);
                            c.add(Restrictions.eq("nome", p.getName()));
                            Professor professor = (Professor) c.uniqueResult();
                            if (professor == null) {
                                professor = new Professor();
                                professor.setNome(p.getName());
                                professor.setCampusLotacao("Arinos");
                                professor.setCargoFuncao("Professor EBTT");
                                s.persist(professor);
                            }
                            if(!professores.contains(professor)){
                                professores.add(professor);
                            }
                        }
                        Turma turma = new Turma();
                        turma.setNome(classe.getName());
                        turma.setProfessorList(professores);
                        turma.setCurso("Não Informado");
                        s.persist(turma);

                    }
                    addMessage("Dados importados com sucesso!");
                }
            });
        } catch (BusinessException ex) {
            addMessage(getSeverityWarn(), ex.getMessage());
        }
    }

    public List<ProfessorVO> getProfessores() {
        List<ProfessorVO> teachers = new ArrayList<ProfessorVO>();
        if (document != null && document.getElementsByTagName("timetable") != null) {
            NodeList listClasses = document.getElementsByTagName("teachers").item(0).getChildNodes();
            for (int i = 0; i < listClasses.getLength(); i++) {
                Node n = listClasses.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    ProfessorVO t = new ProfessorVO();
                    Element e = (Element) n;
                    t.setId(e.getAttribute("id"));
                    t.setName(e.getAttribute("name"));
                    t.setShortName(e.getAttribute("short"));
                    t.setGender(e.getAttribute("gender"));

                    teachers.add(t);
                }
            }
        }
        return teachers;
    }

    public List<ClasseVO> getClasses() {
        List<ClasseVO> classes = new ArrayList<ClasseVO>();
        if (document != null && document.getElementsByTagName("timetable") != null) {
            NodeList listClasses = document.getElementsByTagName("classes").item(0).getChildNodes();
            for (int i = 0; i < listClasses.getLength(); i++) {
                Node n = listClasses.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    ClasseVO c = new ClasseVO();
                    Element e = (Element) n;
                    c.setId(e.getAttribute("id"));
                    c.setName(e.getAttribute("name"));

                    classes.add(c);
                }
            }
        }
        return classes;
    }

    public List<ClasseVO> getClasseComProfessores() {
        List<ClasseVO> classes = getClasses();
        if (document != null && document.getElementsByTagName("timetable") != null) {
            List<ProfessorVO> teachers = getProfessores();
            NodeList listClasses = document.getElementsByTagName("lessons").item(0).getChildNodes();
            for (int i = 0; i < listClasses.getLength(); i++) {
                Node n = listClasses.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) n;
                    String teacherids = e.getAttribute("teacherids");
                    String classids = e.getAttribute("classids");
                    ClasseVO c = new ClasseVO();
                    c.setId(classids);
                    c = classes.get(classes.indexOf(c));
                    if (c.getTeachers() == null) {
                        c.setTeachers(new ArrayList<ProfessorVO>());
                    }
                    String[] tids = teacherids.split(",");
                    for (String tid : tids) {
                        ProfessorVO t = new ProfessorVO();
                        t.setId(tid);
                        if (teachers.contains(t)) {
                            t = teachers.get(teachers.indexOf(t));
                        } else {
                            System.out.println("Not found teacherids: " + t.getId());
                        }
                        if (!c.getTeachers().contains(t)) {
                            c.getTeachers().add(t);
                        }
                    }
                }
            }
        }
        return classes;
    }

}
