package com.dms.document.general;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SequenceService {

	private static final String FORMAT_0 = "%0";
	private static final String D = "d";
	@Autowired
	private GeneralRepository generalRepo;

	public String nextSequenceId(SequenceConstants sequence) {
		return sequence.getValue()
				+ String.format(SequenceService.FORMAT_0 + sequence.getDigitLimit() + SequenceService.D, generalRepo.nextSequenceId(sequence, 1));
	}

}
