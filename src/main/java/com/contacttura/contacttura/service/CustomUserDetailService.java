package com.contacttura.contacttura.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.switchuser.SwitchUserGrantedAuthority;
import org.springframework.stereotype.Component;

import com.contacttura.contacttura.model.ContatctturaUser;
import com.contacttura.contacttura.repository.ContactturaUserRepository;

@Component
public class CustomUserDetailService implements UserDetailsService {
	
	// Método do spring que retorna um userDetails, buscando o user atravês do repositório,
	// recebendo a instância do repositório do user local
	private final ContactturaUserRepository contactturaUserRepository;
	
	@Autowired
	public CustomUserDetailService(	ContactturaUserRepository contactturaUserRepository) {
		this.contactturaUserRepository = contactturaUserRepository;
	}
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		ContatctturaUser user = Optional.ofNullable(contactturaUserRepository.findByUsername(username))
					.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
	
		// Lista que retorna as autoridade e permissões para cada tipo de usuário
		List<GrantedAuthority> authorityListAdmin = AuthorityUtils.createAuthorityList("ROLE_USER, ROLE ADMIN");
		List<GrantedAuthority> authorityListUser = AuthorityUtils.createAuthorityList("ROLE_USER");
		
		// Inserindo os dados do meu model de usuário diretamente dentro do model de usuário do springSecurity,
		// e validando as permissões de administrador ou user
		return new org.springframework.security.core.userdetails.User
				(user.getUsername(), user.getPassword(), user.isAdmin() ? authorityListAdmin : authorityListUser);
	}
	

}
