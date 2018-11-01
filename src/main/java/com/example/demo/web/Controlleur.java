package com.example.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entities.Compte;
import com.example.demo.entities.Operation;
import com.example.demo.metier.igestionDesComptes;

@Controller
public class Controlleur {
	@Autowired
	private igestionDesComptes igestion;
	@RequestMapping("/operation")
	public String index() {
		return "comptes";
	}
	
	@RequestMapping("/consulterCompte")
	public String consulter(Model model,String codeCompte,@RequestParam(name="page",defaultValue="0")int page,@RequestParam(name="size",defaultValue="5")int size) {
try {
	Compte cp=igestion.consulterCompte(codeCompte);
	Page<Operation> pageOperations=igestion.listeOperations(codeCompte,page,size);
	model.addAttribute("listeOperations",pageOperations.getContent());
	int [] pages=new int[pageOperations.getTotalPages()];
	model.addAttribute("pages",pages);
	model.addAttribute("codeCompte",codeCompte);
	model.addAttribute("compte",cp);
}
catch(Exception e) {
	model.addAttribute("exception",e);
}
	return "comptes";
}
	@RequestMapping(value="/saveOperation",method=RequestMethod.POST)
	public String saveOperation(Model model,String typeOperation,String codeCompte,double montant,String codeCompte2) {
		try {
		if(typeOperation.equals("versement")) {
			igestion.verser(codeCompte,montant);
			
		}
		else if(typeOperation.equals("retrait")) {
			igestion.retirer(codeCompte, montant);
		}
		else {
			igestion.virement(codeCompte, codeCompte2, montant);
		}}catch(Exception e) {
			model.addAttribute("erreur",e);
			return "redirect:/consulterCompte?codeCompte="+codeCompte+"&erreur="+e.getMessage();
		}
		return "redirect:/consulterCompte?codeCompte="+codeCompte;
	}
	
	
	
}
