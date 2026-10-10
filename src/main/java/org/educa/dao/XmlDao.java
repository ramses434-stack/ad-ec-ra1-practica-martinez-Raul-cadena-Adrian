package org.educa.dao;

import org.educa.entity.SummaryEntity;

import java.io.IOException;

public interface XmlDao {
    void exportarResumen(SummaryEntity summary, String path, String mesAnio) throws IOException;
}
