package com.mom_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mom_management.dto.MomDTO;
import com.mom_management.model.Mom;
import com.mom_management.repository.MomRepository;

@Service
public class MomService {

	@Autowired
	private MomRepository momRepository;

	public List<MomDTO> getAllMomDTOs() {
		// Implement logic to convert Mom entities to MomDTOs
		// You can use libraries like ModelMapper or manually map the fields
		// For simplicity, I'm assuming a method convertMomToMomDTO in MomService
		return convertMomListToMomDTOList(momRepository.findAll());
	}

	public Optional<MomDTO> getMomDTOById(long id) {
		// Implement logic to convert Mom entity to MomDTO
		// For simplicity, I'm assuming a method convertMomToMomDTO in MomService
		return momRepository.findById(id).map(this::convertMomToMomDTO);
	}

	public MomDTO saveMomDTO(MomDTO momDTO) {
		// Implement logic to convert MomDTO to Mom entity
		// For simplicity, I'm assuming a method convertMomDTOToMom in MomService
		Mom mom = convertMomDTOToMom(momDTO);
		Mom savedMom = momRepository.save(mom);
		// Convert the saved Mom entity back to MomDTO
		return convertMomToMomDTO(savedMom);
	}

	public void deleteMom(long id) {
		momRepository.deleteById(id);
	}

	// Additional methods for conversion
	private List<MomDTO> convertMomListToMomDTOList(List<Mom> moms) {

		return moms.stream().map(this::convertMomToMomDTO).toList();
	}

	private MomDTO convertMomToMomDTO(Mom mom) {

		MomDTO momdto = new MomDTO();
		momdto.setId(mom.getId());
		momdto.setDescription(mom.getDescription());
		momdto.setCreatedDate(mom.getCreatedDate());
		momdto.setUpdatedDate(mom.getUpdatedDate());

		return momdto;
	}

	private Mom convertMomDTOToMom(MomDTO momDTO) {

		Mom mom = new Mom();
		// mom.setId(momDTO.getId());
		mom.setDescription(momDTO.getDescription());
		// mom.setDate(momDTO.getDate());

		return mom;

	}
}