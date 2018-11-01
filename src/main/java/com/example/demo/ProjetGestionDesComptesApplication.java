package com.example.demo;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.example.demo.dao.ClientRepository;
import com.example.demo.dao.CompteRepository;
import com.example.demo.dao.OperationRepository;
import com.example.demo.entities.Client;
import com.example.demo.entities.Compte;
import com.example.demo.entities.CompteCourant;
import com.example.demo.entities.CompteEpargne;
import com.example.demo.entities.Retrait;
import com.example.demo.entities.Versement;
import com.example.demo.metier.igestionDesComptes;

@SpringBootApplication
public class ProjetGestionDesComptesApplication implements CommandLineRunner {
	@Autowired
	 private ClientRepository clientRepository;
	@Autowired
	private CompteRepository compteRepository;
	@Autowired
	private OperationRepository operationRepository;
	@Autowired
	private igestionDesComptes igestion;
	public static void main(String[] args) {
		SpringApplication.run(ProjetGestionDesComptesApplication.class, args);
		
	}

	@Override
	public void run(String... arg0) throws Exception {
	
		 Client c1=clientRepository.save(new Client("Chouchou","chay_chouchou@gmail.com"));
		Client c2=clientRepository.save(new Client("chichi","cha_j@gmail.com"));
		Compte cp1=compteRepository.save(new CompteCourant("c1",new Date(),90000,c1,6000));
		Compte cp2=compteRepository.save(new CompteEpargne("c2",new Date(),6000,c2,5));
		operationRepository.save(new Versement(new Date(),9000,cp1));
		operationRepository.save(new Versement(new Date(),1000,cp1));
		operationRepository.save(new Versement(new Date(),6000,cp1));
		operationRepository.save(new Retrait(new Date(),2800,cp1));
		
		operationRepository.save(new Versement(new Date(),2000,cp2));
		operationRepository.save(new Versement(new Date(),400,cp2));
		operationRepository.save(new Versement(new Date(),4000,cp2));
		operationRepository.save(new Retrait(new Date(),3000,cp2));
		igestion.verser("c1",11122); 
		
	}
}
