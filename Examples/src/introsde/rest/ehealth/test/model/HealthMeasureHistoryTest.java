package introsde.rest.ehealth.test.model;

import introsde.rest.ehealth.model.HealthMeasureHistory;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class HealthMeasureHistoryTest {

	@Test
	public void test() {
		int id2 = 1;
		int measureTypeId2 = 1;

		List<HealthMeasureHistory> hMhistoryList = HealthMeasureHistory
				.getAll();
		System.out.println("Got " + hMhistoryList.size() + " records");
		List<HealthMeasureHistory> retval = new ArrayList<>();
		for (HealthMeasureHistory hm : hMhistoryList) {
			if (hm.getPerson() != null) {
				if (hm.getPerson().getName() != null) {
					System.out.println(hm.getPerson().getName());
				}
				int idperson = hm.getPerson().getIdPerson();
				if (id2 == idperson) {
					if (hm.getMeasureDefinition() != null) {
						int idmeasured = hm.getMeasureDefinition()
								.getIdMeasureDef();
						if (idmeasured == measureTypeId2) {
							retval.add(hm);
							System.out.println(hm.getPerson().getName()
									+ " "
									+ hm.getValue()
									+ " "
									+ hm.getMeasureDefinition()
											.getMeasureName());
						}
					}
				}
			}

		}
	}

}
