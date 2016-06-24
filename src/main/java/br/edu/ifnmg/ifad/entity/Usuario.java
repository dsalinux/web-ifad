/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.ifad.entity;

import br.edu.ifnmg.ifad.util.Assert;
import br.edu.ifnmg.ifad.util.exception.BusinessException;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author danilo
 */
@Entity
@Table(name = "usuario")
@XmlRootElement
public class Usuario extends EntityManageable implements Serializable{
    private static final long serialVersionUID = 1L;
        
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nome")
    private String nome;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "senha")
    private String senha;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="E-mail inválido")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "login")
    private String login;
    @Column(name = "data_desativacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataDesativacao;
    @Basic(optional = false)
    @NotNull
    @Column(name = "data_cadastro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCadastro;
    @Size(max = 30)
    @Column(name = "tema")
    private String tema;
    @Basic(optional = false)
    @NotNull
    @Column(name = "alterar_senha")
    private boolean alterarSenha;
    @Column(name="permissoes")
    private String permissoes;

    public Usuario() {
    }

    public Usuario(Integer id) {
        this.id = id;
    }

    public Usuario(Integer id, String nome, String senha, String email, Date dataCadastro, boolean alterarSenha, String permissoes) {
        this.id = id;
        this.nome = nome;
        this.senha = senha;
        this.email = email;
        this.dataCadastro = dataCadastro;
        this.alterarSenha = alterarSenha;
        this.permissoes = permissoes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Date getDataDesativacao() {
        return dataDesativacao;
    }

    public void setDataDesativacao(Date dataDesativacao) {
        this.dataDesativacao = dataDesativacao;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public boolean getAlterarSenha() {
        return alterarSenha;
    }

    public void setAlterarSenha(boolean alterarSenha) {
        this.alterarSenha = alterarSenha;
    }

    public String getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(String permissoes) {
        this.permissoes = permissoes;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.softop.projetobase.entity.Login[ id=" + id + " ]";
    }

    @Override
    public void save() throws BusinessException {
        valid();
        if(dataCadastro == null){
            dataCadastro = new Date();
        }
        Usuario usuario = this;
        getSession().merge(usuario);
    }

    @Override
    public void remove() throws BusinessException {
        Usuario usuario = (Usuario)getSession().get(Usuario.class, id);
        getSession().delete(usuario);
    }

    @Override
    public void valid() throws BusinessException {
        if(Assert.isStringNullOrEmpty(nome)){
            throw new BusinessException("Por favor informe o nome!");
        }
        if(Assert.isStringNullOrEmpty(senha)){
            throw new BusinessException("Por favor informe a senha!");
        }
        if(!Assert.isValidEmail(email)){
            throw new BusinessException("Por favor informe um email válido!");
        }
        if(Assert.isStringNullOrEmpty(login)){
            throw new BusinessException("Por favor informe o login!");
        }
    }
    
    
}
