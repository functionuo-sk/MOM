package com.mom_management.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mom_management.dto.MomDTO;
import com.mom_management.service.MomService;

@RestController
@RequestMapping("/api/moms")
public class MomController {

	@Autowired
	private MomService momService;

	@GetMapping
	public ResponseEntity<List<MomDTO>> getAllMoms() {
		List<MomDTO> moms = momService.getAllMomDTOs();
		return new ResponseEntity<>(moms, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<MomDTO> getMomById(@PathVariable int id) {
		Optional<MomDTO> mom = momService.getMomDTOById(id);
		return mom.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@PostMapping
	public ResponseEntity<MomDTO> saveMom(@RequestBody MomDTO momDTO) {
		MomDTO savedMomDTO = momService.saveMomDTO(momDTO);
		return new ResponseEntity<>(savedMomDTO, HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteMom(@PathVariable int id) {
		momService.deleteMom(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
