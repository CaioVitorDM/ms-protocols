package ufrn.imd.br.msprotocols.model;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Where;
import ufrn.imd.br.msprotocols.model.builder.ProtocolBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "protocols")
@Where(clause = "active = true")
public class Protocol extends BaseEntity {

    @NotBlank(message = "Error: O nome não pode estar vazio.")
    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @NotNull(message = "Error: O ID do arquivo não pode ser nulo.")
    @Column(nullable = false, unique = true)
    private Long fileId;

    @NotNull(message = "Error: O ID do médico não pode ser nulo.")
    @Column(nullable = false)
    private Long doctorId;

    @ElementCollection
    private List<Long> patientsIdList;

    @NotNull(message = "Error: Especifidade não pode ser nulo.")
    @Column(nullable = false)
    private Boolean isSpecific;

    public static ProtocolBuilder builder(){
        return new ProtocolBuilder();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public List<Long> getPatientsIdList() {
        return patientsIdList;
    }

    public void setPatientsIdList(List<Long> patientsIdList) {
        this.patientsIdList = patientsIdList;
    }

    public Boolean getSpecific() {
        return isSpecific;
    }

    public void setSpecific(Boolean specific) {
        isSpecific = specific;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Protocol protocol = (Protocol) o;
        return Objects.equals(name, protocol.name) && Objects.equals(description, protocol.description) && Objects.equals(fileId, protocol.fileId) && Objects.equals(patientsIdList, protocol.patientsIdList) && Objects.equals(isSpecific, protocol.isSpecific);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, description, fileId, patientsIdList, isSpecific);
    }
}
