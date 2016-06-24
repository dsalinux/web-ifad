package br.edu.ifnmg.ifad.entity.vo;

import java.util.List;
import java.util.Objects;

public class ClasseVO {

    private String id;
    private String name;
    private List<ProfessorVO> teachers;

    public List<ProfessorVO> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<ProfessorVO> teachers) {
        this.teachers = teachers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ClasseVO other = (ClasseVO) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        
        return "Class: "+name;
    }
    
}
