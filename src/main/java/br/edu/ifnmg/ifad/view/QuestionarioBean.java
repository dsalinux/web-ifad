package br.edu.ifnmg.ifad.view;

import br.edu.ifnmg.ifad.entity.Senha;
import br.edu.ifnmg.ifad.entity.Resposta;
import br.edu.ifnmg.ifad.entity.Professor;
import br.edu.ifnmg.ifad.entity.Questao;
import br.edu.ifnmg.ifad.entity.Questionario;
import br.edu.ifnmg.ifad.util.Assert;
import br.edu.ifnmg.ifad.util.StringHelper;
import br.edu.ifnmg.ifad.util.exception.BusinessException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

@ManagedBean
@SessionScoped
public class QuestionarioBean extends AbstractManager {

    private List<Questao> questoes = new ArrayList<Questao>();
    private List<Professor> professores = new ArrayList<Professor>();
    private int indexProfessorAtual = 0;
    private List<Resposta> listaAlunoHasQuestoes = new ArrayList<Resposta>();
    private Senha aluno;
    
    private Boolean tentativaIncorreta = false;
    
    @ManagedProperty(value = "#{configuracaoBean}")
    private ConfiguracaoBean configuracaoBean;
    @ManagedProperty(value = "#{loginAlunoBean}")
    private LoginAlunoBean loginAlunoBean;
    
    @PostConstruct
    public void iniciar(){
        Logger.getLogger(QuestionarioBean.class.getName()).log(Level.WARNING, "Entrou no iniciar()!");
        try {
            doInTransaction(new PersistenceActionWithoutResult() {
                
                @Override
                public void execute(Session s) throws BusinessException {
                    Questionario questionario = configuracaoBean.getEntity().getQuestionario();
                    Criteria criteriaQuestao = s.createCriteria(Questao.class);
                    criteriaQuestao.add(Restrictions.eq("questionario", questionario));
                    questoes = criteriaQuestao.list();
                    aluno = loginAlunoBean.getAlunoLogado();
                    s.refresh(aluno);
                    Criteria criteriaProfessor = s.createCriteria(Professor.class);
                    criteriaProfessor.createAlias("turmaList", "t");
                    criteriaProfessor.add(Restrictions.eq("t.id", aluno.getTurma().getId()));
                    criteriaProfessor.addOrder(Order.asc("nome"));
                    professores = criteriaProfessor.list();
                    Logger.getLogger(QuestionarioBean.class.getName()).log(Level.WARNING, "Listou {0} Professores()!", professores.size());
                    Hibernate.initialize(aluno.getRespostas());
                    listaAlunoHasQuestoes.addAll(aluno.getRespostas());
                    for (Professor professor : professores) {
                        for (Questao questao : questoes) {
                            Resposta alunoHasQuestao = new Resposta();
                            alunoHasQuestao.setSenha(aluno);
                            alunoHasQuestao.setProfessor(professor);
                            alunoHasQuestao.setQuestao(questao);
                            if(!aluno.getRespostas().contains(alunoHasQuestao)){
                                alunoHasQuestao.setSession(s);
                                alunoHasQuestao.save();
                                listaAlunoHasQuestoes.add(alunoHasQuestao);
                            }
                        }
                    }
                    retornarQuestionarioUltimaResposta();
                }
            });
        } catch (BusinessException ex) {
            addMessage(getSeverityWarn(), ex.getMessage());
            Logger.getLogger(QuestionarioBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
//        lista.add("Pontualidade: é pontual e compre os horários de aula e compromissos agendados.");
//        lista.add("Assiduidade: sua presença na instituição se faz sempre que necessário, para o bom andamento dos trabalhos.");
//        lista.add("Organização e disciplina: organiza, sistematiza e cumpre suas atividades respeitando os tempos hábeis as necessidades institucionais.");
//        lista.add("Relacionamento: estabelece uma relação ética e de respeito mútuo, clima de participação e trabalho coletivo com a comunidade interna e externa.");
//        lista.add("Interesse: interessa-se e dedica-se no desenvolvimento ou aperfeiçoamento de atividades educacionais de ensino, pesquisa, extensão ou inovação.");
//        lista.add("Metodologia: utiliza metodologia que incentiva a participação, discussão, expressão de ideias direcionando suas atividades pedagógicas para a formação do perfil humano e profissional do discente.");
//        lista.add("Avaliação: utiliza adequadamente os instrumentos de avaliação, de forma a identificar os avanços na aprendizagem do aluno.");
//        lista.add("Desenvolvimento Profissional: mantém-se atualizado sobre novos conhecimentos aplicando-os na sua área de trabalho.");
//        lista.add("Compromisso institucional: demonstra envolvimento e comprometimento com o trabalho. Empenha-se em manter em bom estado de conservação os equipamentos utilizados e o local de trabalho. Apresenta o plano de ensino de cada disciplina no início de cada semestre ou ano letivo.");
//        lista.add("Ética: demostra comportamento compatível quanto ao sigilo, discrição, moralidade, integridade, educação, cortesia.");
    
    public void retornarQuestionarioUltimaResposta(){
        for (Resposta alunoHasQuestao : listaAlunoHasQuestoes) {
            if(StringHelper.isEmpty(alunoHasQuestao.getResposta()) && professores.contains(alunoHasQuestao.getProfessor())){
                indexProfessorAtual = professores.indexOf(alunoHasQuestao.getProfessor());
                break;
            }
        }
    }
    
    public void nextProfessor(){
        try {
            doInTransaction(new PersistenceActionWithoutResult() {
                
                @Override
                public void execute(Session s) throws BusinessException {
                    for (Resposta alunoHasQuestao : getListaAlunoHasQuestaoByProfessor()) {
            //            addMessage("Questão id: "+alunoHasQuestao.getAlunoHasQuestaoPK().getQuestaoId()+", Resposta: "+alunoHasQuestao.getResposta());
                        if(Assert.isStringNullOrEmpty(alunoHasQuestao.getResposta())){
                            addMessage(getSeverityError(), "Por favor responta todo o questionário antes de continuar!");
                            tentativaIncorreta = true;
                            return;
                        } else {
                            alunoHasQuestao.setSession(s);
                            alunoHasQuestao.save();
                            tentativaIncorreta = false;
                        }
                    }
                    if(hasNextProfessor()){
                        indexProfessorAtual += 1;
                    } else {
                        finalizarQuestionario();
                    }
                }
            });
        } catch (BusinessException ex) {
            addMessage(getSeverityWarn(), ex.getMessage());
            Logger.getLogger(QuestionarioBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void finalizarQuestionario(){
        try {
//            for (AlunoHasQuestao alunoHasQuestao : getListaAlunoHasQuestaoByProfessor()) {
//                if(Assert.isStringNullOrEmpty(alunoHasQuestao.getResposta())){
//                    addMessage(getSeverityWarn(), "Por favor responta todo o questionário antes de continuar!");
//                    return;
//                }
//            }
            doInTransaction(new PersistenceActionWithoutResult() {
                
                @Override
                public void execute(Session s) throws BusinessException {
                    aluno.setDataFinalizacaoResposta(new Date());
                    aluno.setSession(s);
                    aluno.save();
                }
            });
            FacesContext.getCurrentInstance().getExternalContext().redirect("questionario-finalizado.jsf");
        } catch (BusinessException ex) {
            addMessage(getSeverityWarn(), ex.getMessage());
            Logger.getLogger(QuestionarioBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            addMessage(getSeverityError(), "Erro ao finalizar. Tente atualizar a página!");
            Logger.getLogger(QuestionarioBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void previousProfessor(){
        if(hasPreviousProfessor()){
            indexProfessorAtual -= 1;
        }
    }

    public int getTotalProfessores(){
        if(professores == null){
            return 0;
        }
        return professores.size();
    }
    
    public Professor getProfessorAtual(){
        if(professores == null){
            return null;
        }
        Professor p = null;
        try {
            p = professores.get(indexProfessorAtual);
        } catch(Exception e){
        }
        if(p == null){
            p = new Professor();
        }
        return p;
    }
    
    public List<Resposta> getListaAlunoHasQuestaoByProfessor(){
//        if(listaAlunoHasQuestoes == null){
//            if(!Hibernate.isInitialized(aluno.getAlunoHasQuestaoList())){
//                Hibernate.initialize(aluno.getAlunoHasQuestaoList());
//            }
//            listaAlunoHasQuestoes = aluno.getAlunoHasQuestaoList();
//        }
        List<Resposta> list = new ArrayList<Resposta>();
        for (Resposta alunoHasQuestao : listaAlunoHasQuestoes) {
            if(alunoHasQuestao.getProfessor().equals(getProfessorAtual()) && listaAlunoHasQuestoes.contains(alunoHasQuestao)){
                list.add(alunoHasQuestao);
            }
        }
        return list;
//        try {
//            if(Hibernate.isInitialized(aluno.getAlunoHasQuestaoList())){
//                for (AlunoHasQuestao alunoHasQuestao : aluno.getAlunoHasQuestaoList()) {
//                    if(alunoHasQuestao.getProfessor().equals(getProfessorAtual()) && !lista.contains(alunoHasQuestao)){
//                    }
//                }
//            }
//            return lista;
//        } catch (BusinessException ex) {
//            addMessage(getSeverityWarn(), ex.getMessage());
//            Logger.getLogger(QuestionarioBean.class.getName()).log(Level.SEVERE, null, ex);
//            return new ArrayList<AlunoHasQuestao>();
//        }
    }
    
    public boolean hasNextProfessor(){
        return professores != null && indexProfessorAtual+1 < professores.size();
    }
    public boolean hasPreviousProfessor(){
        return professores != null && indexProfessorAtual-1 >= 0 ;
    }
    public boolean isFinalizar(){
        return professores != null && indexProfessorAtual+1 == professores.size() ;
    }
    
    public int getPorcentagemRestante(){
        int total = 0;
        int totalRespondido = 0;
        if(listaAlunoHasQuestoes != null){
            total = listaAlunoHasQuestoes.size();
            for (Resposta alunoHasQuestao : listaAlunoHasQuestoes) {
                if(!Assert.isStringNullOrEmpty(alunoHasQuestao.getResposta())){
                    totalRespondido++;
                }
            }
        }
        if (total > 0 && totalRespondido > 0) {
            return new Float((totalRespondido * 100)/total).intValue();
        } else {
            return 0;
        }
    }

    public List<Questao> getQuestoes() {
        return questoes;
    }
    public void setQuestoes(List<Questao> questoes) {
        this.questoes = questoes;
    }

    public LoginAlunoBean getLoginAlunoBean() {
        return loginAlunoBean;
    }

    public void setLoginAlunoBean(LoginAlunoBean loginAlunoBean) {
        this.loginAlunoBean = loginAlunoBean;
    }
    
    public ConfiguracaoBean getConfiguracaoBean() {
        return configuracaoBean;
    }
    public void setConfiguracaoBean(ConfiguracaoBean configuracaoBean) {
        this.configuracaoBean = configuracaoBean;
    }

    public Boolean getTentativaIncorreta() {
        return tentativaIncorreta;
    }

    public void setTentativaIncorreta(Boolean tentativaIncorreta) {
        this.tentativaIncorreta = tentativaIncorreta;
    }
}
