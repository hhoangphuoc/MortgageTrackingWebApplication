package unit.dao;

import dao.contracts.IInterestOfferDao;
import dao.transaction.ITransaction;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import model.InterestOffer;

public class TestInterestOfferDao extends BaseDaoTest{
    IInterestOfferDao dao;
    @Override
    protected void withTransaction(ITransaction transaction) {
        dao = transaction.interestOfferDao();
    }
    
    @Test
    void get_NotNull(){
		var expected = new InterestOffer();
		expected.setDuration("3 months");
		expected.setInterest_rate(12.5);
		expected.setM_id(8888);
		assertDoesNotThrow(()-> dao.insert(expected));
        var interestOffer = assertDoesNotThrow(()-> dao.getByMId(8888));
		assertNotNull(interestOffer);
    }

	@Test
	void updateThenGet_returnsNew() {
		var expected = new InterestOffer();
		expected.setDuration("3 months");
		expected.setInterest_rate(12.5);
		expected.setM_id(8890);
		assertDoesNotThrow(()-> dao.insert(expected));
		expected.setDuration("6 months");
		expected.setInterest_rate(20.7);
		expected.setM_id(8890);
        assertDoesNotThrow(()->dao.update(expected));
		var actual = assertDoesNotThrow(()-> dao.getByMId(expected.getM_id()));
		assertEquals(expected.getDuration(), actual.getDuration());
		assertEquals(expected.getInterest_rate(), actual.getInterest_rate());
	}

	@Test
	void insertThenGet_returnsNew() {
		var expected = new InterestOffer();
		expected.setDuration("4 months");
		expected.setInterest_rate(12.5);
		expected.setM_id(8889);
        assertDoesNotThrow(()->dao.insert(expected));
		var actual = assertDoesNotThrow(()-> dao.getByMId(expected.getM_id()));
		assertEquals(expected.getDuration(), actual.getDuration());
		assertEquals(expected.getInterest_rate(), actual.getInterest_rate());
		assertEquals(expected.getM_id(), actual.getM_id());
	}
	
	@Test
	void createThenGet_returnsNew() {
		var expected = new InterestOffer();
		expected.setDuration("5 years");
		expected.setInterest_rate(1.2);
		expected.setM_id(8000);
        assertDoesNotThrow(()->dao.createInterestOffer(expected.getM_id(),expected.getDuration()));
		var actual = assertDoesNotThrow(()-> dao.getByMId(expected.getM_id()));
		assertEquals(expected.getDuration(), actual.getDuration());
		assertEquals(expected.getInterest_rate(), actual.getInterest_rate());
		assertEquals(expected.getM_id(), actual.getM_id());

		var expected1 = new InterestOffer();
		expected1.setDuration("1 months");
		expected1.setInterest_rate(0.4);
		expected1.setM_id(8001);
        assertDoesNotThrow(()->dao.createInterestOffer(expected1.getM_id(),expected1.getDuration()));
		var actual1 = assertDoesNotThrow(()-> dao.getByMId(expected1.getM_id()));
		assertEquals(expected1.getDuration(), actual1.getDuration());
		assertEquals(expected1.getInterest_rate(), actual1.getInterest_rate());
		assertEquals(expected1.getM_id(), actual1.getM_id());
		
		var expected2 = new InterestOffer();
		expected2.setDuration("6 months");
		expected2.setInterest_rate(0.6);
		expected2.setM_id(8002);
        assertDoesNotThrow(()->dao.createInterestOffer(expected2.getM_id(),expected2.getDuration()));
		var actual2 = assertDoesNotThrow(()-> dao.getByMId(expected2.getM_id()));
		assertEquals(expected2.getDuration(), actual2.getDuration());
		assertEquals(expected2.getInterest_rate(), actual2.getInterest_rate());
		assertEquals(expected2.getM_id(), actual2.getM_id());
		
		var expected3 = new InterestOffer();
		expected3.setDuration("12 months");
		expected3.setInterest_rate(0.8);
		expected3.setM_id(8003);
        assertDoesNotThrow(()->dao.createInterestOffer(expected3.getM_id(),expected3.getDuration()));
		var actual3 = assertDoesNotThrow(()-> dao.getByMId(expected3.getM_id()));
		assertEquals(expected3.getDuration(), actual3.getDuration());
		assertEquals(expected3.getInterest_rate(), actual3.getInterest_rate());
		assertEquals(expected3.getM_id(), actual3.getM_id());
		
		var expected4 = new InterestOffer();
		expected4.setDuration("2 years");
		expected4.setInterest_rate(1);
		expected4.setM_id(8004);
        assertDoesNotThrow(()->dao.createInterestOffer(expected4.getM_id(),expected4.getDuration()));
		var actual4 = assertDoesNotThrow(()-> dao.getByMId(expected4.getM_id()));
		assertEquals(expected4.getDuration(), actual4.getDuration());
		assertEquals(expected4.getInterest_rate(), actual4.getInterest_rate());
		assertEquals(expected4.getM_id(), actual4.getM_id());
		
		var expected5 = new InterestOffer();
		expected5.setDuration("10 years");
		expected5.setInterest_rate(1.4);
		expected5.setM_id(8005);
        assertDoesNotThrow(()->dao.createInterestOffer(expected5.getM_id(),expected5.getDuration()));
		var actual5 = assertDoesNotThrow(()-> dao.getByMId(expected5.getM_id()));
		assertEquals(expected5.getDuration(), actual5.getDuration());
		assertEquals(expected5.getInterest_rate(), actual5.getInterest_rate());
		assertEquals(expected5.getM_id(), actual5.getM_id());
		
		//test different than possible choice
		var expected6 = new InterestOffer();
		expected6.setDuration("20 years");
		expected6.setInterest_rate(0);
		expected6.setM_id(8006);
        assertDoesNotThrow(()->dao.createInterestOffer(expected6.getM_id(),expected6.getDuration()));
		var actual6 = assertDoesNotThrow(()-> dao.getByMId(expected6.getM_id()));
		assertEquals(expected6.getDuration(), actual6.getDuration());
		assertEquals(expected6.getInterest_rate(), actual6.getInterest_rate());
		assertEquals(expected5.getM_id(), actual5.getM_id());
	}
}
