package com.calchoras.mapper;

import com.calchoras.dto.CompanyDTO;
import com.calchoras.model.Company;

import java.util.ArrayList;
import java.util.List;

public class CompanyMapper {

    // model to view
    public static CompanyDTO toDTO(Company entity) {
        if (entity == null) {
            return null;
        }
        return new CompanyDTO(
                entity.getId(),
                entity.getName()
        );
    }

    public static List<CompanyDTO> toDTO(List<Company> entityList) {
        if (entityList == null) {
            return null;
        }
        List<CompanyDTO> list = new ArrayList<>();
        for (Company entity : entityList) {
            list.add(toDTO(entity));
        }
        return list;
    }

    // view to model
    public static Company toEntity(CompanyDTO dto) {
        if (dto == null) {
            return null;
        }

        Company entity = new Company();
        entity.setId(dto.id());
        entity.setName(dto.name());

        return entity;
    }
}