package com.contacttura.contacttura.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.contacttura.contacttura.model.ContatctturaUser;
import com.contacttura.contacttura.repository.ContactturaUserRepository;


@RestController
@RequestMapping({ "/contactturaUser" })
public class ContactturaUserController {
	@Autowired
	private ContactturaUserRepository repository1;

// Fluxo semelhante ao implements que define que este controlador com seus
// métodos será acessado atraves do repositorio.
//	ContactturaController(ContactturaRepository contacturaRepository){
//		this.repository = contacturaRepository;
//	}

// 		List All
	@GetMapping
// 		http://localhost:8090/contactturaUser	
	public List findAll() {
		return repository1.findAll();
	}

//		Find by id - Busca valor pelo ID especifico
	@GetMapping(value = "{id}")
//		http://localhost:8090/contactturaUser/1			
	public ResponseEntity findById(@PathVariable long id) {
		return repository1.findById(id).map(record -> ResponseEntity.ok().body(record))
				.orElse(ResponseEntity.notFound().build());
	}

//		Create
	@PostMapping
//		http://localhost:8090/ContatctturaUser		
	public ContatctturaUser create(@RequestBody ContatctturaUser contactturaUser) {
		return repository1.save(contactturaUser);
	}

//	Update	
	@PutMapping(value = "{id}")
//	http://localhost:8090/user/1
	public ResponseEntity<?> update(@PathVariable long id,
			@RequestBody ContatctturaUser contatctturaUser){
		return repository1.findById(id).map(record -> {
					record.setName(contatctturaUser.getName());
					record.setUsername(contatctturaUser.getUsername());
					record.setPassword(criptograPassword(contatctturaUser.getPassword()));
					record.setAdmin(false);
					ContatctturaUser update = repository1.save(record);
					
					return ResponseEntity.ok().body("Cliente: \n"
							+ "Nome: " + update.getName() + "\n"
							+ "Username: " + update.getUsername() + "\n"
							+ "Password: Atualizado! \n"
							+ "Atualizado com sucesso!");
				}).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping(path = { "/{id}" })
//		http://localhost:8090/contactturaUser/2
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> delete(@PathVariable long id) {
		return repository1.findById(id).map(record -> {
			repository1.deleteById(id);
			return ResponseEntity.ok().build();
		}).orElse(ResponseEntity.notFound().build());
	}

private String criptograPassword(String password) {
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String passwordCriptografar = passwordEncoder.encode(password);
		
		return passwordCriptografar;
	}
	
}
