package com.example.demo.metier;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.CompteRepository;
import com.example.demo.dao.OperationRepository;
import com.example.demo.entities.Compte;
import com.example.demo.entities.CompteCourant;
import com.example.demo.entities.Operation;
import com.example.demo.entities.Retrait;
import com.example.demo.entities.Versement;
@Service
@Transactional
public class GestionDesComptesImpl implements igestionDesComptes{
	@Autowired
private CompteRepository compteRepository;
	@Autowired
	private OperationRepository operationRepository;
	
	@Override
	public Compte consulterCompte(String codeCpte) {
		Compte cp=compteRepository.findOne(codeCpte);
		if(cp==null) throw new RuntimeException("compte introuvable");
		
		
		return cp;
	}

	@Override
	public void verser(String codeCpte, double montant) {
		Compte cp=consulterCompte(codeCpte);
		Versement v=new Versement(new Date(),montant,cp);
		operationRepository.save(v);
		cp.setSolde(cp.getSolde()+montant);
		compteRepository.save(cp);
		
		
		
	}

	@Override
	public void retirer(String codeCpte, double montant) {
		Compte cp=consulterCompte(codeCpte);
		double facilitiesCaisse=0;
		if(cp instanceof CompteCourant)
			facilitiesCaisse=((CompteCourant) cp).getDecouvert();
		if(cp.getSolde()+facilitiesCaisse<montant)
			throw new RuntimeException("solde insuffisant");
		Retrait r=new Retrait(new Date(),montant,cp);
		operationRepository.save(r);
		cp.setSolde(cp.getSolde()-montant);
		compteRepository.save(cp);
		
	}

	@Override
	public void virement(String codeCpte1, String codeCpte2, double montant) {
		if(codeCpte1.equals(codeCpte2)) {
			throw new RuntimeException("operation impossible");
		}
		retirer(codeCpte1,montant);
		verser(codeCpte2,montant);
	}

	@Override
	public Page<Operation> listeOperations(String codeCpte, int page, int size) {
		
		return operationRepository.listeOperation(codeCpte,new PageRequest(page,size));
	}

}
