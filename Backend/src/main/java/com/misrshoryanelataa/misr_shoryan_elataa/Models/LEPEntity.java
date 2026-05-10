package com.misrshoryanelataa.misr_shoryan_elataa.Models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;

import java.util.List;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class LEPEntity extends StaffEntity{
    public LEPEntity() {

    }

    @JsonManagedReference("lep-child")
    @OneToMany(mappedBy = "lep", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChildEntity> children;

    public List<ChildEntity> getChildren() {
        return children;
    }

    public void setChildren(List<ChildEntity> children) {
        this.children = children;
    }
}
