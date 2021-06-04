package com.springboot3.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.springboot3.model.Pessoa;
import com.springboot3.model.Telefone;
import com.springboot3.repository.PessoaRepository;
import com.springboot3.repository.ProfissaoRepository;
import com.springboot3.repository.TelefoneRepository;

@Controller
public class PessoaController {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private ProfissaoRepository profissaoRepository;
	
	@Autowired
	private ReportUtil reportUtil;
	
	@Autowired
	private TelefoneRepository telefoneRepository;
	
	@RequestMapping(method = RequestMethod.GET, value = "/cadastropessoa")
	public ModelAndView inicio() {
		
		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
		andView.addObject("pessoaobj", new Pessoa());
		
		//Iterable<Pessoa> pessoaIt = pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome")));
		andView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
		andView.addObject("profissoes", profissaoRepository.findAll());
		
		
		return andView;
	}
	
	@GetMapping("pessoaspag")
	public ModelAndView carregaPessoaPorPaginacao(@RequestParam("page") int currentPage,
			@RequestParam("size") int qtdRegisto,
			ModelAndView model, @RequestParam("nomepesquisa") String nomepesquisa) {
		
		//configura o order by 
		Sort sort = Sort.by(Sort.Direction.ASC, "nome"); 
		PageRequest pageable = PageRequest.of(currentPage, qtdRegisto, sort); 
		
		Page<Pessoa> pagePessoa = pessoaRepository.findPessoaByNamePage(nomepesquisa, pageable);
		model.addObject("pessoas", pagePessoa);
		model.addObject("pessoaobj", new Pessoa());
		model.addObject("profissoes", profissaoRepository.findAll());
		model.addObject("nomepesquisa", nomepesquisa);
		model.setViewName("cadastro/cadastropessoa");
		return model;
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "**/salvarpessoa", consumes = {"multipart/form-data"})
	public ModelAndView salvar(@Valid Pessoa pessoa, BindingResult bindingResult, final MultipartFile file) throws IOException {
		
		pessoa.setTelefones(telefoneRepository.getTelefones(pessoa.getId()));
		
		if(bindingResult.hasErrors()) {
			ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
			//Iterable<Pessoa> pessoaIt = pessoaRepository.findAll();
			andView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
			andView.addObject("pessoaobj", pessoa);
			andView.addObject("profissoes", profissaoRepository.findAll());
			
			List<String> msg = new ArrayList<String>();
			
			for (ObjectError objetoError : bindingResult.getAllErrors()) {
				msg.add(objetoError.getDefaultMessage()); // vem das anotações da classe @NotNull etc
			}	
			
			andView.addObject("msg", msg);
			
			return andView;
		}
		
		if(file.getSize() > 0) {
			pessoa.setCurriculo(file.getBytes());
			pessoa.setTipoFileCurriculo(file.getContentType());
			pessoa.setNomeFileCurriculo(file.getOriginalFilename());
			
		}else {
			if(pessoa.getId() != null && pessoa.getId() > 0) {
				Pessoa pessoaTemp = pessoaRepository.findById(pessoa.getId()).get();
				pessoa.setCurriculo(pessoaTemp.getCurriculo());
				pessoa.setTipoFileCurriculo(pessoaTemp.getTipoFileCurriculo());
				pessoa.setNomeFileCurriculo(pessoaTemp.getNomeFileCurriculo());
			}
		}
		
		pessoaRepository.save(pessoa);
		
		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
		//Iterable<Pessoa> pessoaIt = pessoaRepository.findAll();
		andView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
		andView.addObject("pessoaobj", new Pessoa());
		andView.addObject("profissoes", profissaoRepository.findAll());
		
		return andView;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/listapessoas")
	public ModelAndView pessoas() {
		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
		//Iterable<Pessoa> pessoaIt = pessoaRepository.findAll();
		andView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
		andView.addObject("profissoes", profissaoRepository.findAll());
		andView.addObject("pessoaobj", new Pessoa());
		return andView;
	}
	
	@GetMapping("/editarpessoa/{idpessoa}")
	public ModelAndView editar(@PathVariable("idpessoa") Long idpessoa) {
		
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		Optional<Pessoa> p = pessoaRepository.findById(idpessoa);
		
		//configura o order by 
		Sort sort = Sort.by(Sort.Direction.ASC, "nome"); 
		PageRequest pageable = PageRequest.of(0, 5, sort); 
		
		Page<Pessoa> pagePessoa = pessoaRepository.findPessoaByNamePage(null, pageable);
		
		modelAndView.addObject("pessoas", pagePessoa);		
		modelAndView.addObject("pessoaobj", p.get());
		modelAndView.addObject("profissoes", profissaoRepository.findAll());
		return modelAndView;
		
	}
	
	@GetMapping("/telefones/{idpessoa}")
	public ModelAndView telefones(@PathVariable("idpessoa") Long idpessoa) {
		ModelAndView mv = new ModelAndView("cadastro/telefone");
		Optional<Pessoa> p = pessoaRepository.findById(idpessoa);
		mv.addObject("pessoaobj", p.get());
		mv.addObject("telefones", telefoneRepository.getTelefones(idpessoa));
		return mv;
	}
	
	@GetMapping("/excluirpessoa/{idpessoa}")
	public ModelAndView excluir(@PathVariable("idpessoa") Long idpessoa) {
		
		ModelAndView MV = new ModelAndView("cadastro/cadastropessoa");
		
		pessoaRepository.deleteById(idpessoa);	
		
		//Iterable<Pessoa> p = pessoaRepository.findAll();
		MV.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
		MV.addObject("pessoaobj", new Pessoa());
		MV.addObject("profissoes", profissaoRepository.findAll());
		
		return MV;
	}
	
	@PostMapping("**/pesquisarpessoa")
	public ModelAndView pesquisarPorNome(@RequestParam("nomepesquisa") String nomepessoa,
										 @RequestParam("sexopesquisa") String sexopesquisa,
										 @PageableDefault(size = 5, sort = {"nome"}) org.springframework.data.domain.Pageable pageable) {
		
		Page<Pessoa> pessoas = null;
		
		if(sexopesquisa != null && !sexopesquisa.isEmpty()) {
			pessoas = pessoaRepository.findPessoaBySexoPage(nomepessoa, sexopesquisa, pageable);
		}else {
			pessoas = pessoaRepository.findPessoaByNamePage(nomepessoa, pageable);
		}
		
		ModelAndView MV = new ModelAndView("cadastro/cadastropessoa");
		Iterable<Pessoa> p = pessoas;
		MV.addObject("pessoas", p);
		MV.addObject("pessoaobj", new Pessoa());
		MV.addObject("profissoes", profissaoRepository.findAll());
		MV.addObject("nomepesquisa", nomepessoa);
		
		return MV;
	}
	
	@GetMapping("**/pesquisarpessoa")
	public void imprimePDF(@RequestParam("nomepesquisa") String nomepessoa,
										 @RequestParam("sexopesquisa") String sexopesquisa,
										 HttpServletRequest request,
										 HttpServletResponse response) throws Exception {
		List<Pessoa> pessoas = new ArrayList<Pessoa>();
		
		if(sexopesquisa != null && !sexopesquisa.isEmpty()
				&& nomepessoa != null && !nomepessoa.isEmpty()) {
			
			pessoas = pessoaRepository.findPessoaByNomeSexo(nomepessoa, sexopesquisa);			
		}else if (nomepessoa != null && !nomepessoa.isEmpty()) {
			pessoas = pessoaRepository.findPessoaByNome(nomepessoa);
			
		}else if (sexopesquisa != null && !sexopesquisa.isEmpty()) {
			pessoas = pessoaRepository.findPessoaBySexo(sexopesquisa);
			
		}else {
			Iterable<Pessoa> iterator = pessoaRepository.findAll();
			for (Pessoa pessoa : iterator) {
				pessoas.add(pessoa);
			}
		}
		
		/*Chama serviço que gera o relatório*/
		
		byte[] pdf = reportUtil.gerarRelatorio(pessoas, "pessoa", request.getServletContext());
		
		/*Informar o tamanho da resposta para o navegador*/
		response.setContentLength(pdf.length);
		
		/*Informar o navegador o tipo do conteúdo na resposta*/
		response.setContentType("application/octet-stream");
		
		/*Definir o cabeçalho da resposta*/
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", "relatorio.pdf");
		response.setHeader(headerKey, headerValue);
		
		/*Finaliza a resposta para o navegador*/
		response.getOutputStream().write(pdf);
		
		
	}
	
	
	@PostMapping("**/addfonePessoa/{pessoaid}")
	public ModelAndView addfonePessoa(Telefone telefone, @PathVariable("pessoaid") Long pessoaid) {
		ModelAndView mv = new ModelAndView("cadastro/telefone");
		
		Pessoa pessoa = pessoaRepository.findById(pessoaid).get();
		
		if(telefone != null && telefone.getNumero().isEmpty() || telefone.getTipo().isEmpty()) {
			
			List<Telefone> listaTelefones = telefoneRepository.getTelefones(pessoaid);
			
			mv.addObject("pessoaobj", pessoa);
			mv.addObject("telefones", listaTelefones);
			
			List<String> msg = new ArrayList<>();
			
			if(telefone.getNumero().isEmpty()) {
				msg.add("O número deve ser informado!");
			}
			if(telefone.getTipo().isEmpty()) {
				msg.add("O tipo deve ser informado!");
			}
			
			
			
			mv.addObject("msg", msg);
			
			return mv;
			
			
		}
		
		telefone.setPessoa(pessoa);
		telefoneRepository.save(telefone);
		List<Telefone> listaTelefones = telefoneRepository.getTelefones(pessoaid);
		
		mv.addObject("pessoaobj", pessoa);
		mv.addObject("telefones", listaTelefones);
		
		return mv;
	}
	
	@GetMapping("**/excluirTelefone/{idtelefone}")
	public ModelAndView excluirTelefone(@PathVariable("idtelefone") Long idtelefone) {
		
		Pessoa p = telefoneRepository.findById(idtelefone).get().getPessoa();
		telefoneRepository.deleteById(idtelefone);
		ModelAndView mv = new ModelAndView("cadastro/telefone");
		mv.addObject("pessoaobj", p);
		mv.addObject("telefones",telefoneRepository.getTelefones(p.getId()));
		
		return mv;
		
	}
	
	@GetMapping("**/baixarcurriculo/{idpessoa}")
	public void baixarCurriculo(@PathVariable("idpessoa") Long idpessoa, 
			HttpServletResponse response) throws IOException {
		/*Consulta o objeto no banco de dados*/
		Pessoa pessoa = pessoaRepository.findById(idpessoa).get();
		if(pessoa.getCurriculo() != null) {
			/**Setar o tamanho da resposta**/
			response.setContentLength(pessoa.getCurriculo().length);
			/*Tipo do arquivo do banco de dados ou pode ser genérico application/octet-stream*/
			response.setContentType(pessoa.getTipoFileCurriculo());
			
			/*Define cabeçalho da resposta*/
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", pessoa.getNomeFileCurriculo());
			response.setHeader(headerKey, headerValue);
			
			/*Finaliza resposta passando o arquivo*/
			response.getOutputStream().write(pessoa.getCurriculo());
			
		}
	}
	
	
	
}
