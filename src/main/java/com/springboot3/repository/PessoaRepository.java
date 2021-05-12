package com.springboot3.repository;

import java.util.List;

import org.hibernate.criterion.Example;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.springboot3.model.Pessoa;

@Repository
@Transactional
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
	
	@Query("select p from Pessoa p where p.nome like %?1% ")
	List<Pessoa> findPessoaByNome(String nome);
	
	@Query("select p from Pessoa p where p.sexopessoa = ?1 ")
	List<Pessoa> findPessoaBySexo(String sexo);
	
	@Query("select p from Pessoa p where p.nome like %?1% and p.sexopessoa = ?2 ")
	List<Pessoa> findPessoaByNomeSexo(String nome, String pessoa);
	
	default Page<Pessoa> findPessoaByNamePage(String nome, org.springframework.data.domain.Pageable pageable){
		
		Pessoa pessoa = new Pessoa();
		pessoa.setNome(nome);
		
		/*Configurando pesquisa para consultar por partes do nome no banco de dados, igual o like do sql */
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
				.withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		
		/*Une o objeto com o valor e a configuração para consultar*/
		org.springframework.data.domain.Example<Pessoa> example = org.springframework.data.domain.Example.of(pessoa, exampleMatcher);
		
		Page<Pessoa> pessoas = findAll(example, pageable);
		
		return pessoas;
	}
	
	default Page<Pessoa> findPessoaBySexoPage(String nome, String sexo, org.springframework.data.domain.Pageable pageable){
		
		Pessoa pessoa = new Pessoa();
		pessoa.setNome(nome);
		pessoa.setSexopessoa(sexo);
		
		/*Configurando pesquisa para consultar por partes do nome no banco de dados, igual o like do sql */
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
				.withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher("sexopessoa", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		
		/*Une o objeto com o valor e a configuração para consultar*/
		org.springframework.data.domain.Example<Pessoa> example = org.springframework.data.domain.Example.of(pessoa, exampleMatcher);
		
		Page<Pessoa> pessoas = findAll(example, pageable);
		
		return pessoas;
	}
	
}
