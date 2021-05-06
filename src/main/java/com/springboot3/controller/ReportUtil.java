package com.springboot3.controller;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Component
public class ReportUtil implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/*Retorna o PDF em Byte para download no navegador*/
	public byte[] gerarRelatorio(List listDados, String relatorio, ServletContext servletContext) throws Exception {
		
		/*Cria a lista de dados para o relatório com nossa lista de objetos para imprimir*/
		JRBeanCollectionDataSource jrbcds = new JRBeanCollectionDataSource(listDados); 
		
		/*Carrega o caminho do jasper compilado*/
		String caminhoJasper = servletContext.getRealPath("relatorio") + File.separator + relatorio + ".jasper";
		
		/*Carrega o arquivo jasper passando os dados*/
		JasperPrint impressoraJasper = JasperFillManager.fillReport(caminhoJasper, new HashedMap(), jrbcds);
		
		/*Exporta para byte para fazer download*/
		return JasperExportManager.exportReportToPdf(impressoraJasper);
	}
	

}
