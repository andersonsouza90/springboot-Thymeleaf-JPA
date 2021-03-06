package com.springboot3.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
public class Pessoa implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@javax.validation.constraints.NotNull(message = "Nome não pode ser nulo")
	@NotEmpty(message = "nome não pode ser vazio!")
	private String nome;
	
	@javax.validation.constraints.NotNull(message = "Sobrenome não pode ser nulo")
	@NotEmpty(message = "Sobrenome não pode ser vazio!")
	private String sobrenome;
	
	@Min(value = 18, message = "Idade não pode ser menor que 18!")
	private int idade;
	
	private String cep;
	private String rua;
	private String bairro;
	private String cidade;
	private String uf;
	private String ibge;
	private String sexopessoa;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private java.util.Date dataNascimento;
	
	@ManyToOne
	private Profissao profissao;
	
	@Enumerated(EnumType.STRING)
	private Cargo cargo;
	
	@Lob
	private byte[] curriculo;
	
	private String nomeFileCurriculo;
	private String tipoFileCurriculo;
	
	public void setNomeFileCurriculo(String nomeFileCurriculo) {
		this.nomeFileCurriculo = nomeFileCurriculo;
	}
	
	public String getNomeFileCurriculo() {
		return nomeFileCurriculo;
	}
	
	public void setTipoFileCurriculo(String tipoFileCurriculo) {
		this.tipoFileCurriculo = tipoFileCurriculo;
	}
	public String getTipoFileCurriculo() {
		return tipoFileCurriculo;
	}
	
	public void setCurriculo(byte[] curriculo) {
		this.curriculo = curriculo;
	}
	
	public byte[] getCurriculo() {
		return curriculo;
	}
	
	public void setDataNascimento(java.util.Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	
	public java.util.Date getDataNascimento() {
		return dataNascimento;
	}
	
	
	public Cargo getCargo() {
		return cargo;
	}
	
	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}
	
	public Profissao getProfissao() {
		return profissao;
	}
	
	public void setProfissao(Profissao profissao) {
		this.profissao = profissao;
	}
	
	public void setSexopessoa(String sexopessoa) {
		this.sexopessoa = sexopessoa;
	}
	
	public String getSexopessoa() {
		return sexopessoa;
	}
	
	
	@OneToMany(mappedBy = "pessoa", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Telefone> telefones;
	
	
	public List<Telefone> getTelefones() {
		return telefones;
	}
	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}
	public int getIdade() {
		return idade;
	}
	public void setIdade(int idade) {
		this.idade = idade;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSobrenome() {
		return sobrenome;
	}
	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getRua() {
		return rua;
	}
	public void setRua(String rua) {
		this.rua = rua;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getUf() {
		return uf;
	}
	public void setUf(String uf) {
		this.uf = uf;
	}
	public String getIbge() {
		return ibge;
	}
	public void setIbge(String ibge) {
		this.ibge = ibge;
	}
	
	
	
	
}
