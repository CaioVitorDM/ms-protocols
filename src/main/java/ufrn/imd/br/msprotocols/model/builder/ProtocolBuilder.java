package ufrn.imd.br.msprotocols.model.builder;

import ufrn.imd.br.msprotocols.model.Protocol;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProtocolBuilder {

    private Long id;
    private String name;
    private String description;
    private Long fileId;
    private Long doctorId;
    private List<Long> patientsIdList;
    private Boolean isSpecific;

    private ZonedDateTime createdAt;

    public ProtocolBuilder id(Long id){
        this.id = id;
        return this;
    }

    public ProtocolBuilder description(String description){
        this.description = description;
        return this;
    }

    public ProtocolBuilder name(String name){
        this.name = name;
        return this;
    }

    public ProtocolBuilder patientsIdList(List<Long> patientsIdList){
        this.patientsIdList = Objects.requireNonNullElseGet(patientsIdList, ArrayList::new);
        return this;
    }

    public ProtocolBuilder fileId(Long fileId){
        this.fileId = fileId;
        return this;
    }

    public ProtocolBuilder doctorId(Long doctorId){
        this.doctorId = doctorId;
        return this;
    }

    public ProtocolBuilder isSpecific(Boolean isSpecific){
        this.isSpecific = isSpecific;
        return this;
    }

    public ProtocolBuilder createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Protocol build(){
        Protocol protocol= new Protocol();
        protocol.setId(id);
        protocol.setName(name);
        protocol.setDescription(description);
        protocol.setFileId(fileId);
        protocol.setDoctorId(doctorId);
        protocol.setPatientsIdList(patientsIdList);
        protocol.setSpecific(isSpecific);
        protocol.setCreatedAt(createdAt);
        return protocol;
    }
}
